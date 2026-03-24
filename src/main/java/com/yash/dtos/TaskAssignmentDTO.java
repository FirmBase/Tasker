package com.yash.dtos;

import java.util.List;
import java.util.stream.Collectors;

public class TaskAssignmentDTO {
	private Long serialNumber;
	private UserDTO assigneeUserDTO;
	private TaskDTO taskDTO;
	private List<SubtaskAssignmentDTO> subtaskAssignmentDTOs;

	public TaskAssignmentDTO() {
		super();
	}

	public TaskAssignmentDTO(UserDTO assigneeUserDTO, TaskDTO taskDTO, List<SubtaskAssignmentDTO> subtaskAssignmentDTOs) {
		super();
		this.assigneeUserDTO = assigneeUserDTO;
		this.taskDTO = taskDTO;
		this.subtaskAssignmentDTOs = subtaskAssignmentDTOs;
	}

	public TaskAssignmentDTO(Long serialNumber, UserDTO assigneeUserDTO, TaskDTO taskDTO, List<SubtaskAssignmentDTO> subtaskAssignmentDTOs) {
		super();
		this.serialNumber = serialNumber;
		this.assigneeUserDTO = assigneeUserDTO;
		this.taskDTO = taskDTO;
		this.subtaskAssignmentDTOs = subtaskAssignmentDTOs;
	}

	@Override
	public String toString() {
		return String.format("{\"serial_number\": %d, \"assigneeUser\": %s, \"task\": %s, \"subtaskAssignments\": [%s]}", serialNumber, assigneeUserDTO, taskDTO, subtaskAssignmentDTOs.stream().map(SubtaskAssignmentDTO::toString).collect(Collectors.joining(", ")));
	}

	public UserDTO getAssigneeUserDTO() {
		return assigneeUserDTO;
	}

	public void setAssigneeUserDTO(UserDTO assigneeUserDTO) {
		this.assigneeUserDTO = assigneeUserDTO;
	}

	public TaskDTO getTaskDTO() {
		return taskDTO;
	}

	public void setTaskDTO(TaskDTO taskDTO) {
		this.taskDTO = taskDTO;
	}

	public List<SubtaskAssignmentDTO> getSubtaskAssignmentDTOs() {
		return this.subtaskAssignmentDTOs;
	}

	public void setSubtaskAssignmentDTOs(List<SubtaskAssignmentDTO> subtaskAssignmentDTOs) {
		this.subtaskAssignmentDTOs = subtaskAssignmentDTOs;
	}
}
