package com.csmtech.sjta.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.PlotValuationDTO;
import com.csmtech.sjta.entity.PlotValuationEntity;
import com.csmtech.sjta.mobile.dto.ApplicationFlowDto;
import com.csmtech.sjta.mobile.dto.LandVerificationResponseDto;
import com.csmtech.sjta.mobile.repository.LandUseRepository;
import com.csmtech.sjta.mobile.service.CommonService;
import com.csmtech.sjta.repository.PlotValuationRepository;
import com.csmtech.sjta.service.PlotValuationService;
import com.csmtech.sjta.util.CommonUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PlotValuationServiceImpl implements PlotValuationService {

	@Autowired
	PlotValuationRepository plotValuationRepo;

	@Autowired
	LandUseRepository landUseRepo;

	@Autowired
	private CommonService commonService;

	@Value("${tempUpload.path}")
	private String tempUploadPath;
	@Value("${file.path}")
	private String finalUploadPath;

	@Override
	public LandVerificationResponseDto landPlotValuation(PlotValuationDTO landDto) {
		LandVerificationResponseDto response = new LandVerificationResponseDto();
		try {
			List<String> fileUploadList = new ArrayList<>();
			fileUploadList.add(landDto.getFileUploadOwnershipDocument());
			PlotValuationEntity plotEntity1 = new PlotValuationEntity();
			plotEntity1 = plotValuationRepo.findByPlotCodeNative(landDto.getPlotCode());
			if (plotEntity1 != null) {
				response.setStatus(210);
				response.setMessage("Duplicate plot has been assigned");
			} else {
				PlotValuationEntity plotEntity = new PlotValuationEntity();
				plotEntity.setPlotLandInspectionId(landDto.getPlotLandInspectionId());
				plotEntity.setPlotCode(landDto.getPlotCode());
				plotEntity.setAvailableLand(landDto.getAvailableLand());
				plotEntity.setLandOwnerName(landDto.getLandUserName());
				plotEntity.setLandAddress(landDto.getLandAddress());
				plotEntity.setKhatianCode(landDto.getKhatianCode() != null ? landDto.getKhatianCode() : null);
				plotEntity.setTypeOfLandUse(landDto.getTypeOfLandUse());
				plotEntity.setTenure(landDto.getTenure());
				plotEntity.setOwnershipRecord(landDto.getOwnershipRecord());
				plotEntity.setOwnershipRecordDoc(landDto.getFileUploadOwnershipDocument());
				String pricePerAcreString = landDto.getPricePerAcre();
				BigDecimal pricePerAcreBigDecimal = new BigDecimal(pricePerAcreString);

				plotEntity.setPricePerAcre(pricePerAcreBigDecimal);

				String totalPriceString = landDto.getTotalPrice();
				BigDecimal totalPriceBigDecimal = new BigDecimal(totalPriceString);

				plotEntity.setTotalPrice(totalPriceBigDecimal);

				Date parsedDate = new Date();

				plotEntity.setCreatedDate(parsedDate);
				plotEntity.setCreatedBy(landDto.getCreatedBy());
				// plotValuationRepo.save(plotEntity);
				int a = plotValuationRepo.updatePlotValuation(landDto.getPlotLandInspectionId(), landDto.getPlotCode(),
						landDto.getAvailableLand(), landDto.getLandUserName(), landDto.getLandAddress(),
						landDto.getKhatianCode(), landDto.getTypeOfLandUse(), landDto.getTenure(),
						landDto.getOwnershipRecord(), landDto.getFileUploadOwnershipDocument(), pricePerAcreBigDecimal,
						totalPriceBigDecimal, parsedDate, landDto.getCreatedBy());
				for (String fileUpload : fileUploadList) {
					if (fileUpload != null && (!fileUpload.equals(""))) {
						File f = new File(tempUploadPath + fileUpload);
						log.info("fileUpload : " + fileUpload);

						if (f.exists()) {
							Path srcPath = Paths.get(tempUploadPath + fileUpload);
							Path destPath = Paths.get(finalUploadPath + "/" + "plotvaluation/" + fileUpload);

							try {
								CommonUtil.createDirectories(destPath.getParent());
								CommonUtil.copyAndDeleteFile(srcPath, destPath);
							} catch (IOException e) {
								log.error("Error occurred , while copying file :{}", e.getMessage());
							}
						}

					}
				}

				// application flow

				ApplicationFlowDto dto = new ApplicationFlowDto();
				dto.setLandApplicationId(BigInteger.ZERO);
				dto.setApplicationFlowId(BigInteger.valueOf(23));
				dto.setActionDateTime(new Date());
				dto.setActionRoleId(BigInteger.valueOf(4));
				commonService.saveApplicationFlow(dto);

				response.setStatus(200);
				response.setMessage("Plot Valuated successfully");

			}
		} catch (Exception e) {
			log.error("error while persisting data into db: " + e.getMessage());
			response.setStatus(500);
			response.setMessage("error while persisting data into db");
		}

		return response;
	}

}
