package com.csmtech.sjta.serviceImpl;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.DigitalFileKhatianInformation;
import com.csmtech.sjta.entity.Khatian_information;
import com.csmtech.sjta.entity.Plot_information;
import com.csmtech.sjta.repository.DigitalFileRepo;
import com.csmtech.sjta.repository.KhatianBoundaryRepository;
import com.csmtech.sjta.repository.KhatianDigitalInfoRepo;
import com.csmtech.sjta.repository.KhatianInformationRepo;
import com.csmtech.sjta.repository.Plot_informationRepository;
import com.csmtech.sjta.service.KhatianInformationService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@Service
public class KhatianInformationServiceImpl implements KhatianInformationService {
	@Autowired
	private KhatianInformationRepo khatian_informationRepository;
	@Autowired
	private Plot_informationRepository plot_informationRepository;

	@Autowired
	private DigitalFileRepo fileRepo;

	@Autowired
	private KhatianDigitalInfoRepo digiFileRepo;
	@Autowired
	EntityManager entityManager;
	@Autowired
	private KhatianBoundaryRepository khatianBoundRepo;

	@Value("${tempUpload.path}")
	private String tempUploadPath;
	@Value("${file.path}")
	private String finalUploadPath;

	@Value("${rordigitalfile.path}")
	private String rorUploadPath;

	@Value("${casefiledigitalfile.path}")
	private String casefileUploadPath;

	String parentId = null;
	Object dynamicValue = null;
	private static final Logger logger = LoggerFactory.getLogger(KhatianInformationServiceImpl.class);
	JSONObject json = new JSONObject();

	@Override
	public JSONObject save(String data) {
		logger.info("Inside save method of Khatian_informationServiceImpl");
		try {
			ObjectMapper om = new ObjectMapper();
			Khatian_information khatianInformation = om.readValue(data, Khatian_information.class);
			Khatian_information getEntity = khatian_informationRepository
					.findByTxtKhatianCode(khatianInformation.getTxtKhatianCode());
			Integer checkDuplicateKhataNo = khatian_informationRepository.findByKhataNoOfSameTahasil(
					khatianInformation.getTxtKhataNo(), khatianInformation.getSelTahasilCode());

			if (checkDuplicateKhataNo > 0) {
				json.put(CommonConstant.STATUS_KEY, 210);
				json.put(CommonConstant.MESSAGE_KEY, "Duplicate khata no. ");
				return json;
			} else {
				if (getEntity != null) {
					logger.info("inside update method...");
					getEntity.setSelVillageCode(khatianInformation.getSelVillageCode());
					getEntity.setTxtKhatianCode(khatianInformation.getTxtKhatianCode());
					getEntity.setTxtKhataNo(khatianInformation.getTxtKhataNo());
					getEntity.setTxtOwnerName(khatianInformation.getTxtOwnerName());
					getEntity.setTxtmarfatdarname(khatianInformation.getTxtmarfatdarname());
					getEntity.setTxtSotwa(khatianInformation.getTxtSotwa());
					getEntity.setTxtrremarks(khatianInformation.getTxtrremarks());
					getEntity.setTxtPublicationDate(khatianInformation.getTxtPublicationDate());
					Khatian_information updateData = khatian_informationRepository.save(getEntity);
					parentId = updateData.getTxtKhatianCode();
					json.put(CommonConstant.STATUS_KEY, 202);
				} else {
					khatianInformation.setTxtKhatianCode(khatianInformation.getVillKhatianCode());
					Khatian_information saveData = khatian_informationRepository.save(khatianInformation);
					List<DigitalFileKhatianInformation> documentList = khatianInformation.getAddMoreUploadDocuments();
					logger.info("doc list " + documentList);
					int counter = 0;
					for (DigitalFileKhatianInformation document : documentList) {
						String fileDoc = document.getFileDocument();
						if (fileDoc != null && !fileDoc.isEmpty()) {
							String[] fileArray = fileDoc.split("[.]");
							String villageCode = khatianInformation.getSelVillageCode();
							String khatianCode = khatianInformation.getTxtKhataNo().replace("/", "_");
							String newFileDoc;
							if (counter == 1) {
								newFileDoc = villageCode + "-" + khatianCode + "-Copy" + "."
										+ fileArray[fileArray.length - 1];
							} else if (counter > 1) {
								newFileDoc = villageCode + "-" + khatianCode + "-Copy_" + counter + "."
										+ fileArray[fileArray.length - 1];
							} else {
								newFileDoc = villageCode + "-" + khatianCode + "." + fileArray[fileArray.length - 1];
							}
							BigInteger digitalInformationId = digiFileRepo.getLastDigitInfoId();
							fileRepo.saveFile(digitalInformationId, khatianInformation.getTxtKhatianCode(),
									khatianInformation.getTxtKhataNo(), khatianInformation.getSelDistrictCode(),
									khatianInformation.getSelVillageCode(), khatianInformation.getSelTahasilCode(),
									khatianInformation.getCreatedBy(), khatianInformation.getCreatedOn(), newFileDoc);
							String fileUpload = fileDoc;
							if (fileUpload != null && (!fileUpload.equals(""))) {
								File f = new File(tempUploadPath + fileUpload);
								if (f.exists()) {
									Path srcPath = Paths.get(tempUploadPath + fileUpload);
									Path destPath = Paths.get(rorUploadPath + newFileDoc);
									CommonUtil.copyAndDeleteFile(srcPath, destPath);
								}
							}
						}
						counter++;
					}
					parentId = saveData.getTxtKhatianCode();
					json.put(CommonConstant.STATUS_KEY, 200);
				}
			}
			json.put("id", parentId);
		} catch (Exception e) {
			logger.error("Inside save method of Khatian_informationServiceImpl, an error occurred: {}", e.getMessage());
			json.put(CommonConstant.STATUS_KEY, 400);
		}
		return json;
	}

	@Override
	public JSONObject getAll(String formParams) {
		logger.info("Inside getAll method of Khatian_informationServiceImpl");
		JSONObject jsonData = new JSONObject(formParams);
		String selVillageCode = "0";
		String txtKhatianCode = "0";
		String txtKhataNo = "0";
		List<Khatian_information> khatian_informationResp = null;
		if (jsonData.has("selVillageCode") && !jsonData.isNull("selVillageCode")
				&& !jsonData.getString("selVillageCode").equals("")) {
			selVillageCode = jsonData.getString("selVillageCode");
			khatian_informationResp = khatian_informationRepository.findBySelVillageCode(selVillageCode);
		}

		if (jsonData.has("txtKhataNo") && !jsonData.isNull("txtKhataNo")
				&& !jsonData.getString("txtKhataNo").equals("")) {
			txtKhataNo = jsonData.getString("txtKhataNo");
			khatian_informationResp = khatian_informationRepository.findByTxtKhataNo(txtKhataNo, selVillageCode);
		}
		Integer totalDataPresent = khatian_informationRepository.countByAll();

		khatian_informationResp = khatian_informationRepository.findAll(selVillageCode, txtKhatianCode, txtKhataNo);
		List<Khatian_information> list = new ArrayList<>();

		Object[] dynamicValue = null;
		for (Khatian_information entity : khatian_informationResp) {
			try {
				dynamicValue = (Object[]) CommonUtil.getDynSingleData(entityManager,
						"select  village_name, tahasil_code from land_bank.village_master where village_code='"
								+ entity.getSelVillageCode() + "'");
			} catch (Exception ex) {
				dynamicValue = new Object[] { "--" };
			}
			entity.setSelVillageCodeVal((String) dynamicValue[0]);

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
		logger.info("after  query execution method of khatian_masterServiceImpl with value {} ", list);

		json.put(CommonConstant.STATUS_KEY, 200);
		json.put(CommonConstant.RESULT, new JSONArray(khatian_informationResp));
		json.put("count", totalDataPresent);
		return json;
	}

	public static JSONArray fillselDistrictCodeList(EntityManager em, String jsonVal) {
		logger.info("Inside fillselDistrictCodeList method of Khatian_informationServiceImpl");
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
		logger.info("Inside fillselTahasilCodeList method of Khatian_informationServiceImpl");
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

	@Override
	public JSONObject getById(String id) {
		logger.info("Inside getById method of Khatian_informationServiceImpl");

		List<Object[]> entity = plot_informationRepository.findBytxtKhatianCode(id);
		List<Plot_information> plotInfo = new ArrayList<>();
		for (Object[] result : entity) {
			Plot_information plot = new Plot_information();
			plot.setTxtPlotCode((String) result[0]);
			plot.setTxtPlotNo((String) result[1]);
			plot.setTxtKissam((String) result[2]);
			plot.setTxtAreaAcre((BigDecimal) result[3]);

			plotInfo.add(plot);
		}
		List<Object[]> totalPlotAndArea = plot_informationRepository.findTotal(id);
		List<Plot_information> plotInformation = new ArrayList<>();
		for (Object[] total : totalPlotAndArea) {
			Plot_information plotDto = new Plot_information();
			plotDto.setTotalPlot((BigInteger) total[0]);
			plotDto.setTotalArea((BigDecimal) total[1]);

			plotInformation.add(plotDto);
		}
		json.put(CommonConstant.STATUS_KEY, 200);
		json.put(CommonConstant.MESSAGE_KEY, CommonConstant.SUCCESS);
		json.put("total", new JSONArray(plotInformation));
		json.put(CommonConstant.RESULT, new JSONArray(plotInfo));

		return json;
	}

	@Override
	public JSONObject getKhatian(String id) {
		logger.info("Inside getById method of Khatian_informationServiceImpl");
		Khatian_information entity = khatian_informationRepository.findByTxtKhatianCode(id);

		Object[] dynamicValue = null;
		try {
			dynamicValue = (Object[]) CommonUtil.getDynSingleData(entityManager,
					"select  village_name, tahasil_code from land_bank.village_master where village_code='"
							+ entity.getSelVillageCode() + "'");
		} catch (Exception ex) {
			dynamicValue = new Object[] { "--" };
		}
		entity.setSelVillageCodeVal((String) dynamicValue[0]);

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
	public JSONObject viewAll(String selVillageCode) {
		List<Object[]> khatianBoundariesResp = khatianBoundRepo.viewAll(selVillageCode);
		List<Khatian_information> khatianBoundaries = new ArrayList<>();

		for (Object[] result : khatianBoundariesResp) {
			Khatian_information khatianBoundary = new Khatian_information();
			khatianBoundary.setTxtKhataNo((String) result[0]);
			khatianBoundary.setExtent((String) result[1]);

			khatianBoundaries.add(khatianBoundary);
		}

		json.put(CommonConstant.STATUS_KEY, 200);
		json.put(CommonConstant.MESSAGE_KEY, CommonConstant.SUCCESS);
		json.put(CommonConstant.RESULT, new JSONArray(khatianBoundaries));

		return json;

	}

	@Override
	public JSONObject viewDigitalFile(String txtKhatianCode) {

		List<Object> digitalFile = khatian_informationRepository.getFile(txtKhatianCode);
		List<DigitalFileKhatianInformation> digiFile = new ArrayList<>();

		for (Object result : digitalFile) {
			DigitalFileKhatianInformation digiFileInfo = new DigitalFileKhatianInformation();
			digiFileInfo.setFileDocument((String) result);
			digiFile.add(digiFileInfo);
		}

		json.put(CommonConstant.STATUS_KEY, 200);
		json.put(CommonConstant.MESSAGE_KEY, CommonConstant.SUCCESS);
		json.put(CommonConstant.RESULT, new JSONArray(digiFile));

		return json;
	}

	@Override
	public JSONObject searchPlot(String txtKhatianCode, String txtPlotNo) {
		List<Object[]> entity = plot_informationRepository.findBytxtKhatianCodeOfPlotNo(txtKhatianCode, txtPlotNo);
		List<Plot_information> plotInfo = new ArrayList<>();
		for (Object[] result : entity) {
			Plot_information plot = new Plot_information();
			plot.setTxtPlotCode((String) result[0]);
			plot.setTxtPlotNo((String) result[1]);
			plot.setTxtKissam((String) result[2]);
			plot.setTxtAreaAcre((BigDecimal) result[3]);

			plotInfo.add(plot);
		}

		json.put(CommonConstant.STATUS_KEY, 200);
		json.put(CommonConstant.MESSAGE_KEY, CommonConstant.SUCCESS);
		json.put(CommonConstant.RESULT, new JSONArray(plotInfo));

		return json;
	}

}