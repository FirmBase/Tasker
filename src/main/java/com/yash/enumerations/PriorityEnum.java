package com.yash.enumerations;

public enum PriorityEnum {
	URGENT(1),
	MAJOR(2),
	USUAL(3),
	MINOR(4),
	SLIGHT(5);

	private int priority;

	private PriorityEnum(int priority) {
		this.priority = priority;
	}
}
