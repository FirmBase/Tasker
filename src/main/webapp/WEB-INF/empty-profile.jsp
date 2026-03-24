<%@ page isErrorPage="true" language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="en">
	<head>
		<title>Profile</title>
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

			/* For Chrome, Safari, Edge, Opera */
			input::-webkit-outer-spin-button, input::-webkit-inner-spin-button {
				-webkit-appearance: none;
				margin: 0;
			}
			/* For Firefox */
			input[type=number] {
				-moz-appearance: textfield;
			}
			/* Standard property for modern browsers */
			input[type=number] {
				appearance: textfield;
			}
		</style>
		<script type="text/javascript">
			function sameAddress(e) {
				var fields = {
					"temporary-address-input": "permanent-address-input",
					"temporary-district-input": "permanent-district-input",
					"temporary-state-input": "permanent-state-input",
					"temporary-country-input": "permanent-country-input",
					"temporary-phone-no-input": "permanent-phone-no-input",
					"temporary-email-input": "permanent-email-input"
				};
				if (e.checked) {
					for (var [key, value] of Object.entries(fields)) {
						window.document.getElementById(value).value = window.document.getElementById(key).value;
						window.document.getElementById(value).readOnly = true;
					}
				} else {
					for (var [key, value] of Object.entries(fields)) {
						window.document.getElementById(value).readOnly = false;
					}
				}
			}
		</script>
	</head>
	<body>
		<%@ include file="navigation-bar.jsp" %>
		<div class="container">
			<div style="background-color: rgba(0, 0, 0, 0.2);" class="p-3">
				<c:if test="${not empty message}">
					<div class="row">
						<h1 class="h1 text-white text-center"><c:out value="${message}" /></h1>
					</div>
				</c:if>
				<form method="post" action="/Tasker/Home/Profile/" enctype="application/x-www-form-urlencoded">
					<fieldset>
						<legend class="text-white">Profile details</legend>
						<div class="row">
							<div class="col-8 form-floating mx-auto my-4">
								<input class="form-control" type="text" name="name" placeholder="Name" id="name-input" aria-label="Name" maxlength="32" aria-required="true" required autofocus />
								<label for="name-input">Name</label>
							</div>
						</div>
						<div class="row">
							<div class="col-4 form-floating mx-auto my-4">
								 <select class="form-select" name="company" id="company-input" aria-label="Company" selected aria-selected aria-required="true" required>
									<option disabled aria-disabled selected>Select company</option>
									<c:forEach items="${all_companies}" var="company">
										<option value="${company.key}">${company.value}</option>
									</c:forEach>
								 </select>
								 <label for="company-input">Company</label>
							</div>
							<div class="col-4 form-floating mx-auto my-4">
								<input class="form-control" type="text" name="designation" placeholder="Designation" id="designation-input" maxlength="16" aria-label="Designation" aria-required="true" required />
								<label for="designation-input">Designation</label>
							</div>
						</div>
						<div class="row">
							<div class="col-4 form-floating mx-auto my-4">
								<input class="form-control" type="date" name="dob" placeholder="Date of Birth" id="dob-input" aria-label="Date of Birth" aria-required="true" required />
								<label for="dob-input">Date of Birth</label>
							</div>
							<div class="col-4 form-floating mx-auto my-4">
								<input class="form-control" type="text" name="blood-group" placeholder="Blood group" id="blood-group-input" maxlength="2" aria-label="Blood group" aria-required="true" maxlength="2" required />
								<label for="blood-group-input">Blood group</label>
							</div>
						</div>
					</fieldset>
					<fieldset>
						<legend class="text-white">Temporary contact</legend>
						<div class="row">
							<div class="col-8 form-floating mx-auto my-4">
								<input class="form-control" type="text" name="contact-name" placeholder="Contact name" id="temporary-contact-name-input" maxlength="8" aria-label="Contact name" aria-required="true" value="Temporary" required />
								<label for="temporary-contact-name-input">Contact name</label>
							</div>
						</div>
						<div class="row">
							<div class="col-4 form-floating mx-auto my-4">
								<input class="form-control" type="text" name="address" placeholder="Address" id="temporary-address-input" maxlength="64" aria-label="Address" aria-required="true" required />
								<label for="temporary-address-input">Address</label>
							</div>
							<div class="col-4 form-floating mx-auto my-4">
								<input class="form-control" type="text" name="district" placeholder="District" id="temporary-district-input" maxlength="16" aria-label="District" aria-required="true" required />
								<label for="temporary-district-input">District</label>
							</div>
						</div>
						<div class="row">
							<div class="col-4 form-floating mx-auto my-4">
								<input class="form-control" type="text" name="state" placeholder="State" id="temporary-state-input" maxlength="32" aria-label="State" aria-required="true" required />
								<label for="temporary-state-input">State</label>
							</div>
							<div class="col-4 form-floating mx-auto my-4">
								 <select class="form-select" name="country" id="temporary-country-input" aria-label="Country" selected aria-selected aria-required="true" required>
									<option disabled aria-disabled selected>Select country</option>
									<c:forEach items="${countries}" var="country">
										<option value="${country.countryCode}">${country.countryName}</option>
									</c:forEach>
								 </select>
								 <label for="temporary-country-input">Country</label>
							</div>
						</div>
						<div class="row">
							<div class="col-4 form-floating mx-auto my-4">
								<input class="form-control" type="tel" name="phone-no" placeholder="Phone no." id="temporary-phone-no-input" maxlength="16" aria-label="Phone no." aria-required="true" minlength="7" maxlength="16" required />
								<label for="temporary-phone-no-input">Phone no.</label>
							</div>
							<div class="col-4 form-floating mx-auto my-4">
								<input class="form-control" type="email" name="email" placeholder="Email" id="temporary-email-input" maxlength="32" aria-label="Email" aria-required="true" required />
								<label for="temporary-email-input">Email</label>
							</div>
						</div>
					</fieldset>
					<div class="row">
						<div class="col-4 form-floating mx-auto my-4">
							<div class="form-check">
								<label class="form-check-label" for="same-address-input">Same as temporary address</label>
								<input class="form-check-input" type="checkbox" name="same-address" placeholder="Same as temporary address" id="same-address-input" onchange="sameAddress(this);" aria-label="Same as temporary address" value="Same as temporary address" />
							</div>
						</div>
					</div>
					<fieldset>
						<legend class="text-white">Permanent contact</legend>
						<div class="row">
							<div class="col-8 form-floating mx-auto my-4">
								<input class="form-control" type="text" name="contact-name" placeholder="Contact name" id="permanent-contact-name-input" maxlength="8" aria-label="Contact name" aria-required="true" value="Permanent" required />
								<label for="permanent-contact-name-input">Contact name</label>
							</div>
						</div>
						<div class="row">
							<div class="col-4 form-floating mx-auto my-4">
								<input class="form-control" type="text" name="address" placeholder="Address" id="permanent-address-input" maxlength="64" aria-label="Address" aria-required="true" required />
								<label for="permanent-address-input">Address</label>
							</div>
							<div class="col-4 form-floating mx-auto my-4">
								<input class="form-control" type="text" name="district" placeholder="District" id="permanent-district-input" maxlength="16" aria-label="District" aria-required="true" required />
								<label for="permanent-district-input">District</label>
							</div>
						</div>
						<div class="row">
							<div class="col-4 form-floating mx-auto my-4">
								<input class="form-control" type="text" name="state" placeholder="State" id="permanent-state-input" maxlength="32" aria-label="State" aria-required="true" required />
								<label for="permanent-state-input">State</label>
							</div>
							<div class="col-4 form-floating mx-auto my-4">
								 <select class="form-select" name="country" id="permanent-country-input" aria-label="Country" selected aria-selected aria-required="true" required>
									<option disabled aria-disabled selected>Select country</option>
									<c:forEach items="${countries}" var="country">
										<option value="${country.countryCode}">${country.countryName}</option>
									</c:forEach>
								 </select>
								 <label for="permanent-country-input">Country</label>
							</div>
						</div>
						<div class="row">
							<div class="col-4 form-floating mx-auto my-4">
								<input class="form-control" type="tel" name="phone-no" placeholder="Phone no." id="permanent-phone-no-input" maxlength="16" aria-label="Phone no." aria-required="true" minlength="7" maxlength="16" required />
								<label for="permanent-phone-no-input">Phone no.</label>
							</div>
							<div class="col-4 form-floating mx-auto my-4">
								<input class="form-control" type="email" name="email" placeholder="Email" id="permanent-email-input" maxlength="32" aria-label="Email" aria-required="true" required />
								<label for="permanent-email-input">Email</label>
							</div>
						</div>
					</fieldset>
					<div class="row">
						<div class="input-group d-flex justify-content-center">
							<button type="submit" class="btn btn-outline-primary">Submit</button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</body>
</html>
