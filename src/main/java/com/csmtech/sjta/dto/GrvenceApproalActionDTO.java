package com.csmtech.sjta.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrvenceApproalActionDTO {
	
	String approvalAction;
	String actionRemarks;
	Date scheduleinception;
	String intId;

}
