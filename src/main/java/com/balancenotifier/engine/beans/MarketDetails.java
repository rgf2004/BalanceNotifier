package com.balancenotifier.engine.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketDetails {
	private String MarketName;
	private double High;
	private double Low;
	private double Volume;
	private double Last;
	private double BaseVolume;
	private String TimeStamp;
	private double Bid;
	private double Ask;
	private long OpenBuyOrders;
	private long OpenSellOrders;
	private double PrevDay;
	private String Created;

	public String getMarketName() {
		return MarketName;
	}

	public void setMarketName(String marketName) {
		MarketName = marketName;
	}

	public double getHigh() {
		return High;
	}

	public void setHigh(double high) {
		High = high;
	}

	public double getLow() {
		return Low;
	}

	public void setLow(double low) {
		Low = low;
	}

	public double getVolume() {
		return Volume;
	}

	public void setVolume(double volume) {
		Volume = volume;
	}

	public double getLast() {
		return Last;
	}

	public void setLast(double last) {
		Last = last;
	}

	public double getBaseVolume() {
		return BaseVolume;
	}

	public void setBaseVolume(double baseVolume) {
		BaseVolume = baseVolume;
	}

	public String getTimeStamp() {
		return TimeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		TimeStamp = timeStamp;
	}

	public double getBid() {
		return Bid;
	}

	public void setBid(double bid) {
		Bid = bid;
	}

	public double getAsk() {
		return Ask;
	}

	public void setAsk(double ask) {
		Ask = ask;
	}

	public long getOpenBuyOrders() {
		return OpenBuyOrders;
	}

	public void setOpenBuyOrders(long openBuyOrders) {
		OpenBuyOrders = openBuyOrders;
	}

	public long getOpenSellOrders() {
		return OpenSellOrders;
	}

	public void setOpenSellOrders(long openSellOrders) {
		OpenSellOrders = openSellOrders;
	}

	public double getPrevDay() {
		return PrevDay;
	}

	public void setPrevDay(double prevDay) {
		PrevDay = prevDay;
	}

	public String getCreated() {
		return Created;
	}

	public void setCreated(String created) {
		Created = created;
	}

	@Override
	public String toString() {
		return "MarketDetails [MarketName=" + MarketName + ", High=" + High + ", Low=" + Low + ", Volume=" + Volume
				+ ", Last=" + Last + ", BaseVolume=" + BaseVolume + ", TimeStamp=" + TimeStamp + ", Bid=" + Bid
				+ ", Ask=" + Ask + ", OpenBuyOrders=" + OpenBuyOrders + ", OpenSellOrders=" + OpenSellOrders
				+ ", PrevDay=" + PrevDay + ", Created=" + Created + "]";
	}
}
