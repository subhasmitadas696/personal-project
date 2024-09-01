package com.csmtech.sjta.dto;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDTO {
	
	private BigInteger userId;
	private BigInteger citizenProfileDetailsId;
	private String userType;
	private String fullName;
	private String mobileNo;
	private String emailId;
	private BigInteger countint;
	private Boolean userBlockStatus;
	private String userName;

}
