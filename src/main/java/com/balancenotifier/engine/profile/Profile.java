package com.balancenotifier.engine.profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.balancenotifier.engine.beans.BalanceDetails;
import com.balancenotifier.engine.beans.MarketDetails;
import com.balancenotifier.engine.beans.Order;
import com.balancenotifier.telegram.bot.TelegramBot;

public abstract class Profile {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	protected long chatId;
	protected String apiKey;
	protected String secret;
	protected TelegramBot telegramBot;
	protected Map<String, Order> orders = new HashMap<>();
	protected static Map<String, Map<String, MarketDetails>> currrencyPrice = new HashMap<>();

	protected abstract List<Order> getOpenOrders() throws Exception;

	protected abstract List<BalanceDetails> getBalance() throws Exception;

	public abstract String getPrfoileName();

	public abstract String getSellKeyword();

	public abstract String getBuyKeyword();

	private MarketDetails getCurrencyPrice(String currency) {
		return currrencyPrice.get(getPrfoileName()).get(currency);
	}

	public Profile(long chatId, String apiKey, String secret, TelegramBot telegramBot) throws Exception {
		this.chatId = chatId;
		this.apiKey = apiKey;
		this.secret = secret;
		this.telegramBot = telegramBot;

		try {
			this.getOpenOrders().forEach((order) -> {
				this.addOrder(order);
			});
		} catch (Exception e) {
			logger.error("Error While Creating Profile", e);
			throw e;
		}
	}

	public void refreshUserOrders() {

		List<Order> orders = null;
		try {
			orders = this.getOpenOrders();
		} catch (Exception e) {
			logger.error("Error While refreshing User Orders", e);
			return;
		}

		// handle new orders
		for (Order order : orders) {

			boolean found = false;

			List<Order> userOrders = new ArrayList<>(this.orders.values());

			for (Order userOrder : userOrders) {
				if (order.getOrderUuid().equals(userOrder.getOrderUuid())) {
					found = true;
					break;
				}
			}
			if (!found) {
				this.addOrder(order);
				String msgBody = "";
				if (order.getOrderType().equals(getSellKeyword())) {
					msgBody = String.format(
							"Profile [<b>%s</b>] \n New Order has been placed \n Exchange [<b>%s</b>] \n Type [<b>%s</b>] \n Quantity [<b>%.8f</b>] \n Limit [<b>%.8f</b>] \n current ASK [<b>%.8f</b>]",
							getPrfoileName(), order.getExchange(), order.getOrderType(), order.getQuantity(),
							order.getLimit(), getCurrencyPrice(order.getExchange()).getAsk());
				} else {
					msgBody = String.format(
							"Profile [<b>%s</b>] \n New Order has been placed \n Exchange [<b>%s</b>] \n Type [<b>%s</b>] \n Quantity [<b>%.8f</b>] \n Limit [<b>%.8f</b>] \n current BID [<b>%.8f</b>]",
							getPrfoileName(), order.getExchange(), order.getOrderType(), order.getQuantity(),
							order.getLimit(), getCurrencyPrice(order.getExchange()).getBid());
				}
				telegramBot.sendMessage(chatId, msgBody, null);
			}

		}

		// handle cancelled/finished orders

		List<Order> userOrders = new ArrayList<>(this.orders.values());

		for (Order userOrder : userOrders) {

			boolean found = false;
			for (Order order : orders) {
				if (order.getOrderUuid().equals(userOrder.getOrderUuid())) {
					found = true;
					break;
				}
			}
			if (!found) {
				this.orders.remove(userOrder.getOrderUuid());
				String msgBody = "";
				if (userOrder.getOrderType().equals(getSellKeyword())) {
					msgBody = String.format(
							"Profile <b>%s</b> \n This Order has been cancelled/closed \n Exchange <b>%s</b> \n Type <b>%s</b> \n Quantity <b>%.8f</b> \n Limit <b>%.8f</b> \n current ASK <b>%.8f</b>",
							getPrfoileName(), userOrder.getExchange(), userOrder.getOrderType(),
							userOrder.getQuantity(), userOrder.getLimit(),
							getCurrencyPrice(userOrder.getExchange()).getAsk());
				} else {
					msgBody = String.format(
							"Profile <b>%s</b> \n This Order has been cancelled/closed \n Exchange <b>%s</b> \n Type <b>%s</b> \n Quantity <b>%.8f</b> \n Limit <b>%.8f</b> \n current BID <b>%.8f</b>",
							getPrfoileName(), userOrder.getExchange(), userOrder.getOrderType(),
							userOrder.getQuantity(), userOrder.getLimit(),
							getCurrencyPrice(userOrder.getExchange()).getBid());
				}

				telegramBot.sendMessage(chatId, msgBody, null);
			}

		}

	}

	private void addOrder(Order order) {
		this.orders.put(order.getOrderUuid(), order);
	}

	public boolean sendBalance() throws Exception {

		StringBuffer message = new StringBuffer();
		boolean firstTime = true;
		boolean balanceSent = false;
		List<BalanceDetails> balances = getBalance();
		if (!balances.isEmpty()) {

			for (BalanceDetails balance : balances) {

				String msgBody = "";
				if (balance.getBalance() > 0.0) {

					if (firstTime) {
						msgBody = String.format("Profile <b>%s</b>", getPrfoileName());
						firstTime = false;
					}

					if (getCurrencyPrice("BTC-" + balance.getCurrency()) != null) {
						msgBody = String.format(
								" Currency <b>%s</b> | balance <b>%.8f</b> | current BID <b>%.8f</b> | current ASK <b>%.8f</b>",
								balance.getCurrency(), balance.getBalance(),
								getCurrencyPrice("BTC-" + balance.getCurrency()).getBid(),
								getCurrencyPrice("BTC-" + balance.getCurrency()).getAsk());
					} else {
						msgBody = String.format(" Currency <b>%s</b> | balance <b>%.8f</b>", balance.getCurrency(),
								balance.getBalance());
					}
				}

				if (msgBody.length() > 2)
					message.append(msgBody).append("\n------------------------------------\n");
			}

			if (message.length() > 2) {
				telegramBot.sendMessage(chatId, message.toString(), null);
				balanceSent = true;
			}
		}
		return balanceSent;
	}

	private List<Order> getOrders() {
		return new ArrayList<Order>(this.orders.values());
	}

	public boolean sendOrders() {
		StringBuilder message = new StringBuilder();
		boolean ordersSent = false;
		List<Order> orders = getOrders();
		if (!orders.isEmpty()) {
			String msgBody = "";
			msgBody = String.format("Profile <b>%s</b>", getPrfoileName());

			for (Order order : orders) {
				if (order.getOrderType().equals(getSellKeyword())) {
					msgBody = String.format(
							" Exchange <b>%s</b> \n Type <b>%s</b> \n Quantity <b>%.8f</b> \n Limit <b>%.8f</b> \n current ASK <b>%.8f</b>",
							order.getExchange(), order.getOrderType(), order.getQuantity(), order.getLimit(),
							getCurrencyPrice(order.getExchange()).getAsk());
				} else {
					msgBody = String.format(
							" Exchange <b>%s</b> \n Type <b>%s</b> \n Quantity <b>%.8f</b> \n Limit <b>%.8f</b> \n current BID <b>%.8f</b>",
							order.getExchange(), order.getOrderType(), order.getQuantity(), order.getLimit(),
							getCurrencyPrice(order.getExchange()).getBid());
				}

				if (msgBody.length() > 2)
					message.append(msgBody).append("\n------------------------------------\n");
			}

			if (message.length() > 2) {
				telegramBot.sendMessage(chatId, message.toString(), null);
				ordersSent = true;
			}
		}
		return ordersSent;
	}

	protected String encode(String key, String data) throws Exception {

		byte[] byteKey = key.getBytes("UTF-8");
		final String HMAC_SHA512 = "HmacSHA512";
		Mac sha512_HMAC = Mac.getInstance(HMAC_SHA512);
		SecretKeySpec keySpec = new SecretKeySpec(byteKey, HMAC_SHA512);
		sha512_HMAC.init(keySpec);
		byte[] mac_data = sha512_HMAC.doFinal(data.getBytes("UTF-8"));
		String result = bytesToHex(mac_data);
		return result;

	}

	protected String bytesToHex(byte[] bytes) {
		final char[] hexArray = "0123456789ABCDEF".toCharArray();
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

}
