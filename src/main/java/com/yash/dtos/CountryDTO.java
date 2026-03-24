package com.yash.dtos;

public class CountryDTO {
	private Long id;
	private String countryCode;
	private String countryName;

	public CountryDTO() {
		super();
	}

	public CountryDTO(Long id, String countryCode, String countryName) {
		super();
		this.id = id;
		this.countryCode = countryCode;
		this.countryName = countryName;
	}

	@Override
	public String toString() {
		return String.format("{\"id\": %d, \"countryCode\": \"%s\", \"countryName\": \"%s\"}", id, countryCode, countryName);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
}
