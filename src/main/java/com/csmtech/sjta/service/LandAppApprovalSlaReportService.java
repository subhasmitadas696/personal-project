/**
 * 
 */
package com.csmtech.sjta.service;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

import com.csmtech.sjta.dto.LandApplicationApprovalSlaDTO;

/**
 * @author prasanta.sethi
 */
public interface LandAppApprovalSlaReportService {

	public List<LandApplicationApprovalSlaDTO> getLandApplicationReportData(String data);

	public BigInteger getReportCount();

	public List<LandApplicationApprovalSlaDTO> getAuctionApprovalReportData(String data);

	public BigInteger getAuctionReportCount();

	public List<LandApplicationApprovalSlaDTO> getLandVerificationReportData(String data);

	public BigInteger getLandVerificationReportCount();

}
