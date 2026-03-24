package com.yash.dtos;

import java.util.Date;

public class TaskDTO {
	private Long id;
	private String title;
	private String description;
	private StatusDTO statusDTO;
	private UserCredentialDTO assigneeUserCredentialDTO;
	private PriorityDTO priorityDTO;
	private Date startDate;
	private Date dueDate;

	public TaskDTO() {
		super();
	}

	public TaskDTO(Long id, String title, String description, StatusDTO statusDTO, UserCredentialDTO assigneeUserCredentialDTO, PriorityDTO priorityDTO, Date startDate, Date dueDate) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.statusDTO = statusDTO;
		this.assigneeUserCredentialDTO = assigneeUserCredentialDTO;
		this.priorityDTO = priorityDTO;
		this.startDate = startDate;
		this.dueDate = dueDate;
	}

	@Override
	public String toString() {
		return String.format("{\"id\": %d, \"title\": \"%s\", \"description\": \"%s\", \"status\": %s, \"assignee\": %s, \"priority\": %s, \"startDate\": \"%tc\", \"dueDate\": \"%tc\"}", id, title, description, statusDTO, assigneeUserCredentialDTO, priorityDTO, startDate, dueDate);
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

	public StatusDTO getStatusDTO() {
		return statusDTO;
	}

	public void setStatusDTO(StatusDTO statusDTO) {
		this.statusDTO = statusDTO;
	}

	public UserCredentialDTO getAssigneeUserCredentialDTO() {
		return assigneeUserCredentialDTO;
	}

	public void setAssigneeUserCredentialDTO(UserCredentialDTO assigneeUserCredentialDTO) {
		this.assigneeUserCredentialDTO = assigneeUserCredentialDTO;
	}

	public PriorityDTO getPriorityDTO() {
		return priorityDTO;
	}

	public void setPriorityId(PriorityDTO priorityDTO) {
		this.priorityDTO = priorityDTO;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
}
