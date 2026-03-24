<%@ page isErrorPage="true" language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
<html lang="en">
	<head>
		<title>All assgined task</title>
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
		<script type="text/javascript" src="/Tasker/tasks_table.js"></script>
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
	</head>
	<body>
		<%@ include file="navigation-bar.jsp" %>
		<div class="container-fluid">
			<div style="background-color: rgba(0, 0, 0, 0.2);" class="row p-3">
				<table id="tasks_table">
					<thead>
						<tr>
							<th>#</th>
							<th>Task</th>
							<th>Description</th>
							<th>Status</th>
							<th>Priority</th>
							<th>Started</th>
							<th>Due</th>
							<th>Assignee</th>
							<th>Subtasks</th>
						</tr>
					</thead>
					<tbody></tbody>
				</table>
			</div>
			<div class="row">
				<div class="modal fade" id="subtaskModal" tabindex="-1" aria-hidden="true">
					<div class="modal-dialog modal-xl">
						<div style="background-color: rgba(0, 0, 0, 0.5);" class="modal-content bg-dark text-white">
							<div class="modal-header">
								<h5 class="modal-title">Subtasks List</h5>
								<button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
							</div>
							<div class="modal-body">
								<table class="table table-dark table-striped">
								<thead>
									<tr>
										<!-- <th>#</th> -->
										<!-- <th>Subtask Name</th> -->
										<!-- <th>Status</th> -->
										<th>#</th>
										<th>Subtask</th>
										<th>Description</th>
										<th>Status</th>
										<th>Priority</th>
										<th>Assgined On</th>
										<th>Due</th>
										<th>Progress</th>
										<th>Assigned To</th>
										<th>Remarks</th>
										<th>Feedback</th>
									</tr>
								</thead>
								<tbody id="modalTableBody">
									</tbody>
								</table>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<script type="text/javascript">
			/*
			[].forEach.call(window.document.getElementsByTagName("table"), function(table) {
				var dataTable = new DataTable(table, {
					responsive: true
				});
				updateDataTable(dataTable);
			});
			*/
			initializeTasksDataTable();
		</script>
	</body>
</html>
