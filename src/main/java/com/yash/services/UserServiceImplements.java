package com.yash.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.yash.Configuration;
import com.yash.dtos.CompanyDTO;
import com.yash.dtos.ContactDetailsDTO;
import com.yash.dtos.CountryDTO;
import com.yash.dtos.UserCredentialDTO;
import com.yash.dtos.UserDTO;
import com.yash.security.HashPassword;

public class UserServiceImplements implements UserService {
	final private String userExistQuery = "SELECT id, username, email, password, registered_on, is_active FROM user_credentials WHERE username = ?";
	final private String usernameAvailableQuery = "SELECT COUNT(username) AS usernames FROM user_credentials WHERE username = ?";
	final private String userRegisteredQuery = "INSERT INTO user_credentials(username, email, password, registered_on, is_active) VALUES(?, ?, ?, NOW(), TRUE)";
	final private String allUsersQuery =
		"SELECT" +
		"	tasker.user.id," +
		"	tasker.user.name AS name," +
		"	tasker.user.designation," +
		"	tasker.user.birth_date," +
		"	tasker.user.blood_group," +
		"	tasker.user.user_credentials_id," +
		"	tasker.user.employee_id," +
		"	tasker.company.name AS employee_company_name," +
		"	tasker.user.temporary_contact_id," +
		"	temporary_contact_details.name AS temporary_contact_name," +
		"	temporary_contact_details.address AS temporary_contact_address," +
		"	temporary_contact_details.district AS temporary_contact_district," +
		"	temporary_contact_details.state AS temporary_contact_state," +
		"	temporary_contact_details.country AS temporary_contact_country," +
		"	temporary_contact_details.phone_no AS temporary_contact_phone_no," +
		"	temporary_contact_details.email AS temporary_contact_email," +
		"	temporary_contact_details.company_id AS temporary_contact_company_id," +
		"	tasker.user.permanent_contact_id," +
		"	permanent_contact_details.name AS permanent_contact_name," +
		"	permanent_contact_details.address AS permanent_contact_address," +
		"	permanent_contact_details.district AS permanent_contact_district," +
		"	permanent_contact_details.state AS permanent_contact_state," +
		"	permanent_contact_details.country AS permanent_contact_country," +
		"	permanent_contact_details.phone_no AS permanent_contact_phone_no," +
		"	permanent_contact_details.email AS permanent_contact_email," +
		"	permanent_contact_details.company_id AS permanent_contact_company_id," +
		"	tasker.user_credentials.username," +
		"	tasker.user_credentials.email," +
		"	tasker.user_credentials.password," +
		"	tasker.user_credentials.registered_on," +
		"	tasker.user_credentials.is_active" + "\n" +
		"FROM" +
		"	tasker.user" + "\n" +
		"RIGHT JOIN" +
		"	tasker.user_credentials" + "\n" +
		"ON" +
		"	tasker.user.user_credentials_id = tasker.user_credentials.id" + "\n" +
		"LEFT JOIN" +
		"	tasker.company" + "\n" +
		"ON" +
		"	tasker.user.employee_id = tasker.company.id" + "\n" +
		"LEFT JOIN" +
		"	tasker.contact_details temporary_contact_details" + "\n" +
		"ON" +
		"	tasker.user.temporary_contact_id = tasker.temporary_contact_details.id" + "\n" +
		"LEFT JOIN" +
		"	tasker.contact_details permanent_contact_details" + "\n" +
		"ON" +
		"	tasker.user.permanent_contact_id = tasker.permanent_contact_details.id";

	@Override
	public UserCredentialDTO signInUser(Connection connection, String username, String password) throws SQLException {
		final PreparedStatement preparedStatement = connection.prepareStatement(userExistQuery);
		preparedStatement.setString(1, username);
		final ResultSet resultSet = preparedStatement.executeQuery();
		final UserCredentialDTO userCredentialDTO = new UserCredentialDTO();
		if (resultSet.next()) {
			if (HashPassword.checkBCryptPassword(password, resultSet.getString("password"))) {
				userCredentialDTO.setId(resultSet.getLong("id"));
				userCredentialDTO.setUsername(resultSet.getString("username"));
				userCredentialDTO.setEmail(resultSet.getString("email"));
				userCredentialDTO.setRegisteredOn(resultSet.getDate("registered_on"));
				userCredentialDTO.setActive(resultSet.getBoolean("is_active"));
			} else {
				userCredentialDTO.setActive(false);
			}
		}
		else {
			userCredentialDTO.setActive(false);
		}
		resultSet.close();
		preparedStatement.close();
		return userCredentialDTO;
	}

	@Override
	public boolean usernameAvailable(Connection connection, String username) throws SQLException {
		final PreparedStatement preparedStatement = connection.prepareStatement(usernameAvailableQuery);
		preparedStatement.setString(1, username);
		final ResultSet resultSet = preparedStatement.executeQuery();
		resultSet.next();
		final int usernames = resultSet.getInt("usernames");
		resultSet.close();
		preparedStatement.close();
		if (usernames > 0)
			return false;
		else
			return true;
	}

	@Override
	public boolean signUpUser(Connection connection, UserCredentialDTO userCredentialDTO) throws SQLException {
		final PreparedStatement preparedStatement = connection.prepareStatement(userRegisteredQuery, PreparedStatement.RETURN_GENERATED_KEYS);
		preparedStatement.setString(1, userCredentialDTO.getUsername());
		preparedStatement.setString(2, userCredentialDTO.getEmail());
		preparedStatement.setString(3, HashPassword.hashBCryptPassword(userCredentialDTO.getPassword(), Configuration.SALT_ROUND_COST));
		final int rows = preparedStatement.executeUpdate();
		if (rows > 1) {
			ResultSet resultSet = preparedStatement.getGeneratedKeys();
			resultSet.next();
			userCredentialDTO.setId(resultSet.getLong("id"));
			resultSet.close();
			preparedStatement.close();
			return true;
		}
		else {
			preparedStatement.close();
			return false;
		}
	}

	private final String getAllCountriesQuery = "SELECT id, country_code, country_name FROM tasker.countries";

	@Override
	public List<CountryDTO> getAllCountries(final Connection connection) throws SQLException {
		final List<CountryDTO> countryDTOs = new ArrayList<CountryDTO>();
		final Statement statement = connection.createStatement();
		final ResultSet resultSet = statement.executeQuery(getAllCountriesQuery);
		while (resultSet.next()) {
			final CountryDTO countryDTO = new CountryDTO(resultSet.getLong("id"), resultSet.getString("country_code"), resultSet.getString("country_name"));
			countryDTOs.add(countryDTO);
		}
		resultSet.close();
		statement.close();
		return countryDTOs;
	}

	private final String getUserCompanyQuery = "SELECT tasker.company.id, tasker.company.name FROM tasker.company WHERE tasker.company.id = ?";

	@Override
	public CompanyDTO getUserCompany(final Connection connection, final long id) throws SQLException {
		final PreparedStatement preparedStatement = connection.prepareStatement(getUserCompanyQuery);
		preparedStatement.setLong(1, id);
		final ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) {
			final CompanyDTO companyDTO = new CompanyDTO(resultSet.getLong("id"), resultSet.getString("name"));
			resultSet.close();
			preparedStatement.close();
			return companyDTO;
		}
		else {
			resultSet.close();
			preparedStatement.close();
			return null;
		}
	}

	private final String checkUserProfileCompletionQuery =
		"SELECT" +
		"	COUNT(*) AS user_count" + "\n" +
		"FROM" +
		"	tasker.user, tasker.user_credentials" + "\n" +
		"WHERE" +
		"	tasker.user.user_credentials_id = tasker.user_credentials.id" + "\n" +
		"AND" +
		"	tasker.user_credentials.id = ?";

	@Override
	public boolean userProfileCompletion(final Connection connection, final long userCredentialId) {
		boolean recordExists;
		try (final PreparedStatement preparedStatement = connection.prepareStatement(checkUserProfileCompletionQuery)) {
			preparedStatement.setLong(1, userCredentialId);
			final ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next())
				recordExists = resultSet.getInt("user_count") > 0;
			else
				recordExists = false;
			resultSet.close();
		}
		catch (SQLException sqlException) {
			recordExists = false;
			sqlException.printStackTrace();
		}
		return recordExists;
	}

	@Override
	public List<UserDTO> getAllUsers(final Connection connection) throws SQLException {
		final List<UserDTO> userDTOs = new ArrayList<UserDTO>();
		final Statement statement = connection.createStatement();
		final ResultSet resultSet = statement.executeQuery(allUsersQuery);
		while (resultSet.next()) {
			final UserCredentialDTO userCredentialDTO = new UserCredentialDTO(resultSet.getLong("user_credentials_id"), resultSet.getString("username"), resultSet.getString("email"), "", resultSet.getDate("registered_on"), resultSet.getBoolean("is_active"));
			final CompanyDTO companyDTO = new CompanyDTO(resultSet.getLong("employee_id"), resultSet.getString("employee_company_name"));
			final ContactDetailsDTO temporaryContactDetailsDTO = new ContactDetailsDTO(resultSet.getLong("temporary_contact_id"), resultSet.getString("temporary_contact_name"), resultSet.getString("temporary_contact_address"), resultSet.getString("temporary_contact_district"), resultSet.getString("temporary_contact_state"), resultSet.getString("temporary_contact_country"), resultSet.getString("temporary_contact_phone_no"), resultSet.getString("temporary_contact_email"), null);
			final ContactDetailsDTO permanentContactDetailsDTO = new ContactDetailsDTO(resultSet.getLong("permanent_contact_id"), resultSet.getString("permanent_contact_name"), resultSet.getString("permanent_contact_address"), resultSet.getString("permanent_contact_district"), resultSet.getString("permanent_contact_state"), resultSet.getString("permanent_contact_country"), resultSet.getString("permanent_contact_phone_no"), resultSet.getString("permanent_contact_email"), null);
			final UserDTO userDTO = new UserDTO(resultSet.getLong("id"), resultSet.getString("name"), resultSet.getString("designation"), resultSet.getDate("birth_date"), resultSet.getString("blood_group"), companyDTO, userCredentialDTO, temporaryContactDetailsDTO, permanentContactDetailsDTO);
			userDTOs.add(userDTO);
		}
		resultSet.close();
		statement.close();
		return userDTOs;
	}

	private final String getUserProfileQuery =
		"SELECT" +
		"	tasker.user.id," +
		"	tasker.user.name AS user_name," +
		"	tasker.user.designation," +
		"	tasker.user.birth_date," +
		"	tasker.user.blood_group," +
		"	tasker.user.user_credentials_id," +
		"	tasker.user_credentials.username," +
		"	tasker.user_credentials.email," +
		"	tasker.user_credentials.registered_on," +
		"	tasker.user_credentials.is_active," +
		"	tasker.user.employee_id AS employee_id," +
		"	tasker.company.name AS company_name," +
		"	tasker.user.temporary_contact_id," +
		"	temporary_contact_details.name AS temporary_contact_details_name," +
		"	temporary_contact_details.address AS temporary_contact_details_address," +
		"	temporary_contact_details.district AS temporary_contact_details_district," +
		"	temporary_contact_details.state AS temporary_contact_details_state," +
		"	temporary_contact_details.country AS temporary_contact_details_country," +
		// "	temporary_contact_country.country_name AS temporary_contact_details_country_name," +
		"	temporary_contact_details.email AS temporary_contact_details_email," +
		"	temporary_contact_details.phone_no AS temporary_contact_details_phone_no," +
		"	tasker.user.permanent_contact_id," +
		"	permanent_contact_details.name AS permanent_contact_details_name," +
		"	permanent_contact_details.address AS permanent_contact_details_address," +
		"	permanent_contact_details.district AS permanent_contact_details_district," +
		"	permanent_contact_details.state AS permanent_contact_details_state," +
		"	permanent_contact_details.country AS permanent_contact_details_country," +
		// "	permanent_contact_country.country_name AS permanent_contact_details_country_name," +
		"	permanent_contact_details.email AS permanent_contact_details_email," +
		"	permanent_contact_details.phone_no AS permanent_contact_details_phone_no" + "\n" +
		"FROM" +
		"	tasker.user" + "\n" +
		"LEFT JOIN" +
		"	tasker.user_credentials" + "\n" +
		"ON" +
		"	tasker.user.user_credentials_id = tasker.user_credentials.id" + "\n" +
		"LEFT JOIN" +
		"	tasker.company" + "\n" +
		"ON" +
		"	tasker.user.employee_id = tasker.company.id" + "\n" +
		"LEFT JOIN" +
		"	tasker.contact_details temporary_contact_details" + "\n" +
		"ON" +
		"	tasker.user.temporary_contact_id = temporary_contact_details.id" + "\n" +
		"LEFT JOIN" +
		"	tasker.contact_details permanent_contact_details" + "\n" +
		"ON" +
		"	tasker.user.permanent_contact_id = permanent_contact_details.id" + "\n" +
		// "LEFT JOIN" +
		// "	tasker.countries temporary_contact_country" + "\n" +
		// "ON" +
		// "	temporary_contact_details.country = temporary_contact_country.country_code" + "\n" +
		// "LEFT JOIN" +
		// "	tasker.countries permanent_contact_country" + "\n" +
		// "ON" +
		// "	permanent_contact_details.country = permanent_contact_country.country_code" + "\n" +
		"WHERE" +
		"	tasker.user.user_credentials_id = ?";

	@Override
	public UserDTO getUserProfile(final Connection connection, long userCredentialId) throws SQLException {
		final PreparedStatement preparedStatement = connection.prepareStatement(getUserProfileQuery);
		preparedStatement.setLong(1, userCredentialId);
		final ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) {
			final CompanyDTO companyDTO = new CompanyDTO(resultSet.getLong("employee_id"), resultSet.getString("company_name"));
			final UserCredentialDTO userCredentialDTO = new UserCredentialDTO(resultSet.getLong("user_credentials_id"), resultSet.getString("username"), resultSet.getString("email"), "", resultSet.getDate("registered_on"), resultSet.getBoolean("is_active"));
			final ContactDetailsDTO temporaryContactDetailsDTO = new ContactDetailsDTO(resultSet.getLong("temporary_contact_id"), resultSet.getString("temporary_contact_details_name"), resultSet.getString("temporary_contact_details_address"), resultSet.getString("temporary_contact_details_district"), resultSet.getString("temporary_contact_details_state"), resultSet.getString("temporary_contact_details_country"), resultSet.getString("temporary_contact_details_phone_no"), resultSet.getString("temporary_contact_details_email"), null);
			final ContactDetailsDTO permanentContactDetailsDTO = new ContactDetailsDTO(resultSet.getLong("permanent_contact_id"), resultSet.getString("permanent_contact_details_name"), resultSet.getString("permanent_contact_details_address"), resultSet.getString("permanent_contact_details_district"), resultSet.getString("permanent_contact_details_state"), resultSet.getString("permanent_contact_details_country"), resultSet.getString("permanent_contact_details_phone_no"), resultSet.getString("permanent_contact_details_email"), null);
			final UserDTO userDTO = new UserDTO(resultSet.getLong("id"), resultSet.getString("user_name"), resultSet.getString("designation"), resultSet.getDate("birth_date"), resultSet.getString("blood_group"), companyDTO, userCredentialDTO, temporaryContactDetailsDTO, permanentContactDetailsDTO);
			return userDTO;
		}
		else
			return null;
	}

	private final String insertUserPofileQuery = "INSERT INTO tasker.user(name, designation, birth_date, blood_group, user_credentials_id, employee_id, temporary_contact_id, permanent_contact_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
	// private final String insertContactDetailsQuery = "INSERT INTO tasker.contact_details(name, address, district, state, country, phone_no, email, company_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

	@Override
	public boolean insertUserProfile(final Connection connection, final UserDTO userDTO) throws SQLException {
		final boolean autoCommitState = connection.getAutoCommit();
		int row1 = 0, row2 = 0, row = 0;
		try {
			connection.setAutoCommit(false);

			final PreparedStatement preparedStatement1 = connection.prepareStatement(insertContactDetailsQuery, PreparedStatement.RETURN_GENERATED_KEYS);
			preparedStatement1.setString(1, userDTO.getTemporaryContactDetailsDTO().getName());
			preparedStatement1.setString(2, userDTO.getTemporaryContactDetailsDTO().getAddress());
			preparedStatement1.setString(3, userDTO.getTemporaryContactDetailsDTO().getDistrict());
			preparedStatement1.setString(4, userDTO.getTemporaryContactDetailsDTO().getState());
			preparedStatement1.setString(5, userDTO.getTemporaryContactDetailsDTO().getCountry());
			preparedStatement1.setString(6, userDTO.getTemporaryContactDetailsDTO().getPhone_no());
			preparedStatement1.setString(7, userDTO.getTemporaryContactDetailsDTO().getEmail());
			if (userDTO.getTemporaryContactDetailsDTO().getCompanyDTO() == null)
				preparedStatement1.setNull(8, Types.BIGINT);
			else
				preparedStatement1.setLong(8, userDTO.getTemporaryContactDetailsDTO().getCompanyDTO().getId());
			row1 = preparedStatement1.executeUpdate();
			final ResultSet resultSet1 = preparedStatement1.getGeneratedKeys();
			if (resultSet1.next())
				userDTO.getTemporaryContactDetailsDTO().setId(resultSet1.getLong(1));
			resultSet1.close();
			preparedStatement1.close();

			final PreparedStatement preparedStatement2 = connection.prepareStatement(insertContactDetailsQuery, PreparedStatement.RETURN_GENERATED_KEYS);
			preparedStatement2.setString(1, userDTO.getPermanentContactDetailsDTO().getName());
			preparedStatement2.setString(2, userDTO.getPermanentContactDetailsDTO().getAddress());
			preparedStatement2.setString(3, userDTO.getPermanentContactDetailsDTO().getDistrict());
			preparedStatement2.setString(4, userDTO.getPermanentContactDetailsDTO().getState());
			preparedStatement2.setString(5, userDTO.getPermanentContactDetailsDTO().getCountry());
			preparedStatement2.setString(6, userDTO.getPermanentContactDetailsDTO().getPhone_no());
			preparedStatement2.setString(7, userDTO.getPermanentContactDetailsDTO().getEmail());
			if (userDTO.getPermanentContactDetailsDTO().getCompanyDTO() == null)
				preparedStatement2.setNull(8, Types.BIGINT);
			else
				preparedStatement2.setLong(8, userDTO.getPermanentContactDetailsDTO().getCompanyDTO().getId());
			row2 = preparedStatement2.executeUpdate();
			final ResultSet resultSet2 = preparedStatement2.getGeneratedKeys();
			if (resultSet2.next())
				userDTO.getPermanentContactDetailsDTO().setId(resultSet2.getLong(1));
			resultSet2.close();
			preparedStatement2.close();

			final PreparedStatement preparedStatement = connection.prepareStatement(insertUserPofileQuery, PreparedStatement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, userDTO.getName());
			preparedStatement.setString(2, userDTO.getDesignation());
			preparedStatement.setDate(3, new java.sql.Date(userDTO.getDateOfBirth().getTime()));
			preparedStatement.setString(4, userDTO.getBloodGroup());
			preparedStatement.setLong(5, userDTO.getUserCredentialDTO().getId());
			preparedStatement.setLong(6, userDTO.getEmployeeCompanyDTO().getId());
			preparedStatement.setLong(7, userDTO.getTemporaryContactDetailsDTO().getId());
			preparedStatement.setLong(8, userDTO.getPermanentContactDetailsDTO().getId());
			row = preparedStatement.executeUpdate();
			final ResultSet resultSet = preparedStatement.getGeneratedKeys();
			if (resultSet.next())
				userDTO.setId(resultSet.getLong(1));
			resultSet.close();
			preparedStatement.close();

			connection.commit();
		}
		catch (SQLException sqlException) {
			sqlException.printStackTrace();
			connection.rollback();
		}
		finally {
			connection.setAutoCommit(autoCommitState);
		}
		return (row1 > 0) && (row2 > 0) && (row > 0);
	}

	private final String updateUserPofileQuery = "UPDATE tasker.user SET name = ?, designation = ?, birth_date = ?, blood_group = ?, user_credentials_id = ?, employee_id = ?, temporary_contact_id = ?, permanent_contact_id = ? WHERE id = ?";
	private final String updateContactDetailsQuery = "UPDATE tasker.contact_details SET name = ?, address = ?, district = ?, state = ?, country = ?, phone_no = ?, email = ?, company_id = ? WHERE id = ?";

	@Override
	public boolean updateUserProfile(final Connection connection, final UserDTO userDTO) throws SQLException {
		boolean autoCommitState = connection.getAutoCommit();
		int row1 = 0, row2 = 0, row = 0;
		try {
			connection.setAutoCommit(false);

			final PreparedStatement preparedStatement1 = connection.prepareStatement(updateContactDetailsQuery);
			preparedStatement1.setLong(9, userDTO.getTemporaryContactDetailsDTO().getId());
			preparedStatement1.setString(1, userDTO.getTemporaryContactDetailsDTO().getName());
			preparedStatement1.setString(2, userDTO.getTemporaryContactDetailsDTO().getAddress());
			preparedStatement1.setString(3, userDTO.getTemporaryContactDetailsDTO().getDistrict());
			preparedStatement1.setString(4, userDTO.getTemporaryContactDetailsDTO().getState());
			preparedStatement1.setString(5, userDTO.getTemporaryContactDetailsDTO().getCountry());
			preparedStatement1.setString(6, userDTO.getTemporaryContactDetailsDTO().getPhone_no());
			preparedStatement1.setString(7, userDTO.getTemporaryContactDetailsDTO().getEmail());
			if (userDTO.getTemporaryContactDetailsDTO().getCompanyDTO() == null)
				preparedStatement1.setNull(8, Types.BIGINT);
			else
				preparedStatement1.setLong(8, userDTO.getTemporaryContactDetailsDTO().getCompanyDTO().getId());
			row1 = preparedStatement1.executeUpdate();
			preparedStatement1.close();

			final PreparedStatement preparedStatement2 = connection.prepareStatement(updateContactDetailsQuery);
			preparedStatement2.setLong(9, userDTO.getPermanentContactDetailsDTO().getId());
			preparedStatement2.setString(1, userDTO.getPermanentContactDetailsDTO().getName());
			preparedStatement2.setString(2, userDTO.getPermanentContactDetailsDTO().getAddress());
			preparedStatement2.setString(3, userDTO.getPermanentContactDetailsDTO().getDistrict());
			preparedStatement2.setString(4, userDTO.getPermanentContactDetailsDTO().getState());
			preparedStatement2.setString(5, userDTO.getPermanentContactDetailsDTO().getCountry());
			preparedStatement2.setString(6, userDTO.getPermanentContactDetailsDTO().getPhone_no());
			preparedStatement2.setString(7, userDTO.getPermanentContactDetailsDTO().getEmail());
			if (userDTO.getPermanentContactDetailsDTO().getCompanyDTO() == null)
				preparedStatement2.setNull(8, Types.BIGINT);
			else
				preparedStatement2.setLong(8, userDTO.getPermanentContactDetailsDTO().getCompanyDTO().getId());
			row2 = preparedStatement2.executeUpdate();
			preparedStatement2.close();

			final PreparedStatement preparedStatement = connection.prepareStatement(updateUserPofileQuery);
			preparedStatement.setLong(9, userDTO.getId());
			preparedStatement.setString(1, userDTO.getName());
			preparedStatement.setString(2, userDTO.getDesignation());
			preparedStatement.setDate(3, new java.sql.Date(userDTO.getDateOfBirth().getTime()));
			preparedStatement.setString(4, userDTO.getBloodGroup());
			preparedStatement.setLong(5, userDTO.getUserCredentialDTO().getId());
			preparedStatement.setLong(6, userDTO.getEmployeeCompanyDTO().getId());
			preparedStatement.setLong(7, userDTO.getTemporaryContactDetailsDTO().getId());
			preparedStatement.setLong(8, userDTO.getPermanentContactDetailsDTO().getId());
			row = preparedStatement.executeUpdate();
			preparedStatement.close();

			connection.commit();
		}
		catch (SQLException sqlException) {
			sqlException.printStackTrace();
			connection.rollback();
		}
		finally {
			connection.setAutoCommit(autoCommitState);
		}
		return (row1 > 0) && (row2 > 0) && (row > 0);
	}

	private final String getUserContactDetailsQuery =
		"SELECT" +
		"	tasker.contact_details.id," +
		"	tasker.contact_details.name," +
		"	tasker.contact_details.address," +
		"	tasker.contact_details.district," +
		"	tasker.contact_details.state," +
		"	tasker.contact_details.country," +
		"	tasker.countries.country_name," +
		"	tasker.contact_details.phone_no," +
		"	tasker.contact_details.email," +
		"	tasker.contact_details.company_id," +
		"	tasker.company.name AS company_name" + "\n" +
		"FROM" +
		"	tasker.contact_details" + "\n" +
		"LEFT JOIN" +
		"	tasker.company" + "\n" +
		"ON" +
		"	tasker.contact_details.company_id = tasker.company.id" + "\n" +
		"LEFT JOIN" +
		"	tasker.countries" + "\n" +
		"ON" +
		"	tasker.contact_details.country = tasker.countries.country_code" + "\n" +
		"WHERE" +
		"	tasker.contact_details.id = ?";

	@Override
	public ContactDetailsDTO getUserContactDetails(final Connection connection, final long id) throws SQLException {
		final PreparedStatement preparedStatement = connection.prepareStatement(getUserContactDetailsQuery);
		preparedStatement.setLong(1, id);
		final ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) {
			final CompanyDTO companyDTO = new CompanyDTO(resultSet.getLong("company_id"), resultSet.getString("company_name"));
			final ContactDetailsDTO contactDetailsDTO = new ContactDetailsDTO(resultSet.getLong("id"), resultSet.getString("name"), resultSet.getString("address"), resultSet.getString("district"), resultSet.getString("state"), resultSet.getString("country"), resultSet.getString("phone_no"), resultSet.getString("email"), companyDTO);
			resultSet.close();
			return contactDetailsDTO;
		}
		else {
			resultSet.close();
			return null;
		}
	}

	private final String insertContactDetailsQuery = "INSERT INTO tasker.contact_details(name, address, district, state, country, phone_no, email, company_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

	@Override
	public boolean insertContactDetails(final Connection connection, final ContactDetailsDTO contactDetailsDTO) throws SQLException {
		int row = 0;
		final PreparedStatement preparedStatement = connection.prepareStatement(insertContactDetailsQuery, PreparedStatement.RETURN_GENERATED_KEYS);
		preparedStatement.setString(1, contactDetailsDTO.getName());
		preparedStatement.setString(2, contactDetailsDTO.getAddress());
		preparedStatement.setString(3, contactDetailsDTO.getDistrict());
		preparedStatement.setString(4, contactDetailsDTO.getState());
		preparedStatement.setString(5, contactDetailsDTO.getCountry());
		preparedStatement.setString(6, contactDetailsDTO.getPhone_no());
		preparedStatement.setString(7, contactDetailsDTO.getEmail());
		row = preparedStatement.executeUpdate();
		ResultSet resultSet = preparedStatement.getGeneratedKeys();
		if (resultSet.next())
			contactDetailsDTO.setId(resultSet.getLong("id"));
		resultSet.close();
		return row > 0;
	}

	private final String updateContactDetails = "UPDATE tasker.contact_details SET name = ?, address = ?, district = ?, state = ?, country = ?, phone_no = ?, email = ?, company_id = ? WHERE id = ?";

	@Override
	public boolean updateContactDetails(final Connection connection, final ContactDetailsDTO contactDetailsDTO) throws SQLException {
		int row = 0;
		final PreparedStatement preparedStatement = connection.prepareStatement(updateContactDetails);
		preparedStatement.setLong(8, contactDetailsDTO.getId());
		preparedStatement.setString(1, contactDetailsDTO.getName());
		preparedStatement.setString(2, contactDetailsDTO.getAddress());
		preparedStatement.setString(3, contactDetailsDTO.getDistrict());
		preparedStatement.setString(4, contactDetailsDTO.getState());
		preparedStatement.setString(5, contactDetailsDTO.getCountry());
		preparedStatement.setString(6, contactDetailsDTO.getPhone_no());
		preparedStatement.setString(7, contactDetailsDTO.getEmail());
		preparedStatement.close();
		return row > 0;
	}

	final private String findIdByUsernameQuery = "SELECT tasker.user_credentials.id FROM tasker.user_credentials WHERE tasker.user_credentials.username = ?";

	@Override
	public Long findIdByUsername(final Connection connection, final String username) throws SQLException {
		final PreparedStatement preparedStatement = connection.prepareStatement(findIdByUsernameQuery);
		preparedStatement.setString(1, username);
		ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) {
			final long id = resultSet.getLong("id");
			resultSet.close();
			preparedStatement.close();
			return id;
		}
		else {
			resultSet.close();
			preparedStatement.close();
			return null;
		}
	}
}
