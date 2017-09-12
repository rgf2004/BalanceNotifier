package com.balancenotifier.engine.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import com.balancenotifier.core.config.AppConfig;

public class Main {

	// should be the first thing done initialize telegram context
	static 
	{
		ApiContextInitializer.init();
	}

	final static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws TelegramApiRequestException {

		@SuppressWarnings({ "resource", "unused" })
		AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

	}

}
