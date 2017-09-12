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
@Table(name = "ADDRESS")
@NamedQueries({
@NamedQuery(name = "Address.findByUserID", query = "SELECT a FROM WalletAddressModel a WHERE a.user.userId=:userId")
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class WalletAddressModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6341611862056364681L;

	@Id
	@GenericGenerator(name = "address", strategy = "increment")
	@GeneratedValue(generator = "address")
	@Column(name = "ADDRESS_ID")
	private long addressId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="userId")
	private UserModel user;
	
	@Column(name = "ADDRESS_TYPE", nullable = false)
	private String addressType;
	
	@Column(name = "ADDRESS_ALIAS", nullable = false)
	private String addressAlias;
	
	@Column(name = "ADDRESS_STRING", nullable = false)
	private String addressString;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INSERTION_DATE")
	@CreationTimestamp
	private Date insertionDate;

	public long getAddressId() {
		return addressId;
	}

	public void setAddressId(long addressId) {
		this.addressId = addressId;
	}

	public UserModel getUser() {
		return user;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	public String getAddressString() {
		return addressString;
	}

	public void setAddressString(String addressString) {
		this.addressString = addressString;
	}

	public Date getInsertionDate() {
		return insertionDate;
	}

	public void setInsertionDate(Date insertionDate) {
		this.insertionDate = insertionDate;
	}

	public String getAddressAlias() {
		return addressAlias;
	}

	public void setAddressAlias(String addressAlias) {
		this.addressAlias = addressAlias;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((addressAlias == null) ? 0 : addressAlias.hashCode());
		result = prime * result + (int) (addressId ^ (addressId >>> 32));
		result = prime * result + ((addressString == null) ? 0 : addressString.hashCode());
		result = prime * result + ((addressType == null) ? 0 : addressType.hashCode());
		result = prime * result + ((insertionDate == null) ? 0 : insertionDate.hashCode());
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
		WalletAddressModel other = (WalletAddressModel) obj;
		if (addressAlias == null) {
			if (other.addressAlias != null)
				return false;
		} else if (!addressAlias.equals(other.addressAlias))
			return false;
		if (addressId != other.addressId)
			return false;
		if (addressString == null) {
			if (other.addressString != null)
				return false;
		} else if (!addressString.equals(other.addressString))
			return false;
		if (addressType == null) {
			if (other.addressType != null)
				return false;
		} else if (!addressType.equals(other.addressType))
			return false;
		if (insertionDate == null) {
			if (other.insertionDate != null)
				return false;
		} else if (!insertionDate.equals(other.insertionDate))
			return false;
		return true;
	}
	
	
}
