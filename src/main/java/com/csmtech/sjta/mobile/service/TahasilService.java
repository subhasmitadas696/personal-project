package com.csmtech.sjta.mobile.service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.GenericResponse;
import com.csmtech.sjta.dto.PaginationInRegisterDtoResponse;
import com.csmtech.sjta.dto.TahasilTeamUseRequestDto;
import com.csmtech.sjta.mobile.dto.ImageRequestDto;
import com.csmtech.sjta.mobile.dto.OtpResponseDto;
import com.csmtech.sjta.mobile.dto.TahasilPlotDto;
import com.csmtech.sjta.mobile.dto.TahasilResponseDTO;
import com.csmtech.sjta.mobile.dto.VillageDTO;

@Service
public interface TahasilService {

	GenericResponse tahasilLogin(String token,TahasilTeamUseRequestDto tahasilDto);

	OtpResponseDto tahasilMobileInsertion(TahasilTeamUseRequestDto tahasilDto) throws NoSuchAlgorithmException;

	TahasilResponseDTO tahasilOtpVerification(TahasilTeamUseRequestDto tahasilDto);

	TahasilResponseDTO getVillageDetails(TahasilTeamUseRequestDto tahasilTeamUseRequestDto);

	TahasilResponseDTO getVillageDetailsByPlot(VillageDTO villageDto);

	TahasilResponseDTO savePlotAction(TahasilPlotDto tahasilPlotDto);

	TahasilResponseDTO fetchLandTypeDetails();

	TahasilResponseDTO fetchLandUseDetails();

	TahasilResponseDTO fetchLandUseVerificationCompletedDetails(TahasilPlotDto tahasilPlotDto);

	TahasilResponseDTO fetchWebLandUseVerificationCompletedDetails(TahasilPlotDto tahasilPlotDto);

	List<String> viewImage(ImageRequestDto imageRequestDto);

	TahasilResponseDTO tahasilPlotList(PaginationInRegisterDtoResponse res);

}
