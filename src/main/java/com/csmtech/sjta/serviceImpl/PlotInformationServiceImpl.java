package com.csmtech.sjta.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.PlotInformationDTO;
import com.csmtech.sjta.entity.LandGIS;
import com.csmtech.sjta.entity.PlotBoundary;
import com.csmtech.sjta.entity.Plot_information;
import com.csmtech.sjta.repository.LandGISRepo;
import com.csmtech.sjta.repository.PlotBoundaryRepository;
import com.csmtech.sjta.repository.PlotInfoBoundaryRepository;
import com.csmtech.sjta.repository.Plot_informationRepository;
import com.csmtech.sjta.service.PlotInformationService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@Service
public class PlotInformationServiceImpl implements PlotInformationService {
	@Autowired
	private Plot_informationRepository plot_informationRepository;

	@Autowired
	private LandGISRepo landGISRepo;

	@Autowired
	EntityManager entityManager;
	@Autowired
	private PlotBoundaryRepository plotBoundRepo;

	@Autowired
	private PlotInfoBoundaryRepository plotInfoBoundRepo;

	String parentId = null;
	Object dynamicValue = null;
	private static final Logger logger = LoggerFactory.getLogger(PlotInformationServiceImpl.class);
	JSONObject json = new JSONObject();

	@Override
	public JSONObject save(String data) {
		logger.info("Inside save method of Plot_informationServiceImpl");
		try {
			ObjectMapper om = new ObjectMapper();
			Plot_information plot_information = om.readValue(data, Plot_information.class);

			Plot_information getEntity = plot_informationRepository
					.findByTxtPlotCode(plot_information.getTxtPlotCode());

			if (!Objects.isNull(getEntity)) {

				getEntity.setSelKhatianCode(plot_information.getSelKhatianCode());
				getEntity.setTxtPlotCode(plot_information.getTxtPlotCode());
				getEntity.setTxtPlotNo(plot_information.getTxtPlotNo());
				getEntity.setTxtKissam(plot_information.getTxtKissam());
				getEntity.setTxtAreaAcre(plot_information.getTxtAreaAcre());
				getEntity.setTxtrRemarks(plot_information.getTxtrRemarks());
				Plot_information updateData = plot_informationRepository.save(getEntity);
				parentId = updateData.getTxtPlotCode();
				json.put(CommonConstant.STATUS_KEY, 202);
			} else {
				plot_information.setTxtPlotCode(plot_information.getKhatianPlotCode());
				Plot_information saveData = plot_informationRepository.save(plot_information);
				LandGIS getPlotCode = landGISRepo.findByPlotCode(plot_information.getKhatianPlotCode());
				if (Objects.isNull(getPlotCode)) {
					landGISRepo.savePlotCode(plot_information.getKhatianPlotCode());
				}

				parentId = saveData.getTxtPlotCode();
				json.put(CommonConstant.STATUS_KEY, 200);
			}
			json.put("id", parentId);
		} catch (Exception e) {
			logger.error("Inside save method of Plot_informationServiceImpl, an error occurred: {}", e.getMessage());
			json.put(CommonConstant.STATUS_KEY, 400);
		}
		return json;
	}

	@Override
	public JSONObject getById(String id) {
		logger.info("Inside getById method of Plot_informationServiceImpl");
		Plot_information entity = plot_informationRepository.findByTxtPlotCode(id);
		Object[] dynamicValue = null;

		try {
			dynamicValue = (Object[]) CommonUtil.getDynSingleData(entityManager,
					"select khata_no,village_code from land_bank.khatian_information where khatian_code ='"
							+ entity.getSelKhatianCode() + "'");
		} catch (Exception ex) {
			dynamicValue = new Object[] { "--" };
		}
		entity.setSelKhatianCodeVal((String) dynamicValue[0]);

		try {
			dynamicValue = (Object[]) CommonUtil.getDynSingleData(entityManager,
					"select  village_name, tahasil_code,village_code from land_bank.village_master where village_code='"
							+ (String) dynamicValue[1] + "'");
		} catch (Exception ex) {
			dynamicValue = new Object[] { "--" };
		}
		entity.setSelVillageCodeVal((String) dynamicValue[0]);
		entity.setSelVillageCode((String) dynamicValue[2]);

		try {
			dynamicValue = (Object[]) CommonUtil.getDynSingleData(entityManager,
					"select  tahasil_name, district_code, tahasil_code from land_bank.tahasil_master where tahasil_code='"
							+ (String) dynamicValue[1] + "'");
		} catch (Exception ex) {
			dynamicValue = new Object[] { "--" };
		}
		entity.setSelTahasilCodeVal((String) dynamicValue[0]);
		entity.setSelTahasilCode((String) dynamicValue[2]);

		try {
			dynamicValue = (Object[]) CommonUtil.getDynSingleData(entityManager,
					"select  district_name, state_code, district_code from land_bank.district_master where district_code='"
							+ (String) dynamicValue[1] + "'");
		} catch (Exception ex) {
			dynamicValue = new Object[] { "--" };
		}
		entity.setSelDistrictCodeVal((String) dynamicValue[0]);
		entity.setSelDistrictCode((String) dynamicValue[2]);

		try {
			dynamicValue = (Object[]) CommonUtil.getDynSingleData(entityManager,
					"select  state_name, country_code, state_code from land_bank.state_master where state_code='"
							+ (String) dynamicValue[1] + "'");
		} catch (Exception ex) {
			dynamicValue = new Object[] { "--" };
		}
		entity.setSelStateCodeVal((String) dynamicValue[0]);
		entity.setSelStateCode((String) dynamicValue[2]);

		try {
			dynamicValue = (Object[]) CommonUtil.getDynSingleData(entityManager,
					"select  country_name, country_code from land_bank.country_master where country_code='"
							+ (String) dynamicValue[1] + "'");
		} catch (Exception ex) {
			dynamicValue = new Object[] { "--" };
		}
		entity.setSelCountryCodeVal((String) dynamicValue[0]);
		entity.setSelCountryCode((String) dynamicValue[1]);
		return new JSONObject(entity);
	}

	@Override
	public JSONObject getAll(String formParams) {
		logger.info("Inside getAll method of Plot_informationServiceImpl");
		JSONObject jsonData = new JSONObject(formParams);
		String selKhatianCode = "0";
		String txtPlotCode = "0";
		String txtPlotNo = "0";
		List<Plot_information> plot_informationResp = null;
		if (jsonData.has("selKhatianCode") && !jsonData.isNull("selKhatianCode")
				&& !jsonData.getString("selKhatianCode").equals("")) {
			selKhatianCode = jsonData.getString("selKhatianCode");
			plot_informationResp = plot_informationRepository.findBySelKhatianCode(selKhatianCode);
		}
		if (jsonData.has("txtPlotNo") && !jsonData.isNull("txtPlotNo") && !jsonData.getString("txtPlotNo").equals("")) {
			txtPlotNo = jsonData.getString("txtPlotNo");
			plot_informationResp = plot_informationRepository.findByTxtPlotNo(txtPlotNo, selKhatianCode);
		}
		Integer totalDataPresent = plot_informationRepository.countByAll();
		Pageable pageRequest = PageRequest.of(jsonData.has("pageNo") ? jsonData.getInt("pageNo") - 1 : 0,
				jsonData.has("size") ? jsonData.getInt("size") : totalDataPresent);
		plot_informationResp = plot_informationRepository.findAll(selKhatianCode, txtPlotCode, txtPlotNo);
		Object[] dynamicValue = null;

		for (Plot_information entity : plot_informationResp) {
			try {
				dynamicValue = (Object[]) CommonUtil.getDynSingleData(entityManager,
						"select khata_no,village_code from land_bank.khatian_information where khatian_code ='"
								+ entity.getSelKhatianCode() + "'");
			} catch (Exception ex) {
				dynamicValue = new Object[] { "--" };
			}
			entity.setSelKhatianCodeVal((String) dynamicValue[0]);

			try {
				dynamicValue = (Object[]) CommonUtil.getDynSingleData(entityManager,
						"select  village_name, tahasil_code,village_code from land_bank.village_master where village_code='"
								+ (String) dynamicValue[1] + "'");
			} catch (Exception ex) {
				dynamicValue = new Object[] { "--" };
			}
			entity.setSelVillageCodeVal((String) dynamicValue[0]);
			entity.setSelVillageCode((String) dynamicValue[2]);

			try {
				dynamicValue = (Object[]) CommonUtil.getDynSingleData(entityManager,
						"select  tahasil_name, district_code, tahasil_code from land_bank.tahasil_master where tahasil_code='"
								+ (String) dynamicValue[1] + "'");
			} catch (Exception ex) {
				dynamicValue = new Object[] { "--" };
			}
			entity.setSelTahasilCodeVal((String) dynamicValue[0]);
			entity.setSelTahasilCode((String) dynamicValue[2]);

			try {
				dynamicValue = (Object[]) CommonUtil.getDynSingleData(entityManager,
						"select  district_name, state_code, district_code from land_bank.district_master where district_code='"
								+ (String) dynamicValue[1] + "'");
			} catch (Exception ex) {
				dynamicValue = new Object[] { "--" };
			}
			entity.setSelDistrictCodeVal((String) dynamicValue[0]);
			entity.setSelDistrictCode((String) dynamicValue[2]);

			try {
				dynamicValue = (Object[]) CommonUtil.getDynSingleData(entityManager,
						"select  state_name, country_code, state_code from land_bank.state_master where state_code='"
								+ (String) dynamicValue[1] + "'");
			} catch (Exception ex) {
				dynamicValue = new Object[] { "--" };
			}
			entity.setSelStateCodeVal((String) dynamicValue[0]);
			entity.setSelStateCode((String) dynamicValue[2]);

			try {
				dynamicValue = (Object[]) CommonUtil.getDynSingleData(entityManager,
						"select  country_name, country_code from land_bank.country_master where country_code='"
								+ (String) dynamicValue[1] + "'");
			} catch (Exception ex) {
				dynamicValue = new Object[] { "--" };
			}
			entity.setSelCountryCodeVal((String) dynamicValue[0]);
			entity.setSelCountryCode((String) dynamicValue[1]);

		}
		json.put(CommonConstant.STATUS_KEY, 200);
		json.put(CommonConstant.RESULT, new JSONArray(plot_informationResp));
		json.put("count", totalDataPresent);
		return json;
	}

	public static JSONArray fillselDistrictCodeList(EntityManager em, String jsonVal) {
		logger.info("Inside fillselDistrictCodeList method of Tahasil_masterServiceImpl");
		JSONArray mainJSONFile = new JSONArray();
		String query = "Select district_code,district_name from land_bank.district_master where state_code = 'OD' Order By district_name";
		List<Object[]> dataList = CommonUtil.getDynResultList(em, query);
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("district_code", data[0]);
			jsonObj.put("district_name", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselTahasilCodeList(EntityManager em, String jsonVal) {
		logger.info("Inside fillselTahasilCodeList method of Village_masterServiceImpl");
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String val = jsonDepend.get("district_code").toString();
		String query = "Select tahasil_code,tahasil_name from land_bank.tahasil_master where district_code = '" + val
				+ "' Order By tahasil_name";
		List<Object[]> dataList = CommonUtil.getDynResultList(em, query);
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("tahasil_code", data[0]);
			jsonObj.put("tahasil_name", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselVillageCodeList(EntityManager em, String jsonVal) {
		logger.info("Inside fillselVillageCodeList method of Khatian_informationServiceImpl");
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String val = jsonDepend.get("tahasil_code").toString();
		String query = "Select village_code,village_name from land_bank.village_master where tahasil_code = '" + val
				+ "' Order By village_name";
		List<Object[]> dataList = CommonUtil.getDynResultList(em, query);
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("village_code", data[0]);
			jsonObj.put("village_name", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselKhatianCodeList(EntityManager em, String jsonVal) {
		logger.info("Inside fillselKhatianCodeList method of Plot_informationServiceImpl");
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String val = jsonDepend.get("village_code").toString();
		String query = "Select khatian_code,khata_no from land_bank.khatian_information where village_code = '" + val
				+ "' Order By khata_no";
		List<Object[]> dataList = CommonUtil.getDynResultList(em, query);
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("khatian_code", data[0]);
			jsonObj.put("khata_no", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	@Override
	public JSONObject viewAll(String villageCode, String khataNo) {
		List<Object[]> plotBoundariesResp = plotBoundRepo.viewAll(villageCode, khataNo);
		List<PlotBoundary> plotBoundaries = new ArrayList<>();

		for (Object[] result : plotBoundariesResp) {
			PlotBoundary plotBoundary = new PlotBoundary();
			plotBoundary.setPlotCode((String) result[0]);
			plotBoundary.setExtent((String) result[1]);
			plotBoundaries.add(plotBoundary);
		}

		json.put(CommonConstant.STATUS_KEY, 200);
		json.put(CommonConstant.MESSAGE_KEY, CommonConstant.SUCCESS);
		json.put(CommonConstant.RESULT, new JSONArray(plotBoundaries));

		return json;

	}

	@Override
	public JSONObject plotInfoForTableView(String villageCode) {
		List<Object[]> resultList = plotInfoBoundRepo.plotinfoForTableView(villageCode);
		List<PlotInformationDTO> plotinfoBoundaries = new ArrayList<>();
		JSONObject object = new JSONObject();
		if (resultList.size() > 0 && resultList != null) {
			for (Object[] result : resultList) {
				PlotInformationDTO dto = new PlotInformationDTO();
				dto.setPlotCode(result[0] != null ? result[0].toString() : "");
				dto.setPlotNo(result[1] != null ? result[1].toString() : "");
				dto.setVillageCode(result[2] != null ? result[2].toString() : "");

				dto.setKhataNo(result[3] != null ? result[3].toString() : "");
				dto.setAreaAcre(result[4] != null ? result[4].toString() : "");
				dto.setKissam(result[5] != null ? result[5].toString() : "");
				dto.setExtent(result[6] != null ? result[6].toString() : "");
				dto.setDigitalFile(result[7] != null ? result[7].toString() : "");
				dto.setVillageName("");
				dto.setAvailableSts("");

				plotinfoBoundaries.add(dto);
			}
			object.put(CommonConstant.STATUS_KEY, 200);
			object.put(CommonConstant.MESSAGE_KEY, "success ,data fetched for plot information");
			object.put("data", plotinfoBoundaries);
		} else {
			object.put(CommonConstant.STATUS_KEY, 200);
			object.put(CommonConstant.MESSAGE_KEY,
					"data is not present for the respective village code." + villageCode);
		}
		return object;
	}

	@Override
	public JSONObject getKissam() {

		List<String> kissam = plot_informationRepository.getKissam();
		json.put(CommonConstant.STATUS_KEY, 200);
		json.put(CommonConstant.MESSAGE_KEY, CommonConstant.SUCCESS);
		json.put(CommonConstant.RESULT, kissam);

		return json;
	}

	public static JSONArray fillselPlotNoList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String val = "";
		val = jsonDepend.get("khatian_code").toString();
		String query = "SELECT DISTINCT pi.plot_code, pi.plot_no FROM land_bank.plot_information AS pi "
				+ "JOIN land_schedule AS ls ON pi.plot_code = ls.plot_code "
				+ "JOIN land_application AS la ON la.land_application_id = ls.land_application_id "
				+ "WHERE la.khatian_code = :khatianCode ";

		@SuppressWarnings("unchecked")
		List<Object[]> dataList = em.createNativeQuery(query).setParameter("khatianCode", val).getResultList();
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("plot_code", data[0]);
			jsonObj.put("plot_no", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

}