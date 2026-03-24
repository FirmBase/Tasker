package com.yash.dtos;

public class SubtaskAssignmentDTO {
	private UserDTO assignedToUserDTO;
	private SubtaskDTO subtaskDTO;

	public SubtaskAssignmentDTO() {
		super();
	}

	public SubtaskAssignmentDTO(UserDTO assignedToUserDTO, SubtaskDTO subtaskDTO) {
		super();
		this.assignedToUserDTO = assignedToUserDTO;
		this.subtaskDTO = subtaskDTO;
	}

	@Override
	public String toString() {
		return String.format("{\"assignedTo\": %s, \"subtask\": %s}", assignedToUserDTO, subtaskDTO);
	}

	public UserDTO getAssignedToDTO() {
		return assignedToUserDTO;
	}

	public void setAssignedToDTO(UserDTO assignedToUserDTO) {
		this.assignedToUserDTO = assignedToUserDTO;
	}

	public SubtaskDTO getSubtaskDTO() {
		return subtaskDTO;
	}

	public void setSubtaskDTO(SubtaskDTO subtaskDTO) {
		this.subtaskDTO = subtaskDTO;
	}
}
