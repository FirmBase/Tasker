<%@ page isErrorPage="true" language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!doctype html>
<html lang="en">
	<head>
		<title>Add new task</title>
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

		<link type="text/css" href="/Tasker/data_table.css" rel="stylesheet" />
		<%@ include file="toasts-notification.jsp" %>
		<script type="text/javascript">
			function getCurrentDate() {
				var date = new Date();
				var year = date.getFullYear();
				var month = String(date.getMonth() + 1).padStart(2, '0');	// add 1 and pad
				var date_ = String(date.getDate()).padStart(2, '0');	// pad with leading zero
				return [year, month, date_].join('-');
			}
			function valideUserDatalist(el) {
				var options = Array.from(el.list.options).map(o => o.value);
				if (!options.includes(el.value))
					el.value = "";
			}
			var rowsId = 0;
			function insertEmptySubtasks(dataTable, removeButtonDisable) {
				// var count = dataTable.rows().count();
				var count = ++rowsId;
				var title = "<div class=\"form-floating\"><input type=\"text\" class=\"form-control form-control-sm\" name=\"subtask-title\" id=\"subtask_title" + count + "\" maxlength=\"16\" required aria-required /><label for=\"subtask_title" + count + "\">Title</label></div>";
				var description = "<div class=\"form-floating\"><input type=\"text\" class=\"form-control form-control-sm\" name=\"subtask-description\" id=\"subtask_description" + count + "\" maxlength=\"32\" required aria-required /><label for=\"subtask_description" + count + "\">Description</label></div>";
				var priority = "<div class=\"form-floating\"><select class=\"form-select form-select-sm\" name=\"subtask-priority\" id=\"subtask_priority" + count + "\" required aria-required><option value=\"\" disabled selected>Select priority</option><c:forEach var="priority_" items="${priority}"><option value=\"<c:out value="${priority_.id}" />\"><c:out value="${priority_.priorityEnum}" /></option></c:forEach></select><label for=\"subtask_priority" + count + "\">Priority</label></div>";
				var start = "<div class=\"form-floating\"><input type=\"date\" class=\"form-control form-control-sm\" name=\"subtask-start\" id=\"subtask_start" + count + "\" value=\"" + getCurrentDate() + "\" required aria-required /><label for=\"subtask_start" + count + "\">Start</label></div>";
				var due = "<div class=\"form-floating\"><input type=\"date\" class=\"form-control form-control-sm\" name=\"subtask-due\" id=\"subtask_due" + count + "\" required aria-required /><label for=\"subtask_due" + count + "\">Due</label></div>";
				var assignedTo = "<div class=\"form-floating\"><input type=\"text\" class=\"form-control form-control-sm\" name=\"assigned-to\" id=\"subtask_assigned_to" + count + "\" onchange=\"valideUserDatalist(this);\" list=\"workforce_list\" required aria-required /><label for=\"subtask_assigned_to" + count + "\">Assign to</label></div>";
				var remove = "<div class=\"input-group\"><input type=\"button\" class=\"btn btn-success p-1\" onclick=\"dataTable.row($(this).closest('tr')).remove().draw();\" value=\"Remove\"" + (removeButtonDisable ? " disabled" : "")  + " /></div>";
				dataTable.row.add(["<span class=\"text-white\">" + (dataTable.rows().count() + 1) + "</span>", title, description, priority, start, due, assignedTo, remove]).draw(false);
			}

			function requestAIGeneratedSubtasks() {
				var taskTitle = window.document.getElementById("task_title").value;
				var taskDescription = window.document.getElementById("task_description").value;
				if ((taskTitle !== "") && (taskDescription !== "")) {
					var xhttp = new XMLHttpRequest();
					xhttp.onreadystatechange = function() {
						if (this.readyState == 4 && this.status == 200) {
							console.log(this.responseText);
							addToast("AI Task", "Subtasks generated.", 2000);
							dataTable.clear().draw();
							insertEmptySubtasks(dataTable, true);
							JSON.parse(this.responseText).tasks.forEach(function(task, index) {
								if (index > 0)
									insertEmptySubtasks(dataTable, false);
								window.document.getElementsByName("subtask-title")[index].value = task.title;
								window.document.getElementsByName("subtask-description")[index].value = task.description;
								var priority = {};
								[].forEach.call(window.document.getElementsByName("subtask-priority")[index].getElementsByTagName("option"), function(option) {
									if (!option.disabled)
										priority[option.innerText] = option.value;
								});
								console.log(priority);
								window.document.getElementsByName("subtask-priority")[index].value = priority[task.priority];
								window.document.getElementsByName("subtask-due")[index].value = task.dueDate;
							});
						}
					};
					xhttp.open("POST", "/Tasker/API/AI/Generate/Subtasks/", true);
					xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
					xhttp.send("title=" + encodeURIComponent(taskTitle) + "&description=" + encodeURIComponent(taskDescription));
				}
				else
					addToast("Fulfill requirements", "Fill main task title and its description first.", 2000);
			}
			function checkTaskDateInputDiscrepancy() {
				var startDate = window.document.getElementById("start_date").value;
				var dueDate = window.document.getElementById("due_date").value;
				if (new Date(startDate).getTime() <= new Date(dueDate).getTime())
					return true;
				else {
					addToast("Invalid dates", "Task due date cannot be earlier than start.", 2000);
					return false;
				}
			}
			function checkSubtaskDateInputDiscrepancy() {
				var count = Math.min(window.document.getElementsByName("subtask-start").length, window.document.getElementsByName("subtask-due").length);
				var status = true;
				for (var index = 0; index < count; ++index) {
					var startDate = window.document.getElementsByName("subtask-start")[index].value;
					var dueDate = window.document.getElementsByName("subtask-due")[index].value;
					if (new Date(startDate).getTime() <= new Date(dueDate).getTime())
						status = status && true;
					else {
						addToast("Invalid dates", "Subtask due date cannot be earlier thant start.", 2000);
						return false;
					}
				}
				return status;
			}
		</script>
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
		<div class="container">
			<form action="/Tasker/New/Task/" method="post" enctype="application/x-www-form-urlencoded" onsubmit="return checkTaskDateInputDiscrepancy() && checkSubtaskDateInputDiscrepancy()">
				<div class="row">
					<div class="col-4 form-floating mx-auto my-4">
						<input class="form-control" type="text" name="task-title" placeholder="Task title" id="task_title" maxlength="16" aria-label="Task title" required aria-required autofocus />
						<label for="task_title">Task title</label>
					</div>
				</div>
				<div class="row">
					<div class="col-8 form-floating mx-auto my-4">
						<input class="form-control" type="text" name="task-description" placeholder="Task description" maxlength="64" id="task_description" aria-label="Task description" required aria-required />
						<label for="task_description">Task description</label>
					</div>
				</div>
				<div class="row">
					<!--
					<div class="col-4 form-floating mx-auto my-4">
						<select class="form-select" name="status" id="status" aria-label="Status" selected aria-selected required aria-required>
						<option disabled aria-disabled selected>Status</option>
						<c:forEach items="${status}" var="status_">
							<option value="${status_.id}">${status_.statusEnum}</option>
						</c:forEach>
						</select>
						<label for="status">Status</label>
					</div>
					-->
					<div class="col-offset-4 col-4 form-floating mx-auto my-4">
						<select class="form-select" name="priority" id="priority" aria-label="Priority" selected aria-selected required aria-required>
						<option disabled aria-disabled selected>Select priority</option>
						<c:forEach items="${priority}" var="priority_">
							<option value="${priority_.id}">${priority_.priorityEnum}</option>
						</c:forEach>
						</select>
						<label for="priority">Prioriy</label>
					</div>
				</div>
				<div class="row">
					<div class="col-4 form-floating mx-auto my-4">
						<input class="form-control" type="date" name="start-date" placeholder="Start" id="start_date" value="" aria-label="Start" required aria-required />
						<label for="start_date">Start</label>
					</div>
					<div class="col-offset-4 col-4 form-floating mx-auto my-4">
						<input class="form-control" type="date" name="due-date" placeholder="Due" id="due_date" aria-label="Due" required aria-required />
						<label for="due_date">Due</label>
					</div>
				</div>
				<div class="row">
					<fieldset>
						<legend><span class="text-white">Subtask</span></legend>
						<table id="subtasksTable">
							<thead>
								<tr>
									<th class="text-white">#</th>
									<th class="text-white">Title</th>
									<th class="text-white">Description</th>
									<th class="text-white">Priority</th>
									<th class="text-white">Start</th>
									<th class="text-white">Due</th>
									<th class="text-white">Assign to</th>
									<th class="text-white">Action</th>
								</tr>
							</thead>
						</table>
					</fieldset>
				</div>
				<div class="row">
					<div class="input-group d-flex justify-content-center">
						<button type="button" class="btn btn-success">Add new subtask</button>
					</div>
				</div>
				<div class="row">
					<div class="col-2 offset-4">
						<div class="input-group d-flex justify-content-center m-4">
							<button type="button" class="btn btn-primary px-4 py-2" onclick="requestAIGeneratedSubtasks();">AI Auto add</button>
						</div>
					</div>
					<div class="col-2">
						<div class="input-group d-flex justify-content-center m-4">
							<button type="submit" class="btn btn-primary px-4 py-2">Submit</button>
						</div>
					</div>
				</div>
				<div class="row">
					<datalist id="workforce_list">
						<c:forEach items="${workforce}" var="workforce_">
							<option value="${workforce_.name}@${workforce_.userCredentialDTO.username}">${workforce_.name}</option>
						</c:forEach>
					</datalist>
				</div>
			</form>
		</div>
		<div class="toast-container position-fixed bottom-0 end-0 p-3"></div>
		<script type="text/javascript">
			window.document.getElementById("start_date").value = getCurrentDate();
			var dataTable = new DataTable(window.document.getElementById("subtasksTable"), {
				info: false,
				ordering: false,
				paging: false,
				searching: false,
				responsive: true,
				language: {
					emptyTable: "No subtasks."
				},
				autoWidth: false,
				columnDefs: [
					{ width: "1%", target: 0 },
					{ width: "20%", target: 1 },
					{ width: "30%", target: 2 },
					{ width: "15%", target: 3 },
					{ width: "5%", target: 4 },
					{ width: "5%", target: 5 },
					{ width: "20%", target: 6 },
					{ width: "4%", target: 7 }
				]
			});
			[].forEach.call(window.document.getElementsByTagName("button"), function(button) {
				if (button.innerText == "Add new subtask") {
					button.addEventListener("click", function(e) {
						insertEmptySubtasks(dataTable, false);
					});
				}
			});
			insertEmptySubtasks(dataTable, true);
		</script>
	</body>
</html>
