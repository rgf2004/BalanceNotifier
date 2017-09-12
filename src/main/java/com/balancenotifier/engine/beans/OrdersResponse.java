package com.balancenotifier.engine.beans;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrdersResponse extends Response
{
	
	List <Order> result = new ArrayList<Order>();

	public List<Order> getResult() {
		return result;
	}

	public void setResult(List<Order> result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "OrdersResponse [result=" + result + ", success=" + success + ", message=" + message + "]";
	}
}