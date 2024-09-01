package com.csmtech.sjta.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.AddMoreUploadDocumentsDTO;
import com.csmtech.sjta.entity.Land_documents;
import com.csmtech.sjta.mobile.dto.ApplicationFlowDto;
import com.csmtech.sjta.mobile.entity.ApplicationFlowEntity;
import com.csmtech.sjta.mobile.repository.ApplicationFlowRepository;
import com.csmtech.sjta.mobile.service.CommonService;
import com.csmtech.sjta.repository.LandApplicantNativeRepository;
import com.csmtech.sjta.repository.LandDocumentsJPARepository;
import com.csmtech.sjta.repository.PaginationInRegisterClassRepository;
import com.csmtech.sjta.service.Land_documentsService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Transactional
@Service
@Slf4j
public class Land_documentsServiceImpl implements Land_documentsService {
	@Autowired
	@PersistenceContext
	EntityManager em;

	@Autowired
	private LandDocumentsJPARepository land_documentsRepository;

	@Autowired
	private LandApplicantNativeRepository repoapplicant;

	@Autowired
	private PaginationInRegisterClassRepository lastrepo;

	@Autowired
	private LandApplicantNativeRepository reponative;
	
	@Autowired
	ApplicationFlowRepository applicationFlowRepo;
	

	@Autowired
	CommonService commonService;

	Integer parentId = 0;
	Object dynamicValue = null;

	@Value("${tempUpload.path}")
	private String tempUploadPath;
	@Value("${file.path}")
	private String finalUploadPath;

	@Override
	public JSONObject save(String data) {
		JSONObject json = new JSONObject();
		try {
			ObjectMapper om = new ObjectMapper();
			Land_documents land_documents = om.readValue(data, Land_documents.class);
			Land_documents getlist = land_documents;
			List<AddMoreUploadDocumentsDTO> fileUploadList = getlist.getAddMoreUploadDocuments();
			List<String> amfileDocumentNames = new ArrayList<>();
			Integer myInteger = land_documents.getIntParentId();
			String myString = myInteger.toString();
			for (AddMoreUploadDocumentsDTO document : fileUploadList) {
				String amfileDocument = document.getAmfileDocument();
				amfileDocumentNames.add(amfileDocument);
			}

			try {
				for (String fileUpload : amfileDocumentNames) {
					if (!fileUpload.equals("")) {
						File src = new File(tempUploadPath + fileUpload);
						if (src.exists()) {
							log.info(" myString : " + myString);
							File dest = new File(finalUploadPath + "/" + myString + "_" + fileUpload);
							try {
								Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
								Files.delete(src.toPath());
							} catch (IOException e) {
								log.info("inside exception  in Land_documentsServiceImpl !!" + e.getMessage());
							}
						} else {
							log.info("File does not exist at   !! ");
						}
					}
				}
			} catch (Exception e) {
				log.info("inside exception  in Land_documentsServiceImpl !!" + e.getMessage());
			}

			repoapplicant.deleteLandDocument(land_documents.getIntParentId());
			List<AddMoreUploadDocumentsDTO> documentList = land_documents.getAddMoreUploadDocuments();
			log.info("list : " + documentList);
			for (AddMoreUploadDocumentsDTO document : documentList) {
				Integer count = repoapplicant.insertLandDocument(myString + "_" + document.getAmfileDocument(),
						document.getAmtxtDocumentName(), land_documents.getIntCreatedBy(),
						land_documents.getIntParentId(), document.getDocumentNumber());
			}
			String rr = reponative.getAppStatusForLandApplicationId(land_documents.getIntParentId());
			if (rr.equalsIgnoreCase("4")) {
				Integer againApprovde = reponative.getResubmitApproval(land_documents.getIntParentId());
				log.info("Again Resubmit" + againApprovde);
			} else {
				Integer updateAppStatus = repoapplicant.updateAppStatusForLandApplicationId(
						land_documents.getIntParentId(), land_documents.getAppStage());
				log.info("updateAppStatus :: " + updateAppStatus);
			}

			String rrRet = reponative.getAppStatusForLandApplicationId(land_documents.getIntParentId());
			
			BigInteger count;
			List<ApplicationFlowEntity> entity = applicationFlowRepo.findByLandApplicationId(BigInteger.valueOf(land_documents.getIntParentId()));
			
			if(entity.size() > 0) {
			// added for inserting in db only(application flow) Land Application Applied
				ApplicationFlowDto dto = new ApplicationFlowDto();
				dto.setLandApplicationId(BigInteger.valueOf(land_documents.getIntParentId()));
				dto.setApplicationFlowId(BigInteger.valueOf(6));
				dto.setActionDateTime(new Date());
				dto.setActionRoleId(BigInteger.valueOf(2));
				commonService.saveApplicationFlow(dto);
//			}else {
//				// added for inserting in db only(application flow) Application Resubmited by
//				// Applicant
//				ApplicationFlowDto dto = new ApplicationFlowDto();
//				dto.setLandApplicationId(applicantId);
//				dto.setApplicationFlowId(BigInteger.valueOf(6));
//				dto.setActionDateTime(new Date());
//				dto.setActionRoleId(null);
//				commonService.saveApplicationFlow(dto);

			}

//			// added for inserting in db only(application flow) Application Resubmited by
//			// Applicant
//			ApplicationFlowDto dto = new ApplicationFlowDto();
//			dto.setLandApplicationId(BigInteger.valueOf(land_documents.getIntParentId()));
//			dto.setApplicationFlowId(BigInteger.valueOf(6));
//			dto.setActionDateTime(new Date());
//			dto.setActionRoleId(null);
//			commonService.saveApplicationFlow(dto);

			json.put("status", 200);
			json.put("appStatus", rrRet);

		} catch (Exception e) {
			log.error(e.getMessage());
			json.put("status", 400);
		}
		return json;
	}

	@Override
	public JSONObject getById(Integer id) {
		Land_documents entity = land_documentsRepository.findByIntIdAndBitDeletedFlag(id, false);
		List<Land_documents> land_documentsList = land_documentsRepository
				.findByIntParentIdAndBitDeletedFlag(entity.getIntId());

		return new JSONObject(entity);
	}

	@Override
	public JSONArray getAll(String formParams) {
		JSONObject jsonData = new JSONObject(formParams);
		List<Land_documents> land_documentsResp = land_documentsRepository.findAllByBitDeletedFlag(false);
		return new JSONArray(land_documentsResp);
	}

	@Override
	public JSONObject deleteById(Integer id) {
		JSONObject json = new JSONObject();
		try {
			Land_documents entity = land_documentsRepository.findByIntIdAndBitDeletedFlag(id, false);
			entity.setBitDeletedFlag(true);
			land_documentsRepository.save(entity);
			List<Land_documents> land_documentsList = land_documentsRepository.findByIntParentIdAndBitDeletedFlag(id);
			land_documentsList.forEach(t -> t.setBitDeletedFlag(true));
			land_documentsRepository.saveAll(land_documentsList);
			json.put("status", 200);
		} catch (Exception e) {
			log.error(e.getMessage());
			json.put("status", 400);
		}
		return json;
	}

	@Override
	public JSONObject getByIntParentId(Integer intParentId) {

		JSONObject jsonobject = new JSONObject();
		List<Land_documents> entity = land_documentsRepository.getByparentIdd(intParentId);

		if (entity != null) {
			List<Land_documents> land_documentsList = land_documentsRepository
					.findByIntParentIdAndBitDeletedFlag(intParentId);

			jsonobject.put("intId", intParentId);
			jsonobject.put("addMoreUploadDocuments", land_documentsList);
		}

		return jsonobject;
	}

	@Override
	@Transactional
	public String getCurrentStage(Integer landApplicationId) {
		String nativeQuery = "SELECT app_status FROM land_application WHERE land_application_id = :applicantId";
		Object appStage = em.createNativeQuery(nativeQuery).setParameter("applicantId", landApplicationId)
				.getSingleResult();
		if (appStage != null) {
			return appStage.toString();
		}
		return "";
	}

}