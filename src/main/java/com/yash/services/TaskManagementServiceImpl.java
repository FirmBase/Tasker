package com.yash.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.yash.dtos.NewSubtaskDTO;
import com.yash.dtos.NewTaskDTO;
import com.yash.dtos.PriorityDTO;
import com.yash.dtos.StatusDTO;
import com.yash.dtos.SubtaskAssignmentDTO;
import com.yash.dtos.SubtaskDTO;
import com.yash.dtos.TaskAssignmentDTO;
import com.yash.dtos.TaskDTO;
import com.yash.dtos.UserCredentialDTO;
import com.yash.dtos.UserDTO;
import com.yash.enumerations.PriorityEnum;
import com.yash.enumerations.StatusEnum;

public class TaskManagementServiceImpl implements TaskManagementService {
	final private String allTasksQuery =
		"SELECT" +
		"	tasker.task.id AS task_id," +
		"	tasker.task.title AS task_title," +
		"	tasker.task.description AS task_description," +
		"	tasker.task.status AS task_status_id," +
		"	(SELECT tasker.status.label FROM tasker.status WHERE tasker.status.id = tasker.task.status) AS task_status_label," +
		"	tasker.task.priority AS task_priority_id," +
		"	(SELECT tasker.priority.label FROM tasker.priority WHERE tasker.priority.id = tasker.task.priority) AS task_priority_label," +

		"	tasker.sub_task.id AS subtask_id," +
		"	tasker.sub_task.title AS subtask_title," +
		"	tasker.sub_task.description AS subtask_description," +
		"	tasker.sub_task.status AS subtask_status_id," +
		"	(SELECT tasker.status.label FROM tasker.status WHERE tasker.status.id = tasker.sub_task.status) AS subtask_status_label," +
		"	tasker.sub_task.priority AS subtask_priority_id," +
		"	(SELECT tasker.priority.label FROM tasker.priority WHERE tasker.priority.id = tasker.sub_task.priority) AS subtask_priority_label," +

		"	tasker.sub_task.assigned_to AS subtask_assigned_to_id," +
		"	assigned_to_user.id AS subtask_assigned_to_user_id," +
		"	assigned_to_user.name AS subtask_assigned_to_user_name," +
		"	assigned_to_user.designation AS subtask_assigned_to_user_designation," +
		"	assigned_to_user.birth_date AS subtask_assigned_to_user_birth_date," +
		"	assigned_to_user.blood_group AS subtask_assigned_to_user_blood_group," +
		"	assigned_to_user.employee_id AS subtask_assigned_to_user_employee_id," +
		"	assigned_to_user.temporary_contact_id AS subtask_assigned_to_user_temporary_contact_id," +
		"	assigned_to_user.permanent_contact_id AS subtask_assigned_to_user_permanent_contact_id," +
		"	tasker.user_credentials.username AS subtask_assigned_to_username," +
		"	tasker.user_credentials.email AS subtask_assigned_to_email," +
		"	tasker.user_credentials.registered_on AS subtask_assigned_to_registered_on," +
		"	tasker.user_credentials.is_active AS subtask_assigned_to_is_active," +

		"	tasker.sub_task.assigned_date AS subtask_assigned_date," +
		"	tasker.sub_task.due_date AS subtask_due_date," +
		"	tasker.sub_task.current_progress AS subtask_current_progress," +
		"	tasker.sub_task.last_remarks AS subtask_last_remarks," +

		"	tasker.task.assignee_id AS assignee_id," +
		"	assignee_user.id AS assignee_user_id," +
		"	assignee_user.name AS assignee_user_name," +
		"	assignee_user.designation AS assignee_user_designation," +
		"	assignee_user.birth_date AS assignee_user_birth_date," +
		"	assignee_user.blood_group AS assignee_user_blood_group," +
		"	assignee_user.employee_id AS assignee_user_employee_id," +
		"	assignee_user.temporary_contact_id AS assignee_user_temporary_contact_id," +
		"	assignee_user.permanent_contact_id AS assignee_user_permanent_contact_id," +
		"	tasker.user_credentials.username AS assignee_username," +
		"	tasker.user_credentials.email AS assignee_email," +
		"	tasker.user_credentials.registered_on AS assignee_registered_on," +
		"	tasker.user_credentials.is_active AS assignee_is_active," +

		"	tasker.task.start_date AS task_start_date," +
		"	tasker.task.due_date AS task_due_date" + "\n" +
		"FROM" +
		"	tasker.task" + "\n" +
		"LEFT JOIN" +
		"	tasker.sub_task" + "\n" +
		"ON" +
		"	tasker.task.id = tasker.sub_task.task_id" + "\n" +
		"INNER JOIN" +
		"	tasker.user_credentials" + "\n" +
		"ON" +
		"	tasker.task.assignee_id = tasker.user_credentials.id" + "\n" +
		"LEFT JOIN" +
		"	tasker.user assignee_user" + "\n" +
		"ON" +
		"	tasker.task.assignee_id = assignee_user.user_credentials_id" + "\n" +
		"LEFT JOIN" +
		"	tasker.user assigned_to_user" + "\n" +
		"ON" +
		"	tasker.sub_task.assigned_to = assigned_to_user.user_credentials_id" + "\n" +
		"WHERE" +
		"	tasker.task.assignee_id = ?" + "\n" +
		"ORDER BY" +
		"	tasker.sub_task.assigned_date DESC, tasker.sub_task.assigned_date DESC";

	@Override
	public List<TaskAssignmentDTO> getAllTasksAccordingToAssginee(Connection connection, long id) throws SQLException {
		final LinkedHashMap<Long, TaskAssignmentDTO> taskAssignmentDTOs = new LinkedHashMap<Long, TaskAssignmentDTO>();
		long count = 0;
		final PreparedStatement preparedStatement = connection.prepareStatement(allTasksQuery);
		preparedStatement.setLong(1, id);
		final ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			final long taskId = resultSet.getLong("task_id");
			TaskAssignmentDTO taskAssignmentDTO = taskAssignmentDTOs.get(taskId);
			if (taskAssignmentDTO == null) {
				final StatusDTO taskStatusDTO = new StatusDTO(resultSet.getLong("task_status_id"), StatusEnum.valueOf(resultSet.getString("task_status_label")));
				final PriorityDTO taskPriorityDTO = new PriorityDTO(resultSet.getLong("task_priority_id"), PriorityEnum.valueOf(resultSet.getString("task_priority_label")));
				final UserCredentialDTO assigneeUserCredentialDTO = new UserCredentialDTO(resultSet.getLong("assignee_id"), resultSet.getString("assignee_username"), resultSet.getString("assignee_email"), "", resultSet.getDate("assignee_registered_on"), resultSet.getBoolean("assignee_is_active"));
				final UserDTO assigneeUserDTO = new UserDTO(resultSet.getLong("assignee_user_id"), resultSet.getString("assignee_user_name"), resultSet.getString("assignee_user_designation"), resultSet.getDate("assignee_user_birth_date"), resultSet.getString("assignee_user_blood_group"), null, assigneeUserCredentialDTO, null, null);
				final TaskDTO taskDTO = new TaskDTO(taskId, resultSet.getString("task_title"), resultSet.getString("task_description"), taskStatusDTO, assigneeUserCredentialDTO, taskPriorityDTO, resultSet.getDate("task_start_date"), resultSet.getDate("task_due_date"));
				taskAssignmentDTO = new TaskAssignmentDTO(++count, assigneeUserDTO, taskDTO, new ArrayList<SubtaskAssignmentDTO>());

				if (resultSet.getLong("subtask_id") != 0) {
					final StatusDTO subtaskStatusDTO = new StatusDTO(resultSet.getLong("subtask_status_id"), StatusEnum.valueOf(resultSet.getString("subtask_status_label")));
					final PriorityDTO subtaskPriorityDTO = new PriorityDTO(resultSet.getLong("subtask_priority_id"), PriorityEnum.valueOf(resultSet.getString("subtask_priority_label")));
					final UserCredentialDTO assignedToUserCredentialDTO = new UserCredentialDTO(resultSet.getLong("subtask_assigned_to_id"), resultSet.getString("subtask_assigned_to_username"), resultSet.getString("subtask_assigned_to_email"), "", resultSet.getDate("subtask_assigned_to_registered_on"), resultSet.getBoolean("subtask_assigned_to_is_active"));
					final UserDTO assignedToUserDTO = new UserDTO(resultSet.getLong("subtask_assigned_to_user_id"), resultSet.getString("subtask_assigned_to_user_name"), resultSet.getString("subtask_assigned_to_user_designation"), resultSet.getDate("subtask_assigned_to_user_birth_date"), resultSet.getString("subtask_assigned_to_user_blood_group"), null, assignedToUserCredentialDTO, null, null);

					// Warning taskDTO is set null in subtaskDTO to avoid race condition
					final SubtaskDTO subtaskDTO = new SubtaskDTO(resultSet.getLong("subtask_id"), resultSet.getString("subtask_title"), resultSet.getString("subtask_description"), null, subtaskStatusDTO, subtaskPriorityDTO, assignedToUserCredentialDTO, resultSet.getDate("subtask_assigned_date"), resultSet.getDate("subtask_due_date"), resultSet.getInt("subtask_current_progress"), resultSet.getString("subtask_last_remarks"));
					final SubtaskAssignmentDTO subtaskAssignmentDTO = new SubtaskAssignmentDTO(assignedToUserDTO, subtaskDTO);
					taskAssignmentDTO.getSubtaskAssignmentDTOs().add(subtaskAssignmentDTO);
				}
				taskAssignmentDTOs.put(taskId, taskAssignmentDTO);
			}
			else {
				final StatusDTO subtaskStatusDTO = new StatusDTO(resultSet.getLong("subtask_status_id"), StatusEnum.valueOf(resultSet.getString("subtask_status_label")));
				final PriorityDTO subtaskPriorityDTO = new PriorityDTO(resultSet.getLong("subtask_priority_id"), PriorityEnum.valueOf(resultSet.getString("subtask_priority_label")));
				final UserCredentialDTO assignedToUserCredentialDTO = new UserCredentialDTO(resultSet.getLong("assignee_id"), resultSet.getString("assignee_username"), resultSet.getString("assignee_email"), "", resultSet.getDate("assignee_registered_on"), resultSet.getBoolean("assignee_is_active"));
				final UserDTO assignedToUserDTO = new UserDTO(resultSet.getLong("subtask_assigned_to_user_id"), resultSet.getString("subtask_assigned_to_user_name"), resultSet.getString("subtask_assigned_to_user_designation"), resultSet.getDate("subtask_assigned_to_user_birth_date"), resultSet.getString("subtask_assigned_to_user_blood_group"), null, assignedToUserCredentialDTO, null, null);

				// Warning taskDTO is set null in subtaskDTO to avoid race condition
				final SubtaskDTO subtaskDTO = new SubtaskDTO(resultSet.getLong("subtask_id"), resultSet.getString("subtask_title"), resultSet.getString("subtask_description"), null, subtaskStatusDTO, subtaskPriorityDTO, assignedToUserCredentialDTO, resultSet.getDate("subtask_assigned_date"), resultSet.getDate("subtask_due_date"), resultSet.getInt("subtask_current_progress"), resultSet.getString("subtask_last_remarks"));
				final SubtaskAssignmentDTO subtaskAssignmentDTO = new SubtaskAssignmentDTO(assignedToUserDTO, subtaskDTO);
				taskAssignmentDTO.getSubtaskAssignmentDTOs().add(subtaskAssignmentDTO);
			}
		}
		resultSet.close();
		preparedStatement.close();
		return taskAssignmentDTOs.values().stream().collect(Collectors.toList());
	}

	final private String allTasksAssignedToQuery =
		"SELECT" +
		"	tasker.task.id AS task_id," +
		"	tasker.task.title AS task_title," +
		"	tasker.task.description AS task_description," +
		"	tasker.task.status AS task_status_id," +
		"	(SELECT tasker.status.label FROM tasker.status WHERE tasker.status.id = tasker.task.status) AS task_status_label," +
		"	tasker.task.priority AS task_priority_id," +
		"	(SELECT tasker.priority.label FROM tasker.priority WHERE tasker.priority.id = tasker.task.priority) AS task_priority_label," +

		"	tasker.sub_task.id AS subtask_id," +
		"	tasker.sub_task.title AS subtask_title," +
		"	tasker.sub_task.description AS subtask_description," +
		"	tasker.sub_task.status AS subtask_status_id," +
		"	(SELECT tasker.status.label FROM tasker.status WHERE tasker.status.id = tasker.sub_task.status) AS subtask_status_label," +
		"	tasker.sub_task.priority AS subtask_priority_id," +
		"	(SELECT tasker.priority.label FROM tasker.priority WHERE tasker.priority.id = tasker.sub_task.priority) AS subtask_priority_label," +

		"	tasker.sub_task.assigned_to AS subtask_assigned_to_id," +
		"	assigned_to_user.id AS subtask_assigned_to_user_id," +
		"	assigned_to_user.name AS subtask_assigned_to_user_name," +
		"	assigned_to_user.designation AS subtask_assigned_to_user_designation," +
		"	assigned_to_user.birth_date AS subtask_assigned_to_user_birth_date," +
		"	assigned_to_user.blood_group AS subtask_assigned_to_user_blood_group," +
		"	assigned_to_user.employee_id AS subtask_assigned_to_user_employee_id," +
		"	assigned_to_user.temporary_contact_id AS subtask_assigned_to_user_temporary_contact_id," +
		"	assigned_to_user.permanent_contact_id AS subtask_assigned_to_user_permanent_contact_id," +
		"	tasker.user_credentials.username AS subtask_assigned_to_username," +
		"	tasker.user_credentials.email AS subtask_assigned_to_email," +
		"	tasker.user_credentials.registered_on AS subtask_assigned_to_registered_on," +
		"	tasker.user_credentials.is_active AS subtask_assigned_to_is_active," +

		"	tasker.sub_task.assigned_date AS subtask_assigned_date," +
		"	tasker.sub_task.due_date AS subtask_due_date," +
		"	tasker.sub_task.current_progress AS subtask_current_progress," +
		"	tasker.sub_task.last_remarks AS subtask_last_remarks," +

		"	tasker.task.assignee_id AS assignee_id," +
		"	assignee_user.id AS assignee_user_id," +
		"	assignee_user.name AS assignee_user_name," +
		"	assignee_user.designation AS assignee_user_designation," +
		"	assignee_user.birth_date AS assignee_user_birth_date," +
		"	assignee_user.blood_group AS assignee_user_blood_group," +
		"	assignee_user.employee_id AS assignee_user_employee_id," +
		"	assignee_user.temporary_contact_id AS assignee_user_temporary_contact_id," +
		"	assignee_user.permanent_contact_id AS assignee_user_permanent_contact_id," +
		"	tasker.user_credentials.username AS assignee_username," +
		"	tasker.user_credentials.email AS assignee_email," +
		"	tasker.user_credentials.registered_on AS assignee_registered_on," +
		"	tasker.user_credentials.is_active AS assignee_is_active," +

		"	tasker.task.start_date AS task_start_date," +
		"	tasker.task.due_date AS task_due_date" + "\n" +
		"FROM" +
		"	tasker.task" + "\n" +
		"RIGHT JOIN" +
		"	tasker.sub_task" + "\n" +
		"ON" +
		"	tasker.task.id = tasker.sub_task.task_id" + "\n" +
		"INNER JOIN" +
		"	tasker.user_credentials" + "\n" +
		"ON" +
		"	tasker.task.assignee_id = tasker.user_credentials.id" + "\n" +
		"LEFT JOIN" +
		"	tasker.user assignee_user" + "\n" +
		"ON" +
		"	tasker.task.assignee_id = assignee_user.user_credentials_id" + "\n" +
		"LEFT JOIN" +
		"	tasker.user assigned_to_user" + "\n" +
		"ON" +
		"	tasker.sub_task.assigned_to = assigned_to_user.user_credentials_id" + "\n" +
		"WHERE" +
		"	tasker.sub_task.assigned_to = ?" + "\n" +
		"ORDER BY" +
		"	tasker.sub_task.assigned_date DESC, tasker.sub_task.assigned_date DESC";

	@Override
	public List<TaskAssignmentDTO> getAllTasksAssginedTo(Connection connection, long id) throws SQLException {
		final LinkedHashMap<Long, TaskAssignmentDTO> taskAssignmentDTOs = new LinkedHashMap<Long, TaskAssignmentDTO>();
		long count = 0;
		final PreparedStatement preparedStatement = connection.prepareStatement(allTasksAssignedToQuery);
		preparedStatement.setLong(1, id);
		final ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			final long taskId = resultSet.getLong("task_id");
			TaskAssignmentDTO taskAssignmentDTO = taskAssignmentDTOs.get(taskId);
			if (taskAssignmentDTO == null) {
				final StatusDTO taskStatusDTO = new StatusDTO(resultSet.getLong("task_status_id"), StatusEnum.valueOf(resultSet.getString("task_status_label")));
				final PriorityDTO taskPriorityDTO = new PriorityDTO(resultSet.getLong("task_priority_id"), PriorityEnum.valueOf(resultSet.getString("task_priority_label")));
				final UserCredentialDTO assigneeUserCredentialDTO = new UserCredentialDTO(resultSet.getLong("assignee_id"), resultSet.getString("assignee_username"), resultSet.getString("assignee_email"), "", resultSet.getDate("assignee_registered_on"), resultSet.getBoolean("assignee_is_active"));
				final UserDTO assigneeUserDTO = new UserDTO(resultSet.getLong("assignee_user_id"), resultSet.getString("assignee_user_name"), resultSet.getString("assignee_user_designation"), resultSet.getDate("assignee_user_birth_date"), resultSet.getString("assignee_user_blood_group"), null, assigneeUserCredentialDTO, null, null);
				final TaskDTO taskDTO = new TaskDTO(taskId, resultSet.getString("task_title"), resultSet.getString("task_description"), taskStatusDTO, assigneeUserCredentialDTO, taskPriorityDTO, resultSet.getDate("task_start_date"), resultSet.getDate("task_due_date"));
				taskAssignmentDTO = new TaskAssignmentDTO(++count, assigneeUserDTO, taskDTO, new ArrayList<SubtaskAssignmentDTO>());

				if (resultSet.getLong("subtask_id") != 0) {
					final StatusDTO subtaskStatusDTO = new StatusDTO(resultSet.getLong("subtask_status_id"), StatusEnum.valueOf(resultSet.getString("subtask_status_label")));
					final PriorityDTO subtaskPriorityDTO = new PriorityDTO(resultSet.getLong("subtask_priority_id"), PriorityEnum.valueOf(resultSet.getString("subtask_priority_label")));
					final UserCredentialDTO assignedToUserCredentialDTO = new UserCredentialDTO(resultSet.getLong("subtask_assigned_to_id"), resultSet.getString("subtask_assigned_to_username"), resultSet.getString("subtask_assigned_to_email"), "", resultSet.getDate("subtask_assigned_to_registered_on"), resultSet.getBoolean("subtask_assigned_to_is_active"));
					final UserDTO assignedToUserDTO = new UserDTO(resultSet.getLong("subtask_assigned_to_user_id"), resultSet.getString("subtask_assigned_to_user_name"), resultSet.getString("subtask_assigned_to_user_designation"), resultSet.getDate("subtask_assigned_to_user_birth_date"), resultSet.getString("subtask_assigned_to_user_blood_group"), null, assignedToUserCredentialDTO, null, null);

					// Warning taskDTO is set null in subtaskDTO to avoid race condition
					final SubtaskDTO subtaskDTO = new SubtaskDTO(resultSet.getLong("subtask_id"), resultSet.getString("subtask_title"), resultSet.getString("subtask_description"), null, subtaskStatusDTO, subtaskPriorityDTO, assignedToUserCredentialDTO, resultSet.getDate("subtask_assigned_date"), resultSet.getDate("subtask_due_date"), resultSet.getInt("subtask_current_progress"), resultSet.getString("subtask_last_remarks"));
					final SubtaskAssignmentDTO subtaskAssignmentDTO = new SubtaskAssignmentDTO(assignedToUserDTO, subtaskDTO);
					taskAssignmentDTO.getSubtaskAssignmentDTOs().add(subtaskAssignmentDTO);
				}
				taskAssignmentDTOs.put(taskId, taskAssignmentDTO);
			}
			else {
				final StatusDTO subtaskStatusDTO = new StatusDTO(resultSet.getLong("subtask_status_id"), StatusEnum.valueOf(resultSet.getString("subtask_status_label")));
				final PriorityDTO subtaskPriorityDTO = new PriorityDTO(resultSet.getLong("subtask_priority_id"), PriorityEnum.valueOf(resultSet.getString("subtask_priority_label")));
				final UserCredentialDTO assignedToUserCredentialDTO = new UserCredentialDTO(resultSet.getLong("assignee_id"), resultSet.getString("assignee_username"), resultSet.getString("assignee_email"), "", resultSet.getDate("assignee_registered_on"), resultSet.getBoolean("assignee_is_active"));
				final UserDTO assignedToUserDTO = new UserDTO(resultSet.getLong("subtask_assigned_to_user_id"), resultSet.getString("subtask_assigned_to_user_name"), resultSet.getString("subtask_assigned_to_user_designation"), resultSet.getDate("subtask_assigned_to_user_birth_date"), resultSet.getString("subtask_assigned_to_user_blood_group"), null, assignedToUserCredentialDTO, null, null);

				// Warning taskDTO is set null in subtaskDTO to avoid race condition
				final SubtaskDTO subtaskDTO = new SubtaskDTO(resultSet.getLong("subtask_id"), resultSet.getString("subtask_title"), resultSet.getString("subtask_description"), null, subtaskStatusDTO, subtaskPriorityDTO, assignedToUserCredentialDTO, resultSet.getDate("subtask_assigned_date"), resultSet.getDate("subtask_due_date"), resultSet.getInt("subtask_current_progress"), resultSet.getString("subtask_last_remarks"));
				final SubtaskAssignmentDTO subtaskAssignmentDTO = new SubtaskAssignmentDTO(assignedToUserDTO, subtaskDTO);
				taskAssignmentDTO.getSubtaskAssignmentDTOs().add(subtaskAssignmentDTO);
			}
		}
		resultSet.close();
		preparedStatement.close();
		return taskAssignmentDTOs.values().stream().collect(Collectors.toList());
	}

	final private String allSubtaskQuery =
		"SELECT" +
		"	tasker.sub_task.id AS subtask_id," +
		"	tasker.sub_task.title AS subtask_title," +
		"	tasker.sub_task.description AS subtask_description," +
		"	tasker.sub_task.task_id AS subtask_task_id," +
		"	tasker.sub_task.status AS subtask_status_id," +
		"	(SELECT tasker.status.label FROM tasker.status WHERE tasker.status.id = tasker.sub_task.status) AS subtask_status_label," +
		"	tasker.sub_task.priority AS subtask_priority_id," +
		"	(SELECT tasker.priority.label FROM tasker.priority WHERE tasker.priority.id = tasker.sub_task.priority) AS subtask_priority_label," +

		"	tasker.sub_task.assigned_to AS subtask_assigned_to_id," +
		"	tasker.user.id AS subtask_assigned_to_user_id," +
		"	tasker.user.name AS subtask_assigned_to_name," +
		"	tasker.user.designation AS subtask_assigned_to_designation," +
		"	tasker.user.birth_date AS subtask_assigned_to_birth_date," +
		"	tasker.user.blood_group AS subtask_assigned_to_blood_group," +
		"	tasker.user.employee_id AS subtask_assigned_to_employee_id," +
		"	tasker.user.temporary_contact_id AS subtask_assigned_to_temporary_contact_id," +
		"	tasker.user.permanent_contact_id AS subtask_assigned_to_permanent_contact_id," +
		"	tasker.user_credentials.username AS subtask_assigned_to_username," +
		"	tasker.user_credentials.email AS subtask_assigned_to_email," +
		"	tasker.user_credentials.registered_on AS subtask_assigned_to_registered_on," +
		"	tasker.user_credentials.is_active AS subtask_assigned_to_is_active," +

		"	tasker.sub_task.assigned_date AS subtask_assigned_date," +
		"	tasker.sub_task.due_date AS subtask_due_date," +
		"	tasker.sub_task.current_progress AS subtask_current_progress," +
		"	tasker.sub_task.last_remarks AS subtask_last_remarks" + "\n" +
		"FROM" +
		"	tasker.sub_task" + "\n" +
		"INNER JOIN" +
		"	tasker.user_credentials" + "\n" +
		"ON" +
		"	tasker.sub_task.assigned_to = tasker.user_credentials.id" + "\n" +
		"LEFT JOIN" +
		"	tasker.user" + "\n" +
		"ON" +
		"	tasker.user.user_credentials_id = tasker.sub_task.assigned_to" + "\n" +
		"WHERE" +
		"	tasker.sub_task.task_id = ?" + "\n" +
		"ORDER BY" +
		"	tasker.sub_task.assigned_date";

	public List<SubtaskAssignmentDTO> getAllSubtaskManagements(final Connection connection, final Long taskId) throws SQLException {
		final List<SubtaskAssignmentDTO> subtaskAssignmentDTOs = new ArrayList<SubtaskAssignmentDTO>();
		final PreparedStatement preparedStatement = connection.prepareStatement(allSubtaskQuery);
		preparedStatement.setLong(1, taskId);
		final ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			StatusDTO statusDTO = new StatusDTO(resultSet.getLong("subtask_status_id"), StatusEnum.valueOf(resultSet.getString("subtask_status_label")));
			PriorityDTO priorityDTO = new PriorityDTO(resultSet.getLong("subtask_priority_id"), PriorityEnum.valueOf(resultSet.getString("subtask_priority_label")));
			UserCredentialDTO assignedToUserCredentialDTO = new UserCredentialDTO(resultSet.getLong("subtask_assigned_to_id"), resultSet.getString("subtask_assigned_to_username"), resultSet.getString("subtask_assigned_to_email"), "", resultSet.getDate("subtask_assigned_to_registered_on"), resultSet.getBoolean("subtask_assigned_to_is_active"));
			UserDTO assignedToUserDTO = new UserDTO(resultSet.getLong("subtask_assigned_to_user_id"), resultSet.getString("subtask_assigned_to_name"), resultSet.getString("subtask_assigned_to_designation"), resultSet.getDate("subtask_assigned_to_birth_date"), resultSet.getString("subtask_assigned_to_blood_group"), null, assignedToUserCredentialDTO, null, null);
			SubtaskAssignmentDTO subtaskAssignmentDTO = new SubtaskAssignmentDTO(assignedToUserDTO, new SubtaskDTO(resultSet.getLong("subtask_id"), resultSet.getString("subtask_title"), resultSet.getString("subtask_description"), null, statusDTO, priorityDTO, assignedToUserCredentialDTO, resultSet.getDate("subtask_assigned_date"), resultSet.getDate("subtask_due_date"), resultSet.getInt("subtask_current_progress"), resultSet.getString("subtask_last_remarks")));
			subtaskAssignmentDTOs.add(subtaskAssignmentDTO);
		}
		resultSet.close();
		preparedStatement.close();
		return subtaskAssignmentDTOs;
	}

	final private String getAllStatusQuery = "SELECT id, label FROM tasker.status";

	public List<StatusDTO> getAllStatus(final Connection connection) throws SQLException {
		List<StatusDTO> statusDTOs = new ArrayList<StatusDTO>();
		final Statement statement = connection.createStatement();
		final ResultSet resultSet = statement.executeQuery(getAllStatusQuery);
		while (resultSet.next()) {
			final StatusDTO statusDTO = new StatusDTO(resultSet.getLong("id"), StatusEnum.valueOf(resultSet.getString("label")));
			statusDTOs.add(statusDTO);
		}
		resultSet.close();
		statement.close();
		return statusDTOs;
	}

	final private String getAllPriorityQuery = "SELECT id, label FROM tasker.priority";

	public List<PriorityDTO> getAllPriority(final Connection connection) throws SQLException {
		List<PriorityDTO> priorityDTOs = new ArrayList<PriorityDTO>();
		final Statement statement = connection.createStatement();
		final ResultSet resultSet = statement.executeQuery(getAllPriorityQuery);
		while (resultSet.next()) {
			final PriorityDTO priorityDTO = new PriorityDTO(resultSet.getLong("id"), PriorityEnum.valueOf(resultSet.getString("label")));
			priorityDTOs.add(priorityDTO);
		}
		resultSet.close();
		statement.close();
		return priorityDTOs;
	}

	private String getStatusLabelIdQuery = "SELECT id FROM tasker.status WHERE label = ?";

	public long getStatusLabelId(final Connection connection, final StatusEnum statusEnum) throws SQLException {
		long id = 0l;
		final PreparedStatement preparedStatement = connection.prepareStatement(getStatusLabelIdQuery);
		preparedStatement.setString(1, statusEnum.toString());
		final ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next())
			id = resultSet.getLong(1);
		resultSet.close();
		preparedStatement.close();
		return id;
	}

	private String getPriorityLabelIdQuery = "SELECT id FROM tasker.priority WHERE label = ?";

	public long getStatusLabelId(final Connection connection, final PriorityEnum priorityEnum) throws SQLException {
		long id = 0l;
		final PreparedStatement preparedStatement = connection.prepareStatement(getPriorityLabelIdQuery);
		preparedStatement.setString(1, priorityEnum.toString());
		final ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next())
			id = resultSet.getLong(1);
		resultSet.close();
		preparedStatement.close();
		return id;
	}

	final String addNewTaskQuery = "INSERT INTO tasker.task(title, description, status, assignee_id, priority, start_date, due_date) VALUES(?, ?, ?, ?, ?, ?, ?)";
	final String addNewSubtaskQuery = "INSERT INTO tasker.sub_task(title, description, task_id, status, priority, assigned_to, assigned_date, due_date, current_progress, last_remarks) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	public boolean registerNewTask(final Connection connection, final NewTaskDTO newTaskDTO) throws SQLException {
		boolean state = false;
		final boolean autoConnectionState = connection.getAutoCommit();
		connection.setAutoCommit(false);
		try {
			final PreparedStatement taskPreparedStatement = connection.prepareStatement(addNewTaskQuery, PreparedStatement.RETURN_GENERATED_KEYS);
			taskPreparedStatement.setString(1, newTaskDTO.getTaskTitle());
			taskPreparedStatement.setString(2, newTaskDTO.getTaskDescription());
			taskPreparedStatement.setLong(3, getStatusLabelId(connection, StatusEnum.PENDING));
			taskPreparedStatement.setLong(4, newTaskDTO.getTaskPriorityId());
			taskPreparedStatement.setLong(5, newTaskDTO.getTaskAssigneeId());
			taskPreparedStatement.setDate(6, new java.sql.Date(newTaskDTO.getTaskStartDate().getTime()));
			taskPreparedStatement.setDate(7, new java.sql.Date(newTaskDTO.getTaskDueDate().getTime()));
			taskPreparedStatement.executeUpdate();
			final ResultSet resultSet = taskPreparedStatement.getGeneratedKeys();
			resultSet.next();
			final long taskId = resultSet.getLong(1);
			resultSet.close();
			taskPreparedStatement.close();

			final PreparedStatement subtasksPreparedStatement = connection.prepareStatement(addNewSubtaskQuery);
			for (final NewSubtaskDTO newSubtaskDTO: newTaskDTO.getNewSubtaskDTOs()) {
				subtasksPreparedStatement.setString(1, newSubtaskDTO.getSubtaskTitle());
				subtasksPreparedStatement.setString(2, newSubtaskDTO.getSubtaskDescription());
				subtasksPreparedStatement.setLong(3, taskId);
				subtasksPreparedStatement.setLong(4, getStatusLabelId(connection, StatusEnum.PENDING));
				subtasksPreparedStatement.setLong(5, newSubtaskDTO.getSubtaskPriorityId());
				subtasksPreparedStatement.setLong(6, newSubtaskDTO.getSubtaskAssignedToId());
				subtasksPreparedStatement.setDate(7, new java.sql.Date(newSubtaskDTO.getSubtaskAssginedDate().getTime()));
				subtasksPreparedStatement.setDate(8, new java.sql.Date(newSubtaskDTO.getSubtaskDueDate().getTime()));
				subtasksPreparedStatement.setInt(9, 0);
				subtasksPreparedStatement.setString(10, "");
				subtasksPreparedStatement.addBatch();
			}
			final int[] rows = subtasksPreparedStatement.executeBatch();
			subtasksPreparedStatement.close();
			state = Arrays.stream(rows).allMatch(row -> row > 0);

			connection.commit();
		}
		catch (SQLException sqlException) {
			connection.rollback();
			sqlException.printStackTrace();
		}
		finally {
			connection.setAutoCommit(autoConnectionState);
		}
		return state;
	}

	private final String saveSubtaskProgressQuery = "UPDATE tasker.sub_task SET status = ?, current_progress = ?, last_remarks = ? WHERE id = ?";
	private final String saveTaskProgressQuery = "UPDATE tasker.task SET tasker.task.status = ? WHERE tasker.task.id = ?";

	public boolean saveSubtaskProgress(final Connection connection, final long taskId, final long subtaskId, final Short currentProgress, final String lastRemarks) throws SQLException {
		int row1 = 0, row2 = 0;
		final boolean autoCommitState = connection.getAutoCommit();
		try {
			connection.setAutoCommit(false);

			final PreparedStatement preparedStatement1 = connection.prepareStatement(saveSubtaskProgressQuery);
			preparedStatement1.setLong(4, subtaskId);
			preparedStatement1.setLong(1, getStatusLabelId(connection, currentProgress > 0 ? currentProgress > 99 ? StatusEnum.COMPLETED : StatusEnum.WORKING : StatusEnum.PENDING));
			preparedStatement1.setShort(2, currentProgress);
			preparedStatement1.setString(3, lastRemarks);
			row1 = preparedStatement1.executeUpdate();
			preparedStatement1.close();

			final PreparedStatement preparedStatement2 = connection.prepareStatement(saveTaskProgressQuery);
			preparedStatement2.setLong(2, taskId);
			preparedStatement2.setLong(1, getSubtaskStatusProgress(connection, taskId).getId());
			row2 = preparedStatement2.executeUpdate();
			preparedStatement2.close();

			connection.commit();
		}
		catch (SQLException sqlException) {
			connection.rollback();
			sqlException.printStackTrace();
		}
		finally {
			connection.setAutoCommit(autoCommitState);
		}
		return (row1 > 0) && (row2 > 0);
	}

	final String subtaskProgressStatusQuery = "SELECT tasker.sub_task.status AS subtask_status_id, (SELECT tasker.status.label FROM tasker.status WHERE tasker.status.id = tasker.sub_task.status) AS subtask_status_label FROM tasker.sub_task WHERE tasker.sub_task.task_id = ?";

	public StatusDTO getSubtaskStatusProgress(final Connection connection, final long taskId) throws SQLException {
		final PreparedStatement preparedStatement = connection.prepareStatement(subtaskProgressStatusQuery);
		preparedStatement.setLong(1, taskId);
		final ResultSet resultSet = preparedStatement.executeQuery();
		final List<StatusDTO> statusDTOs = new ArrayList<StatusDTO>();
		while (resultSet.next()) {
			final StatusDTO statusDTO = new StatusDTO(resultSet.getLong("subtask_status_id"), StatusEnum.valueOf(resultSet.getString("subtask_status_label")));
			statusDTOs.add(statusDTO);
		}
		preparedStatement.close();
		if (statusDTOs.stream().allMatch(statusDTO -> statusDTO.getStatusEnum() == StatusEnum.COMPLETED))
			// return statusDTOs.get(0);
			return new StatusDTO(getStatusLabelId(connection, StatusEnum.COMPLETED), StatusEnum.COMPLETED);
		else if (statusDTOs.stream().allMatch(statusDTO -> statusDTO.getStatusEnum() == StatusEnum.PENDING))
			// return statusDTOs.get(0);
			return new StatusDTO(getStatusLabelId(connection, StatusEnum.PENDING), StatusEnum.PENDING);
		else if (statusDTOs.stream().anyMatch(statusDTO -> statusDTO.getStatusEnum() == StatusEnum.WORKING))
			return new StatusDTO(getStatusLabelId(connection, StatusEnum.WORKING), StatusEnum.WORKING);
		else
			return new StatusDTO(getStatusLabelId(connection, StatusEnum.WAITING), StatusEnum.WAITING);
	}
}
