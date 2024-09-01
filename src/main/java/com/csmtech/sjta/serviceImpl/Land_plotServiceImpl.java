package com.csmtech.sjta.serviceImpl;

import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.entity.Land_applicant;
import com.csmtech.sjta.entity.Land_plot;
import com.csmtech.sjta.repository.LandApplicantJPARepository;
import com.csmtech.sjta.repository.LandApplicantNativeRepository;
import com.csmtech.sjta.repository.LandPlotJPARepository;
import com.csmtech.sjta.service.Land_plotService;
import com.csmtech.sjta.util.CommonConstant;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Transactional
@Service
@Slf4j
public class Land_plotServiceImpl implements Land_plotService {
	@Autowired
	private LandPlotJPARepository land_plotRepository;
	@Autowired
	private LandApplicantJPARepository Land_applicantRepository;
	@Autowired
	EntityManager entityManager;

	@Autowired
	private LandApplicantNativeRepository reponative;

	Integer parentId;
	Object dynamicValue = null;

	@Override
	public JSONObject save(String data) {
		JSONObject json = new JSONObject();
		try {
			ObjectMapper om = new ObjectMapper();
			Land_plot land_plot = om.readValue(data, Land_plot.class);
			Land_plot getEntity = land_plotRepository
					.findByIntLandApplicantIdAndBitDeletedFlag(land_plot.getIntLandApplicantId(), false);
			Integer plot = land_plot.getIntLandApplicantId();
			if (!Objects.isNull(land_plot.getIntLandApplicantId()) && plot.compareTo(plot) > 0 && getEntity != null) {
				getEntity.setSelPlotNo(land_plot.getSelPlotNo());
				getEntity.setTxtTotalRakba(land_plot.getTxtTotalRakba());
				getEntity.setTxtPurchaseRakba(land_plot.getTxtPurchaseRakba());
				String rr = reponative.getAppStatusForLandApplicationId(land_plot.getIntLandApplicantId());
				if (rr.equalsIgnoreCase("4")) {
				} else {
					Land_plot updateData = land_plotRepository.save(getEntity);
				}
				json.put(CommonConstant.STATUS_KEY, 202);
			} else {
				Land_plot saveData = land_plotRepository.save(land_plot);
				parentId = saveData.getIntLandApplicantId();
				json.put(CommonConstant.STATUS_KEY, 200);
			}

		} catch (Exception e) {
			log.error(e.getMessage());
			json.put(CommonConstant.STATUS_KEY, 400);
		}
		return json;
	}

	@Override
	public JSONObject getById(Integer id) {
		Land_plot entity = land_plotRepository.findByIntIdAndBitDeletedFlag(id, false);
		Land_applicant entity1 = Land_applicantRepository.findByIntId(id);
		try {
			dynamicValue = entityManager
					.createNativeQuery(
							"select district_name from land_bank.district_master where district_code= :districtCode")
					.setParameter("districtCode", entity1.getSelDistrictName()).getSingleResult();
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity1.setSelDistrictNameVal(dynamicValue.toString());
		try {
			dynamicValue = entityManager
					.createNativeQuery(
							"select tahasil_name from land_bank.tahasil_master where tahasil_code= :tahasilCode")
					.setParameter("tahasilCode", entity1.getSelTehsilName()).getSingleResult();
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity1.setSelTehsilNameVal(dynamicValue.toString());
		try {
			dynamicValue = entityManager
					.createNativeQuery(
							"select village_name from land_bank.village_master where village_code= :villageCode")
					.setParameter("villageCode", entity1.getSelMouza()).getSingleResult();
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity1.setSelMouzaVal(dynamicValue.toString());
		try {
			dynamicValue = entityManager
					.createNativeQuery(
							"select khata_no from land_bank.khatian_information where khatian_code= :khatianCode")
					.setParameter("khatianCode", entity1.getSelKhataNo()).getSingleResult();
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity1.setSelKhataNoVal(dynamicValue.toString());
		try {
			dynamicValue = entityManager
					.createNativeQuery("select plot_no from land_bank.plot_information where plot_code= :plotCode")
					.setParameter("plotCode", entity.getSelPlotNo()).getSingleResult();
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelPlotNoVal(dynamicValue.toString());

		return new JSONObject(entity);
	}

	@Override
	public JSONArray getAll(String formParams) {
		JSONObject jsonData = new JSONObject(formParams);
		List<Land_plot> land_plotResp = land_plotRepository.findAllByBitDeletedFlag();

		for (Land_plot entity : land_plotResp) {
			Land_applicant entity1 = Land_applicantRepository.findByIntId(entity.getIntLandApplicantId());
			try {
				dynamicValue = entityManager.createNativeQuery(
						"select district_name from land_bank.district_master where district_code= :districtCode")
						.setParameter("districtCode", entity1.getSelDistrictName()).getSingleResult();
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity1.setSelDistrictNameVal(dynamicValue.toString());
			try {
				dynamicValue = entityManager
						.createNativeQuery(
								"select tahasil_name from land_bank.tahasil_master where tahasil_code= :tahasilCode")
						.setParameter("tahasilCode", entity1.getSelTehsilName()).getSingleResult();
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity1.setSelTehsilNameVal(dynamicValue.toString());
			try {
				dynamicValue = entityManager
						.createNativeQuery(
								"select village_name from land_bank.village_master where village_code= :villageCode")
						.setParameter("villageCode", entity1.getSelMouza()).getSingleResult();
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity1.setSelMouzaVal(dynamicValue.toString());
			try {
				dynamicValue = entityManager
						.createNativeQuery(
								"select khata_no from land_bank.khatian_information where khatian_code= :khatianCode")
						.setParameter("khatianCode", entity1.getSelKhataNo()).getSingleResult();
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity1.setSelKhataNoVal(dynamicValue.toString());
			try {
				dynamicValue = entityManager
						.createNativeQuery("select plot_no from land_bank.plot_information where plot_code= :plotCode")
						.setParameter("plotCode", entity.getSelPlotNo()).getSingleResult();
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelPlotNoVal(dynamicValue.toString());
		}
		return new JSONArray(land_plotResp);
	}

	@Override
	public JSONObject deleteById(Integer id) {
		JSONObject json = new JSONObject();
		try {
			Land_plot entity = land_plotRepository.findByIntIdAndBitDeletedFlag(id, false);
			entity.setBitDeletedFlag(true);
			land_plotRepository.save(entity);
			json.put(CommonConstant.STATUS_KEY, 200);
		} catch (Exception e) {
			log.error(e.getMessage());
			json.put(CommonConstant.STATUS_KEY, 400);
		}
		return json;
	}

	public static JSONArray fillselDistrictNameList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();

		String query = " SELECT DISTINCT dm.district_code AS district_id, dm.district_name AS district_name\r\n"
				+ "FROM land_bank.district_master dm\r\n"
				+ "JOIN land_bank.tahasil_master ki ON ki.district_code = dm.district_code\r\n"
				+ "JOIN land_bank.village_master kii ON ki.tahasil_code = kii.tahasil_code\r\n"
				+ "JOIN land_bank.khatian_information kiii ON kii.village_code = kiii.village_code\r\n"
				+ "WHERE dm.state_code = 'OD'\r\n" + "ORDER BY district_name;\r\n" + " ";

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
		String val = jsonDepend.get("district_id").toString();
		String query = " select DISTINCT (ki.tahasil_code) as tehsil_id,(ki.tahasil_name) as\r\n"
				+ " tehsil_name from land_bank.tahasil_master ki\r\n"
				+ " JOIN land_bank.village_master kii ON ki.tahasil_code = kii.tahasil_code\r\n"
				+ " JOIN land_bank.khatian_information kiii ON kii.village_code = kiii.village_code  "
				+ " where  district_code= '" + val + "' order by tehsil_name";
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

	// add here map extension
	public static JSONArray fillselMouzaList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String val = "";
		val = jsonDepend.get("tehsil_id").toString();
		String query = "  Select DISTINCT (vm.village_code) as mouza_id,(vm.village_name) as mouza_name,\r\n"
				+ " CAST(ST_extent(ST_Transform(vb.geom, 3857)) AS character varying) AS extent\r\n"
				+ " from land_bank.village_master vm  \r\n"
				+ " left join land_bank.village_boundary vb on(vm.village_code=vb.village_code) \r\n"
				+ " JOIN land_bank.khatian_information kiii ON (vm.village_code = kiii.village_code)  "
				+ "where  vm.tahasil_code='" + val + "'  group by vm.village_code order by vm.village_name";
		@SuppressWarnings("unchecked")
		List<Object[]> dataList = em.createNativeQuery(query).getResultList();
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("mouza_id", data[0]);
			jsonObj.put("mouza_name", data[1]);
			jsonObj.put("extent", data[2]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselKhataNoList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String val = "";
		val = jsonDepend.get("mouza_id").toString();
		String query = "Select (khatian_code) as khata_no_id,(khata_no) as khata_no "
				+ " from land_bank.khatian_information where village_code='" + val + "'";
		@SuppressWarnings("unchecked")
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
		Integer seVal = 0;
		val = jsonDepend.get("khata_no_id").toString();
		seVal = jsonDepend.getInt("createdBy");
		String query = "SELECT DISTINCT pi.plot_code AS plot_no_id, pi.plot_no AS plot_no, pi.kissam,"
				+ " pi.area_acre, pi.khatian_code FROM land_bank.plot_information pi LEFT JOIN "
				+ " land_schedule ls USING(plot_code) LEFT JOIN "
				+ " land_application la USING(land_application_id) WHERE pi.khatian_code ='" + val + "' AND "
				+ " pi.plot_code NOT IN (SELECT DISTINCT ls.plot_code FROM land_schedule ls WHERE"
				+ " ls.created_by = '" + seVal + "') AND pi.plot_code NOT IN "
				+ " (SELECT msa.plot_no from application.meeting_schedule_applicant msa " + ")";

		@SuppressWarnings("unchecked")
		List<Object[]> dataList = em.createNativeQuery(query).getResultList();
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("plot_code", data[0]);
			jsonObj.put("plot_no", data[1]);
			jsonObj.put("kissam", data[2]);
			jsonObj.put("area_acre", data[3].toString());
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselVarietiesList(EntityManager em, String jsonVal) {
		JSONArray mainJSONFile = new JSONArray();
		String query = "Select (plot_code) as varieties_id,(plot_no) as varieties_name  from  land_bank.plot_information";
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

	@Override
	public JSONObject getByIntLandApplicantId(Integer IntLandApplicantId) {
		Land_plot entity = land_plotRepository.findByIntLandApplicantIdAndBitDeletedFlag(IntLandApplicantId, false);
		Land_applicant entity1 = Land_applicantRepository.findByIntId(IntLandApplicantId);
		JSONObject jsonObj = new JSONObject();
		if (entity != null) {
			try {
				dynamicValue = entityManager.createNativeQuery(
						"select district_name from land_bank.district_master where district_code= :districtCode")
						.setParameter("districtCode", entity1.getSelDistrictName()).getSingleResult();
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity1.setSelDistrictNameVal(dynamicValue.toString());
			try {
				dynamicValue = entityManager
						.createNativeQuery(
								"select tahasil_name from land_bank.tahasil_master where tahasil_code= :tahasilCode")
						.setParameter("tahasilCode", entity1.getSelTehsilName()).getSingleResult();
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity1.setSelTehsilNameVal(dynamicValue.toString());
			try {
				dynamicValue = entityManager
						.createNativeQuery(
								"select village_name from land_bank.village_master where village_code= :villageCode")
						.setParameter("villageCode", entity1.getSelMouza()).getSingleResult();
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity1.setSelMouzaVal(dynamicValue.toString());
			try {
				dynamicValue = entityManager
						.createNativeQuery(
								"select khata_no from land_bank.khatian_information where khatian_code= :khatianCode")
						.setParameter("khatianCode", entity1.getSelKhataNo()).getSingleResult();
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity1.setSelKhataNoVal(dynamicValue.toString());
			try {
				dynamicValue = entityManager
						.createNativeQuery("select plot_no from land_bank.plot_information where plot_code= :plotCode")
						.setParameter("plotCode", entity.getSelPlotNo()).getSingleResult();
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelPlotNoVal(dynamicValue.toString());

			jsonObj.put("data", new JSONObject(entity));
		} else {
			jsonObj.put("data", "");
		}

		return jsonObj;
	}

	@Override
	public JSONObject deleteByIntLandApplicantId(Integer IntLandApplicantId) {

		JSONObject json = new JSONObject();
		try {
			Land_plot entity = land_plotRepository.deleteByIntLandApplicantIdAndBitDeletedFlag(IntLandApplicantId,
					false);
			entity.setBitDeletedFlag(true);
			land_plotRepository.save(entity);
			json.put(CommonConstant.STATUS_KEY, 200);
		} catch (Exception e) {
			log.error(e.getMessage());
			json.put(CommonConstant.STATUS_KEY, 400);
		}
		return json;
	}

}
