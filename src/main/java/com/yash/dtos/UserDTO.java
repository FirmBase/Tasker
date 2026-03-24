package com.yash.dtos;

import java.util.Date;

public class UserDTO {
	private Long id;
	private String name;
	private String designation;
	private Date dateOfBirth;
	private String bloodGroup;
	private UserCredentialDTO userCredentialDTO;
	private CompanyDTO employeeCompanyDTO;
	private ContactDetailsDTO temporaryContactDetailsDTO;
	private ContactDetailsDTO permanentContactDetailsDTO;

	public UserDTO() {
		super();
	}

	public UserDTO(Long id, String name, String designation, Date dateOfBirth, String bloodGroup, CompanyDTO employeeCompanyDTO, UserCredentialDTO userCredentialDTO, ContactDetailsDTO temporaryContactDetailsDTO, ContactDetailsDTO permanentContactDetailsDTO) {
		super();
		this.id = id;
		this.name = name;
		this.designation = designation;
		this.bloodGroup = bloodGroup;
		this.dateOfBirth = dateOfBirth;
		this.employeeCompanyDTO = employeeCompanyDTO;
		this.permanentContactDetailsDTO = permanentContactDetailsDTO;
		this.temporaryContactDetailsDTO = temporaryContactDetailsDTO;
		this.userCredentialDTO = userCredentialDTO;
	}

	@Override
	public String toString() {
		return String.format("{\"id\": %d, \"name\": \"%s\", \"designation\": \"%s\", \"bloodGroup\": \"%s\", \"dob\": \"%tc\", \"employeeCompany\": %s, \"permanentContactDetails\": %s, \"temporaryContactDetails\": %s, \"userCredential\": %s}", id, name, designation, bloodGroup, dateOfBirth, employeeCompanyDTO, permanentContactDetailsDTO, temporaryContactDetailsDTO, userCredentialDTO);
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

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public UserCredentialDTO getUserCredentialDTO() {
		return userCredentialDTO;
	}

	public void setUserCredentialDTO(UserCredentialDTO userCredentialDTO) {
		this.userCredentialDTO = userCredentialDTO;
	}

	public CompanyDTO getEmployeeCompanyDTO() {
		return employeeCompanyDTO;
	}

	public void setEmployeeCompanyDTO(CompanyDTO employeeCompanyDTO) {
		this.employeeCompanyDTO = employeeCompanyDTO;
	}

	public ContactDetailsDTO getTemporaryContactDetailsDTO() {
		return temporaryContactDetailsDTO;
	}

	public void setTemporaryContactDetailsDTO(ContactDetailsDTO temporaryContactDetailsDTO) {
		this.temporaryContactDetailsDTO = temporaryContactDetailsDTO;
	}

	public ContactDetailsDTO getPermanentContactDetailsDTO() {
		return permanentContactDetailsDTO;
	}

	public void setPermanenDetailsDTO(ContactDetailsDTO permanentContactDetailsDTO) {
		this.permanentContactDetailsDTO = permanentContactDetailsDTO;
	}
}
