package com.balancenotifier.engine.manager;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import com.balancenotifier.core.config.ConfigUtil;
import com.balancenotifier.engine.threads.BalanceRefresh;
import com.balancenotifier.telegram.bot.TelegramBot;
import com.balancenotifier.telegram.handler.TelegramUserHandler;

@Service
public class BalanceNotifierEngine {

	@Autowired
	private ConfigUtil configUtil;

	@Autowired
	private TelegramUserHandler telegramUserHandler;
	
	@Autowired
	private BalanceRefresh balanceRefresh;

	final static Logger logger = LoggerFactory.getLogger(BalanceNotifierEngine.class);

	private TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
	private TelegramBot telegramBot;

	private static final String TOKEN_PARAM_NAME = "telegram.token";
	private static final String BOT_NAME_PARAM = "telegram.botname";

	@PostConstruct
	public void init() {

		try {

			telegramBot = new TelegramBot(telegramUserHandler,
					configUtil.getEnvironment().getRequiredProperty(TOKEN_PARAM_NAME),
					configUtil.getEnvironment().getRequiredProperty(BOT_NAME_PARAM));

			telegramUserHandler.initAndLoadUsers(telegramBot);
			telegramBotsApi.registerBot(telegramBot);

			balanceRefresh.init(telegramUserHandler, telegramBot);
			Thread balanceRefreshThread = new Thread(balanceRefresh);
			balanceRefreshThread.start();
			

		} catch (TelegramApiRequestException e) {
			logger.error("Couldn't start Telegram Bot", e);
		} catch (Exception e) {
			logger.error("General Error", e);
		}
	}

}
