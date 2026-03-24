package com.yash.dtos;

public class FeedbackDTO {
	private Long id;
	// private UserCredentialDTO userCredentialDTO;
	// private TaskDTO taskDTO;
	private SubtaskDTO subtaskDTO;
	private Short rating;
	private String remarks;

	public FeedbackDTO() {
		super();
	}

	public FeedbackDTO(Long id, SubtaskDTO subtaskDTO, Short rating, String remarks) {
		this.id = id;
		this.subtaskDTO = subtaskDTO;
		this.rating = rating;
		this.remarks = remarks;
	}

	public FeedbackDTO(Long id, UserCredentialDTO userCredentialDTO, TaskDTO taskDTO, SubtaskDTO subtaskDTO, Short rating, String remarks) {
		this.id = id;
		// this.userCredentialDTO = userCredentialDTO;
		// this.taskDTO = taskDTO;
		this.subtaskDTO = subtaskDTO;
		this.rating = rating;
		this.remarks = remarks;
	}

	@Override
	public String toString() {
		// return String.format("{\"id\": %d, \"userCredential\": %s, \"task\": %s, \"subtask\": %s, \"rating\": %d, \"remarks\": \"%s\"}", id, userCredentialDTO, taskDTO, subtaskDTO, rating, remarks);
		return String.format("{\"id\": %d, \"subtask\": %s, \"rating\": %d, \"remarks\": \"%s\"}", id, subtaskDTO, rating, remarks);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	// public UserCredentialDTO getUserCredentialDTO() {
		// return userCredentialDTO;
	// }

	// public void setUserCredentialDTO(UserCredentialDTO userCredentialDTO) {
		// this.userCredentialDTO = userCredentialDTO;
	// }

	// public TaskDTO getTaskDTO() {
		// return taskDTO;
	// }

	// public void setTaskDTO(TaskDTO taskDTO) {
		// this.taskDTO = taskDTO;
	// }

	public SubtaskDTO getSubtaskDTO() {
		return subtaskDTO;
	}

	public void setSubtaskDTO(SubtaskDTO subtaskDTO) {
		this.subtaskDTO = subtaskDTO;
	}

	public Short getRating() {
		return rating;
	}

	public void setRating(Short rating) {
		this.rating = rating;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
