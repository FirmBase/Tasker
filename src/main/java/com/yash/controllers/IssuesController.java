package com.yash.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.stream.Collectors;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.yash.dtos.IssueDTO;
import com.yash.security.SessionValidation;
import com.yash.services.IssuesService;
import com.yash.services.IssuesServiceImpl;

@WebServlet(urlPatterns = "/Issue/")
public class IssuesController extends HttpServlet {
	private Connection connection;
	private final IssuesService issuesService = new IssuesServiceImpl();

	public IssuesController() throws SQLException {
		super();
		try {
			InitialContext initialContext = new InitialContext();
			DataSource dataSource = (DataSource) initialContext.lookup("java:comp/env/jdbc/HikariDB");
			connection = dataSource.getConnection();
		}
		catch (NamingException namingException) {
			namingException.printStackTrace();
		}
	}

	@Override
	protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
		if ((httpServletRequest.getServletPath().equalsIgnoreCase("/Issue/")) && SessionValidation.sessionValid(httpServletRequest)) {
			try (final PrintWriter printWriter = httpServletResponse.getWriter()) {
				httpServletResponse.setContentType("application/json");
				printWriter.append('[');
				printWriter.append(issuesService.allIssues(connection).toString());
				printWriter.append(']');
			}
			catch (IOException ioException) {
				ioException.printStackTrace();
				throw ioException;
			}
			catch (SQLException sqlException) {
				sqlException.printStackTrace();
			}
		}
		else {
			httpServletResponse.sendRedirect("/Tasker/Login/");
		}
	}

	@Override
	protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
		if (httpServletRequest.getServletPath().equalsIgnoreCase("/Issue/") && SessionValidation.sessionValid(httpServletRequest)) {
		}
	}
}
