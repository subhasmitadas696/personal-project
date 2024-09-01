package com.csmtech.sjta.dto;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LandAllertmentSlcCompliteDTO {
	
	private BigInteger meetingId;
    private String meetingUniqueNo;
    private Short meetingLevle;
    private BigInteger landMeetingCount;
    
    //hear the plot through record get DTO parameter
    private BigInteger landAppId;
    private String plotNo;
    private String applicantName;
    private String pricePerAcer;
    private String totalArea;
    private String purchaseArea;
    private String totalPrice;
    private String venue;
    private String meetingDate;
    private String meetingPurpose;
    private String combineName;
    private BigInteger bidderId;
    private BigInteger landId;
    private String plotCode;

}
