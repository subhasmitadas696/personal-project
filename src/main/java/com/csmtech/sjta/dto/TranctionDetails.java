package com.csmtech.sjta.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TranctionDetails {

	private String orderId;
	private String courency;
	private Integer amount;
	private String key;
}
