package com.yash.dtos;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class NewTaskDTO {
	private String taskTitle;
	private String taskDescription;
	private Long taskAssigneeId;
	private Long taskPriorityId;
	private Date taskStartDate;
	private Date taskDueDate;
	private List<NewSubtaskDTO> newSubtaskDTOs;

	public NewTaskDTO() {
		super();
	}

	public NewTaskDTO(String taskTitle, String taskDescription, Long taskAssigneeId, Long taskPriorityId, Date taskStartDate, Date taskDueDate, List<NewSubtaskDTO> newSubtaskDTOs) {
		super();
		this.taskTitle = taskTitle;
		this.taskDescription = taskDescription;
		this.taskAssigneeId = taskAssigneeId;
		this.taskPriorityId = taskPriorityId;
		this.taskStartDate = taskStartDate;
		this.taskDueDate = taskDueDate;
		this.newSubtaskDTOs = newSubtaskDTOs;
	}

	@Override
	public String toString() {
		return String.format("{\"taskTitle\": \"%s\", \"taskDescription\": \"%s\", \"taskPriorityId\": %d, \"taskStartDate\": \"%tc\", \"taskDueDate\": \"%tc\", \"newSubtasks\": [%s]}", taskTitle, taskDescription, taskPriorityId, taskStartDate, taskDueDate, newSubtaskDTOs.stream().map(NewSubtaskDTO::toString).collect(Collectors.joining(", ")));
	}

	public String getTaskTitle() {
		return taskTitle;
	}

	public void setTaskTitle(String taskTitle) {
		this.taskTitle = taskTitle;
	}

	public String getTaskDescription() {
		return taskDescription;
	}

	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}

	public Long getTaskAssigneeId() {
		return taskAssigneeId;
	}

	public void setTaskAssigneeId(Long taskAssigneeId) {
		this.taskAssigneeId = taskAssigneeId;
	}

	public Long getTaskPriorityId() {
		return taskPriorityId;
	}

	public void setTaskPriorityId(Long taskPriorityId) {
		this.taskPriorityId = taskPriorityId;
	}

	public Date getTaskStartDate() {
		return taskStartDate;
	}

	public void setTaskStartDate(Date taskStartDate) {
		this.taskStartDate = taskStartDate;
	}

	public Date getTaskDueDate() {
		return taskDueDate;
	}

	public void setTaskDueDate(Date taskDueDate) {
		this.taskDueDate = taskDueDate;
	}

	public List<NewSubtaskDTO> getNewSubtaskDTOs() {
		return newSubtaskDTOs;
	}

	public void setNewSubtaskDTOs(List<NewSubtaskDTO> newSubtaskDTOs) {
		this.newSubtaskDTOs = newSubtaskDTOs;
	}
}
