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

import com.yash.Configuration;
import com.yash.dtos.FeedbackDTO;
import com.yash.dtos.UserCredentialDTO;
import com.yash.security.SessionValidation;
import com.yash.services.FeedbackService;
import com.yash.services.FeedbackServiceImpl;

@WebServlet(urlPatterns = {"/Feedbacks/", "/Feedback/Subtask/", "/Feedback/User/*", "/API/Feedback/User/*"})
public class FeedbackController extends HttpServlet {
	private Connection connection;
	private final FeedbackService feedbackService = new FeedbackServiceImpl();

	public FeedbackController() throws SQLException {
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
		if (httpServletRequest.getServletPath().equalsIgnoreCase("/Feedbacks/") && SessionValidation.sessionValid(httpServletRequest)) {
			try {
				final UserCredentialDTO userCredentialDTO = (UserCredentialDTO) httpServletRequest.getSession(false).getAttribute(Configuration.USER_SESSION_ATTRIBUTE);
				httpServletRequest.setAttribute("feedbackDTOs", feedbackService.getFeedbacksByUserId(connection, userCredentialDTO.getId()));
				httpServletRequest.getRequestDispatcher("/WEB-INF/feedback.jsp").forward(httpServletRequest, httpServletResponse);
			}
			catch (SQLException sqlException) {
				sqlException.printStackTrace();
			}
		}
		else if (httpServletRequest.getServletPath().equalsIgnoreCase("/Feedback/Subtask/") && SessionValidation.sessionValid(httpServletRequest)) {
			try {
				final long subtaskId = Long.parseLong(httpServletRequest.getParameter("subtask-id"));
				httpServletRequest.setAttribute("subtaskId", subtaskId);
				httpServletRequest.setAttribute("feedbackDTOs", feedbackService.getFeedbacksBySubtaskId(connection, subtaskId));
				httpServletRequest.getRequestDispatcher("/WEB-INF/record-feedback.jsp").forward(httpServletRequest, httpServletResponse);
			}
			catch (SQLException sqlException) {
				sqlException.printStackTrace();
			}
			catch (NumberFormatException numberFormatException) {
				numberFormatException.printStackTrace();
				throw numberFormatException;
			}
		}
		/*
		else if (httpServletRequest.getServletPath().startsWith("/Feedback/") && httpServletRequest.getPathInfo().matches("/\\d+") && SessionValidation.sessionValid(httpServletRequest)) {
			final Long userCredentialId = Long.parseLong(httpServletRequest.getPathInfo().split("/")[1]);
			httpServletRequest.getRequestDispatcher("/WEB-INF/feedback.jsp").forward(httpServletRequest, httpServletResponse);
			try {
				httpServletRequest.setAttribute("feedbacks", feedbackService.getFeedbacksByUserId(connection, userCredentialId));
			}
			catch (SQLException sqlException) {
				sqlException.printStackTrace();
			}
		}
		*/
		else if (httpServletRequest.getServletPath().startsWith("/API/Feedback/") && httpServletRequest.getPathInfo().matches("/\\d+") && SessionValidation.sessionValid(httpServletRequest)) {
			final long userCredentialId = Long.parseLong(httpServletRequest.getPathInfo().split("/")[1]);
			try (final PrintWriter printWriter = httpServletResponse.getWriter()) {
				httpServletResponse.setContentType("application/json");
				printWriter.append('[');
				printWriter.append(feedbackService.getFeedbacksByUserId(connection, userCredentialId).stream().map(FeedbackDTO::toString).collect(Collectors.joining(", ")));
				printWriter.append(']');
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
		if (httpServletRequest.getServletPath().equalsIgnoreCase("/Feedback/Subtask/") && SessionValidation.sessionValid(httpServletRequest)) {
			try {
				final Long subtaskId = Long.parseLong(httpServletRequest.getParameter("subtask-id"));
				final Short rating = Short.parseShort(httpServletRequest.getParameter("rating"));
				final String remarks = httpServletRequest.getParameter("remarks");
				feedbackService.putFeedbackUser(connection, subtaskId, rating, remarks);
				httpServletResponse.sendRedirect("/Tasker/Home/");
			}
			catch (SQLException sqlException) {
				sqlException.printStackTrace();
			}
			catch (NumberFormatException numberFormatException) {
				numberFormatException.printStackTrace();
				throw numberFormatException;
			}
		}
	}
}
