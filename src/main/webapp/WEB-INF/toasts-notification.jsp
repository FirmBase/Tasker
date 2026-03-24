<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

		<script type="text/javascript">
			function addToast(caption, message, delay) {
				var toastContainer = window.document.querySelector(".toast-container");
				if (toastContainer) {
					var toast = window.document.createElement("div");
					toast.classList.add("toast");
					toast.setAttribute("role", "alert");
					toast.setAttribute("aria-live", "assertive");
					toast.setAttribute("aria-atomic", "true");
					toastContainer.appendChild(toast);

					var toastHeader = window.document.createElement("div");
					toastHeader.classList.add("toast-header");
					toast.appendChild(toastHeader);

					var toastIcon = window.document.createElement("img");
					toastIcon.classList.add("rounded", "me-2");
					toastIcon.setAttribute("src", "");
					toastIcon.setAttribute("alt", "");
					toastHeader.appendChild(toastIcon);

					var toastCaption = window.document.createElement("strong");
					toastCaption.classList.add("me-auto");
					toastHeader.appendChild(toastCaption);
					toastCaption.innerHTML = caption;

					var toastTimeout = window.document.createElement("small");
					toastHeader.appendChild(toastTimeout);

					var toastCloseButton = window.document.createElement("button");
					toastCloseButton.setAttribute("type", "button");
					toastCloseButton.classList.add("btn-close");
					toastCloseButton.setAttribute("data-bs-dismiss", "toast");
					toastCloseButton.setAttribute("aria-label", "Close");
					toastHeader.appendChild(toastCloseButton);

					var toastBody = window.document.createElement("div");
					toastBody.classList.add("toast-body");
					toastBody.innerHTML = message;
					toast.appendChild(toastBody);
					window.document.body.appendChild(toastContainer);

					var toastInstance = new bootstrap.Toast(toast, {
						animation: true,
						autohide: true,
						delay: delay
					});
					toastInstance.show();

					toast.addEventListener("hidden.bs.toast", function () {
						toast.remove();
					});
				}
			}
		</script>
