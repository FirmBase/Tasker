package com.yash.dtos;

import com.yash.enumerations.StatusEnum;

public class StatusDTO {
	private Long id;
	private StatusEnum statusEnum;

	public StatusDTO() {
		super();
	}

	public StatusDTO(Long id, StatusEnum statusEnum) {
		super();
		this.id = id;
		this.statusEnum = statusEnum;
	}

	@Override
	public String toString() {
		return String.format("{\"id\": %d, \"label\": \"%s\"}", id, statusEnum);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public StatusEnum getStatusEnum() {
		return statusEnum;
	}

	public void setStatusEnum(StatusEnum statusEnum) {
		this.statusEnum = statusEnum;
	}
}
