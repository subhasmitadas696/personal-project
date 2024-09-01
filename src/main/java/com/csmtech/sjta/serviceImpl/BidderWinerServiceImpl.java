package com.csmtech.sjta.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.ShowWinerDTO;
import com.csmtech.sjta.dto.WinnerFinalResponesDTO;
import com.csmtech.sjta.dto.WinnerMultiDTO;
import com.csmtech.sjta.mobile.dto.ApplicationFlowDto;
import com.csmtech.sjta.mobile.repository.ApplicationFlowRepository;
import com.csmtech.sjta.mobile.service.CommonService;
import com.csmtech.sjta.repository.BidderWinerRepository;
import com.csmtech.sjta.repository.LandAreaStatisticsRepository;
import com.csmtech.sjta.service.BidderWinerService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BidderWinerServiceImpl implements BidderWinerService {

	@Autowired
	private BidderWinerRepository repo;

	@Autowired
	private CommonService commonService;

	@Autowired
	ApplicationFlowRepository applicationFlowRepo;

	@Value("${tempUpload.path}")
	private String tempUploadPath;
	@Value("${file.path}")
	private String finalUploadPath;

	@Autowired
	LandAreaStatisticsRepository landGis;

	@Override
	public List<ShowWinerDTO> getCustomResults(Integer pageNO, Integer pageSize) {
		return repo.getCustomResults(pageNO, pageSize);
	}

	@Override
	public BigInteger getCountOfLiveAuctions() {
		return repo.getCountOfLiveAuctions();
	}

	@Override
	public WinnerFinalResponesDTO getWinerFinalRecordData(BigInteger tenderId) {
		List<Object[]> getSingleRes = repo.getWinnerResult(tenderId);
		List<Object[]> getMultiRes = repo.getWinnerResultMulti(tenderId);
		List<WinnerMultiDTO> retrnResultMulti = new ArrayList<>();
		for (Object[] result : getMultiRes) {
			WinnerMultiDTO dto = new WinnerMultiDTO();
			dto.setTenderId((BigInteger) result[0]);
			dto.setAuctionName((String) result[1]);
			dto.setBidderName((String) result[2]);
			dto.setHighestBidPrice((BigDecimal) result[3]);
			retrnResultMulti.add(dto);
		}
		WinnerFinalResponesDTO finalResponse = new WinnerFinalResponesDTO();
		for (Object[] result : getSingleRes) {
			finalResponse.setAuctionNumberGen((String) result[0]);
			finalResponse.setAuctionName((String) result[1]);
			finalResponse.setRoyality((BigDecimal) result[2]);
			finalResponse.setLiveAuctionId((BigInteger) result[3]);
			finalResponse.setWinnerName((String) result[4]);
			finalResponse.setHighestBidPrice((BigDecimal) result[5]);
			finalResponse.setWinnerDocument((String) result[6]);
			finalResponse.setPlotCode((String) result[7]);
			finalResponse.setUserId((BigInteger) result[8]);
			finalResponse.setPlotNo((String) result[9]);
			finalResponse.setLandApplicantId((BigInteger) result[10]);
			finalResponse.setTotalArea((String) result[11]);
			finalResponse.setPurchaseArea((String) result[12]);
			finalResponse.setPricePerAcer((String) result[13]);
			finalResponse.setTotalPrice((String) result[14]);
		}
		finalResponse.setGetMultiRecord(retrnResultMulti);

		return finalResponse;
	}

	@Override
	public Integer updateWinnerDocument(BigInteger liveAuctionId, String documentName, String data) {
		JSONObject js = new JSONObject(data);
		if (!documentName.equals("")) {
			File f = new File(tempUploadPath + documentName);
			if (f.exists()) {

				File folder = new File(finalUploadPath + "/auction");
				if (!folder.exists()) {
					folder.mkdirs();
				}
				File src = new File(tempUploadPath + documentName);
				File dest = new File(folder + "/" + documentName);
				try {
					Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
					Files.delete(src.toPath());
				} catch (IOException e) {
					log.error("error occured while displaying update winner: " + e.getMessage());
				}
			}
		}

		// application flow
		BigInteger tenderAuctionPlotId = applicationFlowRepo.findTenderAuctionIdLiveAuction(liveAuctionId);
		BigInteger auctionPlotId = applicationFlowRepo.findAuctionPlotId(tenderAuctionPlotId);
		List<BigInteger> landAppId = applicationFlowRepo.findLandAppIds(auctionPlotId.intValue());

		for (BigInteger appId : landAppId) {
			ApplicationFlowDto dto = new ApplicationFlowDto();
			dto.setLandApplicationId(appId != null ? appId : BigInteger.ZERO);
			dto.setApplicationFlowId(BigInteger.valueOf(29));
			dto.setActionDateTime(new Date());
			dto.setActionRoleId(BigInteger.valueOf(4));
			commonService.saveApplicationFlow(dto);
		}

		Integer countInsert = landGis.updateLandGisSingle(js.getString("plotCode"), 3, 3);

		return repo.updateWinnerDocument(liveAuctionId, documentName, js.getBigInteger("createdBy"),
				js.getBigInteger("landApplicantId"), js.getString("plotNo"), js.getBigDecimal("totalArea"),
				js.getBigDecimal("purchaseArea"), js.getBigDecimal("pricePerAcer"), js.getBigDecimal("totalPrice"),
				js.getInt("auctionFlag"), js.getString("plotCode"));
	}

}
