package com.yash.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yash.security.SessionValidation;
import com.yash.utilities.GeminiGenAI;

@WebServlet(urlPatterns = "/API/AI/Generate/Subtasks/")
public class AIController extends HttpServlet {
	public AIController() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
		processRequest(httpServletRequest, httpServletResponse);
	}

	@Override
	protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
		processRequest(httpServletRequest, httpServletResponse);
	}

	protected void processRequest(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse) {
		try (final PrintWriter printWriter = httpServletResponse.getWriter()) {
			if (httpServletRequest.getServletPath().equalsIgnoreCase("/API/AI/Generate/Subtasks/") && SessionValidation.sessionValid(httpServletRequest)) {
				final String taskTitle = URLDecoder.decode(httpServletRequest.getParameter("title"), "UTF-8");
				final String taskDescription = URLDecoder.decode(httpServletRequest.getParameter("description"), "UTF-8");
				httpServletResponse.setContentType("application/json");
				printWriter.append(GeminiGenAI.generateSubtasks(taskTitle, taskDescription));
			}
			else {
				httpServletResponse.setContentType("text/plain");
				printWriter.append("URL invalid or user authentication failed.");
			}
		}
		catch (final IOException ioException) {
			ioException.printStackTrace();
		}
	}
}
