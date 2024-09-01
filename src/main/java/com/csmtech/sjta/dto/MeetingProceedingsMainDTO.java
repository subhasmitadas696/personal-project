package com.csmtech.sjta.dto;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingProceedingsMainDTO {


	List<MeetingProceedingsDTO> respones;
	List<MeetingProceedingsOneDTO> respones1;
	List<MeetingProceedingsTwoDTO> respones2; 
	List<MeetingProceedingsThreeDTO> respones3;

}
