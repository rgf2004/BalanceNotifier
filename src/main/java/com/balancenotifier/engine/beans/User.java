package com.balancenotifier.engine.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.balancenotifier.data.model.UserModel;
import com.balancenotifier.engine.profile.Profile;
import com.balancenotifier.telegram.handler.ProfileType;
import com.balancenotifier.telegram.handler.UserStatus;
import com.balancenotifier.telegram.handler.WalletType;

public class User implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8844608986694444627L;

	private long userChatID;
	private String apiKey ;
	private String secret;
	private UserStatus status;
	private UserModel userModel;

	private WalletType cachedWalletType;
	private String cachedWalletAddress;
	private String cachedAddressAlias;
	
	private ProfileType cachedProfileType;
	private String cachedApiKey;
	private String cachedSecret;
	private UserStatus cachedStatus;
	
	private List<Profile> profiles = new ArrayList<>();

	//private List<WalletAddressModel> wallets = new ArrayList<>();
	private Map<String, Wallet> wallets = new HashMap<>();
	
	public User()
	{
		
	}
	
	public User(UserModel userModel)
	{
		userChatID = userModel.getChatID();
		this.userModel = userModel;
	}
	
	public void backupUserStatus()
	{
		this.cachedStatus = this.status;
	}
	
	public void revertUserStatus()
	{
		this.status = this.cachedStatus;
	}
	
	public long getUserChatID() {
		return userChatID;
	}
	public void setUserChatID(long channelID) {
		this.userChatID = channelID;
	}
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public ProfileType getCachedProfileType() {
		return cachedProfileType;
	}

	public void setCachedProfileType(ProfileType cachedProfileType) {
		this.cachedProfileType = cachedProfileType;
	}

	public String getCachedApiKey() {
		return cachedApiKey;
	}

	public void setCachedApiKey(String cachedApiKey) {
		this.cachedApiKey = cachedApiKey;
	}

	public String getCachedSecret() {
		return cachedSecret;
	}

	public void setCachedSecret(String cachedSecret) {
		this.cachedSecret = cachedSecret;
	}
	
	public UserModel getUserModel() {
		return userModel;
	}

	public List<Profile> getProfiles() {
		return profiles;
	}

	public void setProfiles(List<Profile> profiles) {
		this.profiles = profiles;
	}

	public Map<String, Wallet> getWallets() {
		return wallets;
	}

	public void setWallets(Map<String, Wallet> wallets) {
		this.wallets = wallets;
	}

	public WalletType getCachedWalletType() {
		return cachedWalletType;
	}

	public void setCachedWalletType(WalletType cachedWalletType) {
		this.cachedWalletType = cachedWalletType;
	}

	public String getCachedWalletAddress() {
		return cachedWalletAddress;
	}

	public void setCachedWalletAddress(String cachedWalletAddress) {
		this.cachedWalletAddress = cachedWalletAddress;
	}

	public String getCachedAddressAlias() {
		return cachedAddressAlias;
	}

	public void setCachedAddressAlias(String cachedAddressAlias) {
		this.cachedAddressAlias = cachedAddressAlias;
	}
	
	
}
