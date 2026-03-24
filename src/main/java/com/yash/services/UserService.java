package com.yash.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.yash.dtos.CompanyDTO;
import com.yash.dtos.ContactDetailsDTO;
import com.yash.dtos.CountryDTO;
import com.yash.dtos.UserCredentialDTO;
import com.yash.dtos.UserDTO;

public interface UserService {
	public UserCredentialDTO signInUser(Connection connection, String username, String password) throws SQLException;
	public boolean usernameAvailable(Connection connection, String username) throws SQLException;
	public boolean signUpUser(Connection connection, UserCredentialDTO userCredentialDTO) throws SQLException;
	public List<CountryDTO> getAllCountries(final Connection connection) throws SQLException;
	public CompanyDTO getUserCompany(final Connection connection, final long id) throws SQLException;
	public boolean userProfileCompletion(final Connection connection, final long userCredentialId);
	public List<UserDTO> getAllUsers(final Connection connection) throws SQLException;
	public UserDTO getUserProfile(final Connection connection, long userCredentialId) throws SQLException;
	public boolean insertUserProfile(final Connection connection, final UserDTO userDTO) throws SQLException;
	public boolean updateUserProfile(final Connection connection, final UserDTO userDTO) throws SQLException;
	public ContactDetailsDTO getUserContactDetails(final Connection connection, final long id) throws SQLException;
	public boolean insertContactDetails(final Connection connection, final ContactDetailsDTO contactDetailsDTO) throws SQLException;
	public boolean updateContactDetails(final Connection connection, final ContactDetailsDTO contactDetailsDTO) throws SQLException;
	public Long findIdByUsername(final Connection connection, final String username) throws SQLException;
}
