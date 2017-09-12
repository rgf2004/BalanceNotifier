package com.balancenotifier.data.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.balancenotifier.data.dao.UserDao;
import com.balancenotifier.data.model.UserModel;

@Service
@Transactional
public class UserHandler {

	@Autowired
	private UserDao userDao;
	
	public List<UserModel> findAll() 
	{
		return userDao.findAll();
	}
	
	public boolean saveUser(UserModel userModel) throws Exception 
	{
		try
		{
			userDao.createEntity(userModel);
						
			return true;
		}
		catch (Exception ex)
		{
			throw ex;
		}		
	}
	
}
