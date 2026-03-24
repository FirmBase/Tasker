package com.yash.enumerations;

public enum StatusEnum {
	PENDING(1),
	WORKING(2),
	WAITING(3),
	COMPLETED(4);

	private int status;

	private StatusEnum(int status) {
		this.status = status;
	}
}
