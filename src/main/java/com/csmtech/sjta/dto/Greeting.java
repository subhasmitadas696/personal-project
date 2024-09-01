package com.csmtech.sjta.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Greeting {

	private List<BigDecimal> content;
	private Integer status;
	private BigDecimal highestPrice;
	private BigInteger numberOfBids;
	private BigInteger tenderid;
	private BigDecimal maxBidPrice;
	private BigInteger userId;
	
	
	
	public Greeting(Integer status) {
		super();
		this.status = status;
	}
	
	
}
