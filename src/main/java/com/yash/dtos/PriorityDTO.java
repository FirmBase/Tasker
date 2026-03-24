package com.yash.dtos;

import com.yash.enumerations.PriorityEnum;

public class PriorityDTO {
	private Long id;
	private PriorityEnum priorityEnum;

	public PriorityDTO() {
		super();
	}

	public PriorityDTO(Long id, PriorityEnum priorityEnum) {
		super();
		this.id = id;
		this.priorityEnum = priorityEnum;
	}

	@Override
	public String toString() {
		return String.format("{\"id\": %d, \"label\": \"%s\"}", id, priorityEnum);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PriorityEnum getPriorityEnum() {
		return priorityEnum;
	}

	public void setPriorityEnum(PriorityEnum priorityEnum) {
		this.priorityEnum = priorityEnum;
	}
}
