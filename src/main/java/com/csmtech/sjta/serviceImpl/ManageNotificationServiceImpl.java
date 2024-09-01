package com.csmtech.sjta.serviceImpl;

import java.io.File;
import java.io.IOException;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.entity.ManageNotification;
import com.csmtech.sjta.repository.ManageNotificationRepository;
import com.csmtech.sjta.repository.NotificationsNativeRepository;
import com.csmtech.sjta.service.ManageNotificationService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Transactional
@Service
@Slf4j
public class ManageNotificationServiceImpl implements ManageNotificationService {
	@Autowired
	private ManageNotificationRepository m_notificationRepository;

	@Autowired
	private NotificationsNativeRepository notiRepo;

	@Autowired
	EntityManager entityManager;

	@Value("${tempUpload.path}")
	private String tempUploadPath;
	@Value("${file.path}")
	private String finalUploadPath;

	Integer parentId = 0;
	Object dynamicValue = null;

	@Override
	public JSONObject save(String data) {
		JSONObject json = new JSONObject();
		try {
			ObjectMapper om = new ObjectMapper();
			ManageNotification notification = om.readValue(data, ManageNotification.class);
			List<String> fileUploadList = new ArrayList<>();
			fileUploadList.add(notification.getFileUploadDocument());
			if (!Objects.isNull(notification.getIntId()) && notification.getIntId() > 0) {
				ManageNotification updateData = notiRepo.updateRecord(notification);
				parentId = updateData.getIntId();
				json.put(CommonConstant.STATUS_KEY, 202);
			} else {
				Integer saveData = notiRepo.saveRecord(notification);
				parentId = saveData;
				json.put(CommonConstant.STATUS_KEY, 200);
			}
			for (String fileUpload : fileUploadList) {
				if (fileUpload != null && (!fileUpload.equals(""))) {
					File f = new File(tempUploadPath + fileUpload);
					if (f.exists()) {
						Path srcPath = Paths.get(tempUploadPath + fileUpload);
						Path destPath = Paths.get(finalUploadPath + "/" + "notification/" + fileUpload);

						try {
							CommonUtil.createDirectories(destPath.getParent());
							CommonUtil.copyAndDeleteFile(srcPath, destPath);
						} catch (IOException e) {
							log.error("Error occurred , while copying file :{}", e.getMessage());
						}
					}

				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			json.put(CommonConstant.STATUS_KEY, 400);
		}
		return json;
	}

	@Override
	public JSONObject getById(Integer id) {
		ManageNotification entity = m_notificationRepository.findByIntId(id);
		return new JSONObject(entity);
	}

	@Override
	public JSONArray getAll(String formParams) {
		JSONObject jsonData = new JSONObject(formParams);
		Integer offset = jsonData.getInt("pageSize") * (jsonData.getInt("pageNo") - 1);
		String title = jsonData.getString("txttitle");
		List<ManageNotification> m_notificationResp = null;
		if (title != null) {
			m_notificationResp = m_notificationRepository.findByTxttitle(jsonData.getString("txttitle"),
					jsonData.getInt("pageSize"), offset);
		} else {
			m_notificationResp = m_notificationRepository
					.findAllByBitDeletedFlagWithPagination(jsonData.getInt("pageSize"), offset);
		}
		return new JSONArray(m_notificationResp);
	}

	@Override
	public JSONObject deleteById(Integer id, Integer updatedby) {
		JSONObject json = new JSONObject();
		try {
			notiRepo.deleteById(id, updatedby);
			json.put(CommonConstant.STATUS_KEY, 200);
		} catch (Exception e) {
			log.error(e.getMessage());
			json.put(CommonConstant.STATUS_KEY, 400);
		}
		return json;
	}

	@Override
	public Integer getTotalAppCount(String formParams) {

		JSONObject jsonData = new JSONObject(formParams);
		Integer offset = jsonData.getInt("pageSize") * (jsonData.getInt("pageNo") - 1);
		String title = jsonData.getString("txttitle");
		Integer count = 0;
		if (title != null) {
			count = m_notificationRepository.getCountOfTitle(jsonData.getString("txttitle"));
		} else {
			count = m_notificationRepository.getCount();
		}

		return count;
	}

	@Override
	public JSONObject unpublishById(Integer id, Integer updatedby) {
		JSONObject json = new JSONObject();
		try {
			notiRepo.unpublishById(id, updatedby);
			json.put(CommonConstant.STATUS_KEY, 200);
		} catch (Exception e) {
			log.error(e.getMessage());
			json.put(CommonConstant.STATUS_KEY, 400);
		}
		return json;
	}

	@Override
	public JSONObject publishById(Integer id, Integer updatedby) {
		JSONObject json = new JSONObject();
		try {
			Integer check = m_notificationRepository.checkPublish(id);
			if (check == 1) {
				notiRepo.unpublishById(id, updatedby);
				json.put(CommonConstant.STATUS_KEY, "success");
				json.put(CommonConstant.MESSAGE_KEY, "Notification Unpublished");
			} else if (check == 0) {
				notiRepo.publishById(id, updatedby);
				json.put(CommonConstant.STATUS_KEY, "fail");
				json.put(CommonConstant.MESSAGE_KEY, "Notification Published");
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			json.put(CommonConstant.STATUS_KEY, 400);
		}
		return json;
	}

	@Override
	public Integer checkPublish(Integer id) {
		return m_notificationRepository.checkPublish(id);
	}

}