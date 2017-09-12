package com.balancenotifier.telegram.bot;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.balancenotifier.telegram.handler.TelegramUserHandler;

public class TelegramBot extends TelegramLongPollingBot {

	private static final Logger log = LoggerFactory.getLogger(TelegramBot.class);

	private final String token;
	private final String botName;

	private TelegramUserHandler telegramUserHandler;

	public TelegramBot(TelegramUserHandler telegramHandler, String token, String botName) {

		super();

		this.token = token;
		this.botName = botName;

		this.telegramUserHandler = telegramHandler;
	}

	@Override
	public String getBotUsername() {
		return botName;
	}

	@Override
	public void onUpdateReceived(Update update) {
		telegramUserHandler.handleIncomingMessage(update);
	}

	@Override
	public String getBotToken() {
		return token;
	}

	public void sendMessage(long chatID, String text, List<String> keyboards) {
		
		if (chatID != 0) {
			log.info("This message will be sent to user [{}] : [{}]", chatID, text);
			SendMessage message = new SendMessage().setChatId(chatID).setText(text);
			
			
			if (keyboards != null) {
				List<KeyboardRow> keyboardRows = new ArrayList<>();
				keyboards.forEach((value) -> {
					KeyboardButton button = new KeyboardButton();
					button.setText(value);
					KeyboardRow keyboardRow = new KeyboardRow();
					keyboardRow.add(button);
					keyboardRows.add(keyboardRow);
				});

				ReplyKeyboardMarkup replyKeyBoard = new ReplyKeyboardMarkup();
				replyKeyBoard.setKeyboard(keyboardRows);
				replyKeyBoard.setOneTimeKeyboard(true);
				replyKeyBoard.setResizeKeyboard(true);
				
				message.setReplyMarkup(replyKeyBoard);
			}
			message.enableHtml(true);
			try {
				sendMessage(message); // Call method to send the message
			} catch (TelegramApiException e) {
				log.error("Error Occured", e);
			}
		}
	}
}
