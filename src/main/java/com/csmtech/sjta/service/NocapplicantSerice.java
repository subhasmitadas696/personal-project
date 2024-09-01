package com.csmtech.sjta.service;

import java.math.BigInteger;

import org.springframework.web.multipart.MultipartFile;

import com.csmtech.sjta.dto.ApplicantDTO;
import com.csmtech.sjta.dto.ApplicantPlotDTO;
import com.csmtech.sjta.dto.NocApplicantRequestDTO;
import com.csmtech.sjta.dto.savenocNocPlotDetaisRecxord;

public interface NocapplicantSerice {

	public Integer saveNocApplicant(NocApplicantRequestDTO reqDto, String filePath);

	public Integer saveNocPlot(savenocNocPlotDetaisRecxord dto);

	public Integer saveNocDocument(String halPattaValue, String sabikPattaValue, String sabikHalComparValue,
			String setlementYadastValue, String registeredDeedValue,String fileDocumentaryProofofOccupancyifany, BigInteger nocAppId, BigInteger createdby);
	
	public ApplicantDTO getApplicantDetailsById(BigInteger id);
	
	public savenocNocPlotDetaisRecxord getApplicantPlotDetailsById(BigInteger id);

}
