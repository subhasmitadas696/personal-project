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
public class MeetingProceedingsTwoDTO {
	
	private BigInteger meetingId;
	private String plotNo;
	

}
