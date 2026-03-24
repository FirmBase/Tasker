function updateTasksDataTable(dataTable, url) {
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if ((this.readyState == 4) && (this.status == 200)) {
			// console.log(this.responseText);
			dataTable.clear();
			dataTable.rows.add(JSON.parse(this.responseText));
			dataTable.draw();
		}
	}
	xhttp.open("GET", url, true);
	xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	xhttp.send();
}

function initializeTasksDataTable() {
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if ((this.readyState == 4) && (this.status == 200)) {
			// console.log(this.responseText);
			var tasksDataTable = $("#tasks_table").DataTable({
				data: JSON.parse(this.responseText),
				columns: [
					{ data: "serial_number" },
					{ data: "task.title" },
					{ data: "task.description" },
					{
						data: "task.status.label",
						render: function(data, type, row) {
							return "<span class=\"badge bg-secondary\">" + data + "</span>";
						}
					},
					{
						data: "task.priority.label",
						render: function(data, type, row) {
							return "<span class=\"badge bg-warning text-dark\">" + data + "</span>";
						}
					},
					{
						data: "task.startDate",
						render: function(data, type, row) {
							return parseDate(data);
						}
					},
					{
						data: "task.dueDate",
						render: function(data, type, row) {
							return parseDate(data);
						}
					},
					{ data: "assigneeUser.name"},
					{
						data: null,
						render: function(data, type, row) {
							return "<button type=\"button\" class=\"btn btn-primary\" data-bs-toggle=\"modal\" data-bs-target=\"#subtaskModal\" onclick=\"initializeSubtasksDataTable(" + row.task.id + ");\">Subtasks</button>";
						}
					}
				],
				paging: false,
				responsive: true,
				footer: true
				/*,
				fnRowCallback: function(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
					var index = iDisplayIndexFull + 1;
					$("td:first", nRow).html(index);
					return nRow;
				}*/
			});
			var tasksDataTableUpdateInterval = setInterval(function() {
				// updateTasksDataTable(tasksDataTable, "/Tasker/API/Tasks/");
			}, 5000);
		}
	}
	xhttp.open("GET", "/Tasker/API/Tasks/", true);
	xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	xhttp.send();
}

function parseDate(text) {
	var date = new Date(text.replace("IST", "+05:30"));
	console.log(date.toString());
	return ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"][date.getDay() - 1] + " " + [date.getDate(), ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"][date.getMonth() - 1], date.getFullYear()].join('-');
}

function loadSubtasks(taskId) {
	const tableBody = document.getElementById('modalTableBody');

	// Clear old data
	tableBody.innerHTML = '<tr><td colspan="3">Loading...</td></tr>';

	// Simulate fetching data (Replace this with your actual data logic)
	setTimeout(() => {
		tableBody.innerHTML = `
			<tr>
				<td>1</td>
				<td>Refining logic for Task ${taskId}</td>
				<td><span class="badge bg-warning text-dark">In Progress</span></td>
			</tr>
			<tr>
				<td>2</td>
				<td>Finalizing UI for Task ${taskId}</td>
				<td><span class="badge bg-secondary">Pending</span></td>
			</tr>
		`;
	}, 300);
}

function initializeSubtasksDataTable(taskId) {
	[].forEach.call(window.document.getElementById("subtaskModal").getElementsByTagName("table"), function(table) {
		if (table.getElementsByTagName("tbody").length > 0) {
			[].forEach.call(table.getElementsByTagName("tbody"), function(tbody) {
				tbody.remove();
			});
		}
		var tbody = window.document.createElement("tbody");
		table.appendChild(tbody);
		var xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
			if ((this.readyState == 4) && (this.status == 200)) {
				if (JSON.parse(this.responseText).length < 1) {
					tbody.innerHTML = "<tr><td colspan=\"11\"><center>No subtasks</center></td></tr>";
				}
				else
					JSON.parse(this.responseText).forEach(function(element, index) {
						tbody.innerHTML +=
							"<tr>" +
								"<td>" + (index + 1) + "</td>" +
								"<td>" + element.subtask.title + "</td>" +
								"<td>" + element.subtask.description + "</td>" +
								"<td><span class=\"badge bg-secondary\">" + element.subtask.status.label + "</span></td>" +
								"<td><span class=\"badge bg-warning text-dark\">" + element.subtask.priority.label + "</span></td>" +
								"<td>" + parseDate(element.subtask.assignedDate) + "</td>" +
								"<td>" + parseDate(element.subtask.dueDate) + "</td>" +
								"<td>" + element.subtask.currentProgress + "%</td>" +
								"<td>" + element.assignedTo.name + "</td>" +
								"<td>" + element.subtask.lastRemarks + "</td>" +
								"<td>" +
									"<form action=\"/Tasker/Feedback/Subtask/\" method=\"get\" enctype=\"application/x-www-form-urlencoded\">" +
										"<input type=\"hidden\" class=\"form-control\" name=\"subtask-id\" value=\"" + element.subtask.id + "\" />" +
										"<button type=\"submit\" class=\"btn btn-outline-primary\">Feedback</button>" +
									"</form>" +
								"</td>" +
							"</tr>";
					});
			}
		};
		xhttp.open("GET", "/Tasker/API/Subtasks/" + taskId, true);
		xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		xhttp.send();
	});
}
