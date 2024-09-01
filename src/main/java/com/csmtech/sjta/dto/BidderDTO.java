package com.csmtech.sjta.dto;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BidderDTO {

	private String price;
	private BigInteger initId;
	private BigInteger tenderid;

}
