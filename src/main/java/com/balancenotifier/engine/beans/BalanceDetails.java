package com.balancenotifier.engine.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BalanceDetails {

	private String Currency;
	private double Balance;
	private double Available;
	private double Pending;
	
	public String getCurrency() {
		return Currency;
	}
	public void setCurrency(String currency) {
		Currency = currency;
	}
	public double getBalance() {
		return Balance;
	}
	public void setBalance(double balance) {
		Balance = balance;
	}
	public double getAvailable() {
		return Available;
	}
	public void setAvailable(double available) {
		Available = available;
	}
	public double getPending() {
		return Pending;
	}
	public void setPending(double pending) {
		Pending = pending;
	}
}
