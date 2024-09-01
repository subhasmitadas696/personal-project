/**
 * 
 */
package com.csmtech.sjta.dto;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author prasanta.sethi
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LandApplicationApprovalSlaDTO {
	private BigInteger landApplicationApprovalId;
	private String applicationNo;
	private String applicantName;
	private String pendingAt;
	private Integer noOfDayDelayed;
	private String bidderformMApplicationNo;
	private Short approvalStatus;
	private String district;
	private String tahasil;
	private String village;
	private String khataNo;
	private String plotNO;
	private Short coApproveStatus;
	private Short tahasilApproveStatus;
}
