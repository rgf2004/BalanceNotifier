package com.balancenotifier.data.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "PROFILE")
@NamedQueries({
@NamedQuery(name = "Profile.findByUserID", query = "SELECT p FROM ProfileModel p WHERE p.user.userId=:userId")
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ProfileModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5569392344761976532L;

	
	@Id
	@GenericGenerator(name = "profile", strategy = "increment")
	@GeneratedValue(generator = "profile")
	@Column(name = "PROFILE_ID")
	private long profileId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="userId")
	private UserModel user;
	
	@Column(name = "PROFILE_TYPE", nullable = false, unique = true)
	private String profileType;
	
	@Column(name = "TOKEN")
	private String token;
	
	@Column(name = "SECRET")
	private String secret;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INSERTION_DATE")
	@CreationTimestamp
	private Date insertionDate;

	public long getProfileId() {
		return profileId;
	}

	public void setProfileId(long profileId) {
		this.profileId = profileId;
	}

	public UserModel getUser() {
		return user;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}

	public String getProfileType() {
		return profileType;
	}

	public void setProfileType(String profileType) {
		this.profileType = profileType;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public Date getInsertionDate() {
		return insertionDate;
	}

	public void setInsertionDate(Date insertionDate) {
		this.insertionDate = insertionDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((insertionDate == null) ? 0 : insertionDate.hashCode());
		result = prime * result + (int) (profileId ^ (profileId >>> 32));
		result = prime * result + ((profileType == null) ? 0 : profileType.hashCode());
		result = prime * result + ((secret == null) ? 0 : secret.hashCode());
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProfileModel other = (ProfileModel) obj;
		if (insertionDate == null) {
			if (other.insertionDate != null)
				return false;
		} else if (!insertionDate.equals(other.insertionDate))
			return false;
		if (profileId != other.profileId)
			return false;
		if (profileType == null) {
			if (other.profileType != null)
				return false;
		} else if (!profileType.equals(other.profileType))
			return false;
		if (secret == null) {
			if (other.secret != null)
				return false;
		} else if (!secret.equals(other.secret))
			return false;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Profile [profileId=" + profileId + ", user=" + user + ", profileType=" + profileType + ", token="
				+ token + ", secret=" + secret + ", insertionDate=" + insertionDate + "]";
	}
	
	
}
