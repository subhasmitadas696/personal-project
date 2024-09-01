/**
 * 
 */
package com.csmtech.sjta.serviceImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.controller.AuctionReportService;
import com.csmtech.sjta.dto.AuctionReportDTO;
import com.csmtech.sjta.repository.AuctionReportRepository;
import com.csmtech.sjta.util.ExcelHelper;

/**
 * @author prasanta.sethi
 */
@Service
public class AuctionReportServiceImpl implements AuctionReportService {

	@Autowired
	private AuctionReportRepository reportNativeRepo;

	@Override
	public List<AuctionReportDTO> getAuctionReportData(String data) {
		List<AuctionReportDTO> finalReportData = new ArrayList<>();
		JSONObject jsonData = new JSONObject(data);
		Integer pageSize = jsonData.getInt("size");
		Integer pageNo = jsonData.getInt("pageNo");
		Integer offset = (pageNo - 1) * pageSize;
		List<Object[]> getRecord = reportNativeRepo.getReportData(pageSize, offset);
		for (Object[] result : getRecord) {
			AuctionReportDTO dto = new AuctionReportDTO();
			dto.setTenderAuctionId((BigInteger) result[0]);
			dto.setAuctionNumber((String) result[1]);
			dto.setAuctionDate((Date) result[2]);
			dto.setWinnerName((String) result[3]);
			dto.setWinnerBidAmount((BigDecimal) result[4]);
			dto.setDistrict((String) result[5]);
			dto.setTahasil((String) result[6]);
			dto.setVillage((String) result[7]);
			dto.setKhata((String) result[8]);
			dto.setPlot((String) result[9]);
//			dto.setLandDetails((String) result[7]);
			finalReportData.add(dto);
		}
		return finalReportData;
	}

	@Override
	public BigInteger getReportCount() {
		return reportNativeRepo.getReportCount();
	}

	@Override
	public List<AuctionReportDTO> getParticipantData(String data) {
		List<AuctionReportDTO> participantData = new ArrayList<>();
		JSONObject jsonData = new JSONObject(data);
		BigInteger tenderAuctionId = jsonData.getBigInteger("tenderAuctionId");
		List<Object[]> getRecord = reportNativeRepo.participantData(tenderAuctionId);
		for (Object[] result : getRecord) {
			AuctionReportDTO dto = new AuctionReportDTO();
			dto.setWinnerName((String) result[2]);
			dto.setWinnerBidAmount((BigDecimal) result[1]);
			participantData.add(dto);
		}
		return participantData;
	}

	@Override
	public BigInteger getParticipantCount() {
		return null;
	}

	@Override
	public ByteArrayInputStream getAuctionDataForReportExcel() throws IOException {
		List<AuctionReportDTO> finalReportData = new ArrayList<>();
		List<Object[]> getRecord = reportNativeRepo.getAuctionDataForReportExcel();
		for (Object[] result : getRecord) {
			AuctionReportDTO dto = new AuctionReportDTO();
			dto.setTenderAuctionId((BigInteger) result[0]);
			dto.setAuctionNumber((String) result[1]);
			dto.setLandDetails((String) result[2]);
			dto.setAuctionDate((Date) result[3]);
			dto.setWinnerName((String) result[4]);
			dto.setWinnerBidAmount((BigDecimal) result[5]);
			dto.setParticipantDetails((String) result[6]);
			finalReportData.add(dto);
		}
		return ExcelHelper.getAuctionReport(finalReportData);
	}

	@Override
	public List<AuctionReportDTO> getBidderAllRecordHistory(String data) {
		List<AuctionReportDTO> participantData = new ArrayList<>();
		JSONObject jsonData = new JSONObject(data);
		BigInteger tenderAuctionId = jsonData.getBigInteger("tenderAuctionId");
		List<Object[]> getRecord = reportNativeRepo.getBidderAllRecordHistory(tenderAuctionId);
		for (Object[] result : getRecord) {
			AuctionReportDTO dto = new AuctionReportDTO();
			dto.setWinnerName((String) result[2]);
			dto.setWinnerBidAmount((BigDecimal) result[1]);
			dto.setBidDateTime((Date) result[3]);
			participantData.add(dto);
		}
		return participantData;
	}

	@Override
	public BigInteger getBidderAllRecordHistoryCount(String data) {
		JSONObject jsonData = new JSONObject(data);
		BigInteger tenderAuctionId = jsonData.getBigInteger("tenderAuctionId");
		return reportNativeRepo.getBidderAllRecordHistoryCount(tenderAuctionId);
	}

}
