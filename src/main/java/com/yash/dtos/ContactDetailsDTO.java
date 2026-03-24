package com.yash.dtos;

public class ContactDetailsDTO {
	private Long id;
	private String name;
	private String address;
	private String district;
	private String state;
	private String country;
	private String phone_no;
	private String email;
	private CompanyDTO companyDTO;

	public ContactDetailsDTO() {
		super();
	}

	public ContactDetailsDTO(Long id, String name, String address, String district, String state, String country, String phone_no, String email, CompanyDTO companyDTO) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.district = district;
		this.state = state;
		this.country = country;
		this.phone_no = phone_no;
		this.email = email;
		this.companyDTO = companyDTO;
	}

	@Override
	public String toString() {
		return String.format("{\"id\": %d, \"name\": \"%s\", \"address\": \"%s\", \"district\": \"%s\", \"state\": \"%s\", \"country\": \"%s\", \"phoneNo\": \"%s\", \"email\": \"%s\", \"company\": %s}", id.longValue(), name, address, district, state, country, phone_no, email, companyDTO);
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPhone_no() {
		return phone_no;
	}

	public void setPhone_no(String phone_no) {
		this.phone_no = phone_no;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public CompanyDTO getCompanyDTO() {
		return companyDTO;
	}

	public void setCompanyDTO(CompanyDTO companyDTO) {
		this.companyDTO = companyDTO;
	}
}
