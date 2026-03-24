package com.yash.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yash.security.SessionValidation;

@WebServlet(urlPatterns = "/Check/Updates/", asyncSupported = true)
public class LiveUpdateController extends HttpServlet {
	private static final Queue<AsyncContext> asyncContexts = new ConcurrentLinkedQueue<AsyncContext>();

	public LiveUpdateController() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
		httpServletResponse.sendRedirect("/Tasker/Home/");
	}

	@Override
	protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
		final AsyncContext asyncContext = httpServletRequest.startAsync();
		asyncContext.setTimeout(30000);	// 30s timeout

		asyncContext.addListener(new AsyncListener() {
			@Override
			public void onStartAsync(AsyncEvent asyncEvent) {
			}

			@Override
			public void onTimeout(AsyncEvent asyncEvent) {
				try {
					asyncContexts.remove(asyncContext);
					final HttpServletResponse httpServletResponse = (HttpServletResponse) asyncEvent.getAsyncContext().getResponse();
					httpServletResponse.setStatus(HttpServletResponse.SC_NO_CONTENT);
					asyncEvent.getAsyncContext().complete();
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}

			@Override
			public void onError(AsyncEvent asyncEvent) {
			}

			@Override
			public void onComplete(AsyncEvent asyncEvent) {
			}
		});
		httpServletResponse.setContentType("text/plain");
		asyncContexts.add(asyncContext);
	}

	public static void spreadUpdate(String message) {
		while (!asyncContexts.isEmpty()) {
			final AsyncContext asyncContext = asyncContexts.poll();
			if (asyncContext != null) {
				try (final PrintWriter printWriter = asyncContext.getResponse().getWriter()) {
					printWriter.write(message);
					asyncContext.complete();
				} catch (IOException ioException) {
					ioException.printStackTrace();
				}
			}
		}
	}

	public static Queue<AsyncContext> getAsynccontexts() {
		return asyncContexts;
	}
}
