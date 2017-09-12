package com.balancenotifier.engine.threads;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.balancenotifier.core.config.ConfigUtil;
import com.balancenotifier.engine.beans.User;
import com.balancenotifier.telegram.handler.TelegramUserHandler;
import com.balancenotifier.telegram.handler.UserStatus;

@Service
public class OrdersRefresh implements Runnable { 

	final static Logger logger = LoggerFactory.getLogger(OrdersRefresh.class);

	@Autowired
	private ConfigUtil configUtil;

	private TelegramUserHandler telegramUserHandler;

	public void setTelegramUserHandler(TelegramUserHandler telegramUserHandler) {
		this.telegramUserHandler = telegramUserHandler;
	}

	@Override
	public void run() {

		logger.info("Start Orders Refresh ... ");
		long timeInterval = Long.parseLong(configUtil.getEnvironment().getRequiredProperty("profile.check.interval"));
		logger.info("Configured Orders Refresh interval is [{}] seconds", timeInterval);

		while (true) {
			try {
				getOpenOrders();
				Thread.sleep(timeInterval * 1000);
			} catch (Exception e) {
				logger.error("Error While refresh Orders prices", e);
			}
		}

	}

	private void getOpenOrders() throws Exception {

		Map<Long, User> users = telegramUserHandler.getUsers();

		users.forEach((chatID, user) -> {

			if (!user.getProfiles().isEmpty()) {
				user.getProfiles().forEach((profile) -> profile.refreshUserOrders());

			}
		});

	}
}
