package com.csmtech.sjta.dto;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MettingUplodeMomPlotsRecordDTO {
	
	private Integer auctionClickId;
	private String plotsNo;
	private BigInteger createdBy;
	private Short auctionprocessFlag;

}
