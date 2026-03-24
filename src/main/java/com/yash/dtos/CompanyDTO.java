package com.yash.dtos;

public class CompanyDTO {
	private Long id;
	private String name;

	public CompanyDTO() {
		super();
	}

	public CompanyDTO(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	@Override
	public String toString() {
		return String.format("{\"id\": %d, \"name\": \"%s\"}", id, name);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
