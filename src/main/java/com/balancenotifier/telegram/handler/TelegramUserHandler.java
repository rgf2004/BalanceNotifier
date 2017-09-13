package com.balancenotifier.telegram.handler;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.objects.Update;

import com.balancenotifier.core.config.ConfigUtil;
import com.balancenotifier.data.handler.UserHandler;
import com.balancenotifier.data.handler.WalletAddressHandler;
import com.balancenotifier.data.model.UserModel;
import com.balancenotifier.data.model.WalletAddressModel;
import com.balancenotifier.engine.beans.User;
import com.balancenotifier.engine.beans.Wallet;
import com.balancenotifier.engine.blockchain.BlockChainExplorerFactory;
import com.balancenotifier.telegram.bot.TelegramBot;
import com.balancenotifier.telegram.bot.UserCommand;

@Service
public class TelegramUserHandler {

	private static final Logger logger = LoggerFactory.getLogger(TelegramUserHandler.class);

	private TelegramBot telegramBot;

	@Autowired
	private ConfigUtil configUtil;

	@Autowired
	private UserHandler userHandler;

	@Autowired
	private WalletAddressHandler walletHandler;

	@Autowired
	private BlockChainExplorerFactory blockChainExplorerFactory;

	private Map<Long, User> users = new HashMap<>();

	public void initAndLoadUsers(TelegramBot bot) {

		this.telegramBot = bot;

		logger.info("Start Loading Telegram Users ...");
		List<UserModel> usersList = userHandler.findAll();
		for (UserModel userModel : usersList) {

			User user = new User(userModel);

			List<WalletAddressModel> wallets = walletHandler.getWalletAddressesByUserID(userModel.getUserId());

			wallets.forEach(wallet -> {

				double balance = blockChainExplorerFactory.getExplorer(WalletType.fromString(wallet.getAddressType()))
						.getBalance(wallet.getAddressString());

				Wallet walletBean = new Wallet(wallet, balance);

				user.getWallets().put(wallet.getAddressString(), walletBean);
			});

			if (user.getWallets().size() > 0)
				user.setStatus(UserStatus.ACTIVE);

			users.put(user.getUserChatID(), user);
			logger.info("Existing user has been loaded [{}]", user);
		}
		logger.info("Ending Loading Telegram Users ...");
	}

	public void handleIncomingMessage(Update update) {
		// We check if the update has a message and the message has text
		logger.info("Incoming Update [{}]", update);
		if (update.hasMessage() && update.getMessage().hasText()) {
			try {
				User user = users.get(update.getMessage().getChatId());
				if (user == null) {
					// first time case
					HandleNewUser(update);
				} else {
					HandleExistingUser(user, update);
				}
			} catch (Exception e) {
				logger.error("Error Occured", e);
			}
		}
	}

	private void HandleExistingUser(User user, Update update) {

		String message = "";

		try {

			if (update.getMessage().getText().startsWith("/")) {

				UserCommand command = UserCommand.fromString(update.getMessage().getText());

				switch (command) {

				case START:
					handleHelpCommand(user);
					break;

				case HELP:
					handleHelpCommand(user);
					break;

				case ADD_NEW_ADDRESS:
					user.backupUserStatus();
					user.setStatus(UserStatus.ADD_NEW_ADDRESS);
					handleAddNewAddress(user, update.getMessage().getText());
					break;

				case CONTACTUS:
					message = String.format(
							"If you want to contact the developer you can do so at @RamyFeteha or ramyfeteha@gmail.com");
					telegramBot.sendMessage(user.getUserChatID(), message, null);
					break;

				case VERSION:
					String version = configUtil.getAppVersion();
					message = String.format(version);
					telegramBot.sendMessage(user.getUserChatID(), message, null);
				}
				;
			} else {
				if (user.getStatus() == UserStatus.WAITING_FOR_WALLET_NAME
						|| user.getStatus() == UserStatus.WAITING_FOR_ADDRESS_ALIAS
						|| user.getStatus() == UserStatus.WAITING_FOR_WALLET_ADDRESS) {
					handleAddNewAddress(user, update.getMessage().getText());
					return;
				} else {
					message = String.format("Invalid Command, Please use a valid command");

					telegramBot.sendMessage(user.getUserChatID(), message, null);
				}

			}

		} catch (Exception e) {

			logger.error("Error Occured", e);

			message = String.format("Invalid Command, Please use a valid command");

			telegramBot.sendMessage(user.getUserChatID(), message, null);
		}

	}

	private void handleAddNewAddress(User user, String text) {
		String message = "";

		if (user.getStatus() == UserStatus.HAS_NO_ADDRESS || user.getStatus() == UserStatus.ADD_NEW_ADDRESS) {
			message = String.format("Please select the WALLET Type");
			List<String> wallets = new ArrayList<>();

			for (WalletType wallet : WalletType.values()) {
				wallets.add(wallet.getValue());
			}

			telegramBot.sendMessage(user.getUserChatID(), message, wallets);
			user.setStatus(UserStatus.WAITING_FOR_WALLET_NAME);
			return;
		} else if (user.getStatus() == UserStatus.WAITING_FOR_WALLET_NAME) {
			try {
				WalletType walletType = WalletType.fromString(text);

				user.setCachedWalletType(walletType);

				message = String.format("Please Enter Address Label");
				telegramBot.sendMessage(user.getUserChatID(), message, null);

				user.setStatus(UserStatus.WAITING_FOR_ADDRESS_ALIAS);

			} catch (IllegalArgumentException e) {

				message = String.format("Invalid Wallet Type, Please enter the command again");
				telegramBot.sendMessage(user.getUserChatID(), message, null);

				user.setStatus(UserStatus.HAS_NO_ADDRESS);
			}
		} else if (user.getStatus() == UserStatus.WAITING_FOR_ADDRESS_ALIAS) {

			user.setCachedAddressAlias(text);

			message = String.format("Please Enter Wallet Address");
			telegramBot.sendMessage(user.getUserChatID(), message, null);

			user.setStatus(UserStatus.WAITING_FOR_WALLET_ADDRESS);

		} else if (user.getStatus() == UserStatus.WAITING_FOR_WALLET_ADDRESS) {
			// check address then save them and update user status to active
			// and tell him that everthing is ok
			user.setCachedWalletAddress(text);

			saveNewAddress(user);

		}

	}

	private void saveNewAddress(User user) {

		String message = "";

		WalletAddressModel wallet = new WalletAddressModel();
		wallet.setAddressType(user.getCachedWalletType().getValue());
		wallet.setAddressAlias(user.getCachedAddressAlias());
		wallet.setAddressString(user.getCachedWalletAddress());
		wallet.setUser(user.getUserModel());

		try {

			Wallet walletBean = new Wallet(wallet, 0f);
			user.getWallets().put(wallet.getAddressString(), walletBean);
			user.setStatus(UserStatus.ACTIVE);

			walletHandler.saveWalletAddress(wallet);

			message = String.format(
					"Address has been added to your account, please note that in case address is not correct balance will be Zero");
			telegramBot.sendMessage(user.getUserChatID(), message, null);

		} catch (Exception e) {

			user.revertUserStatus();
			logger.error("Error Occured", e);
			message = String.format(
					"Invalid Address, please try adding it again using command /add_address and follow the steps");
			telegramBot.sendMessage(user.getUserChatID(), message, null);
		}

	}

	private void HandleNewUser(Update update) {
		User user = SaveNewUser(update);

		if (user != null) // start adding profile process
		{
			handleHelpCommand(user);
		}
	}

	private User SaveNewUser(Update update) {
		UserModel userModel = new UserModel();
		userModel.setChatID(update.getMessage().getFrom().getId());
		userModel.setFirstName(update.getMessage().getFrom().getFirstName());
		userModel.setLastName(update.getMessage().getFrom().getLastName());
		userModel.setUserName(update.getMessage().getFrom().getUserName());

		String message = "";

		try {
			User user = saveUser(userModel);
			if (user != null) {

				message = String.format(
						"Hello %s, Welcome to BalanceNotify Bot which will help you to deal with your balance deposit/withdraws",
						userModel.getFirstName() != null ? userModel.getFirstName() : "My Friend");

				return user;
			}

		} catch (Exception e) {
			logger.error("Error Occured", e);

			message = "Sorry BalanceNotify Bot is under maintenance now please visit it later ";

			return null;

		} finally {

			telegramBot.sendMessage(userModel.getChatID(), message, null);

		}

		return null;
	}

	private User saveUser(UserModel userModel) throws Exception {

		boolean isInserted = userHandler.saveUser(userModel);
		if (isInserted) {
			User user = new User(userModel);
			user.setStatus(UserStatus.HAS_NO_ADDRESS);
			users.put(user.getUserChatID(), user);
			return user;
		} else {
			return null;
		}
	}

	public void handleHelpCommand(User user) {
		try {

			Path path = Paths.get(getClass().getClassLoader().getResource("help.txt").toURI());
			List<String> lines = Files.readAllLines(path,StandardCharsets.UTF_8);
			
			StringBuffer strBuffer = new StringBuffer();
			for (String line : lines)
				strBuffer.append(line).append("\n");
			
			telegramBot.sendMessage(user.getUserChatID(), strBuffer.toString(), null);
			
		} catch (IOException | URISyntaxException e) {
			logger.error("Error Occured", e);
		}
	}

	public Map<Long, User> getUsers() {
		return new HashMap<>(users);
	}

	public void updateUserBalance(long chatID, Wallet wallet, double balance) {
		users.get(chatID).getWallets().get(wallet.getWalletModel().getAddressString()).setBalance(balance);
	}

	public void updateUserZeroBalanceRetry(long chatID, Wallet wallet) {
		int retries = users.get(chatID).getWallets().get(wallet.getWalletModel().getAddressString())
				.getZeroBalanceRetries();
		users.get(chatID).getWallets().get(wallet.getWalletModel().getAddressString()).setZeroBalanceRetries(++retries);
	}

	public void resetUserZeroBalanceRetry(long chatID, Wallet wallet) {
		users.get(chatID).getWallets().get(wallet.getWalletModel().getAddressString()).setZeroBalanceRetries(0);
	}

}
