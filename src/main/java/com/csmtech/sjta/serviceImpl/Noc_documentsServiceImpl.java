package com.csmtech.sjta.serviceImpl;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.entity.Noc_documents;
import com.csmtech.sjta.repository.NocDocumentsRepository;
import com.csmtech.sjta.service.Noc_documentsService;
import com.csmtech.sjta.util.CommonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Transactional
@Service
@Slf4j
public class Noc_documentsServiceImpl implements Noc_documentsService {
	@Autowired
	private NocDocumentsRepository nocDocumentsRepository;
	@Autowired
	EntityManager entityManager;

	Integer parentId = 0;
	Object dynamicValue = null;

	@Override
	public JSONObject save(String data) {
		JSONObject json = new JSONObject();
		try {
			ObjectMapper om = new ObjectMapper();
			Noc_documents nocDocuments = om.readValue(data, Noc_documents.class);
			List<String> fileUploadList = new ArrayList<>();
			fileUploadList.add(nocDocuments.getFileHalPatta());
			fileUploadList.add(nocDocuments.getFileSabikPatta());
			fileUploadList.add(nocDocuments.getFileSabikHalComparisonStatement());
			fileUploadList.add(nocDocuments.getFileSettlementYaddast());
			fileUploadList.add(nocDocuments.getFileRegisteredDeed());
			if (!Objects.isNull(nocDocuments.getIntId()) && nocDocuments.getIntId() > 0) {
				Noc_documents getEntity = nocDocumentsRepository.findByIntIdAndBitDeletedFlag(nocDocuments.getIntId(),
						false);
				getEntity.setFileHalPatta(nocDocuments.getFileHalPatta());
				getEntity.setFileSabikPatta(nocDocuments.getFileSabikPatta());
				getEntity.setFileSabikHalComparisonStatement(nocDocuments.getFileSabikHalComparisonStatement());
				getEntity.setFileSettlementYaddast(nocDocuments.getFileSettlementYaddast());
				getEntity.setFileRegisteredDeed(nocDocuments.getFileRegisteredDeed());
				Noc_documents updateData = nocDocumentsRepository.save(getEntity);
				parentId = updateData.getIntId();
				json.put("status", 202);
			} else {
				Noc_documents saveData = nocDocumentsRepository.save(nocDocuments);
				parentId = saveData.getIntId();
				json.put("status", 200);
			}
			for (String fileUpload : fileUploadList) {
				if (!fileUpload.equals("")) {
					File f = new File("src/storage/tempfile/" + fileUpload);
					if (f.exists()) {
						Path srcPath = Paths.get("src/storage/tempfile/" + fileUpload);
						Path destPath = Paths.get("src/storage/upload-documents/" + fileUpload);
						CommonUtil.copyAndDeleteFile(srcPath, destPath);
					}
				}
			}
		} catch (Exception e) {
			log.info("Exception inside save() in Noc_documentsServiceImpl !!" + e.getMessage());
			json.put("status", 400);
		}
		return json;
	}

	@Override
	public JSONObject getById(Integer id) {
		Noc_documents entity = nocDocumentsRepository.findByIntIdAndBitDeletedFlag(id, false);

		return new JSONObject(entity);
	}

	@Override
	public JSONArray getAll(String formParams) {
		List<Noc_documents> noc_documentsResp = nocDocumentsRepository.findAllByBitDeletedFlag(false);
		return new JSONArray(noc_documentsResp);
	}

	@Override
	public JSONObject deleteById(Integer id) {
		JSONObject json = new JSONObject();
		try {
			Noc_documents entity = nocDocumentsRepository.findByIntIdAndBitDeletedFlag(id, false);
			entity.setBitDeletedFlag(true);
			nocDocumentsRepository.save(entity);
			json.put("status", 200);
		} catch (Exception e) {
			log.info("Exception inside deleteById() in Noc_documentsServiceImpl !!" + e.getMessage());
			json.put("status", 400);
		}
		return json;
	}

}