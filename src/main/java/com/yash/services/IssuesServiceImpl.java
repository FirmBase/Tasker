package com.yash.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.yash.dtos.IssueDTO;
import com.yash.dtos.PriorityDTO;
import com.yash.dtos.StatusDTO;
import com.yash.dtos.SubtaskDTO;
import com.yash.dtos.TaskDTO;
import com.yash.dtos.UserCredentialDTO;
import com.yash.enumerations.PriorityEnum;
import com.yash.enumerations.StatusEnum;

public class IssuesServiceImpl implements IssuesService {
	private final String allIssuesQuery_ =
		"SELECT" +
		"	tasker.issues.id," +
		"	tasker.issues.task_id," +
		"	tasker.task.title AS task_title," +
		"	tasker.task.description AS task_description," +
		"	tasker.task.status AS task_status," +
		"	(SELECT tasker.status.label FROM tasker.status) AS task_status_label," +
		"	tasker.task.priority AS task_priority," +
		"	(SELECT tasker.priority.label FROM tasker.priority) AS task_priority_label," +
		"	tasker.task.assignee_id," +
		"	assignee_user_credential.username AS assignee_username," +
		"	assignee_user_credential.email AS assginee_email," +
		"	assignee_user_credential.registered_on AS assignee_registered_on," +
		"	assignee_user_credential.is_active AS assginee_is_active," +
		"	tasker.task.start_date AS task_start_date," +
		"	tasker.task.due_date AS task_due_date," +
		"	tasker.issues.sub_task_id," +
		"	tasker.sub_task.title AS subtask_title," +
		"	tasker.sub_task.description AS subtask_description," +
		"	tasker.sub_task.status AS subtask_status," +
		"	(SELECT tasker.status.label FROM tasker.status) AS subtask_status_label," +
		"	tasker.sub_task.priority AS subtask_priority," +
		"	(SELECT tasker.sub_task.priority FROM tasker.priority) AS subtask_priority_label," +
		"	tasker.sub_task.assigned_to AS assigned_to_id," +
		"	assigned_to_user_credential.username AS assigned_to_username," +
		"	assigned_to_user_credential.email AS assigned_to_email," +
		"	assigned_to_user_credential.registered_on AS assigned_to_registered_on," +
		"	assigned_to_user_credential.is_active AS assigned_to_is_active," +
		"	tasker.sub_task.assigned_date AS subtask_assigned_date," +
		"	tasker.sub_task.due_date AS subtask_due_date," +
		"	tasker.sub_task.current_progress," +
		"	tasker.sub_task.last_remarks," +
		"	tasker.issues.subject," +
		"	tasker.issues.description," +
		"	tasker.issues.raised_on," +
		"	tasker.issues.resolve_status" + "\n" +
		"FROM" +
		"	tasker.issues" + "\n" +
		"LEFT JOIN" +
		"	tasker.task" + "\n" +
		"ON" +
		"	tasker.issues.task_id = tasker.task.id" + "\n" +
		"LEFT JOIN " +
		"	tasker.user_credentials assignee_user_credential" + "\n" +
		"ON" +
		"	tasker.task.assignee_id = assignee_user_credential.id" + "\n" +
		"LEFT JOIN" +
		"	tasker.sub_task" + "\n" +
		"ON" +
		"	tasker.issues.sub_task_id = tasker.sub_task.id" + "\n" +
		"LEFT JOIN" +
		"	tasker.user_credentials assigned_to_user_credential" + "\n" +
		"ON" +
		"	tasker.task.assignee_id = assigned_to_user_credential.id";

		private final String allIssuesQuery =
			"SELECT" +
			"	tasker.issues.id," +
			"	tasker.issues.task_id," +

			"	task_issue.title AS issue_task_title," +
			"	task_issue.description AS issue_task_description," +
			"	task_issue.status AS issue_task_status," +
			"	(SELECT tasker.status.label FROM tasker.status WHERE tasker.status.id = task_issue.status) AS issue_task_status_label," +
			"	task_issue.priority AS issue_task_priority," +
			"	(SELECT tasker.priority.label FROM tasker.priority WHERE tasker.priority.id = task_issue.priority) AS issue_task_priority_label," +
			"	task_issue.assignee_id AS issue_assignee_id," +
			"	issue_assignee_user_credential.username AS issue_assignee_username," +
			"	issue_assignee_user_credential.email AS issue_assginee_email," +
			"	issue_assignee_user_credential.registered_on AS issue_assignee_registered_on," +
			"	issue_assignee_user_credential.is_active AS issue_assginee_is_active," +
			"	task_issue.start_date AS issue_task_start_date," +
			"	task_issue.due_date AS issue_task_due_date," +

			"	tasker.sub_task.title AS subtask_title," +
			"	tasker.sub_task.description AS subtask_description," +
			"	tasker.sub_task.task_id AS subtask_id," +
			"	tasker.task.title AS task_title," +
			"	tasker.task.description AS task_description," +
			"	tasker.task.status AS task_status," +
			"	(SELECT tasker.status.label FROM tasker.status WHERE tasker.status.id = tasker.task.status) AS task_status_label," +
			"	tasker.task.priority AS task_priority," +
			"	(SELECT tasker.priority.label FROM tasker.priority WHERE tasker.priority.id = tasker.task.priority) AS task_priority_label," +
			"	tasker.task.assignee_id," +
			"	tasker.user_credentials.username AS assignee_username," +
			"	tasker.user_credentials.email AS assginee_email," +
			"	tasker.user_credentials.registered_on AS assignee_registered_on," +
			"	tasker.user_credentials.is_active AS assginee_is_active," +
			"	tasker.task.start_date AS task_start_date," +
			"	tasker.task.due_date AS task_due_date," +

			"	tasker.sub_task.status AS subtask_status," +
			"	(SELECT tasker.status.label FROM tasker.status) AS subtask_status_label," +
			"	tasker.sub_task.priority AS subtask_priority," +
			"	(SELECT tasker.sub_task.priority FROM tasker.priority) AS subtask_priority_label," +
			"	tasker.sub_task.assigned_to AS assigned_to_id," +
			"	assigned_to_user_credential.username AS assigned_to_username," +
			"	assigned_to_user_credential.email AS assigned_to_email," +
			"	assigned_to_user_credential.registered_on AS assigned_to_registered_on," +
			"	assigned_to_user_credential.is_active AS assigned_to_is_active," +
			"	tasker.sub_task.assigned_date AS subtask_assigned_date," +
			"	tasker.sub_task.due_date AS subtask_due_date," +
			"	tasker.sub_task.current_progress," +
			"	tasker.sub_task.last_remarks," +

			"	tasker.issues.subject," +
			"	tasker.issues.description," +
			"	tasker.issues.rasied_on," +
			"	tasker.issues.resolve_status" + "\n" +
			"FROM" +
			"	tasker.issues" + "\n" +
			"LEFT JOIN" +
			"	tasker.task task_issue" + "\n" +
			"ON" +
			"	tasker.issues.task_id = task_issue.id" + "\n" +
			"LEFT JOIN " +
			"	tasker.user_credentials issue_assignee_user_credential" + "\n" +
			"ON" +
			"	task_issue.assignee_id = issue_assignee_user_credential.id" + "\n" +
			"LEFT JOIN" +
			"	tasker.sub_task" + "\n" +
			"ON" +
			"	tasker.issues.sub_task_id = tasker.sub_task.id" + "\n" +
			"LEFT JOIN" +
			"	tasker.user_credentials assigned_to_user_credential" + "\n" +
			"ON" +
			"	task_issue.assignee_id = assigned_to_user_credential.id" + "\n" +
			"LEFT JOIN" +
			"	tasker.task" + "\n" +
			"ON" +
			"	tasker.sub_task.task_id = tasker.task.id" + "\n" +
			"LEFT JOIN" +
			"	tasker.user_credentials" + "\n" +
			"ON" +
			"	tasker.task.assignee_id = tasker.user_credentials.id";

	@Override
	public List<IssueDTO> allIssues(final Connection connection) throws SQLException {
		final List<IssueDTO> issueDTOs = new ArrayList<IssueDTO>();
		final Statement statement = connection.createStatement();
		final ResultSet resultSet = statement.executeQuery(allIssuesQuery);
		while (resultSet.next()) {
			final StatusDTO issueStatusDTO = new StatusDTO(resultSet.getLong("issue_task_status"), StatusEnum.valueOf(resultSet.getString("issue_task_status_label")));
			final PriorityDTO issuePriorityDTO = new PriorityDTO(resultSet.getLong("issue_task_priority"), PriorityEnum.valueOf(resultSet.getString("issue_task_priority_label")));
			final UserCredentialDTO issueAssigneeUserCredentialDTO = new UserCredentialDTO(resultSet.getLong("issue_assignee_id"), resultSet.getString("issue_assignee_username"), resultSet.getString("issue_assignee_email"), "", resultSet.getDate("issue_assignee_registered_on"), resultSet.getBoolean("issue_assginee_is_active_on"));
			final TaskDTO issueTaskDTO = new TaskDTO(resultSet.getLong("issue_task_id"), resultSet.getString("issue_task_title"), resultSet.getString("issue_task_description"), issueStatusDTO, issueAssigneeUserCredentialDTO, issuePriorityDTO, resultSet.getDate("issue_task_start_date"), resultSet.getDate("issue_task_due_date"));

			final StatusDTO taskStatusDTO = new StatusDTO(resultSet.getLong("task_status"), StatusEnum.valueOf(resultSet.getString("task_status_label")));
			final PriorityDTO taskPriorityDTO = new PriorityDTO(resultSet.getLong("task_priority"), PriorityEnum.valueOf(resultSet.getString("task_priority_label")));
			final UserCredentialDTO assigneeUserCredentialDTO = new UserCredentialDTO(resultSet.getLong("assignee_id"), resultSet.getString("assignee_username"), resultSet.getString("assignee_email"), "", resultSet.getDate("assignee_registered_on"), resultSet.getBoolean("assginee_is_active_on"));
			final TaskDTO taskDTO = new TaskDTO(resultSet.getLong("task_id"), resultSet.getString("task_title"), resultSet.getString("task_description"), taskStatusDTO, assigneeUserCredentialDTO, taskPriorityDTO, resultSet.getDate("task_start_date"), resultSet.getDate("task_due_date"));
			final StatusDTO subtaskStatusDTO = new StatusDTO(resultSet.getLong("subtask_status"), StatusEnum.valueOf(resultSet.getString("subtask_status_label")));
			final PriorityDTO subtaskPriorityDTO = new PriorityDTO(resultSet.getLong("subtask_priority"), PriorityEnum.valueOf(resultSet.getString("subtask_prioriy_label")));
			final UserCredentialDTO assignedToUserCredentialDTO = new UserCredentialDTO(resultSet.getLong("assigned_to_id"), resultSet.getString("assigned_to_username"), resultSet.getString("assigned_to_email"), "", resultSet.getDate("assigned_to_registered_on"), resultSet.getBoolean("assigned_to_is_active"));
			final SubtaskDTO subtaskDTO = new SubtaskDTO(resultSet.getLong("sub_task_id"), resultSet.getString("subtask_title"), resultSet.getString("subtask_description"), taskDTO, subtaskStatusDTO, subtaskPriorityDTO, assignedToUserCredentialDTO, resultSet.getDate("subtask_assigned_date"), resultSet.getDate("subtask_due_date"), resultSet.getInt("subtask_current_progress"), resultSet.getString("subtask_last_remarks"));

			final IssueDTO issueDTO = new IssueDTO(resultSet.getLong("id"), issueTaskDTO, subtaskDTO, resultSet.getString("subject"), resultSet.getString("description"), resultSet.getDate("raised_on"), resultSet.getBoolean("resolve_status"));
			issueDTOs.add(issueDTO);
		}
		resultSet.close();
		statement.close();
		return issueDTOs;
	}

	private final String raiseIssueQuery = "INSERT INTO tasker.issues(task_id, sub_task_id, subject, description, raised_on, resolve_status) VALUES(?, ?, ?, ?, ?, ?)";

	@Override
	public boolean raiseIssue(final Connection connection, final Long taskId, final Long subtaskId, final String subject, final String description) throws SQLException {
		int row = 0;
		final PreparedStatement preparedStatement = connection.prepareStatement(raiseIssueQuery);
		preparedStatement.setLong(1, taskId);
		preparedStatement.setLong(2, subtaskId);
		preparedStatement.setString(3, subject);
		preparedStatement.setString(4, description);
		preparedStatement.setDate(5, new java.sql.Date(new java.util.Date().getTime()));
		preparedStatement.setBoolean(6, false);
		row = preparedStatement.executeUpdate();
		preparedStatement.close();
		return row > 0;
	}

	private final String addressIssueQuery = "UPDATE tasker.issues SET tasker.issues.resolve_status = ? WHERE tasker.issues.id";

	@Override
	public boolean addressIssue(final Connection connection, final long id, final boolean resolveState) throws SQLException {
		int row = 0;
		final PreparedStatement preparedStatement = connection.prepareStatement(addressIssueQuery);
		preparedStatement.setLong(2, id);
		preparedStatement.setBoolean(1, resolveState);
		row = preparedStatement.executeUpdate();
		preparedStatement.close();
		return row > 0;
	}
}
