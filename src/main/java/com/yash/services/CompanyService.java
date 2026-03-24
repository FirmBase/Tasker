package com.yash.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

public interface CompanyService {
	public HashMap<Long, String> getAllCompanies(Connection connection) throws SQLException;
}
