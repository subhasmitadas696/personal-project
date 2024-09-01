package com.csmtech.sjta.dto;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingPlotsRecordDTO {

	private BigInteger meetingId;
	private String plotCode;
	private String meetingUniqueNo;
	private BigInteger meetingSheduleId;
	private List<Map<String, String>> plotDetails;  

}
