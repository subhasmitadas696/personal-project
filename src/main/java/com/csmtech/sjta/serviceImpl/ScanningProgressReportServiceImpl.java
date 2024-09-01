package com.csmtech.sjta.serviceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.repository.ScanningProgressReportRepository;
import com.csmtech.sjta.service.ScanningProgressReportService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class ScanningProgressReportServiceImpl implements ScanningProgressReportService {

	@Autowired
	private ScanningProgressReportRepository scanning_progress_report_repo;
	@Autowired
	EntityManager entityManager;

	Integer parentId = 0;
	Object dynamicValue = null;
	JSONObject json = new JSONObject();
	@Value("${tempUpload.path}")
	private String tempUploadPath;
	@Value("${file.path}")
	private String finalUploadPath;

	@Override
	public JSONObject saveProgressData(String data) {
		try {
			ObjectMapper om = new ObjectMapper();
			JsonNode jsonNode = om.readTree(data);

			String csvFile = jsonNode.get("fileUploadCSV17").asText();

			// For CSV
			if (!csvFile.equals("")) {
				File f = new File(tempUploadPath + csvFile);
				if (f.exists()) {

					File folder = new File(finalUploadPath + "/scanning_progress_report");
					if (!folder.exists()) {
						folder.mkdirs();
					}

					File src = new File(tempUploadPath + csvFile);
					File dest = new File(folder + "/" + csvFile);

					try {
						Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
						Files.delete(src.toPath());
					} catch (IOException e) {
						// logger.error(e.getMessage());
					}
				}
			}

			// read data from excel file
			try {
				// Specify the path to your Excel file
				String excelFilePath = finalUploadPath + "/scanning_progress_report/" + csvFile;

				// Open the Excel file using FileInputStream
				FileInputStream inputStream = new FileInputStream(new File(excelFilePath));

				// Create a Workbook object for the Excel file
				Workbook workbook = new XSSFWorkbook(inputStream);

				// Get the first sheet in the Excel file (index 0)
				Sheet sheet = workbook.getSheetAt(0);
				int lastRowIndex = sheet.getLastRowNum();
				String[] desiredHeaders = { "district", "totalTahasil", "totalVillage", "totalKhatian", "totalPlot",
						"area", "totalPages", "status" };

				@SuppressWarnings("unchecked")
				Map<String, String>[] excelData = new Map[sheet.getPhysicalNumberOfRows() - 4];
				// Iterate through rows and cells to read data
				int rowIndex = 0;
				for (Row row : sheet) {

					if (row.getRowNum() > 2 && row.getRowNum() != lastRowIndex) {
						Map<String, String> rowData = new HashMap<>();
						int columnIndex = 0;
						for (Cell cell : row) {
							if (cell.getColumnIndex() > 0) {
								switch (cell.getCellType()) {
								case STRING:
									if (cell.getStringCellValue().equals("")) {
										rowData.put(desiredHeaders[columnIndex], "");
									} else {
										rowData.put(desiredHeaders[columnIndex], cell.getStringCellValue());
									}

									break;
								case NUMERIC:
									if (cell.getNumericCellValue() >= 0 ) {
										rowData.put(desiredHeaders[columnIndex],
												Double.toString(cell.getNumericCellValue()));
									} else {
										rowData.put(desiredHeaders[columnIndex], "0");
									}

									break;
									
								case BLANK:
									rowData.put(desiredHeaders[columnIndex], "");
									break;
								default:
									// Handle other cell types as needed
								}
								columnIndex++;
							}
						}
						excelData[rowIndex] = rowData;
						rowIndex++;
					}
				}

				// Close the workbook and input stream
				workbook.close();
				inputStream.close();
				
				scanning_progress_report_repo.saveProgressData(excelData);
				json.put("status", 202);
			} catch (IOException e) {
				log.info("Inside Exception !! of saveProgressData");
			}


		} catch (NegativeArraySizeException nas) {
			json.put("status", 403);
		}
		catch (Exception e) {
			 log.info("Inside Exception !!");
			json.put("status", 400);
		}
		
		return json;
	}

	@Override
	public JSONObject getProgressData(String data) {
		json.put("status", 200);
		json.put("result", scanning_progress_report_repo.getProgressData(data));
		return json;
	}

}
