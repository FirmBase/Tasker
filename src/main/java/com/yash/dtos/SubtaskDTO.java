package com.yash.dtos;

import java.util.Date;

public class SubtaskDTO {
	private Long id;
	private String title;
	private String description;
	private TaskDTO taskDTO;
	private StatusDTO statusDTO;
	private PriorityDTO priorityDTO;
	private UserCredentialDTO assignedToUserCredentialDTO;
	private Date assignedDate;
	private Date dueDate;
	private Integer currentProgress;
	private String lastRemarks;

	public SubtaskDTO() {
		super();
	}

	public SubtaskDTO(Long id, String title, String description, TaskDTO taskDTO, StatusDTO statusDTO, PriorityDTO priorityDTO, UserCredentialDTO assignedToUserCredentialDTO, Date assignedDate, Date dueDate, Integer currentProgress, String lastRemarks) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.taskDTO = taskDTO;
		this.statusDTO = statusDTO;
		this.priorityDTO = priorityDTO;
		this.assignedToUserCredentialDTO = assignedToUserCredentialDTO;
		this.assignedDate = assignedDate;
		this.dueDate = dueDate;
		this.currentProgress = currentProgress;
		this.lastRemarks = lastRemarks;
	}

	@Override
	public String toString() {
		return String.format("{\"id\": %d, \"title\": \"%s\", \"description\": \"%s\", \"task\": %s, \"status\": %s, \"priority\": %s, \"assignedTo\": %s, \"assignedDate\": \"%tc\", \"dueDate\": \"%tc\", \"currentProgress\": %d, \"lastRemarks\": \"%s\"}", id, title, description, taskDTO, statusDTO, priorityDTO, assignedToUserCredentialDTO, assignedDate, dueDate, currentProgress, lastRemarks);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TaskDTO getTaskDTO() {
		return taskDTO;
	}

	public void setTaskDTO(TaskDTO taskDTO) {
		this.taskDTO = taskDTO;
	}

	public StatusDTO getStatusDTO() {
		return statusDTO;
	}

	public void setStatusDTO(StatusDTO statusDTO) {
		this.statusDTO = statusDTO;
	}

	public PriorityDTO getPriorityDTO() {
		return priorityDTO;
	}

	public void setPriorityDTO(PriorityDTO priorityDTO) {
		this.priorityDTO = priorityDTO;
	}

	public UserCredentialDTO getAssignedToUserCredentialDTO() {
		return assignedToUserCredentialDTO;
	}

	public void setAssignedToId(UserCredentialDTO assignedToUserCredentialDTO) {
		this.assignedToUserCredentialDTO = assignedToUserCredentialDTO;
	}

	public Date getAssignedDate() {
		return assignedDate;
	}

	public void setAssignedDate(Date assignedDate) {
		this.assignedDate = assignedDate;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Integer getCurrentProgress() {
		return currentProgress;
	}

	public void setCurrentProgress(Integer currentProgress) {
		this.currentProgress = currentProgress;
	}

	public String getLastRemarks() {
		return lastRemarks;
	}

	public void setLastRemarks(String lastRemarks) {
		this.lastRemarks = lastRemarks;
	}
}
