package com.csmtech.sjta.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddOfficerDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BigInteger userId;
	private String fullName;
	private String mobileNo;
	private String emailId;
	private String userName;
	private Integer departmentId;
	private String departmentName;
	private Integer roleId;
	private String roleName;
	private Boolean userBlockStatus;
	private BigInteger countint;

}
