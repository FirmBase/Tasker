package com.yash;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Servlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException {
		try (final PrintWriter printWriter = httpServletResponse.getWriter()) {
			httpServletResponse.setContentType(getServletInfo());
			printWriter.append("Text");
		}
		catch (final IOException ioException) {
			ioException.printStackTrace();
		}
	}
}
