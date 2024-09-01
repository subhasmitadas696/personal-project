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

import com.csmtech.sjta.entity.Noc_plot;
import com.csmtech.sjta.repository.NocPlotRepository;
import com.csmtech.sjta.service.Noc_plotService;
import com.csmtech.sjta.util.CommonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Transactional
@Service
@Slf4j
public class Noc_plotServiceImpl implements Noc_plotService {
	@Autowired
	private NocPlotRepository noc_plotRepository;
	@Autowired
	EntityManager entityManager;

	Integer parentId = 0;
	Object dynamicValue = null;

	@Override
	public JSONObject save(String data) {
		JSONObject json = new JSONObject();
		try {
			ObjectMapper om = new ObjectMapper();
			Noc_plot noc_plot = om.readValue(data, Noc_plot.class);
			List<String> fileUploadList = new ArrayList<>();
			fileUploadList.add(noc_plot.getFileDocumentaryProofofOccupancyifany());
			if (!Objects.isNull(noc_plot.getIntId()) && noc_plot.getIntId() > 0) {
				Noc_plot getEntity = noc_plotRepository.findByIntIdAndBitDeletedFlag(noc_plot.getIntId(), false);
				getEntity.setSelDistrictName(noc_plot.getSelDistrictName());
				getEntity.setSelTehsilName(noc_plot.getSelTehsilName());
				getEntity.setSelMouza(noc_plot.getSelMouza());
				getEntity.setSelKhataNo(noc_plot.getSelKhataNo());
				getEntity.setSelPlotNo(noc_plot.getSelPlotNo());
				getEntity.setTxtTotalRakba(noc_plot.getTxtTotalRakba());
				getEntity.setTxtPurchaseRakba(noc_plot.getTxtPurchaseRakba());
				getEntity.setSelVarieties(noc_plot.getSelVarieties());
				getEntity.setFileDocumentaryProofofOccupancyifany(noc_plot.getFileDocumentaryProofofOccupancyifany());
				getEntity.setTxtFixedPriceperAcreofPurchasedLand(noc_plot.getTxtFixedPriceperAcreofPurchasedLand());
				getEntity.setTxtTotalCostofLandPurchased(noc_plot.getTxtTotalCostofLandPurchased());
				getEntity.setTxtOthers(noc_plot.getTxtOthers());
				Noc_plot updateData = noc_plotRepository.save(getEntity);
				parentId = updateData.getIntId();
				json.put("status", 202);
			} else {
				Noc_plot saveData = noc_plotRepository.save(noc_plot);
				parentId = saveData.getIntId();
				json.put("status", 200);
			}
			for (String fileUpload : fileUploadList) {
				if (!fileUpload.equals("")) {
					File f = new File("src/storage/tempfile/" + fileUpload);
					if (f.exists()) {
						Path srcPath = Paths.get("src/storage/tempfile/" + fileUpload);
						Path destPath = Paths.get("src/storage/plot-details/" + fileUpload);
						CommonUtil.copyAndDeleteFile(srcPath, destPath);
					}
				}
			}
		} catch (Exception e) {
			log.info("Exception inside save() in Noc_plotServiceImpl !!" + e.getMessage());
			json.put("status", 400);
		}
		return json;
	}

	@Override
	public JSONObject getById(Integer id) {
		Noc_plot entity = noc_plotRepository.findByIntIdAndBitDeletedFlag(id, false);
		try {
			dynamicValue = entityManager
					.createNativeQuery(
							"select district_name from m_district where district_id=" + entity.getSelDistrictName())
					.getSingleResult();
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelDistrictNameVal(dynamicValue.toString());
		try {
			dynamicValue = entityManager
					.createNativeQuery("select tehsil_name from m_tehsil where tehsil_id=" + entity.getSelTehsilName())
					.getSingleResult();
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelTehsilNameVal(dynamicValue.toString());
		try {
			dynamicValue = entityManager
					.createNativeQuery("select mouza_name from m_mouza where mouza_id=" + entity.getSelMouza())
					.getSingleResult();
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelMouzaVal(dynamicValue.toString());
		try {
			dynamicValue = entityManager
					.createNativeQuery("select khata_no from m_khata_no where khata_no_id=" + entity.getSelKhataNo())
					.getSingleResult();
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelKhataNoVal(dynamicValue.toString());
		try {
			dynamicValue = entityManager
					.createNativeQuery("select plot_no from m_plot_no where plot_no_id=" + entity.getSelPlotNo())
					.getSingleResult();
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelPlotNoVal(dynamicValue.toString());
		try {
			dynamicValue = entityManager
					.createNativeQuery(
							"select varieties_name from m_varieties where varieties_id=" + entity.getSelVarieties())
					.getSingleResult();
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelVarietiesVal(dynamicValue.toString());

		return new JSONObject(entity);
	}

	@Override
	public JSONArray getAll(String formParams) {
		JSONObject jsonData = new JSONObject(formParams);
		List<Noc_plot> noc_plotResp = noc_plotRepository.findAllByBitDeletedFlag(false);
		for (Noc_plot entity : noc_plotResp) {
			try {
				dynamicValue = entityManager
						.createNativeQuery(
								"select district_name from m_district where district_id=" + entity.getSelDistrictName())
						.getSingleResult();
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelDistrictNameVal(dynamicValue.toString());
			try {
				dynamicValue = entityManager
						.createNativeQuery(
								"select tehsil_name from m_tehsil where tehsil_id=" + entity.getSelTehsilName())
						.getSingleResult();
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelTehsilNameVal(dynamicValue.toString());
			try {
				dynamicValue = entityManager
						.createNativeQuery("select mouza_name from m_mouza where mouza_id=" + entity.getSelMouza())
						.getSingleResult();
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelMouzaVal(dynamicValue.toString());
			try {
				dynamicValue = entityManager
						.createNativeQuery(
								"select khata_no from m_khata_no where khata_no_id=" + entity.getSelKhataNo())
						.getSingleResult();
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelKhataNoVal(dynamicValue.toString());
			try {
				dynamicValue = entityManager
						.createNativeQuery("select plot_no from m_plot_no where plot_no_id=" + entity.getSelPlotNo())
						.getSingleResult();
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelPlotNoVal(dynamicValue.toString());
			try {
				dynamicValue = entityManager
						.createNativeQuery(
								"select varieties_name from m_varieties where varieties_id=" + entity.getSelVarieties())
						.getSingleResult();
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelVarietiesVal(dynamicValue.toString());

		}
		return new JSONArray(noc_plotResp);
	}

	@Override
	public JSONObject deleteById(Integer id) {
		JSONObject json = new JSONObject();
		try {
			Noc_plot entity = noc_plotRepository.findByIntIdAndBitDeletedFlag(id, false);
			entity.setBitDeletedFlag(true);
			noc_plotRepository.save(entity);
			json.put("status", 200);
		} catch (Exception e) {
			log.info("Exception inside deleteById() in Noc_plotServiceImpl !!" + e.getMessage());
			json.put("status", 400);
		}
		return json;
	}

	public static JSONArray fillselDistrictNameList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		String query = "Select district_id,district_name from m_district where deleted_flag = '0' and state_id = 1";
		@SuppressWarnings("unchecked")
		List<Object[]> dataList = em.createNativeQuery(query).getResultList();
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("district_id", data[0]);
			jsonObj.put("district_name", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselTehsilNameList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String val = "";
		val = jsonDepend.get("district_id").toString();
		String query = "Select tehsil_id,tehsil_name from m_tehsil where deleted_flag = '0' and district_id=" + val;
		@SuppressWarnings("unchecked")
		List<Object[]> dataList = em.createNativeQuery(query).getResultList();
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("tehsil_id", data[0]);
			jsonObj.put("tehsil_name", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselMouzaList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String val = "";
		val = jsonDepend.get("tehsil_id").toString();
		String query = "Select mouza_id,mouza_name from m_mouza where deleted_flag = '0' and tehsil_id=" + val;
		@SuppressWarnings("unchecked")
		List<Object[]> dataList = em.createNativeQuery(query).getResultList();
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("mouza_id", data[0]);
			jsonObj.put("mouza_name", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselKhataNoList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String val = "";
		val = jsonDepend.get("mouza_id").toString();
		String query = "Select khata_no_id,khata_no from m_khata_no where deleted_flag = '0' and mouza_id=" + val;
		List<Object[]> dataList = em.createNativeQuery(query).getResultList();
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("khata_no_id", data[0]);
			jsonObj.put("khata_no", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselPlotNoList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String val = "";
		val = jsonDepend.get("khata_no_id").toString();
		String query = "Select plot_no_id,plot_no from m_plot_no where deleted_flag = '0' and khata_no_id=" + val;
		@SuppressWarnings("unchecked")
		List<Object[]> dataList = em.createNativeQuery(query).getResultList();
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("plot_no_id", data[0]);
			jsonObj.put("plot_no", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselVarietiesList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		String query = "Select varieties_id,varieties_name from m_varieties where deleted_flag = '0'";
		@SuppressWarnings("unchecked")
		List<Object[]> dataList = em.createNativeQuery(query).getResultList();
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("varieties_id", data[0]);
			jsonObj.put("varieties_name", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

}