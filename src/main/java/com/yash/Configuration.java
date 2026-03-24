package com.yash;

public class Configuration {
	public static String DBMS_URL = "jdbc:mariadb://localhost:3306/tasker";
	public static String DBMS_USER = "yash";
	public static String DBMS_PASSWORD = "yash";
	public static String DBMS_DRIVER_CLASS = "org.mariadb.jdbc.Driver";

	public static String USER_SESSION_ATTRIBUTE = "User";

	public static short SALT_ROUND_COST = 12;

	public static String SMTP_USERNAME = "";
	public static String SMTP_PASSWORD = "psof" + "qiux" + "vqdy" + "anvn";
	public static String SMTP_SERVER_HOST = "smtp.mail.yahoo.com";
	public static short SMTP_SSL_TLS_PORT = 587;

	private final static String defaultGeminiAPIKey = "AIzaSyA0i_j_n3ejsvHWmk4w-xQQsGaWd0bJVqM";
	private final static String tasker = "AIzaSyDva_gT3YGZtXE3o0ETH-8wYT7FJeU3DgI";
	public static String GEMINI_API_KEY = tasker;

	private Configuration() {
		super();
	};
}
