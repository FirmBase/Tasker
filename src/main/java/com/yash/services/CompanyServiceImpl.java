package com.yash.services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class CompanyServiceImpl implements CompanyService {
	final private String allCompanyQuery = "SELECT id, name FROM tasker.company";

	@Override
	public HashMap<Long, String> getAllCompanies(final Connection connection) throws SQLException {
		HashMap<Long, String> allCompanies = new HashMap<Long, String>();
		final Statement statement = connection.createStatement();
		final ResultSet resultSet = statement.executeQuery(allCompanyQuery);
		while (resultSet.next()) {
			allCompanies.put(resultSet.getLong("id"), resultSet.getString("name"));
		}
		resultSet.close();
		statement.close();
		return allCompanies;
	}
}
