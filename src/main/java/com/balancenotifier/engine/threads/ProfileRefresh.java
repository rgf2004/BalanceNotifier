package com.balancenotifier.engine.threads;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.balancenotifier.core.config.ConfigUtil;
import com.balancenotifier.engine.profile.BittrexProfile;

@Service
public class ProfileRefresh implements Runnable {

	final static Logger logger = LoggerFactory.getLogger(ProfileRefresh.class);

	@Autowired
	ConfigUtil configUtil;

	@Override
	public void run() {

		logger.info("Start Profile Refresh Thread ... ");
		long sleepInterval = Long.parseLong(configUtil.getEnvironment().getProperty("profile.refresh.interval"));
		logger.info("Configured Profile Refresh interval is [{}] seconds", sleepInterval);
		
		while (true) {
			try {
				logger.info("Start Refresh Exchange Prices...");
				BittrexProfile.refreshCurrencyPrices();
				logger.info("End Refresh Exchange Prices...");
				Thread.sleep(sleepInterval * 1000);
			} catch (Exception e) {
				logger.error("Error While refresh Exchange prices", e);
			}
		}

	}

}
