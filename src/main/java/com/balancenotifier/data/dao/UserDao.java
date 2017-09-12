package com.balancenotifier.data.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.balancenotifier.data.model.UserModel;

@Repository
public class UserDao extends JPADao<UserModel>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1961291506412066999L;

	public UserModel getUserByChatId(long chatId)
	{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("chatID", chatId);		

		List<UserModel> users = super.findByNamedQuery("User.findByChatID", params);
		
		if (users == null || users.isEmpty())
			return null;
		else
			return users.get(0);
	}
	
}
