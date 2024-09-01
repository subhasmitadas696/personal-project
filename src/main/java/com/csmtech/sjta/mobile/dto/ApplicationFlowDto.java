package com.csmtech.sjta.mobile.dto;

import java.math.BigInteger;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationFlowDto {

	private BigInteger flowId;
	private BigInteger landApplicationId;
	private BigInteger applicationFlowId;
	private BigInteger actionRoleId;
	private Date actionDateTime;
	private String flow;
	
}
