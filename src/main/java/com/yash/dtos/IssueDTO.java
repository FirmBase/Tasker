package com.yash.dtos;

import java.util.Date;

public class IssueDTO {
	private Long id;
	private TaskDTO taskDTO;
	private SubtaskDTO subtaskDTO;
	private String subject;
	private String description;
	private Date raisedOn;
	private Boolean resolveStatus;

	public IssueDTO() {
		super();
	}

	public IssueDTO(Long id, TaskDTO taskDTO, SubtaskDTO subtaskDTO, String subject, String description, Date raisedOn, Boolean resolveStatus) {
		super();
		this.id = id;
		this.taskDTO = taskDTO;
		this.subtaskDTO = subtaskDTO;
		this.subject = subject;
		this.description = description;
		this.raisedOn = raisedOn;
		this.resolveStatus = resolveStatus;
	}

	@Override
	public String toString() {
		return String.format("{\"id\": %d, \"taskDTO\": %s, \"subtaskDTO\": %s, \"subject\": \"%s\", \"description\": \"%s\", \"resolveStatus\": %b, \"raisedDate\": \"%tc\"}", id, taskDTO, subtaskDTO, subject, description, resolveStatus, raisedOn);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TaskDTO getTaskDTO() {
		return taskDTO;
	}

	public void setTaskDTO(TaskDTO taskDTO) {
		this.taskDTO = taskDTO;
	}

	public SubtaskDTO getSubtaskDTO() {
		return subtaskDTO;
	}

	public void setSubtaskDTO(SubtaskDTO subtaskDTO) {
		this.subtaskDTO = subtaskDTO;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getRaisedOn() {
		return raisedOn;
	}

	public void setRaisedOn(Date raisedOn) {
		this.raisedOn = raisedOn;
	}

	public Boolean getResolveStatus() {
		return resolveStatus;
	}

	public void setResolveStatus(Boolean resolveStatus) {
		this.resolveStatus = resolveStatus;
	}
}
