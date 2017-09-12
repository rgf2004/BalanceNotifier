package com.balancenotifier.telegram.bot;

public enum UserCommand {
	ADD_NEW_ADDRESS("/add_address"),	
	VERSION("/version"),
	CONTACTUS("/contact");

	private final String value;

	private UserCommand(String t) {
		value = t;
	}

	public String getValue() {
		return value;
	}

	public static UserCommand fromString(String value) throws IllegalArgumentException {
		if (value != null) {
			for (UserCommand b : values()) {
				if (value.equalsIgnoreCase(b.value)) {
					return b;
				}
			}
		}
		throw new IllegalArgumentException("Invalid Command");
	}
}
