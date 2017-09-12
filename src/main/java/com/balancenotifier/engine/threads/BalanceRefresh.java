package com.balancenotifier.engine.threads;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.balancenotifier.core.config.ConfigUtil;
import com.balancenotifier.engine.beans.User;
import com.balancenotifier.engine.blockchain.BlockChainExplorerFactory;
import com.balancenotifier.telegram.bot.TelegramBot;
import com.balancenotifier.telegram.handler.TelegramUserHandler;
import com.balancenotifier.telegram.handler.WalletType;

@Service
public class BalanceRefresh implements Runnable {

	final static Logger logger = LoggerFactory.getLogger(OrdersRefresh.class);

	@Autowired
	private ConfigUtil configUtil;

	@Autowired
	private BlockChainExplorerFactory blockChainExplorerFactory;

	private TelegramUserHandler telegramUserHandler;
	private TelegramBot telegramBot;

	public void init(TelegramUserHandler telegramUserHandler, TelegramBot telegramBot) {
		this.telegramUserHandler = telegramUserHandler;
		this.telegramBot = telegramBot;
	}

	@Override
	public void run() {

		logger.info("Start Balance Refresh ... ");
		long timeInterval = Long.parseLong(configUtil.getEnvironment().getRequiredProperty("balance.refrech.interval"));
		logger.info("Configured Balance Refresh interval is [{}] seconds", timeInterval);

		while (true) {
			try {
				refreshBalances();
				Thread.sleep(timeInterval * 1000);
			} catch (Exception e) {
				logger.error("Error While refresh Orders prices", e);
			}
		}

	}

	private void refreshBalances() {
		Map<Long, User> users = telegramUserHandler.getUsers();

		int zeroBalanceRetries = Integer.parseInt(configUtil.getEnvironment().getRequiredProperty("balance.zero.max.retries"));
		
		users.forEach((chatID, user) -> {

			if (!user.getWallets().isEmpty()) {

				user.getWallets().forEach((address, wallet) -> {

					double newBalance = blockChainExplorerFactory
							.getExplorer(WalletType.fromString(wallet.getWalletModel().getAddressType()))
							.getBalance(wallet.getWalletModel().getAddressString());

					if (newBalance == 0 && wallet.getZeroBalanceRetries() < zeroBalanceRetries) {
						telegramUserHandler.updateUserZeroBalanceRetry(chatID, wallet);
					} 
					else 
					{					
						telegramUserHandler.resetUserZeroBalanceRetry(chatID, wallet);
						
						double balance = wallet.getBalance();
						if (balance != newBalance) {

							String msgBody = "";
							if (newBalance > balance) {

								msgBody = String.format(
										"Address [<b>%s</b>] \n New deposit with amount [<b>%.8f</b>] \n current Balance [<b>%.8f</b>]",
										wallet.getWalletModel().getAddressAlias().length() != 0
												? wallet.getWalletModel().getAddressAlias()
												: wallet.getWalletModel().getAddressString(),
										newBalance - balance, newBalance);

								telegramUserHandler.updateUserBalance(chatID, wallet, newBalance);

							} else if (newBalance < balance) {
								msgBody = String.format(
										"Address [<b>%s</b>] \n New withdraw with amount [<b>%.8f</b>] \n current Balance [<b>%.8f</b>]",
										wallet.getWalletModel().getAddressAlias().length() != 0
												? wallet.getWalletModel().getAddressAlias()
												: wallet.getWalletModel().getAddressString(),
										newBalance - balance, newBalance);

								telegramUserHandler.updateUserBalance(chatID, wallet, newBalance);
							}

							telegramBot.sendMessage(chatID, msgBody, null);
						}
					}
				});

			}

			if (!user.getProfiles().isEmpty()) {
				user.getProfiles().forEach((profile) -> profile.refreshUserOrders());

			}
		});
	}

}
