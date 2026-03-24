package com.yash.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
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
import com.yash.dtos.NewSubtaskDTO;
import com.yash.dtos.NewTaskDTO;
import com.yash.dtos.SubtaskAssignmentDTO;
import com.yash.dtos.TaskAssignmentDTO;
import com.yash.dtos.UserCredentialDTO;
import com.yash.security.SessionValidation;
import com.yash.services.TaskManagementService;
import com.yash.services.TaskManagementServiceImpl;
import com.yash.services.UserService;
import com.yash.services.UserServiceImplements;

@WebServlet(urlPatterns = {"/API/Tasks/", "/API/Subtasks/*", "/New/Task/"})
public class TaskManagementController extends HttpServlet {
	final private TaskManagementService taskManagementService = new TaskManagementServiceImpl();
	final private UserService userService = new UserServiceImplements();
	private Connection connection;

	public TaskManagementController() throws SQLException {
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
		if (httpServletRequest.getServletPath().matches("/API/Subtasks") && httpServletRequest.getPathInfo().matches("/\\d+") && SessionValidation.sessionValid(httpServletRequest)) {
			final Long taskId = Long.parseLong(httpServletRequest.getPathInfo().split("/")[1]);
			try (final PrintWriter printWriter = httpServletResponse.getWriter()) {
				httpServletResponse.setContentType("application/json");
				printWriter.append('[');
				printWriter.append(taskManagementService.getAllSubtaskManagements(connection, taskId).stream().map(SubtaskAssignmentDTO::toString).collect(Collectors.joining(", ")));
				printWriter.append(']');
			}
			catch (SQLException sqlException) {
				sqlException.printStackTrace();
			}
			catch (Exception exception) {
				exception.printStackTrace();
			}
		}
		else if (httpServletRequest.getServletPath().equalsIgnoreCase("/API/Tasks/") && SessionValidation.sessionValid(httpServletRequest)) {
			try (final PrintWriter printWriter = httpServletResponse.getWriter()) {
				httpServletResponse.setContentType("application/json");
				printWriter.append('[');
				printWriter.append(taskManagementService.getAllTasksAccordingToAssginee(connection, ((UserCredentialDTO) httpServletRequest.getSession(false).getAttribute(Configuration.USER_SESSION_ATTRIBUTE)).getId()).stream().map(TaskAssignmentDTO::toString).collect(Collectors.joining(", ")));
				printWriter.append(']');
			}
			catch (SQLException sqlException) {
				sqlException.printStackTrace();
			}
			catch (Exception exception) {
				exception.printStackTrace();
			}
		}
		else if (httpServletRequest.getServletPath().equalsIgnoreCase("/New/Task/") && SessionValidation.sessionValid(httpServletRequest)) {
			try {
				final UserCredentialDTO userCredentialDTO = (UserCredentialDTO) httpServletRequest.getSession(false).getAttribute(Configuration.USER_SESSION_ATTRIBUTE);
				httpServletRequest.setAttribute("status", taskManagementService.getAllStatus(connection));
				httpServletRequest.setAttribute("priority", taskManagementService.getAllPriority(connection));
				httpServletRequest.setAttribute("workforce", userService.getAllUsers(connection).stream().filter(userDTO -> userDTO.getUserCredentialDTO().getId() != userCredentialDTO.getId()).collect(Collectors.toList()));
				System.out.println("" + userService.getAllUsers(connection));
				httpServletRequest.getRequestDispatcher("/WEB-INF/new-task.jsp").forward(httpServletRequest, httpServletResponse);
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
		if (httpServletRequest.getServletPath().equalsIgnoreCase("/New/Task/") && SessionValidation.sessionValid(httpServletRequest)) {
			try {
				final String taskName = httpServletRequest.getParameter("task-title");
				final String taskDescription = httpServletRequest.getParameter("task-description");
				final String taskPriority = httpServletRequest.getParameter("priority");
				final Date taskStartDate = new SimpleDateFormat("yyyy-MM-dd").parse(httpServletRequest.getParameter("start-date"));
				final Date taskDueDate = new SimpleDateFormat("yyyy-MM-dd").parse(httpServletRequest.getParameter("due-date"));
				final String[] subtaskNames = httpServletRequest.getParameterValues("subtask-title");
				final String[] subtaskDescriptions = httpServletRequest.getParameterValues("subtask-description");
				final String[] subtaskPriorities = httpServletRequest.getParameterValues("subtask-priority");
				final Date[] subtaskAssignDates = new Date[httpServletRequest.getParameterValues("subtask-start").length];
				for (int index = 0; index < httpServletRequest.getParameterValues("subtask-due").length; ++index) {
					subtaskAssignDates[index] = new SimpleDateFormat("yyyy-MM-dd").parse(httpServletRequest.getParameterValues("subtask-start")[index]);
				}
				final Date[] subtaskDueDates = new Date[httpServletRequest.getParameter("subtask-due").length()];
				for (int index = 0; index < httpServletRequest.getParameterValues("subtask-due").length; ++index) {
					subtaskDueDates[index] = new SimpleDateFormat("yyyy-MM-dd").parse(httpServletRequest.getParameterValues("subtask-due")[index]);
				}
				final String[] subtaskAssignTos = httpServletRequest.getParameterValues("assigned-to");

				final int minLength = Collections.min(Arrays.asList(
					subtaskNames.length,
					subtaskDescriptions.length,
					subtaskPriorities.length,
					subtaskAssignTos.length,
					subtaskAssignDates.length,
					subtaskDueDates.length
				));
				/*
				final List<String[]> subtasks = new ArrayList<String[]>();
				for (int index = 0; index < minLength; index++) {
					subtasks.add(new String[] {
						subtaskNames[index],
						subtaskDescriptions[index],
						subtaskPriorities[index],
						subtaskAssignTos[index]
					});
				}
				*/
				final List<NewSubtaskDTO> newSubtaskDTOs = new ArrayList<NewSubtaskDTO>();
				for (int index = 0; index < minLength; ++index) {
					final String assignedToUsername = subtaskAssignTos[index].split("@")[subtaskAssignTos[index].split("@").length - 1];
					final Long assignedToId = userService.findIdByUsername(connection, assignedToUsername);
					final NewSubtaskDTO newSubtaskDTO = new NewSubtaskDTO(subtaskNames[index], subtaskDescriptions[index], assignedToId, Long.parseLong(subtaskPriorities[index]), subtaskAssignDates[index], subtaskDueDates[index]);
					newSubtaskDTOs.add(newSubtaskDTO);
				}
				final UserCredentialDTO assgineeUserCredentialDTO = (UserCredentialDTO) httpServletRequest.getSession(false).getAttribute(Configuration.USER_SESSION_ATTRIBUTE);
				final NewTaskDTO newTaskDTO = new NewTaskDTO(taskName, taskDescription, assgineeUserCredentialDTO.getId(), Long.parseLong(taskPriority), taskStartDate, taskDueDate, newSubtaskDTOs);
				taskManagementService.registerNewTask(connection, newTaskDTO);
				httpServletResponse.sendRedirect("/Tasker/Home/");
			}
			catch (ParseException parseException) {
				parseException.printStackTrace();
			}
			catch (SQLException sqlException) {
				sqlException.printStackTrace();
			}
		}
		else
			httpServletResponse.sendRedirect("/Tasker/Login/");
	}
}
