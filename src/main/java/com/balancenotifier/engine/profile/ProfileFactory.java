package com.balancenotifier.engine.profile;

import com.balancenotifier.telegram.bot.TelegramBot;
import com.balancenotifier.telegram.handler.ProfileType;

public final class ProfileFactory {

	public static Profile createProfile(ProfileType type, String apiKey, String secret, long chatId,
			TelegramBot telegramBot) throws Exception {
		switch (type) {
		case BITTREX:
			return new BittrexProfile(chatId, apiKey, secret, telegramBot);
		//case POLONIX:
			//return new PolonixProfile(chatId, apiKey, secret, telegramBot);
		}
		;

		return null;
	}

}
