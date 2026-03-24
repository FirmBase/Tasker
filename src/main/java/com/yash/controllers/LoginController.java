package com.yash.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.yash.dtos.ToastDTO;
import com.yash.dtos.UserCredentialDTO;
import com.yash.security.SessionValidation;
import com.yash.services.UserService;
import com.yash.services.UserServiceImplements;

@WebServlet(urlPatterns = {"/Login/", "/Register/", "/Logout/", "/Check/Username/"})
public class LoginController extends HttpServlet {
	final private UserService userService = new UserServiceImplements();
	private Connection connection;

	public LoginController() throws SQLException {
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
		if (httpServletRequest.getServletPath().equalsIgnoreCase("/Login/"))
			if (SessionValidation.sessionValid(httpServletRequest))
				httpServletResponse.sendRedirect("/Tasker/Home/");
			else {
				httpServletRequest.setAttribute("toastsDTO", new ToastDTO("Login", "Login by entering credentials at this page."));
				httpServletRequest.getRequestDispatcher("/WEB-INF/login.jsp").forward(httpServletRequest, httpServletResponse);
			}
		else if (httpServletRequest.getServletPath().equalsIgnoreCase("/Register/"))
			if (SessionValidation.sessionValid(httpServletRequest)) {
				SessionValidation.sessionInvalidate(httpServletRequest);
				httpServletRequest.setAttribute("toastsDTO", new ToastDTO("Register", "You\'ve been logged out."));
				httpServletRequest.getRequestDispatcher("/WEB-INF/register.jsp").forward(httpServletRequest, httpServletResponse);
			}
			else {
				httpServletRequest.setAttribute("toastsDTO", new ToastDTO("Register", "User can register here."));
				httpServletRequest.getRequestDispatcher("/WEB-INF/register.jsp").forward(httpServletRequest, httpServletResponse);
			}
		else if (httpServletRequest.getServletPath().equalsIgnoreCase("/Logout/")) {
			SessionValidation.sessionInvalidate(httpServletRequest);
			System.out.println("User signed out.");
			httpServletResponse.sendRedirect("/Tasker/Login/");
		}
	}

	@Override
	protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
		if (httpServletRequest.getServletPath().equalsIgnoreCase("/Login/")) {
			// Sign in
			final String username = httpServletRequest.getParameter("username");
			final String password = httpServletRequest.getParameter("password");
			try {
				final UserCredentialDTO userCredentialDTO = userService.signInUser(connection, username, password);
				if (userCredentialDTO.isActive()) {
					SessionValidation.createUserSession(httpServletRequest, userCredentialDTO);
					System.out.println("User signed in.");
					httpServletResponse.sendRedirect("/Tasker/Home/");
				} else {
					System.out.println("User present but not active.");
					httpServletResponse.sendRedirect("/Tasker/Login/");
				}
			}
			catch (SQLException sqlException) {
				sqlException.printStackTrace();
			}
		}
		else if (httpServletRequest.getServletPath().equalsIgnoreCase("/Register/")) {
			// Sign up
			final String username = httpServletRequest.getParameter("username");
			final String email = httpServletRequest.getParameter("email");
			final String password = httpServletRequest.getParameter("password");
			try {
				if (userService.usernameAvailable(connection, username)) {
					userService.signUpUser(connection, new UserCredentialDTO(null, username, email, password, null, true));
					System.out.println("User registered.");
					httpServletResponse.sendRedirect("/Tasker/Login/");
				}
				else {
					System.out.println("Username already exists.");
					httpServletResponse.sendRedirect("/Tasker/Login/");
				}
			}
			catch (SQLException sqlException) {
				sqlException.printStackTrace();
			}
		}
		else if (httpServletRequest.getServletPath().equalsIgnoreCase("/Check/Username/")) {
			final String username = httpServletRequest.getParameter("username");
			try (final PrintWriter printWriter = httpServletResponse.getWriter()) {
				httpServletResponse.setContentType("text/plain");
				if (userService.usernameAvailable(connection, username))
					printWriter.append("available");
				else
					printWriter.append("unavailable");
			}
			catch (SQLException sqlException) {
				sqlException.printStackTrace();
			}
		}
	}
}
