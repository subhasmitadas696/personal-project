package com.csmtech.sjta.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuctionPriviewDTO {
	
	private BigInteger intid;
    private String auctionName;
    private BigInteger auctionId;
    private String combinedValues;
    private Integer leasePeriodYears;
    private String committeeMemberName;
    private BigDecimal mgqAnnuallyIncome;
    private BigDecimal minimumAdditionalCharge;
    private BigDecimal royalty;
    private Date formMSubmitStartDate;
    private Date formMSubmitEndDate;
    private Date securityDepositStartDate;
    private Date securityDepositEndDate;
    private Date bidDocumentDownloadStartDate;
    private Date bidDocumentDownloadEndDate;
    private Date dateOfTechnicalEvaluation;
    private BigDecimal applicationFeeNotRefund;
    private BigDecimal securityAmountDeposit;
    private Date slotForAuctionFromDate;
    private Date slotForAuctionToDate;
    private String auctionName2;
    private String committeeMemberNameVal;
    private Boolean publishStatus;
    private String districtCode;
    private String tahasilCode;
    private String villageCode;
    private String khataNo;
    private Short auctionProcessFlag;
    private BigInteger auctionPlotDetailsId;
    private Short goForAuctionFlag;
    private String memberName;

}
