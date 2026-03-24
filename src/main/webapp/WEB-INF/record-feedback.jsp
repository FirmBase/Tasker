<%@ page isErrorPage="true" language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!doctype html>
<html lang="en">
	<head>
		<title>Feedback records</title>
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
			function outputRangeInput(range) {
				range.parentElement.getElementsByTagName("output")[0].textContent = range.value;
			}
		</script>
	</head>
	<body>
		<%@ include file="navigation-bar.jsp" %>
		<div class="container">
			<c:if test="${fn:length(feedbackDTOs) > 0}">
				<c:forEach items="${feedbackDTOs}" var="feedbackDTO">
					<div class="row text-light">
						<div class="col-4 mx-auto my-4 text-center">
							<c:out value="${feedbackDTO.subtaskDTO.taskDTO.title}" />
						</div>
						<div class="col-4 mx-auto my-4 text-center">
							<c:out value="${feedbackDTO.subtaskDTO.title}" />
						</div>
						<div class="col-4 mx-auto my-4 text-center">
							<c:out value="${feedbackDTO.subtaskDTO.currentProgress}%" />
						</div>
					</div>
					<form style="background-color: rgba(0, 0, 0, 0.2);" class="p-4" action="/Tasker/Feedback/Subtask/" method="post" enctype="application/x-www-form-urlencoded">
						<div class="row">
							<div class="col-4">
								<input class="form-control" type="hidden" name="subtask-id" placeholder="Subtask" id="subtask-input" aria-label="Subtask" value="${feedbackDTO.subtaskDTO.id}" required />
							</div>
						</div>
						<div class="row">
							<div class="col-4 form-floating mx-auto my-4 w-25">
								<label class="form-label text-white" for="rating-input">Rating</label>
								<input class="form-range" type="range" name="rating" placeholder="Rating" id="rating-input" value="${feedbackDTO.rating}" aria-label="Rating" aria-required="true" min="1" max="10" step="1" oninput="outputRangeInput(this);" required autofocus />
								<output class="text-white" for="rating-input">${feedbackDTO.rating}</output>
							</div>
						</div>
						<div class="row">
							<div class="col-8 form-floating mx-auto my-4">
								<input class="form-control" type="text" name="remarks" placeholder="Remarks" id="remarks-input" value="${feedbackDTO.remarks}" maxlength="32" aria-label="Remarks" aria-required="true" required />
								<label class="form-label" for="remarks-input">Remarks</label>
							</div>
						</div>
						<div class="row">
							<div class="col-12 input-group d-flex justify-content-center">
								<button type="submit" class="btn btn-primary">Submit</button>
							</div>
						</div>
					</form>
				</c:forEach>
			</c:if>
			<c:if test="${fn:length(feedbackDTOs) < 1}">
				<form style="background-color: rgba(0, 0, 0, 0.2);" class="p-4" action="/Tasker/Feedback/Subtask/" method="post" enctype="application/x-www-form-urlencoded">
					<div class="row">
						<div class="col-4">
							<input class="form-control" type="hidden" name="subtask-id" placeholder="Subtask" id="subtask-input" value="${subtaskId}" aria-label="Subtask" required />
						</div>
					</div>
					<div class="row">
						<div class="col-4 form-floating mx-auto my-4 w-25">
							<label class="form-label text-white" for="rating-input">Rating</label>
							<input class="form-range" type="range" name="rating" placeholder="Rating" id="rating-input" value="0" aria-label="Rating" aria-required="true" min="1" max="10" step="1" oninput="outputRangeInput(this);" required autofocus />
							<output class="text-white" for="rating-input"></output>
						</div>
					</div>
					<div class="row">
						<div class="col-8 form-floating mx-auto my-4">
							<input class="form-control" type="text" name="remarks" placeholder="Remarks" id="remarks-input" aria-label="Remarks" aria-required="true" required />
							<label class="form-label" for="remarks-input">Remarks</label>
						</div>
					</div>
					<div class="row">
						<div class="col-12 input-group d-flex justify-content-center">
							<button type="submit" class="btn btn-primary">Submit</button>
						</div>
					</div>
				</form>
			</c:if>
		</div>
		<script type="text/javascript">
			[].forEach.call(window.document.getElementsByTagName("tbody"), function(tbody) {
				count = 0;
				[].forEach.call(tbody.getElementsByTagName("tr"), function(tr) {
					tr.getElementsByTagName("td")[0].innerText = "" + ++count;
				});
			});
		</script>
	</body>
</html>
