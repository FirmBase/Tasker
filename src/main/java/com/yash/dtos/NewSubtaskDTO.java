package com.yash.dtos;

import java.util.Date;

public class NewSubtaskDTO {
	private String subtaskTitle;
	private String subtaskDescription;
	private Long subtaskAssignedToId;
	private Long subtaskPriorityId;
	private Date subtaskAssignedDate;
	private Date subtaskDueDate;

	public NewSubtaskDTO() {
		super();
	}

	public NewSubtaskDTO(String subtaskTitle, String subtaskDescription, Long subtaskAssignedToId, Long subtaskPriorityId, Date subtaskAssignedDate, Date subtaskDueDate) {
		super();
		this.subtaskTitle = subtaskTitle;
		this.subtaskDescription = subtaskDescription;
		this.subtaskAssignedToId = subtaskAssignedToId;
		this.subtaskPriorityId = subtaskPriorityId;
		this.subtaskAssignedDate = subtaskAssignedDate;
		this.subtaskDueDate = subtaskDueDate;
	}

	@Override
	public String toString() {
		return String.format("{\"subtaskTitle\": \"%s\", \"subtaskDescription\": \"%s\", \"subtaskAssignedToId\": %d, \"subtaskPriorityId\": %d, \"subtaskAssignedDate\": \"%tc\", \"subtaskDueDate\": \"%tc\"}", subtaskTitle, subtaskDescription, subtaskAssignedToId, subtaskPriorityId, subtaskAssignedDate, subtaskDueDate);
	}

	public String getSubtaskTitle() {
		return subtaskTitle;
	}

	public void setSubtaskTitle(String subtaskTitle) {
		this.subtaskTitle = subtaskTitle;
	}

	public String getSubtaskDescription() {
		return subtaskDescription;
	}

	public void setSubtaskDescription(String subtaskDescription) {
		this.subtaskDescription = subtaskDescription;
	}

	public Long getSubtaskAssignedToId() {
		return subtaskAssignedToId;
	}

	public void setSubtaskAssignedToId(Long subtaskAssignedToId) {
		this.subtaskAssignedToId = subtaskAssignedToId;
	}

	public Long getSubtaskPriorityId() {
		return subtaskPriorityId;
	}

	public void setSubtaskPriorityId(Long subtaskPriorityId) {
		this.subtaskPriorityId = subtaskPriorityId;
	}

	public Date getSubtaskAssginedDate() {
		return subtaskAssignedDate;
	}

	public void setSubtaskAssginedDate(Date subtaskAssignedDate) {
		this.subtaskAssignedDate = subtaskAssignedDate;
	}

	public Date getSubtaskDueDate() {
		return subtaskDueDate;
	}

	public void setSubtaskDueDate(Date subtaskDueDate) {
		this.subtaskDueDate = subtaskDueDate;
	}
}
