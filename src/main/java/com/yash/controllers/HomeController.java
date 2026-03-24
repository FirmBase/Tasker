package com.yash.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.yash.Configuration;
import com.yash.dtos.CompanyDTO;
import com.yash.dtos.ContactDetailsDTO;
import com.yash.dtos.UserCredentialDTO;
import com.yash.dtos.UserDTO;
import com.yash.security.SessionValidation;
import com.yash.services.CompanyService;
import com.yash.services.CompanyServiceImpl;
import com.yash.services.UserService;
import com.yash.services.UserServiceImplements;

@WebServlet(urlPatterns = {"/Home/", "/Home/Profile/"})
public class HomeController extends HttpServlet {
	private final UserService userService = new UserServiceImplements();
	private final CompanyService companyService = new CompanyServiceImpl();
	private Connection connection;

	public HomeController() throws SQLException {
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
		if ((!httpServletRequest.getServletPath().equalsIgnoreCase("/Home/Profile/")) && SessionValidation.sessionValid(httpServletRequest) && (!userService.userProfileCompletion(connection, ((UserCredentialDTO) httpServletRequest.getSession(false).getAttribute(Configuration.USER_SESSION_ATTRIBUTE)).getId()))) {
			httpServletResponse.sendRedirect("/Tasker/Home/Profile/");
		}
		else if (httpServletRequest.getServletPath().equalsIgnoreCase("/Home/") && (SessionValidation.sessionValid(httpServletRequest)))
			httpServletRequest.getRequestDispatcher("/WEB-INF/home.jsp").forward(httpServletRequest, httpServletResponse);
		else if (httpServletRequest.getServletPath().equalsIgnoreCase("/Home/Profile/") && (SessionValidation.sessionValid(httpServletRequest))) {
			try {
				final UserCredentialDTO userCredentialDTO = (UserCredentialDTO) httpServletRequest.getSession(false).getAttribute(Configuration.USER_SESSION_ATTRIBUTE);
				final UserDTO userDTO = userService.getUserProfile(connection, userCredentialDTO.getId());
				httpServletRequest.setAttribute("countries", userService.getAllCountries(connection));
				httpServletRequest.setAttribute("all_companies", companyService.getAllCompanies(connection));
				if (userDTO == null) {
					httpServletRequest.getRequestDispatcher("/WEB-INF/empty-profile.jsp").forward(httpServletRequest, httpServletResponse);
				}
				else {
					httpServletRequest.setAttribute("userDTO", userDTO);
					httpServletRequest.getRequestDispatcher("/WEB-INF/profile.jsp").forward(httpServletRequest, httpServletResponse);
				}
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
		if (httpServletRequest.getServletPath().equalsIgnoreCase("/Home/") && SessionValidation.sessionValid(httpServletRequest))
			httpServletRequest.getRequestDispatcher("/WEB-INF/home.jsp").forward(httpServletRequest, httpServletResponse);
		else if (httpServletRequest.getServletPath().equalsIgnoreCase("/Home/Profile/") && SessionValidation.sessionValid(httpServletRequest)) {
			final UserCredentialDTO userCredentialDTO = (UserCredentialDTO) httpServletRequest.getSession(false).getAttribute(Configuration.USER_SESSION_ATTRIBUTE);
			// final List<String> parameters = Arrays.asList("name", "company", "designation", "dob", "blood-group", "contact-name", "address", "district", "state", "country", "phone-no", "email").stream().map(parameter -> httpServletRequest.getParameter(parameter)).collect(Collectors.toList());
			final Map<String, String[]> parameters = httpServletRequest.getParameterMap();
			// final String userName = httpServletRequest.getParameter("name");
			// final String company = httpServletRequest.getParameter("company");
			// final String designation = httpServletRequest.getParameter("designation");
			// final String dateOfBirth = httpServletRequest.getParameter("dob");
			// final String bloodGroup = httpServletRequest.getParameter("blood-group");
			try {
				// final boolean requiredPresent = parameters.values().stream().flatMap(Arrays::stream).anyMatch(Objects::isNull);
				final Date dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").parse(parameters.get("dob")[0]);
				if (true) {
					final CompanyDTO companyDTO = userService.getUserCompany(connection, Long.parseLong(parameters.get("company")[0]));
					final ContactDetailsDTO temporaryContactDetailsDTO = new ContactDetailsDTO(null, parameters.get("contact-name")[0], parameters.get("address")[0], parameters.get("district")[0], parameters.get("state")[0], parameters.get("country")[0], parameters.get("phone-no")[0], parameters.get("email")[0], null);
					final ContactDetailsDTO permanentContactDetailsDTO = new ContactDetailsDTO(null, parameters.get("contact-name")[1], parameters.get("address")[1], parameters.get("district")[1], parameters.get("state")[1], parameters.get("country")[1], parameters.get("phone-no")[1], parameters.get("email")[1], null);
					UserDTO userDTO = userService.getUserProfile(connection, userCredentialDTO.getId());
					if (userDTO == null) {
						userDTO = new UserDTO(null, parameters.get("name")[0], parameters.get("designation")[0], dateOfBirth, parameters.get("blood-group")[0], companyDTO, userCredentialDTO, temporaryContactDetailsDTO, permanentContactDetailsDTO);
						System.out.println(userService.insertUserProfile(connection, userDTO) ? "User profile added" : "User profile failed to save");
					}
					else {
						userDTO.setName(parameters.get("name")[0]);
						userDTO.setDesignation(parameters.get("designation")[0]);
						userDTO.setDateOfBirth(dateOfBirth);
						userDTO.setBloodGroup(parameters.get("blood-group")[0]);
						userDTO.setEmployeeCompanyDTO(companyDTO);
						temporaryContactDetailsDTO.setId(userDTO.getTemporaryContactDetailsDTO().getId());
						userDTO.setTemporaryContactDetailsDTO(temporaryContactDetailsDTO);
						permanentContactDetailsDTO.setId(userDTO.getPermanentContactDetailsDTO().getId());
						userDTO.setPermanenDetailsDTO(permanentContactDetailsDTO);
						System.out.println(userService.updateUserProfile(connection, userDTO) ? "User profile updated" : "User profile updation failed");
					}
				}
				httpServletRequest.setAttribute("message", "Saved!");
				final UserDTO userDTO = userService.getUserProfile(connection, userCredentialDTO.getId());
				httpServletRequest.setAttribute("userDTO", userDTO);
				httpServletRequest.setAttribute("countries", userService.getAllCountries(connection));
				httpServletRequest.setAttribute("all_companies", companyService.getAllCompanies(connection));
				httpServletRequest.getRequestDispatcher("/WEB-INF/profile.jsp").forward(httpServletRequest, httpServletResponse);
			}
			catch (SQLException sqlException) {
				sqlException.printStackTrace();
			}
			catch (ParseException parseException) {
				parseException.printStackTrace();
			}
		}
		else
			httpServletResponse.sendRedirect("/Tasker/Login/");
	}
}
