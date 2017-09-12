package com.balancenotifier.engine.beans;

import com.balancenotifier.data.model.WalletAddressModel;

public class Wallet {

	WalletAddressModel walletModel;
	double balance;
	int    zeroBalanceRetries = 0;
	
	public Wallet(WalletAddressModel walletModel, double balance) {
		this.walletModel = walletModel;
		this.balance = balance;
	}
	
	public WalletAddressModel getWalletModel() {
		return walletModel;
	}
	public void setWalletModel(WalletAddressModel walletModel) {
		this.walletModel = walletModel;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}

	public int getZeroBalanceRetries() {
		return zeroBalanceRetries;
	}

	public void setZeroBalanceRetries(int zeroBalanceRetries) {
		this.zeroBalanceRetries = zeroBalanceRetries;
	}

}
