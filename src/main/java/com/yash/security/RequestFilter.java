package com.yash.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
		final HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

		final List<String> bypassURLs = Arrays.asList(
			"/Tasker/Home/",
			"/Tasker/tasks_table.js",
			"/Tasker/tasks_table.css",
			"/Tasker/data_table.css",
			"/Tasker/Login/",
			"/Tasker/Register/",
			"/Tasker/Logout/",
			"/Tasker/Check/",
			"/Tasker/Task/",
			"/Tasker/Tasker/",
			"/Tasker/media/",
			"/Tasker/js/",
			"/Tasker/css/"
		);
		// System.out.println("Intercepted: " + httpServletRequest.getRequestURI());

		// final HttpSession httpSession = httpServletRequest.getSession(false);
		if (bypassURLs.stream().anyMatch(httpServletRequest.getRequestURI()::startsWith)) {
			// Pass the request to the next entity in the chain (servlet or next filter)
			filterChain.doFilter(httpServletRequest, httpServletResponse);
		}
		else if (!SessionValidation.sessionValid(httpServletRequest)) {
			httpServletResponse.sendRedirect("/Tasker/Login/");
		}
		else {
			// Pass the request to the next entity in the chain (servlet or next filter)
			filterChain.doFilter(httpServletRequest, httpServletResponse);
		}
	}

	@Override
	public void destroy() {
	}
}
