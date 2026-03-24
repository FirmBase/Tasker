package com.yash;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.yash.controllers.LiveUpdateController;

public class ContextListener implements ServletContextListener {
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		LiveUpdateController.getAsynccontexts().clear();
	}
}
