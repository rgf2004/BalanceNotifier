package com.balancenotifier.data.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.balancenotifier.data.dao.WalletDao;
import com.balancenotifier.data.model.WalletAddressModel;

@Service
@Transactional
public class WalletAddressHandler {

	@Autowired
	private WalletDao walletDao;

	public boolean saveWalletAddress(WalletAddressModel wallet) throws Exception{
		try {
			walletDao.createEntity(wallet);
			return true;
		} catch (Exception ex) {
			throw ex;
		}
	}

	public List<WalletAddressModel> getWalletAddressesByUserID(long userId) {
		return walletDao.getWalletAddressesByUserID(userId);
	}
}
