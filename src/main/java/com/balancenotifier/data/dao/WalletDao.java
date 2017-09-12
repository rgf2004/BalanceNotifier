package com.balancenotifier.data.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.balancenotifier.data.model.WalletAddressModel;

@Repository
public class WalletDao extends JPADao<WalletAddressModel>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3335668234449208413L;

	public List<WalletAddressModel> getWalletAddressesByUserID(long userId)
	{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		
		return this.findByNamedQuery("Address.findByUserID", params);
	}
}
