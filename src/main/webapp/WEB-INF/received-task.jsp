<%@ page isErrorPage="true" language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<!doctype html>
<html lang="en">
	<head>
		<title>Tasks recevied</title>
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1" />

		<link rel="preconnect" href="https://fonts.googleapis.com" />
		<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
		<link href="https://fonts.googleapis.com/css2?family=Google+Sans:ital,opsz,wght@0,17..18,400..700;1,17..18,400..700&display=swap" rel="stylesheet" />

		<link type="text/css" href="/Tasker/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous" />
		<script type="text/javascript" src="/Tasker/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>

		<script type="text/javascript" src="/Tasker/js/jquery-4.0.0.min.js" integrity="sha256-OaVG6prZf4v69dPg6PhVattBXkcOWQB62pdZ3ORyrao=" crossorigin="anonymous"></script>
		<link href="/Tasker/css/datatables.min.css" rel="stylesheet" integrity="sha384-wCnlGUpaekN+Mtc+qIoipdqIqe2dvC7hWyzVg8wajZ1sxKnVTbnyBd7pyx7JT0Su" crossorigin="anonymous" />
		<script type="text/javascript" src="/Tasker/js/datatables.min.js" integrity="sha384-aQ8I1X2x8U0AR8D7C4Ah0OvZlwMslQdN5YDAQBA56jXrrhcECijs/i7H+5DDrlV1" crossorigin="anonymous"></script>

		<link type="text/css" href="/Tasker/tasks_table.css" rel="stylesheet" />
		<style type="text/css">
			* {
				font-family: "Montserrat", "sans-serif";
			}
			body {
				background: url("/Tasker/media/StockSnap_AEDCD6UPR2.jpg");
				background-position: center;
				/* background: radial-gradient(circle, rgba(255, 255, 255, 1) 0%, rgba(87, 199, 133, 1) 50%, rgba(237, 221, 83, 1) 100%); */
			}
			.collapsing {
				transition: height 0.5s ease-in-out;
			}
		</style>

		<script type="text/javascript">
			function subtableDisplay(table) {
				if (table.getAttribute("aria-expanded") == "true")
					table.getElementsByTagName("td")[0].innerText = "-";
				else if (table.getAttribute("aria-expanded") == "false")
					table.getElementsByTagName("td")[0].innerText = "+";
			}
			function outputChange(range) {
				range.parentElement.getElementsByTagName("output")[0].textContent = range.value + "%";
			}
			function sendProgress(button, taskId, subtaskId) {
				var progress = window.document.getElementById("user-progress" + taskId + subtaskId).value;
				var lastRemarks = window.document.getElementById("user-remarks" + taskId + subtaskId).value;
				// var base64LastRemarks = btoa(String.fromCharCode(new TextEncoder().encode(lastRemarks)));
				var xhttp = new XMLHttpRequest();
				xhttp.onreadystatechange = function() {
					if ((this.readyState == 4) && (this.status == 200)) {
						alert(this.responseText);
						window.location.reload();
					}
				};
				xhttp.open("POST", "/Tasker/Update/", true);
				xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
				xhttp.send("task-id=" + taskId + "&subtask-id=" + subtaskId + "&current-progress=" + progress + "&last-remarks=" + encodeURIComponent(lastRemarks));
			}
			/*
			function parseAllDate(text) {
				window.document.querySelectorAll("td").forEach(function(td) {
					try {
						var date = new Date(text.replace("IST", "+05:30"));
						console.log(date.toString());
						td.innerText = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"][date.getDay() - 1] + " " + [date.getDate(), ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"][date.getMonth() - 1], date.getFullYear()].join('-');
					} catch (error) {
						console.error(error);
					}
				});
				event.currentTarget.removeEventListener("DOMContentLoaded", parseAllDate);
			}
			window.addEventListener("DOMContentLoaded", parseAllDate);
			*/
		</script>
	</head>
	<body>
		<%@ include file="navigation-bar.jsp" %>
		<div class="container-fluid">
			<div class="row">
				<table class="table table-bordered table-hover w-100">
					<thead class="bg-secondary text-light">
						<tr>
							<th></th>
							<th>Title</th>
							<th>Description</th>
							<th>Status</th>
							<th>Priority</th>
							<th>Assignee</th>
							<th>Start</th>
							<th>Due</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${taskAssignmentDTOs}" var="taskAssignmentDTO">
							<tr data-bs-toggle="collapse" data-bs-target="#details${taskAssignmentDTO.taskDTO.id}" aria-expanded="false" aria-controls="details1" style="cursor: pointer;" onclick="subtableDisplay(this);">
								<td class="text-light icon">+</td>
								<td class="text-light"><c:out value="${taskAssignmentDTO.taskDTO.title}" /></td>
								<td class="text-light"><c:out value="${taskAssignmentDTO.taskDTO.description}" /></td>
								<td class="text-light"><span class="badge bg-secondary"><c:out value="${taskAssignmentDTO.taskDTO.statusDTO.statusEnum}" /></span></td>
								<td class="text-light"><span class="badge bg-warning text-dark"><c:out value="${taskAssignmentDTO.taskDTO.priorityDTO.priorityEnum}" /></span></td>
								<td class="text-light"><c:out value="${taskAssignmentDTO.assigneeUserDTO.name}" /></td>
								<td class="text-light">
									<fmt:parseDate value="${taskAssignmentDTO.taskDTO.startDate}" var="parsedDate" pattern="yyyy-MM-dd" />
									<fmt:formatDate value="${parsedDate}" pattern="E dd-MMM-yyyy" />
								</td>
								<td class="text-light">
									<fmt:parseDate value="${taskAssignmentDTO.taskDTO.dueDate}" var="parsedDate" pattern="yyyy-MM-dd" />
									<fmt:formatDate value="${parsedDate}" pattern="E dd-MMM-yyyy" />
								</td>
							</tr>
							<tr class="collapse" id="details${taskAssignmentDTO.taskDTO.id}">
								<td></td>
								<td colspan="7">
									<table class="table mb-0">
										<thead>
											<tr>
												<th class="text-light">Title</th>
												<th class="text-light">Description</th>
												<th class="text-light">Status</th>
												<th class="text-light">Priority</th>
												<th class="text-light">Assigned to</th>
												<th class="text-light">Assgin on</th>
												<th class="text-light">Due</th>
												<th class="text-light">Progress</th>
												<th class="text-light">Remarks</th>
												<th class="text-light">Action</th>
												<th class="text-light">Raise issue</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${taskAssignmentDTO.subtaskAssignmentDTOs}" var="subtaskAssignmentDTO">
												<tr>
													<td class="text-light"><c:out value="${subtaskAssignmentDTO.subtaskDTO.title}" /></td>
													<td class="text-light"><c:out value="${subtaskAssignmentDTO.subtaskDTO.description}" /></td>
													<td class="text-light"><span class="badge bg-secondary"><c:out value="${subtaskAssignmentDTO.subtaskDTO.statusDTO.statusEnum}" /></span></td>
													<td class="text-light"><span class="badge bg-warning text-dark"><c:out value="${subtaskAssignmentDTO.subtaskDTO.priorityDTO.priorityEnum}" /></span></td>
													<td class="text-light"><c:out value="${subtaskAssignmentDTO.assignedToDTO.name}" /></td>
													<td class="text-light">
														<fmt:parseDate value="${subtaskAssignmentDTO.subtaskDTO.assignedDate}" var="parsedDate" pattern="yyyy-MM-dd" />
														<fmt:formatDate value="${parsedDate}" pattern="E dd-MMM-yyyy" />
													</td>
													<td class="text-light">
														<fmt:parseDate value="${subtaskAssignmentDTO.subtaskDTO.dueDate}" var="parsedDate" pattern="yyyy-MM-dd" />
														<fmt:formatDate value="${parsedDate}" pattern="E dd-MMM-yyyy" />
													</td>
													<td class="text-light">
														<div class="input-group">
															<label for="user-progress${taskAssignmentDTO.taskDTO.id}${subtaskAssignmentDTO.subtaskDTO.id}" class="form-label form-label-sm">Progress made</label>
															<input type="range" class="form-range w-25 mx-2" name="user-progress" id="user-progress${taskAssignmentDTO.taskDTO.id}${subtaskAssignmentDTO.subtaskDTO.id}" min="0" max="100" step="1" oninput="outputChange(this);" value="${subtaskAssignmentDTO.subtaskDTO.currentProgress}" />
															<output for="user-progress${taskAssignmentDTO.taskDTO.id}${subtaskAssignmentDTO.subtaskDTO.id}" aria-hidden="true"></output>
														</div>
													</td>
													<td>
														<div class="form-floating">
															<input type="text" class="form-control form-control-sm" name="user-remarks" id="user-remarks${taskAssignmentDTO.taskDTO.id}${subtaskAssignmentDTO.subtaskDTO.id}" placeholder="Remarks" maxlength="32" aria-level="Remarks" value="${subtaskAssignmentDTO.subtaskDTO.lastRemarks}" />
															<label for="user-remarks${taskAssignmentDTO.taskDTO.id}${subtaskAssignmentDTO.subtaskDTO.id}" class="form-label form-label-sm">Remarks</label>
														</div>
													</td>
													<td>
														<button type="button" class="btn btn-success" onclick="sendProgress(this, ${taskAssignmentDTO.taskDTO.id}, ${subtaskAssignmentDTO.subtaskDTO.id});">Save</button>
													</td>
													<td>
														<button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#subtaskModal">Report</button>
													</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="row">
				<div class="modal fade" id="subtaskModal" tabindex="-1" aria-hidden="true">
					<div class="modal-dialog modal-lg">
						<div style="background-color: rgba(0, 0, 0, 0.5);" class="modal-content">
							<div class="modal-header">
								<h5 class="modal-title">Subtasks List</h5>
								<button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
							</div>
							<div class="modal-body">
								<form action="/Tasker/Issue/Raise/" method="post" enctype="application/x-www-form-urlencoded">
									<div class="form-floating mx-auto my-4 w-50">
										<input class="form-control" type="text" name="subject" placeholder="Subject" id="subject-input" value="" aria-label="Subject" aria-required="true" required autofocus />
										<label for="subject-input">Subject</label>
									</div>
									<div class="form-floating mx-auto my-4">
										<input class="form-control" type="text" name="description" placeholder="Description" id="description-input" value="" aria-label="Description" aria-required="true" required />
										<label for="description-input">Description</label>
									</div>
									<div class="input-group d-flex justify-content-center">
										<button type="submit" class="btn btn-primary">Submit</button>
									</div>
								</form>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="toast-container position-fixed bottom-0 end-0 p-3"></div>
		<script type="text/javascript">
			[].forEach.call(window.document.getElementsByTagName("output"), function(output) {
				output.textContent = output.parentElement.getElementsByTagName("input")[0].value + "%";
			});
		</script>
		<%@ include file="toasts-notification.jsp" %>
	</body>
</html>
