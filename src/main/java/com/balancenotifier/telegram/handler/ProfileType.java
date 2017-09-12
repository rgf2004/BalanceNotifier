package com.balancenotifier.telegram.handler;

public enum ProfileType {

	BITTREX("Bittrex");
	//POLONIX("Polonix");
	
	private final String value;

	private ProfileType(String t) {
		value = t;
	}

	public String getValue() {
		return value;
	}

	public static ProfileType fromString(String value) throws IllegalArgumentException {
		if (value != null) {
			for (ProfileType b : values()) {
				if (value.equalsIgnoreCase(b.value)) {
					return b;
				}
			}
		}
		throw new IllegalArgumentException("Invalid Profile Type");
	}
	
	
}
