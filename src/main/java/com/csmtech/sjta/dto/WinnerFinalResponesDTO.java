package com.csmtech.sjta.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WinnerFinalResponesDTO {
	
	
	private BigInteger liveAuctionId;
	private String auctionNumberGen;
	private String auctionName;
	private BigDecimal royality;
	private String winnerName;
	private BigDecimal highestBidPrice;
	private String winnerDocument;
	private String plotCode;
	private BigInteger userId;
	private String plotNo;
	private BigInteger landApplicantId;
	private String totalArea;
	private String purchaseArea;
	private String pricePerAcer;
	private String totalPrice;
	
	
	private List<WinnerMultiDTO> getMultiRecord;

}
