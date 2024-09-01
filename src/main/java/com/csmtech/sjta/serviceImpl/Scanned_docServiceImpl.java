package com.csmtech.sjta.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.entity.Scanned_doc;
import com.csmtech.sjta.repository.Scanned_docRepository;
import com.csmtech.sjta.service.Scanned_docService;
import com.csmtech.sjta.util.CommonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@Service
public class Scanned_docServiceImpl implements Scanned_docService {

	@Autowired
	private Scanned_docRepository scanned_docRepository;
	@Autowired
	EntityManager entityManager;

	Integer parentId = 0;
	Object dynamicValue = null;
	private static final Logger logger = LoggerFactory.getLogger(Scanned_docServiceImpl.class);
	JSONObject json = new JSONObject();
	@Value("${tempUpload.path}")
	private String tempUploadPath;
	@Value("${scanfile.path}")
	private String finalUploadPath;

	@Override
	public JSONObject save(String data) {
		logger.info("Inside save method of Scanned_docServiceImpl");
		try {
			ObjectMapper om = new ObjectMapper();
			Scanned_doc scanned_doc = om.readValue(data, Scanned_doc.class);
			Object csvFileNew = "";

			try {
				csvFileNew = CommonUtil.getDynSingleData(entityManager,
						"SELECT upload_csv FROM scanned_doc WHERE deleted_flag = '0' and (upload_csv is not null or upload_csv != '') ORDER BY scanned_doc_id DESC LIMIT 1 ");
			} catch (Exception ex) {
				csvFileNew = "";
			}
			String csvFile = csvFileNew.toString();

			String pdfFile = scanned_doc.getFileUploadScannedPDF16();
			String[] fileArray = scanned_doc.getOriginalFileName().split("[.]");
			SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss_a");
			Date date = new Date();
			Timestamp ts = new Timestamp(date.getTime());
			String formattedDate = formatter.format(ts);

			String pdfFileNew = fileArray[0] + "_" + formattedDate + "." + fileArray[fileArray.length - 1];

			if (!Objects.isNull(scanned_doc.getIntId()) && scanned_doc.getIntId() > 0) {
				Scanned_doc getEntity = scanned_docRepository.findByIntIdAndBitDeletedFlag(scanned_doc.getIntId());
				getEntity.setFileNo(scanned_doc.getFileNo());
				getEntity.setFileType(scanned_doc.getFileType());
				getEntity.setSelDistrict12(scanned_doc.getSelDistrict12());
				getEntity.setSelTehsil13(scanned_doc.getSelTehsil13());
				getEntity.setFileUploadScannedPDF16(pdfFileNew);
				getEntity.setFileUploadCSV17(csvFile);
				Scanned_doc updateData = scanned_docRepository.save(getEntity);
				parentId = updateData.getIntId();
				json.put("status", 202);
			} else {
				// check duplicate
				scanned_doc.setFileUploadScannedPDF16(pdfFileNew);
				scanned_doc.setFileUploadCSV17(csvFile);
				Scanned_doc saveData = scanned_docRepository.save(scanned_doc);
				parentId = saveData.getIntId();
				json.put("status", 200);
			}

			if (parentId > 0) {

				// For PDF
				if (!pdfFile.equals("")) {
					File f = new File(tempUploadPath + pdfFile);
					if (f.exists()) {
						String fileTypePath = "";
						if (scanned_doc.getFileType() == 1) {
							fileTypePath = finalUploadPath + "/ror";
						} else {
							fileTypePath = finalUploadPath + "/case_file";
						}
						File folder1 = new File(fileTypePath);
						if (!folder1.exists()) {
							folder1.mkdirs();
						}

						File folder2 = new File(fileTypePath + "/" + scanned_doc.getSelDistrict12());
						if (!folder2.exists()) {
							folder2.mkdirs();
						}

						String folderPathPdf = fileTypePath + "/" + scanned_doc.getSelDistrict12() + "/"
								+ scanned_doc.getSelTehsil13();

						File folder = new File(folderPathPdf);
						if (!folder.exists()) {
							folder.mkdirs();
						}

						File src = new File(tempUploadPath + pdfFile);
						File dest = new File(folderPathPdf + "/" + pdfFileNew);
						try {
							Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
							Files.delete(src.toPath());

						} catch (IOException e) {
							logger.error(e.getMessage());
						}
					}
				}

			}

		} catch (Exception e) {
			logger.error("Inside save method of Scanned_docServiceImp , some error occur {} ", e.getMessage());
			json.put("status", 400);
		}
		return json;
	}

	@Override
	public JSONObject getById(Integer id) {
		logger.info("Inside getById method of Scanned_docServiceImpl");
		Scanned_doc entity = scanned_docRepository.findByIntIdAndBitDeletedFlag(id);
		try {
			dynamicValue = CommonUtil.getDynSingleData(entityManager,
					"select district_name from land_bank.district_master where district_code='"
							+ entity.getSelDistrict12() + "'");
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelDistrict12Val(dynamicValue.toString());
		try {
			dynamicValue = CommonUtil.getDynSingleData(entityManager,
					"select tahasil_name from land_bank.tahasil_master where tahasil_code='" + entity.getSelTehsil13()
							+ "'");
		} catch (Exception ex) {
			dynamicValue = "--";
		}
		entity.setSelTehsil13Val(dynamicValue.toString());

		return new JSONObject(entity);
	}

	@Override
	public JSONObject getAll(String formParams) {
		logger.info("Inside getAll method of Scanned_docServiceImpl");
		JSONObject jsonData = new JSONObject(formParams);
		Integer totalDataPresent = scanned_docRepository.countByBitDeletedFlag();
		Pageable pageRequest = PageRequest.of(jsonData.has("pageNo") ? jsonData.getInt("pageNo") - 1 : 0,
				jsonData.has("size") ? jsonData.getInt("size") : totalDataPresent);

		List<Scanned_doc> scanned_docResp;

		if (jsonData.getString("selDistrict12") != null && !jsonData.getString("selDistrict12").equals("")
				&& !(jsonData.getString("selDistrict12").isEmpty())) {
			scanned_docResp = scanned_docRepository.findAllByBitDeletedFlag(pageRequest,
					jsonData.getString("selDistrict12"));
		} else {
			scanned_docResp = scanned_docRepository.findAllByBitDeletedFlag(pageRequest);
		}

		for (Scanned_doc entity : scanned_docResp) {
			try {
				dynamicValue = CommonUtil.getDynSingleData(entityManager,
						"select district_name from land_bank.district_master where district_code='"
								+ entity.getSelDistrict12() + "'");
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelDistrict12Val(dynamicValue.toString());

			try {
				dynamicValue = CommonUtil.getDynSingleData(entityManager,
						"select tahasil_name from land_bank.tahasil_master where tahasil_code='"
								+ entity.getSelTehsil13() + "'");
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelTehsil13Val(dynamicValue.toString());

			String fileTypePath = "";
			if (entity.getFileType() == 1) {
				fileTypePath = finalUploadPath + "/ror";
			} else {
				fileTypePath = finalUploadPath + "/case_file";
			}
			long csvFileSize = 0;
			String csvFileFinalSize = "0";
			File csvfile = new File(fileTypePath + "/" + entity.getFileUploadCSV17());
			if (csvfile.exists()) {
				try {
					csvFileSize = CommonUtil.getFileSize(fileTypePath, entity.getFileUploadCSV17());
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
				csvFileFinalSize = CommonUtil.formatSizeUnits(csvFileSize);
			}
			entity.setCsvFileSize(csvFileFinalSize);

			String folderPathPdf = fileTypePath + "/" + entity.getSelDistrict12() + "/" + entity.getSelTehsil13();

			long pdfFileSize = 0;
			String pdfFileFinalSize = "0";

			File pdffile = new File(folderPathPdf + "/" + entity.getFileUploadScannedPDF16());
			if (pdffile.exists()) {
				try {
					pdfFileSize = CommonUtil.getFileSize(folderPathPdf, entity.getFileUploadScannedPDF16());
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
				pdfFileFinalSize = CommonUtil.formatSizeUnits(pdfFileSize);
			}
			entity.setPdfFileSize(pdfFileFinalSize);
		}
		json.put("status", 200);
		json.put("result", new JSONArray(scanned_docResp));
		json.put("count", totalDataPresent);
		return json;
	}

	@Override
	public JSONObject deleteById(Integer id) {
		logger.info("Inside deleteById method of Scanned_docServiceImpl");
		try {

			scanned_docRepository.deleteFlagById(id);
			json.put("status", 200);
		} catch (Exception e) {
			logger.error("Inside deleteById method of Scanned_docServiceImpl,  some error occur {} ", e.getMessage());
			json.put("status", 400);
		}
		return json;
	}

	public static JSONArray fillselDistrict12List(EntityManager em, String jsonVal) {
		logger.info("Inside fillselDistrict12List method of Scanned_docServiceImpl");
		JSONArray mainJSONFile = new JSONArray();
		String query = "select district_code,district_name from land_bank.district_master where state_code = 'OD' order by district_name";
		List<Object[]> dataList = CommonUtil.getDynResultList(em, query);
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("district_code", data[0]);
			jsonObj.put("district_name", data[1]);
			mainJSONFile.put(jsonObj);
		}
		return mainJSONFile;
	}

	public static JSONArray fillselTehsil13List(EntityManager em, String jsonVal) {
		logger.info("Inside fillselTehsil13List method of Scanned_docServiceImpl");
		JSONArray mainJSONFile = new JSONArray();
		JSONObject jsonDepend = new JSONObject(jsonVal);
		String val = "";
		val = jsonDepend.get("district_id").toString();
		String query = "select tahasil_code,tahasil_name from land_bank.tahasil_master where district_code='" + val
				+ "' order by tahasil_name";
		List<Object[]> dataList = CommonUtil.getDynResultList(em, query);
		for (Object[] data : dataList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("tahasil_code", data[0]);
			jsonObj.put("tahasil_name", data[1]);
			mainJSONFile.put(jsonObj);
		}

		return mainJSONFile;
	}

	@Override
	public JSONObject saveMetadata(String data) {
		logger.info("Inside save method of Scanned_docServiceImpl");
		try {
			ObjectMapper om = new ObjectMapper();
			Scanned_doc scanned_doc = om.readValue(data, Scanned_doc.class);

			String csvFile = scanned_doc.getFileUploadCSV17();
			String[] fileArray = scanned_doc.getOriginalFileName().split("[.]");
			SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss_a");
			Date date = new Date();
			Timestamp ts = new Timestamp(date.getTime());
			String formattedDate = formatter.format(ts);

			String csvFileNew = fileArray[0] + "_" + formattedDate + "." + fileArray[fileArray.length - 1];

			scanned_docRepository.saveMetadata(scanned_doc.getFileType(), csvFileNew);
			json.put("status", 202);

			// For CSV
			if (!csvFile.equals("")) {
				File f = new File(tempUploadPath + csvFile);
				if (f.exists()) {
					String folderPath = "";
					if (scanned_doc.getFileType() == 1) {
						folderPath = finalUploadPath + "/ror";
					} else {
						folderPath = finalUploadPath + "/case_file";
					}

					File folder = new File(folderPath);
					if (!folder.exists()) {
						folder.mkdirs();
					}

					File src = new File(tempUploadPath + csvFile);
					File dest = new File(folderPath + "/" + csvFileNew);

					try {
						Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
						Files.delete(src.toPath());
					} catch (IOException e) {
						logger.error(e.getMessage());
					}
				}
			}

		} catch (Exception e) {
			logger.error("Inside save method of Scanned_docServiceImpl , some error occur {} ", e.getMessage());
			json.put("status", 400);
		}
		return json;
	}

	@Override
	public JSONObject getAllData(String formParams) {
		logger.info("Inside getAll method of Scanned_docServiceImpl");
		JSONObject jsonData = new JSONObject(formParams);
		Integer totalDataPresent = scanned_docRepository.countByBitDeletedFlag();
		Pageable pageRequest = PageRequest.of(jsonData.has("pageNo") ? jsonData.getInt("pageNo") - 1 : 0,
				jsonData.has("size") ? jsonData.getInt("size") : totalDataPresent);

		List<Scanned_doc> scanned_docResp;

		if (jsonData.getString("selDistrict12") != null && !jsonData.getString("selDistrict12").equals("")
				&& !(jsonData.getString("selDistrict12").isEmpty())) {
			scanned_docResp = scanned_docRepository.findAllData(pageRequest, jsonData.getString("selDistrict12"));
		} else {
			scanned_docResp = scanned_docRepository.findAllData(pageRequest);
		}

		for (Scanned_doc entity : scanned_docResp) {
			try {
				dynamicValue = CommonUtil.getDynSingleData(entityManager,
						"select district_name from land_bank.district_master where district_code='"
								+ entity.getSelDistrict12() + "'");
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelDistrict12Val(dynamicValue.toString());
			try {
				dynamicValue = CommonUtil.getDynSingleData(entityManager,
						"select tahasil_name from land_bank.tahasil_master where tahasil_code='"
								+ entity.getSelTehsil13() + "'");
			} catch (Exception ex) {
				dynamicValue = "--";
			}
			entity.setSelTehsil13Val(dynamicValue.toString());

			String fileTypePath = "";
			if (entity.getFileType() == 1) {
				fileTypePath = finalUploadPath + "/ror";
			} else {
				fileTypePath = finalUploadPath + "/case_file";
			}
			long csvFileSize = 0;
			String csvFileFinalSize = "0";

			File csvfile = new File(fileTypePath + "/" + entity.getFileUploadCSV17());
			if (csvfile.exists()) {
				try {
					csvFileSize = CommonUtil.getFileSize(fileTypePath, entity.getFileUploadCSV17());
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
				csvFileFinalSize = CommonUtil.formatSizeUnits(csvFileSize);
			}
			entity.setCsvFileSize(csvFileFinalSize);

			String folderPathPdf = fileTypePath + "/" + entity.getSelDistrict12() + "/" + entity.getSelTehsil13();

			long pdfFileSize = 0;
			String pdfFileFinalSize = "0";

			File pdffile = new File(folderPathPdf + "/" + entity.getFileUploadScannedPDF16());
			if (pdffile.exists()) {
				try {
					pdfFileSize = CommonUtil.getFileSize(folderPathPdf, entity.getFileUploadScannedPDF16());
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
				pdfFileFinalSize = CommonUtil.formatSizeUnits(pdfFileSize);
			}
			entity.setPdfFileSize(pdfFileFinalSize);
		}
		json.put("status", 200);
		json.put("result", new JSONArray(scanned_docResp));
		json.put("count", totalDataPresent);
		return json;
	}

}