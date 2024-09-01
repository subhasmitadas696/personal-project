/**
 * 
 */
package com.csmtech.sjta.repository;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

/**
 * @author prasanta.sethi
 */
public interface LandAppApprovalSlaReportRepository {

	public List<Object[]> getReportData(Integer pageSize, Integer offset);

	public BigInteger getReportCount();

	public BigInteger getAuctionReportCount();

	public List<Object[]> getAuctionReportData(Integer pageSize, Integer offset);

	public List<Object[]> getLandVerificationData(Integer pageSize, Integer offset);

	public BigInteger getLandVerificationReportCount();
	

}
