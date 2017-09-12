package com.balancenotifier.telegram.handler;

public enum WalletType {

	ENT("Eternity"),
	ARC("Arctic");
//	ARC("Arctic");

	
	private final String value;

	private WalletType(String t) {
		value = t;
	}

	public String getValue() {
		return value;
	}

	public static WalletType fromString(String value) throws IllegalArgumentException {
		if (value != null) {
			for (WalletType b : values()) {
				if (value.equalsIgnoreCase(b.value)) {
					return b;
				}
			}
		}
		throw new IllegalArgumentException("Invalid Wallet Type");
	}
	
}
