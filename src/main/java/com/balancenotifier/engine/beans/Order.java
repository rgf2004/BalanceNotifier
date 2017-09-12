package com.balancenotifier.engine.beans;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Order implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -661031273471167892L;
	
	String OrderUuid;
	String Exchange;
	String OrderType;
	double Quantity;
	double Limit;
	
	public String getOrderUuid() {
		return OrderUuid;
	}
	public void setOrderUuid(String orderUuid) {
		OrderUuid = orderUuid;
	}
	public String getExchange() {
		return Exchange;
	}
	public void setExchange(String exchange) {
		Exchange = exchange;
	}
	public String getOrderType() {
		return OrderType;
	}
	public void setOrderType(String orderType) {
		OrderType = orderType;
	}
	public double getQuantity() {
		return Quantity;
	}
	public void setQuantity(double quantity) {
		Quantity = quantity;
	}
	public double getLimit() {
		return Limit;
	}
	public void setLimit(double limit) {
		Limit = limit;
	}
	
	
	@Override
	public String toString() {
		return "Order [OrderUuid=" + OrderUuid + ", Exchange=" + Exchange + ", OrderType=" + OrderType + ", Quantity="
				+ Quantity + ", Limit=" + String.format("%.8f", Limit) + "]";
	}
	
	
}
