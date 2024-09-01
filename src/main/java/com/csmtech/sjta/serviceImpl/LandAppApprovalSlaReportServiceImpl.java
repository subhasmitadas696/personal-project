/**
 * 
 */
package com.csmtech.sjta.serviceImpl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.dto.LandApplicationApprovalSlaDTO;
import com.csmtech.sjta.repository.LandAppApprovalSlaReportRepository;
import com.csmtech.sjta.service.LandAppApprovalSlaReportService;

/**
 * 
 */
@Service
public class LandAppApprovalSlaReportServiceImpl implements LandAppApprovalSlaReportService {
	@Autowired
	private LandAppApprovalSlaReportRepository reportNativeRepo;

	@Override
	public List<LandApplicationApprovalSlaDTO> getLandApplicationReportData(String data) {
		List<LandApplicationApprovalSlaDTO> finalReportData = new ArrayList<>();
		JSONObject jsonData = new JSONObject(data);
		Integer pageSize = jsonData.getInt("size");
		Integer pageNo = jsonData.getInt("pageNo");
		Integer offset = (pageNo - 1) * pageSize;
		List<Object[]> getRecord = reportNativeRepo.getReportData(pageSize, offset);
		for (Object[] result : getRecord) {
			LandApplicationApprovalSlaDTO dto = new LandApplicationApprovalSlaDTO();
			dto.setApplicationNo((String) result[1]);
			dto.setApplicantName((String) result[2]);
			dto.setPendingAt((String) result[3]);
			dto.setNoOfDayDelayed((Integer) result[4]);
			finalReportData.add(dto);
		}
		return finalReportData;

	}

	@Override
	public BigInteger getReportCount() {
		return reportNativeRepo.getReportCount();
	}

	@Override
	public List<LandApplicationApprovalSlaDTO> getAuctionApprovalReportData(String data) {
		List<LandApplicationApprovalSlaDTO> finalReportData = new ArrayList<>();
		JSONObject jsonData = new JSONObject(data);
		Integer pageSize = jsonData.getInt("size");
		Integer pageNo = jsonData.getInt("pageNo");
		Integer offset = (pageNo - 1) * pageSize;
		List<Object[]> getRecord = reportNativeRepo.getAuctionReportData(pageSize, offset);
		for (Object[] result : getRecord) {
			LandApplicationApprovalSlaDTO dto = new LandApplicationApprovalSlaDTO();
			dto.setBidderformMApplicationNo((String) result[6]);
			dto.setApplicantName((String) result[4]);
			dto.setPendingAt((String) result[5]);
			dto.setNoOfDayDelayed((Integer) result[3]);
			finalReportData.add(dto);
		}
		return finalReportData;
	}

	public BigInteger getAuctionReportCount() {
		return reportNativeRepo.getAuctionReportCount();
	}

	@Override
	public List<LandApplicationApprovalSlaDTO> getLandVerificationReportData(String data) {
		List<LandApplicationApprovalSlaDTO> finalVerificationData = new ArrayList<>();
		JSONObject jsonData = new JSONObject(data);
		Integer pageSize = jsonData.getInt("size");
		Integer pageNo = jsonData.getInt("pageNo");
		Integer offset = (pageNo - 1) * pageSize;
		List<Object[]> getRecord = reportNativeRepo.getLandVerificationData(pageSize, offset);
		for (Object[] result : getRecord) {
			LandApplicationApprovalSlaDTO dto = new LandApplicationApprovalSlaDTO();
			dto.setDistrict((String) result[0]);
			dto.setTahasil((String) result[1]);
			dto.setVillage((String) result[2]);
			dto.setKhataNo((String) result[3]);
			dto.setPlotNO((String) result[4]);
			
			String pendingWith = "";
			if((Short) result[5] == 0 && (Short) result[6] == 0) {
				pendingWith = "Pending at Tahasil & CO";
			} else if((Short) result[5] == 0) {
				pendingWith = "Pending at CO";
			} else if((Short) result[6] == 0) {
				pendingWith = "Pending at Tahasil";
			}
			
			dto.setPendingAt(pendingWith);
			dto.setNoOfDayDelayed((Integer) result[7]);
			finalVerificationData.add(dto);
		}
		return finalVerificationData;
	}

	@Override
	public BigInteger getLandVerificationReportCount() {
		return reportNativeRepo.getLandVerificationReportCount();
	}

}
