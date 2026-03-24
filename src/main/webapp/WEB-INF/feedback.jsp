<%@ page isErrorPage="true" language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<!doctype html>
<html lang="en">
	<head>
		<title>Feedback</title>
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
		</script>
	</head>
	<body>
		<%@ include file="navigation-bar.jsp" %>
		<div class="container">
			<div style="background-color: rgba(0, 0, 0, 0.2);" class="row p-0 my-4 mx-1">
				<table class="table table-dark table-striped my-1 mx-0">
				<thead>
					<tr>
						<th>#</th>
						<th>Subtask</th>
						<th>Description</th>
						<th>Rating</th>
						<th>Remarks</th>
						<th>Status</th>
						<!-- <th>Assgined On</th> -->
						<th>Due</th>
						<th>Progress</th>
						<!-- <th>Assigned To</th> -->
						<th>Remarks</th>
						<th>Task</th>
						<th>Description</th>
						<!-- <th>Assignee</th> -->
						<th>Status</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${feedbackDTOs}" var="feedbackDTO">
						<tr>
							<td></td>
							<td><c:out value="${feedbackDTO.subtaskDTO.title}" /></td>
							<td><c:out value="${feedbackDTO.subtaskDTO.description}" /></td>
							<td><c:out value="${feedbackDTO.rating}" /></td>
							<td><c:out value="${feedbackDTO.remarks}" /></td>
							<td><span class="badge bg-primary"><c:out value="${feedbackDTO.subtaskDTO.statusDTO.statusEnum}" /></span></td>
							<td>
								<fmt:parseDate value="${feedbackDTO.subtaskDTO.dueDate}" var="parsedDate" pattern="yyyy-MM-dd" />
								<fmt:formatDate value="${parsedDate}" pattern="E dd-MMM-yyyy" />
							</td>
							<td><c:out value="${feedbackDTO.subtaskDTO.currentProgress}%" /></td>
							<td><c:out value="${feedbackDTO.subtaskDTO.lastRemarks}" /></td>
							<td><c:out value="${feedbackDTO.subtaskDTO.taskDTO.title}" /></td>
							<td><c:out value="${feedbackDTO.subtaskDTO.taskDTO.description}" /></td>
							<td><span class="badge bg-info"><c:out value="${feedbackDTO.subtaskDTO.taskDTO.statusDTO.statusEnum}" /></span></td>
						</tr>
					</c:forEach>
				</tbody>
				</table>
			</div>
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
