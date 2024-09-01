package com.csmtech.sjta.dto;

import java.math.BigInteger;

import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author prasanta.sethi
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryDetailsDTO {
	private BigInteger queryId;
	private String query;
	private String name;
	private String mobileno;
	private Integer answer;
	private String captchaId;
}
