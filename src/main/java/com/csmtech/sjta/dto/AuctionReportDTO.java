/**
 * 
 */
package com.csmtech.sjta.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author prasanta.sethi
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuctionReportDTO {
	private BigInteger tenderAuctionId;
	private String auctionNumber;
	private String district;
	private String tahasil;
	private String village;
	private String khata;
	private String plot;
	private String landDetails;
	private Date auctionDate;
	private String winnerName;
	private BigDecimal winnerBidAmount;
	private BigInteger count;
	private List<Map<String, String>> dataList;
	private String participantDetails;
	private Date bidDateTime;
}
