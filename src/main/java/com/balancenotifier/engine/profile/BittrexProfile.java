package com.balancenotifier.engine.profile;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import com.balancenotifier.engine.beans.BalanceDetails;
import com.balancenotifier.engine.beans.MarketDetails;
import com.balancenotifier.engine.beans.Order;
import com.balancenotifier.engine.beans.OrdersResponse;
import com.balancenotifier.telegram.bot.TelegramBot;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BittrexProfile extends Profile {

	private static final String PROFILE_NAME = "Bittrex";
	
	private static final String SELL_KEYWORD = "LIMIT_SELL";
	
	private static final String BUY_KEYWORD = "LIMIT_BUY";
	
	public BittrexProfile(long chatId, String apiKey, String secret, TelegramBot telegramBot) throws Exception {
		super(chatId, apiKey, secret, telegramBot);
	}
		
	@Override
	public String getPrfoileName() {	
		return PROFILE_NAME;
	}
	
	@Override
	public String getSellKeyword() {
		return SELL_KEYWORD;
	}

	@Override
	public String getBuyKeyword() {
		return BUY_KEYWORD;
	}
	
	@Override
	public List<Order> getOpenOrders() throws Exception {
		
		long nonce = new Date().getTime();

		StringBuilder url = new StringBuilder("https://bittrex.com/api/v1.1/");
		url.append("market/getopenorders").append("?apikey=").append(this.apiKey).append("&nonce=")
				.append(nonce);

		HttpHeaders headers = new HttpHeaders();

		try {
			headers.add("apisign", encode(this.secret, url.toString()));
		} catch (Exception e) {
			logger.error("Error while setting header ", e);
		}

		HttpEntity<String> request = new HttpEntity<String>(headers);

		RestTemplate restTemplate = new RestTemplate();
		String response = restTemplate.postForObject(url.toString(), request, String.class);

		Gson gson = new GsonBuilder().create();

		OrdersResponse orderResponse = gson.fromJson(response, OrdersResponse.class);

		if (!orderResponse.isSuccess())
			throw new Exception("Error While Getting Orders - " + orderResponse.getMessage());
		
		return orderResponse.getResult();
				
	}
	
	@Override
	public List<BalanceDetails> getBalance() throws Exception {
		
		long nonce = new Date().getTime();

		StringBuilder url = new StringBuilder("https://bittrex.com/api/v1.1/");
		url.append("account/getbalances").append("?apikey=").append(this.apiKey).append("&nonce=")
				.append(nonce);

		HttpHeaders headers = new HttpHeaders();

		try {
			headers.add("apisign", encode(this.secret, url.toString()));
		} catch (Exception e) {
			logger.error("Error while setting header ", e);
		}

		HttpEntity<String> request = new HttpEntity<String>(headers);

		RestTemplate restTemplate = new RestTemplate();
		String response = restTemplate.postForObject(url.toString(), request, String.class);

		Gson gson = new GsonBuilder().create();
		
		BalanceResponse balanceResponse = gson.fromJson(response, BalanceResponse.class);

		if (!balanceResponse.isSuccess())
			throw new Exception("Error While Getting Balances - " + balanceResponse.getMessage());
		
		return balanceResponse.getResult();
	}
	
	public static void refreshCurrencyPrices() throws Exception {

		
		StringBuilder url = new StringBuilder("https://bittrex.com/api/v1.1/public/getmarketsummaries");


		RestTemplate restTemplate = new RestTemplate();
		String response = restTemplate.postForObject(url.toString(), null, String.class);

		Gson gson = new GsonBuilder().create();

		MarketSummaryResponse marketResponse = gson.fromJson(response, MarketSummaryResponse.class);

		if (!marketResponse.isSuccess())
			throw new Exception("Error While Getting Orders - " + marketResponse.getMessage());

		
		Map<String, MarketDetails> currencyDetails = new HashMap<>();
		
		marketResponse.getResult().forEach((marketDetails) -> {
			currencyDetails.put(marketDetails.getMarketName(), marketDetails);
		});
		
		currrencyPrice.put(PROFILE_NAME, currencyDetails);
	}	
}

@JsonIgnoreProperties(ignoreUnknown = true)
class MarketSummaryResponse {
	
	boolean success;
	String message;
	List<MarketDetails> result = new ArrayList<>();
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<MarketDetails> getResult() {
		return result;
	}
	public void setResult(List<MarketDetails> result) {
		this.result = result;
	}
	
}

@JsonIgnoreProperties(ignoreUnknown = true)
class BalanceResponse {
	
	boolean success;
	String message;
	List<BalanceDetails> result = new ArrayList<>();
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<BalanceDetails> getResult() {
		return result;
	}
	public void setResult(List<BalanceDetails> result) {
		this.result = result;
	}
	
}

