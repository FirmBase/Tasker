package com.yash.dtos;

public class ToastDTO {
	private String caption;
	private String message;

	public ToastDTO() {
		super();
	}

	public ToastDTO(String caption, String message) {
		super();
		this.caption = caption;
		this.message = message;
	}

	@Override
	public String toString() {
		return String.format("{\"caption\": \"%s\", \"message\": \"%s\"}", caption, message);
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
