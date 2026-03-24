package com.yash.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.yash.dtos.NewTaskDTO;
import com.yash.dtos.PriorityDTO;
import com.yash.dtos.StatusDTO;
import com.yash.dtos.SubtaskAssignmentDTO;
import com.yash.dtos.TaskAssignmentDTO;

public interface TaskManagementService {
	public List<TaskAssignmentDTO> getAllTasksAccordingToAssginee(Connection connection, long id) throws SQLException;
	public List<TaskAssignmentDTO> getAllTasksAssginedTo(Connection connection, long id) throws SQLException;
	public List<SubtaskAssignmentDTO> getAllSubtaskManagements(final Connection connection, final Long taskId) throws SQLException;
	public List<StatusDTO> getAllStatus(final Connection connection) throws SQLException;
	public List<PriorityDTO> getAllPriority(final Connection connection) throws SQLException;
	public boolean registerNewTask(final Connection connection, final NewTaskDTO newTaskDTO) throws SQLException;
	public boolean saveSubtaskProgress(final Connection connection, final long taskId, final long subtaskId, final Short currentProgress, final String lastRemarks) throws SQLException;
}
