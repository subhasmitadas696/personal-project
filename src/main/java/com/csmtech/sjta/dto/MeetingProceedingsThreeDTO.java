package com.csmtech.sjta.dto;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MeetingProceedingsThreeDTO {
	
	@Id
	private BigInteger meetingId;
	private String plotNo;
	private BigInteger landAppId;
	private String landAppName;
	private String landAppNo;
	private Short mettingLevleId;
	private String plotIds;
	private Integer approvalStatus;

}
