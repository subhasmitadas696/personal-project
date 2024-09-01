package com.csmtech.sjta.mobile.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.LandApplicantDTO;
import com.csmtech.sjta.dto.PlotValuationDTO;
import com.csmtech.sjta.mobile.dto.LandVerificationResponseDto;
import com.csmtech.sjta.mobile.dto.PlotLandInspectionDto;
import com.csmtech.sjta.mobile.dto.TahasilResponseDTO;

@Service
public interface LandOfficerVerificationService {
	
	public LandVerificationResponseDto assignToCO(PlotLandInspectionDto landDto);

	public LandVerificationResponseDto coSubmitInspection(PlotLandInspectionDto landDto);

	public JSONObject fetchDetailsById(LandApplicantDTO landDto);

	public LandVerificationResponseDto fetchPendingAndCompleteCount();

	public LandVerificationResponseDto fetchPendingRecords();

	public LandVerificationResponseDto fetchCompleteRecords();

	public LandVerificationResponseDto viewInspectionDetails(PlotLandInspectionDto landDto);

	public LandVerificationResponseDto getVillageInformation(PlotLandInspectionDto landDto);

	public LandVerificationResponseDto fetchPendingRecordsByVillage(PlotLandInspectionDto landDto);

	public LandVerificationResponseDto saveTahasildarPlotAction(PlotLandInspectionDto landDto);

	public LandVerificationResponseDto fetchTahasildarPendingRecords(PlotLandInspectionDto landDto);

	public LandVerificationResponseDto fetchTahasildarCompleteRecords(PlotLandInspectionDto landDto);

	public LandVerificationResponseDto getVillageInformationForTahasil(PlotLandInspectionDto landDto);

	public LandVerificationResponseDto fetchTahasilPendingAndCompleteCount(PlotLandInspectionDto landDto);

	public LandVerificationResponseDto fetchTahasilPendingRecordsByVillage(PlotLandInspectionDto landDto);

	public LandVerificationResponseDto viewTahasildarInspectionDetails(PlotLandInspectionDto landDto);

	public LandVerificationResponseDto viewTahasildarInspectionDetailsWeb(PlotLandInspectionDto landDto);

	public LandVerificationResponseDto viewTahasildarValuationForm(PlotLandInspectionDto landDto);

	public LandVerificationResponseDto viewTahasildarValuationFormByplotCode(PlotLandInspectionDto landDto);

	public LandVerificationResponseDto assignToCONr(PlotLandInspectionDto landDto);


}
