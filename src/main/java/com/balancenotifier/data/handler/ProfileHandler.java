package com.balancenotifier.data.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.balancenotifier.data.dao.ProfileDao;
import com.balancenotifier.data.model.ProfileModel;

@Service
@Transactional
public class ProfileHandler {

	@Autowired
	private ProfileDao profileDao;

	public boolean saveProfile(ProfileModel profile) throws Exception {
		try {
			profileDao.createEntity(profile);
			return true;
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	public List<ProfileModel> getProfilesByUserID(long userId)
	{
		return profileDao.getProfilesByUserID(userId);
	}

}
