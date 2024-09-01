package com.csmtech.sjta.dto;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MettingUplodeMomAuctionApprovalDTO {
	
	private Integer value;
	private BigInteger landId;
	private BigInteger mettingId;
	private String plotcode;

}
