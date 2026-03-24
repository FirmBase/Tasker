package com.yash.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.yash.Configuration;
import com.yash.dtos.UserCredentialDTO;

public class SessionValidation {
	public static HttpSession createUserSession(final HttpServletRequest httpServletRequest, final UserCredentialDTO userCredentialDTO) {
		sessionInvalidate(httpServletRequest);
		final HttpSession httpSession = httpServletRequest.getSession();
		httpSession.setAttribute(Configuration.USER_SESSION_ATTRIBUTE, userCredentialDTO);
		return httpSession;
	}

	public static boolean sessionValid(final HttpServletRequest httpServletRequest) {
		if (httpServletRequest.getSession(false) != null) {
			final UserCredentialDTO userCredentialDTO = (UserCredentialDTO) httpServletRequest.getSession(false).getAttribute(Configuration.USER_SESSION_ATTRIBUTE);
			return userCredentialDTO != null;
		}
		else
			return false;
	}

	public static void sessionInvalidate(final HttpServletRequest httpServletRequest) {
		if (httpServletRequest.getSession(false) != null) {
			httpServletRequest.getSession(false).removeAttribute(Configuration.USER_SESSION_ATTRIBUTE);
			httpServletRequest.getSession(false).invalidate();
		}
	}
}
