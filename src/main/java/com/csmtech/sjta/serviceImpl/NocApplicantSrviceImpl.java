package com.csmtech.sjta.serviceImpl;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.ApplicantDTO;
import com.csmtech.sjta.dto.NocApplicantRequestDTO;
import com.csmtech.sjta.dto.savenocNocPlotDetaisRecxord;
import com.csmtech.sjta.repository.NocapplicantClassRepository;
import com.csmtech.sjta.service.NocapplicantSerice;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NocApplicantSrviceImpl implements NocapplicantSerice {

	@Autowired
	private NocapplicantClassRepository repo;

	@Override
	public Integer saveNocApplicant(NocApplicantRequestDTO reqDto, String filePathcome) {

		Integer saveApplicaintRecord = null;

		String filepath = filePathcome;

		String txtApplicantName = reqDto.getTxtApplicantName();
		String txtFatherHusbandName = reqDto.getTxtFatherHusbandName();
		String txtMobileNo = reqDto.getTxtMobileNo();
		String txtEmail = reqDto.getTxtEmail();
		Integer selDocumentType = reqDto.getSelDocumentType();
		String txtDocumentNo = reqDto.getTxtDocumentNo();
		Integer selCurrState = reqDto.getSelState();
		Integer selCurrDistrict = reqDto.getSelDistrict();
		Integer selCurrBlockULB = reqDto.getSelBlockULB();
		Integer selCurrGPWardNo = reqDto.getSelGPWardNo();
		Integer selCurrVillageLocalAreaName = reqDto.getSelVillageLocalAreaName();
		String txtCurrPoliceStation = reqDto.getTxtPoliceStation();
		String txtCurrPostOffice = reqDto.getTxtPostOffice();
		String txtCurrHabitationStreetNoLandmark = reqDto.getTxtHabitationStreetNoLandmark();
		String txtCurrHouseNo = reqDto.getTxtHouseNo();
		String txtCurrPinCode = reqDto.getTxtPinCode();
		Integer selPerState = reqDto.getSelState17();
		Integer selPerDistrict = reqDto.getSelDistrict18();
		Integer selPerBlockULB = reqDto.getSelBlockULB19();
		Integer selPerGPWARDNumber = reqDto.getSelGPWARDNumber();
		Integer selPerVillageLocalAreaName = reqDto.getSelVillageLocalAreaName21();
		String txtPerPoliceStation = reqDto.getTxtPoliceStation22();
		String txtPerPostOffice = reqDto.getTxtPostOffice23();
		String txtPerHabitationStreetNoLandmark = reqDto.getTxtHabitationStreetNoLandmark24();
		String txtPerHouseNo = reqDto.getTxtHouseNo25();
		String txtPerPinCode = reqDto.getTxtPinCode26();
		Integer createdby = reqDto.getCreatedBy();

		if (reqDto.getApplicantid() == null) {
			saveApplicaintRecord = repo.saveNocApplicant(txtApplicantName, txtFatherHusbandName, txtMobileNo, txtEmail,
					selDocumentType, txtDocumentNo, filepath, selCurrState, selCurrDistrict, selCurrBlockULB,
					selCurrGPWardNo, selCurrVillageLocalAreaName, txtCurrPoliceStation, txtCurrPostOffice,
					txtCurrHabitationStreetNoLandmark, txtCurrHouseNo, txtCurrPinCode, selPerState, selPerDistrict,
					selPerBlockULB, selPerGPWARDNumber, selPerVillageLocalAreaName, txtPerPoliceStation,
					txtPerPostOffice, txtPerHabitationStreetNoLandmark, txtPerHouseNo, txtPerPinCode, createdby);
		}

		else
			saveApplicaintRecord = repo.saveNocApplicantUpdate(txtApplicantName, txtFatherHusbandName, txtMobileNo,
					txtEmail, selDocumentType, txtDocumentNo, filepath, selCurrState, selCurrDistrict, selCurrBlockULB,
					selCurrGPWardNo, selCurrVillageLocalAreaName, txtCurrPoliceStation, txtCurrPostOffice,
					txtCurrHabitationStreetNoLandmark, txtCurrHouseNo, txtCurrPinCode, selPerState, selPerDistrict,
					selPerBlockULB, selPerGPWARDNumber, selPerVillageLocalAreaName, txtPerPoliceStation,
					txtPerPostOffice, txtPerHabitationStreetNoLandmark, txtPerHouseNo, txtPerPinCode,
					reqDto.getCreatedBy(), reqDto.getApplicantid());

		return saveApplicaintRecord;

	}

	@Override
	public Integer saveNocPlot(savenocNocPlotDetaisRecxord dto) {

		Integer insertnocapp = repo.updateNocApplicantPlotDetails(dto);

		return repo.batchInsertNocPlots(dto);
	}

	@Override
	public Integer saveNocDocument(String halPattaValue, String sabikPattaValue, String sabikHalComparValue,
			String setlementYadastValue, String registeredDeedValue, String fileDocumentaryProofofOccupancyifany,
			BigInteger nocAppId, BigInteger createdby) {
		return repo.saveNocDocument(halPattaValue, sabikPattaValue, sabikHalComparValue, setlementYadastValue,
				registeredDeedValue, fileDocumentaryProofofOccupancyifany, nocAppId, createdby);
	}

	@Override
	public ApplicantDTO getApplicantDetailsById(BigInteger id) {
		return repo.getApplicantDetailsById(id);
	}

	@Override
	public savenocNocPlotDetaisRecxord getApplicantPlotDetailsById(BigInteger id) {
		return repo.getApplicantPlotDetailsById(id);
	}

}
