package com.yash.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.yash.dtos.FeedbackDTO;
import com.yash.dtos.PriorityDTO;
import com.yash.dtos.StatusDTO;
import com.yash.dtos.SubtaskDTO;
import com.yash.dtos.TaskDTO;
import com.yash.dtos.UserCredentialDTO;
import com.yash.enumerations.PriorityEnum;
import com.yash.enumerations.StatusEnum;

public class FeedbackServiceImpl implements FeedbackService {
	private final String checkFeedbackRow = "SELECT tasker.feedback.id, tasker.feedback.rating, tasker.feedback.remarks FROM tasker.feedback WHERE tasker.feedback.sub_task_id";
	private final String putFeedbackQuery = "INSERT INTO tasker.feedback(sub_task_id, rating, remarks) VALUES(?, ?, ?)";
	private final String updateFeedbackQuery = "UPDATE tasker.feedback SET tasker.feedback.rating = ?, tasker.feedback.remarks = ? WHERE tasker.feedback.sub_task_id = ?";

	@Override
	public boolean putFeedbackUser(Connection connecton, long subtaskId, Short rating, String remarks) throws SQLException {
		int row = 0;
		final PreparedStatement preparedStatement_ = connecton.prepareStatement(checkFeedbackRow);
		preparedStatement_.setLong(1, subtaskId);
		final ResultSet resultSet_ = preparedStatement_.executeQuery();
		if (resultSet_.next()) {
			final PreparedStatement preparedStatement = connecton.prepareStatement(updateFeedbackQuery);
			preparedStatement.setLong(3, subtaskId);
			preparedStatement.setInt(1, rating);
			preparedStatement.setString(2, remarks);
			row = preparedStatement.executeUpdate();
		}
		else {
			final PreparedStatement preparedStatement = connecton.prepareStatement(putFeedbackQuery, PreparedStatement.RETURN_GENERATED_KEYS);
			preparedStatement.setLong(1, subtaskId);
			preparedStatement.setShort(2, rating);
			preparedStatement.setString(3, remarks);
			row = preparedStatement.executeUpdate();
			ResultSet resultSet = preparedStatement.getGeneratedKeys();
			if (resultSet.next())
				resultSet.getLong(1);
			preparedStatement.close();
		}
		return row > 0;
	}

	/*
	private final String getFeedbackByUserIdQuery =
		"SELECT" +
		"	id," +
		"	user_credentials_id," +
		"	task_id," +
		"	sub_task_id," +
		"	rating," +
		"	remarks" +
		"FROM" +
		"	tasker.feedback" +
		"INNER JOIN" +
		"	tasker.user_credentials" +
		"ON" +
		"	tasker.feedback.user_credentials_id = tasker.user_credentials.id" +
		"LEFT JOIN" +
		"	tasker.task" +
		"ON" +
		"	tasker.feedback.task_id = tasker.task.id" +
		"LEFT JOIN" +
		"	tasker.sub_task" +
		"ON" +
		"	tasker.feedback.sub_task_id = tasker.sub_task.id" +
		"WHERE" +
		"	user_credentials_id = ?";
	*/
	private final String getFeedbackByUserIdQuery =
		"SELECT" +
		"	tasker.feedback.id AS feedback_id," +
		"	tasker.feedback.sub_task_id AS subtask_id," +
		"	tasker.sub_task.title AS subtask_title," +
		"	tasker.sub_task.description AS subtask_description," +
		"	tasker.sub_task.task_id," +
		"	tasker.task.title AS task_name," +
		"	tasker.task.description AS task_description," +
		"	tasker.task.status AS task_status_id," +
		"	(SELECT tasker.status.label FROM tasker.status WHERE tasker.status.id = tasker.task.status) AS task_status," +
		"	tasker.task.assignee_id," +
		"	assignee_user_credential.username AS assignee_username," +
		"	assignee_user_credential.email AS assignee_email," +
		"	assignee_user_credential.registered_on AS assignee_registered_on," +
		"	assignee_user_credential.is_active AS assignee_is_active," +
		"	tasker.task.priority AS task_priority_id," +
		"	(SELECT tasker.priority.label FROM tasker.priority WHERE tasker.priority.id = tasker.task.priority) AS task_priority," +
		"	tasker.task.start_date AS task_start_date," +
		"	tasker.task.due_date AS task_due_date," +
		"	tasker.sub_task.status AS subtask_status_id," +
		"	(SELECT tasker.status.label FROM tasker.status WHERE tasker.status.id = tasker.sub_task.status) AS subtask_status," +
		"	tasker.sub_task.assigned_to AS assigned_to_id," +
		"	assign_to_user_credential.username AS assigned_to_username," +
		"	assign_to_user_credential.email AS assigned_to_email," +
		"	assign_to_user_credential.registered_on AS assigned_to_registered_on," +
		"	assign_to_user_credential.is_active AS assigned_to_is_active," +
		"	tasker.sub_task.assigned_date AS subtask_assigned_date," +
		"	tasker.sub_task.priority AS subtask_priority_id," +
		"	(SELECT tasker.priority.label FROM tasker.priority WHERE tasker.priority.id = tasker.sub_task.priority) AS subtask_priority," +
		"	tasker.sub_task.due_date AS subtask_due_date," +
		"	tasker.sub_task.current_progress AS subtask_current_progress," +
		"	tasker.sub_task.last_remarks AS subtask_remarks," +
		"	tasker.feedback.rating AS feedback_rating," +
		"	tasker.feedback.remarks AS feedback_remarks" + "\n" +
		"FROM" +
		"	tasker.feedback" + "\n" +
		"INNER JOIN" +
		"	tasker.sub_task" + "\n" +
		"ON" +
		"	tasker.feedback.sub_task_id = tasker.sub_task.id" + "\n" +
		"LEFT JOIN" +
		"	tasker.user_credentials assign_to_user_credential" + "\n" +
		"ON" +
		"	tasker.sub_task.assigned_to = assign_to_user_credential.id" + "\n" +
		"LEFT JOIN" +
		"	tasker.task" + "\n" +
		"ON" +
		"	tasker.sub_task.task_id = tasker.task.id" + "\n" +
		"LEFT JOIN" +
		"	tasker.user_credentials assignee_user_credential" + "\n" +
		"ON" +
		"	tasker.task.assignee_id = assignee_user_credential.id" + "\n";

	@Override
	public List<FeedbackDTO> getFeedbacksByUserId(Connection connection, Long userCredentialId) throws SQLException {
		List<FeedbackDTO> feedbackDTOs = new ArrayList<FeedbackDTO>();
		final String filterAssignedTo =
		"WHERE" +
		"	tasker.sub_task.assigned_to = ?";
		final PreparedStatement preparedStatement = connection.prepareStatement(getFeedbackByUserIdQuery + filterAssignedTo);
		preparedStatement.setLong(1, userCredentialId);
		final ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			final StatusDTO taskStatusDTO = new StatusDTO(resultSet.getLong("task_status_id"), StatusEnum.valueOf(resultSet.getString("task_status")));
			final UserCredentialDTO assigneeUserCredentialDTO = new UserCredentialDTO(resultSet.getLong("assignee_id"), resultSet.getString("assignee_username"), resultSet.getString("assignee_email"), "", resultSet.getDate("assignee_registered_on"), resultSet.getBoolean("assignee_is_active"));
			final PriorityDTO priorityDTO = new PriorityDTO(resultSet.getLong("task_priority_id"), PriorityEnum.valueOf(resultSet.getString("task_priority")));
			final TaskDTO taskDTO = new TaskDTO(resultSet.getLong("task_id"), resultSet.getString("task_name"), resultSet.getString("task_description"), taskStatusDTO, assigneeUserCredentialDTO, priorityDTO, resultSet.getDate("task_start_date"), resultSet.getDate("task_due_date"));
			final StatusDTO subtaskStatusDTO = new StatusDTO(resultSet.getLong("subtask_status_id"), StatusEnum.valueOf(resultSet.getString("subtask_status")));
			final UserCredentialDTO assignedToUserCredentialDTO = new UserCredentialDTO(resultSet.getLong("assigned_to_id"), resultSet.getString("assigned_to_username"), resultSet.getString("assigned_to_email"), "", resultSet.getDate("assigned_to_registered_on"), resultSet.getBoolean("assigned_to_is_active"));
			final PriorityDTO subtaskPriorityDTO = new PriorityDTO(resultSet.getLong("subtask_priority_id"), PriorityEnum.valueOf(resultSet.getString("subtask_priority")));
			final SubtaskDTO subtaskDTO = new SubtaskDTO(resultSet.getLong("subtask_id"), resultSet.getString("subtask_title"), resultSet.getString("subtask_description"), taskDTO, subtaskStatusDTO, subtaskPriorityDTO, assignedToUserCredentialDTO, resultSet.getDate("subtask_assigned_date"), resultSet.getDate("subtask_due_date"), resultSet.getInt("subtask_current_progress"), resultSet.getString("subtask_remarks"));
			final FeedbackDTO feedbackDTO = new FeedbackDTO(resultSet.getLong("feedback_id"), subtaskDTO, resultSet.getShort("feedback_rating"), resultSet.getString("feedback_remarks"));
			feedbackDTOs.add(feedbackDTO);
		}
		resultSet.close();
		preparedStatement.close();
		return feedbackDTOs;
	}

	@Override
	public List<FeedbackDTO> getFeedbacksBySubtaskId(Connection connection, Long subtaskId) throws SQLException {
		final List<FeedbackDTO> feedbackDTOs = new ArrayList<FeedbackDTO>();
		final String filterFeedback =
		"WHERE" +
		"	tasker.feedback.sub_task_id = ?";
		final PreparedStatement preparedStatement = connection.prepareStatement(getFeedbackByUserIdQuery + filterFeedback);
		preparedStatement.setLong(1, subtaskId);
		final ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) {
			final StatusDTO taskStatusDTO = new StatusDTO(resultSet.getLong("task_status_id"), StatusEnum.valueOf(resultSet.getString("task_status")));
			final UserCredentialDTO assigneeUserCredentialDTO = new UserCredentialDTO(resultSet.getLong("assignee_id"), resultSet.getString("assignee_username"), resultSet.getString("assignee_email"), "", resultSet.getDate("assignee_registered_on"), resultSet.getBoolean("assignee_is_active"));
			final PriorityDTO priorityDTO = new PriorityDTO(resultSet.getLong("task_priority_id"), PriorityEnum.valueOf(resultSet.getString("task_priority")));
			final TaskDTO taskDTO = new TaskDTO(resultSet.getLong("task_id"), resultSet.getString("task_name"), resultSet.getString("task_description"), taskStatusDTO, assigneeUserCredentialDTO, priorityDTO, resultSet.getDate("task_start_date"), resultSet.getDate("task_due_date"));
			final StatusDTO subtaskStatusDTO = new StatusDTO(resultSet.getLong("subtask_status_id"), StatusEnum.valueOf(resultSet.getString("subtask_status")));
			final UserCredentialDTO assignedToUserCredentialDTO = new UserCredentialDTO(resultSet.getLong("assigned_to_id"), resultSet.getString("assigned_to_username"), resultSet.getString("assigned_to_email"), "", resultSet.getDate("assigned_to_registered_on"), resultSet.getBoolean("assigned_to_is_active"));
			final PriorityDTO subtaskPriorityDTO = new PriorityDTO(resultSet.getLong("subtask_priority_id"), PriorityEnum.valueOf(resultSet.getString("subtask_priority")));
			final SubtaskDTO subtaskDTO = new SubtaskDTO(resultSet.getLong("subtask_id"), resultSet.getString("subtask_title"), resultSet.getString("subtask_description"), taskDTO, subtaskStatusDTO, subtaskPriorityDTO, assignedToUserCredentialDTO, resultSet.getDate("subtask_assigned_date"), resultSet.getDate("subtask_due_date"), resultSet.getInt("subtask_current_progress"), resultSet.getString("subtask_remarks"));
			final FeedbackDTO feedbackDTO = new FeedbackDTO(resultSet.getLong("feedback_id"), subtaskDTO, resultSet.getShort("feedback_rating"), resultSet.getString("feedback_remarks"));
			feedbackDTOs.add(feedbackDTO);
		}
		resultSet.close();
		preparedStatement.close();
		return feedbackDTOs;
	}

	private final String removeUserFeedbackQuery = "DELETE FROM tasker.feedback WHERE tasker.feedback.id = ?";

	@Override
	public boolean removeUserFeedback(final Connection connection, final long id) throws SQLException {
		int row = 0;
		final PreparedStatement preparedStatement = connection.prepareStatement(removeUserFeedbackQuery);
		preparedStatement.setLong(1, id);
		row = preparedStatement.executeUpdate();
		preparedStatement.close();
		return row > 0;
	}
}
