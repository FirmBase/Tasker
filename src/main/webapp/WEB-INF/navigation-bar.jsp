<%@ page language="java" contentType="text/html; charset=UTF-8" %>
		<nav class="navbar navbar-expand-lg navbar-light bg-light">
			<div class="container-fluid">
				<a class="navbar-brand" href="#">Tasker</a>
				<button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
					<span class="navbar-toggler-icon"></span>
				</button>
				<div class="collapse navbar-collapse" id="navbarSupportedContent">
					<ul class="navbar-nav me-auto mb-2 mb-lg-0">
						<li class="nav-item">
							<a class="nav-link" aria-current="page" href="/Tasker/Home/">Home</a>
						</li>
						<li class="nav-item">
							<a class="nav-link" href="/Tasker/New/Task/">New Task</a>
						</li>
						<li class="nav-item">
							<a class="nav-link" href="/Tasker/Task/Received/">Assigned Task</a>
						</li>
						<li class="nav-item">
							<a class="nav-link" href="/Tasker/Feedbacks/">Feedbacks</a>
						</li>
						<!--
						<li class="nav-item">
							<a class="nav-link disabled" href="#" tabindex="-1" aria-disabled="true">Disabled</a>
						</li>
						-->
					</ul>
					<ul class="navbar-nav ms-auto mb-2 mb-lg-0">
						<li class="nav-item dropdown">
							<a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
								More
							</a>
							<ul class="dropdown-menu" aria-labelledby="navbarDropdown">
								<li><a class="dropdown-item" href="/Tasker/Home/Profile/">Profile</a></li>
								<li><hr class="dropdown-divider" /></li>
								<li><a class="dropdown-item" href="/Tasker/Logout/">Logout</a></li>
							</ul>
						</li>
					</ul>
					<!--
					<form class="d-flex">
						<input class="form-control me-2" type="search" placeholder="Search" aria-label="Search">
						<button class="btn btn-outline-success" type="submit">Search</button>
					</form>
					-->
				</div>
			</div>
		</nav>
		<script type="text/javascript">
			[].forEach.call(window.document.getElementById("navbarSupportedContent").getElementsByTagName("a"), function(a) {
				if (a.href == window.location.href)
					a.classList.add("active");
				else
					a.classList.remove("active");
			});
		</script>
