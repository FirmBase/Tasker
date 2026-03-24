package com.yash.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.yash.Configuration;
import com.yash.dtos.UserCredentialDTO;
import com.yash.security.SessionValidation;
import com.yash.services.TaskManagementService;
import com.yash.services.TaskManagementServiceImpl;

@WebServlet(urlPatterns = {"/Task/Received/", "/Update/"})
public class AssignmentController extends HttpServlet {
	private Connection connection;
	final private TaskManagementService taskManagementService = new TaskManagementServiceImpl();

	public AssignmentController() throws SQLException {
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
		if (httpServletRequest.getServletPath().equalsIgnoreCase("/Task/Received/") && (SessionValidation.sessionValid(httpServletRequest))) {
			final UserCredentialDTO userCredentialDTO = (UserCredentialDTO) httpServletRequest.getSession(false).getAttribute(Configuration.USER_SESSION_ATTRIBUTE);
			try {
				httpServletRequest.setAttribute("taskAssignmentDTOs", taskManagementService.getAllTasksAssginedTo(connection, userCredentialDTO.getId()));
				System.out.println("" + taskManagementService.getAllTasksAssginedTo(connection, userCredentialDTO.getId()));
				httpServletRequest.getRequestDispatcher("/WEB-INF/received-task.jsp").forward(httpServletRequest, httpServletResponse);
			}
			catch (SQLException sqlException) {
				sqlException.printStackTrace();
			}
		}
		else
			httpServletResponse.sendRedirect("/Tasker/Login/");
	}

	@Override
	protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
		if (httpServletRequest.getServletPath().equalsIgnoreCase("/Update/") && SessionValidation.sessionValid(httpServletRequest)) {
			try {
				final PrintWriter printWriter = httpServletResponse.getWriter();
				httpServletResponse.setContentType("text/plain");
				final Long taskId = Long.parseLong(httpServletRequest.getParameter("task-id"));
				final Long subtaskId = Long.parseLong(httpServletRequest.getParameter("subtask-id"));
				final Short currentProgress = Short.parseShort(httpServletRequest.getParameter("current-progress"));
				final String lastRemarks = httpServletRequest.getParameter("last-remarks");
				final boolean checkState = Arrays.asList(taskId, subtaskId, currentProgress, lastRemarks).stream().allMatch(parameter -> parameter != null);
				if (checkState) {
					if (taskManagementService.saveSubtaskProgress(connection, taskId, subtaskId, currentProgress, URLDecoder.decode(lastRemarks, "UTF-8" /* StandardCharsets.UTF_8 */)))
						printWriter.append("success");
					else
						printWriter.append("fail");
				}
				else
					printWriter.append("fail");
				printWriter.close();
			}
			catch (SQLException sqlException) {
				sqlException.printStackTrace();
			}
		}
		else
			httpServletResponse.sendRedirect("/Tasker/Login/");
	}
}
