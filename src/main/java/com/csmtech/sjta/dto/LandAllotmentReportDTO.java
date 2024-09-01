package com.csmtech.sjta.dto;

import java.io.Serializable;
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
public class LandAllotmentReportDTO implements Serializable {

	/**
	 * @author prasanta.sethi
	 */
	private static final long serialVersionUID = -8652132287646852390L;
	
	@Id
	private String districtCode;
	private String districtName;
	private BigInteger pendingCount;
	private BigInteger form16Count;
	private BigInteger registerCount;

}
