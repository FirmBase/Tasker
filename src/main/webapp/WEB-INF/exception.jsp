<%@ page isErrorPage="true" language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.io.StringWriter" %>
<%@ page import="java.io.PrintWriter" %>

<!doctype html>
<html lang="en">
	<head>
		<title>Exception</title>
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1" />
		<link rel="preconnect" href="https://fonts.googleapis.com" />
		<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
		<link href="https://fonts.googleapis.com/css2?family=Google+Sans:ital,opsz,wght@0,17..18,400..700;1,17..18,400..700&display=swap" rel="stylesheet" />
		<link type="text/css" href="/Tasker/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous" />
		<script type="text/javascript" src="/Tasker/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
		<style type="text/css">
			* {
				font-family: "Montserrat", "sans-serif";
			}
			body {
				background: url("/Tasker/media/StockSnap_AEDCD6UPR2.jpg");
				background-position: center;
				/* background: radial-gradient(circle, rgba(255, 255, 255, 1) 0%, rgba(87, 199, 133, 1) 50%, rgba(237, 221, 83, 1) 100%); */
			}
		</style>
		<script type="text/javascript"></script>
	</head>
	<body>
		<div class="container-fluid p-4 bg-danger text-center">
			<h1 class="h1">Internal Error</h1>
			<strong>Status Code:</strong> <%= request.getAttribute("javax.servlet.error.status_code") %><br />
			<strong>Error Message:</strong> <%= request.getAttribute("javax.servlet.error.message") %><br />
		</div>
		<div class="container-fluid p-3 text-left text-white">
			<% if (exception != null) { %>
					<p><strong>Type:</strong> <%= exception.getClass().getName() %></p>
					<p><strong>Message:</strong> <%= exception.getMessage() %></p>
			<% } %>
		</div>
		<div class="container">
			<div class="row">
				<div class="m-12 text-left text-white">
					<%
						final StringWriter stringWriter = new StringWriter();
						final PrintWriter printWriter = new PrintWriter(stringWriter);
						exception.printStackTrace(printWriter);
						out.print(stringWriter.toString());
					%>
				</div>
			</div>
			<div class="row">
				<div class="col-6">
					<h2 class="h2 p-5 m-0">Go back to <a href="../login.html">Login</a></h2>
				</div>
				<div class="col-6">
					<h2 class="h2 p-5 m-0">Go to registration <a href="../register.html">Register</a></h2>
				</div>
			</div>
		</div>
	</body>
</html>
