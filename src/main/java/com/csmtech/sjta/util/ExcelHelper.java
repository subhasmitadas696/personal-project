package com.csmtech.sjta.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.csmtech.sjta.dto.AuctionReportDTO;
import com.csmtech.sjta.dto.IllegitimateLandUseDTO;
import com.csmtech.sjta.dto.LandApplicationReportDTO;
import com.csmtech.sjta.dto.LandInformationReportDTO;
import com.csmtech.sjta.dto.LesaeCaseReportDTO;
import com.csmtech.sjta.dto.PaymentCollectionHistoryDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExcelHelper {

	public static final String HEADER_MAIN_DISTRICT = "LAND INFORMATION DISTRICT REPORT";
	public static final String HEADER_MAIN_LEASE_CASE_DISTRICT = "LEASE CASE DISTRICT WISE  REPORT";
	public static final String HEADER_MAIN_LEASE_CASE_TAHASIL = "LEASE CASE TAHASIL WISE  REPORT";
	public static final String HEADER_MAIN_LEASE_CASE_VILLAGE = "LEASE CASE VILLAGE WISE  REPORT";
	public static final String HEADER_MAIN_LEASE_CASE_KHATA = "LEASE CASE KHATA WISE  REPORT";
	public static final String HEADER_MAIN_LEASE_CASE_PLOT = "LEASE CASE PLOT WISE  REPORT";
	public static final String HEADER_MAIN_TAHASIL = "LAND INFORMATION TAHASIL REPORT";
	public static final String AUCTION_REPORT = "AUCTION REPORT";
	public static final String HEADER_MAIN_VILLAGE = "LAND INFORMATION MOUZA REPORT";
	public static final String HEADER_MAIN_KHATA = "LAND INFORMATION KHATA REPORT";
	public static final String HEADER_MAIN_PLOT = "LAND INFORMATION PLOT REPORT";

//	public static final String HEADER_MAIN_LAND = "LAND APPLICATION REPORT";
	public static final String HEADER_MAIN_LAND_DISTRICT = "DISTRICT WISE LAND APPLICATION  REPORT";
	public static final String HEADER_MAIN_LAND_TAHASIL = "TAHASIL WISE LAND APPLICATION  REPORT";
	public static final String HEADER_DISTRICT_WISE_ILLEGITIMATE_LAND_DETAILS = "DISTRICT WISE ILLEGITIMATE LAND DETAILS";
	public static final String HEADER_TAHASIL_WISE_ILLEGITIMATE_LAND_DETAILS = "TAHASIL WISE ILLEGITIMATE LAND DETAILS";

	public static final String[] HEADER_DISTRICT = { "District Name", "Village Count", "Tahasil Count", "Khata Count",
			"Plot Count", "Area In Acer" };
	public static final String[] HEADER_LEASE_CASE_DISTRICT = { "District Name", "No. Of Lease Case", "Total Plot",
			"Total DLSC Meeting", "Total TLSC Meeting", "Total MC Meeting" };
	public static final String[] HEADER_LEASE_CASE_TAHASIL = { "Tahasil Name", "No. Of Lease Case", "Total Plot",
			"Total DLSC Meeting", "Total TLSC Meeting", "Total MC Meeting" };
	public static final String[] HEADER_LEASE_CASE_VILLAGE = { "Village Name", "No. Of Lease Case", "Total Plot",
			"Total DLSC Meeting", "Total TLSC Meeting", "Total MC Meeting" };
	public static final String[] HEADER_LEASE_CASE_KHATA = { "Khata No.", "No. Of Lease Case", "Total Plot",
			"Total DLSC Meeting", "Total TLSC Meeting", "Total MC Meeting" };
	public static final String[] HEADER_LEASE_CASE_PLOT = { "Plot No.", " Field Inquery ", "DLSC Meeting",
			"TLSC Meeting ", "MC Meeting", "Notice Issue", "Consideration Money Deposited", "Remerk" };
	public static final String[] HEADER_TAHASIL = { "Tahasil Name", "Village Count", "Khata Count", "Plot Count",
			"Area In Acer" };

	public static final String[] HEADER_AUCTION = { "Auction Number", "Land Details", "Auction Date", "Winner Name",
			"Winner Bid Amount", "Participant Details" };
	public static final String[] HEADER_VILLAGE = { "Village Name", "Khata Count", "Plot Count", "Area In Acer" };
	public static final String[] HEADER_KHATA = { "Khata No.", "Woner Name", "Marfatdar Name", "Sotwar",
			"Publication Date", "Plot Count", "Area In Acer" };
	public static final String[] HEADER_PLOT = { "Plot No.", "Kissam", "Total Area In Acer" };

//	public static final String[] HEADER_LAND = { "District Name", "Applied Area(in Acre)", "Total", "Pending",
//			"Approved", "Rejected", "Reverted" };

	public static final String[] HEADER_LAND_DISTRICT = { "Tahasil Name", "Applied Area(in Acre)", "Total", "Pending",
			"Approved", "Rejected", "Reverted" };

	public static final String[] HEADER_LAND_TAHASIL = { "Village Name", "Applied Area(in Acre)", "Total", "Pending",
			"Approved", "Rejected", "Reverted" };

	public static final String[] HEADER_ILLEGITIMATE_DISTRICT = { "District", "Total No. of application", "Pending",
			"Assigned", "Application On Hold", "Completed", "Closed", "Discarded" };
	public static final String[] HEADER_ILLEGITIMATE_TAHASIL = { "Tahasil", "Total No. of application", "Pending",
			"Assigned", "Application On Hold", "Completed", "Closed", "Discarded" };

	public static final String DISTRICT_RECORD = "DISTRICT_RECORD";
	public static final String LEASE_CASE_DISTRICT_RECORD = "LEASE_CASE_DISTRICT_RECORD";
	public static final String LEASE_CASE_TAHASIL_RECORD = "LEASE_CASE_TAHASIL_RECORD";
	public static final String LEASE_CASE_VILLAGE_RECORD = "LEASE_CASE_VILLAGE_RECORD";
	public static final String LEASE_CASE_KHATA_RECORD = "LEASE_CASE_KHATA_RECORD";
	public static final String LEASE_CASE_PLOT_RECORD = "LEASE_CASE_PLOT_RECORD";
	public static final String TAHASIL_RECORD = "TAHASIL_RECORD";
	public static final String AUCTION_RECORD = "AUCTION_RECORD";
	public static final String VILLAGE_RECORD = "VILLAGE_RECORD";
	public static final String KHATA_RECORD = "KHATA_RECORD";
	public static final String PLOT_RECORD = "KHATA_RECORD";

//	public static final String LAND_RECORD = "LAND_RECORD";
	public static final String DISTRICT_WISE_LAND_RECORD = "DISTRICT_WISE_LAND_RECORD";
	public static final String TAHASIL_WISE_LAND_RECORD = "TAHASIL_WISE_LAND_RECORD";

	public static final String HEADER_MAIN_LAND = "LAND APPLICATION REPORT";

	public static final String PAYMENT_COLLECTION_HISTORY_REPORT = "PAYMENT COLLECTION REPORT";

	public static final String LAND_ALLOTMENT_PAYMENT_COLLECTION_HISTORY_REPORT = "LAND ALLOTMENT PAYMENT COLLECTION REPORT";

	public static final String[] HEADER_LAND = { "District Name", "Applied Area (in Acre)", "Total", "Pending",
			"Approved", "Rejected", "Reverted" };

	public static final String[] HEADER_PAYMENT_COLLECTION = { "District Name", "January", "February", "March", "April",
			"May", "June", "July", "August", "September", "October", "November", "December", "Total Amount" };

	public static final String[] TAHASIL_PAYMENT_COLLECTION = { "Tahasil Name", "January", "February", "March", "April",
			"May", "June", "July", "August", "September", "October", "November", "December", "Total Amount" };

	public static final String[] VILLAGE_PAYMENT_COLLECTION = { "Village Name", "January", "February", "March", "April",
			"May", "June", "July", "August", "September", "October", "November", "December", "Total Amount" };

	public static final String[] KHATIAN_PAYMENT_COLLECTION = { "Khata No.", "January", "February", "March", "April",
			"May", "June", "July", "August", "September", "October", "November", "December", "Total Amount" };

	public static final String[] PLOT_PAYMENT_COLLECTION = { "Plot No.", "January", "February", "March", "April", "May",
			"June", "July", "August", "September", "October", "November", "December", "Total Amount", "Applicant Name",
			"Purchase Area(in Acre)", "Total Price", "Land Order No.", "Land Order Date" };

//	public static final String[] SUB_HEADER_LAND = { "Total", "Pending", "Approved", "Rejected", "Reverted" };
	public static final String LAND_RECORD = "LAND_RECORD";

	public static final String PAYMENT_COLLECTION_REPORT = "PAYMENT_COLLECTION_REPORT";

	public static final String LAND_ALLOTMENT_PAYMENT_COLLECTION_REPORT = "LAND_ALLOTMENT_PAYMENT_COLLECTION_REPORT";

	public static ByteArrayInputStream districtRecordToExcel(List<LandInformationReportDTO> recordForDistrict)
			throws IOException {
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet(DISTRICT_RECORD);

			// Create cell style for main header
			CellStyle mainHeaderStyle = workbook.createCellStyle();
			mainHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
			mainHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			Font font = workbook.createFont();
			font.setBold(true);
			font.setFontHeightInPoints((short) 14);
			mainHeaderStyle.setFont(font);
			mainHeaderStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
			mainHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			// Create header row
			Row headerRow = sheet.createRow(0);
			Cell mainHeaderCell = headerRow.createCell(0);
			mainHeaderCell.setCellValue(HEADER_MAIN_DISTRICT);
			mainHeaderCell.setCellStyle(mainHeaderStyle);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, HEADER_DISTRICT.length - 1)); // Merge cells for main
																								// header

			// Create header for the excel value name
			Row subHeaderRow = sheet.createRow(1);
			CellStyle headerStyle = workbook.createCellStyle();
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			Font subHeaderFont = workbook.createFont();
			subHeaderFont.setBold(true);
			headerStyle.setFont(subHeaderFont);
			for (int i = 0; i < HEADER_DISTRICT.length; i++) {
				Cell headerCell = subHeaderRow.createCell(i);
				headerCell.setCellValue(HEADER_DISTRICT[i]);
				headerCell.setCellStyle(headerStyle);
			}

			// Fill data rows
			int rowIndex = 2; // Start from row index 2
			for (LandInformationReportDTO dto : recordForDistrict) {
				Row dataRow = sheet.createRow(rowIndex++);
				dataRow.createCell(0)
						.setCellValue(dto.getDistrictName() != null ? dto.getDistrictName().toString() : "");
				dataRow.createCell(1)
						.setCellValue(dto.getTahasilCount() != null ? dto.getTahasilCount().toString() : "");
				dataRow.createCell(2)
						.setCellValue(dto.getVillageCount() != null ? dto.getVillageCount().toString() : "");
				dataRow.createCell(3).setCellValue(dto.getKhataCount() != null ? dto.getKhataCount().toString() : "");
				dataRow.createCell(4).setCellValue(dto.getPlotCount() != null ? dto.getPlotCount().toString() : "");
				dataRow.createCell(5).setCellValue(dto.getSumAreaAcer() != null ? dto.getSumAreaAcer().toString() : "");
			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			log.info("inside error in ExcellHelper : !!!" + e.getMessage());
		}
		return null;

	}

	public static ByteArrayInputStream TahasilRecordToExcel(List<LandInformationReportDTO> recordForDistrict)
			throws IOException {
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet(TAHASIL_RECORD);

			// Create cell style for main header
			CellStyle mainHeaderStyle = workbook.createCellStyle();
			mainHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
			mainHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			Font font = workbook.createFont();
			font.setBold(true);
			font.setFontHeightInPoints((short) 14);
			mainHeaderStyle.setFont(font);
			mainHeaderStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
			mainHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			// Create header row
			Row headerRow = sheet.createRow(0);
			Cell mainHeaderCell = headerRow.createCell(0);
			mainHeaderCell.setCellValue(HEADER_MAIN_TAHASIL);
			mainHeaderCell.setCellStyle(mainHeaderStyle);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, HEADER_TAHASIL.length - 1)); // Merge cells for main
																								// header

			// Create header for the excel value name
			Row subHeaderRow = sheet.createRow(1);
			CellStyle headerStyle = workbook.createCellStyle();
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			Font subHeaderFont = workbook.createFont();
			subHeaderFont.setBold(true);
			headerStyle.setFont(subHeaderFont);
			for (int i = 0; i < HEADER_TAHASIL.length; i++) {
				Cell headerCell = subHeaderRow.createCell(i);
				headerCell.setCellValue(HEADER_TAHASIL[i]);
				headerCell.setCellStyle(headerStyle);
			}

			// Fill data rows
			int rowIndex = 2; // Start from row index 2
			for (LandInformationReportDTO dto : recordForDistrict) {
				Row dataRow = sheet.createRow(rowIndex++);
				dataRow.createCell(0).setCellValue(dto.getTahasilName() != null ? dto.getTahasilName().toString() : "");
				dataRow.createCell(1)
						.setCellValue(dto.getVillageCount() != null ? dto.getVillageCount().toString() : "");
				dataRow.createCell(2).setCellValue(dto.getKhataCount() != null ? dto.getKhataCount().toString() : "");
				dataRow.createCell(3).setCellValue(dto.getPlotCount() != null ? dto.getPlotCount().toString() : "");
				dataRow.createCell(4).setCellValue(dto.getSumAreaAcer() != null ? dto.getSumAreaAcer().toString() : "");
			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			log.info("inside Excell helper TahasilRecordToExcel() !!! " + e.getMessage());

		}
		return null;
	}

	public static ByteArrayInputStream VillageRecordToExcel(List<LandInformationReportDTO> recordForDistrict)
			throws IOException {
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet(VILLAGE_RECORD);

			// Create cell style for main header
			CellStyle mainHeaderStyle = workbook.createCellStyle();
			mainHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
			mainHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			Font font = workbook.createFont();
			font.setBold(true);
			font.setFontHeightInPoints((short) 14);
			mainHeaderStyle.setFont(font);
			mainHeaderStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
			mainHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			// Create header row
			Row headerRow = sheet.createRow(0);
			Cell mainHeaderCell = headerRow.createCell(0);
			mainHeaderCell.setCellValue(HEADER_MAIN_VILLAGE);
			mainHeaderCell.setCellStyle(mainHeaderStyle);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, HEADER_VILLAGE.length - 1)); // Merge cells for main
																								// header

			// Create header for the excel value name
			Row subHeaderRow = sheet.createRow(1);
			CellStyle headerStyle = workbook.createCellStyle();
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			Font subHeaderFont = workbook.createFont();
			subHeaderFont.setBold(true);
			headerStyle.setFont(subHeaderFont);
			for (int i = 0; i < HEADER_VILLAGE.length; i++) {
				Cell headerCell = subHeaderRow.createCell(i);
				headerCell.setCellValue(HEADER_VILLAGE[i]);
				headerCell.setCellStyle(headerStyle);
			}

			// Fill data rows
			int rowIndex = 2; // Start from row index 2
			for (LandInformationReportDTO dto : recordForDistrict) {
				Row dataRow = sheet.createRow(rowIndex++);
				dataRow.createCell(0).setCellValue(dto.getVillageName() != null ? dto.getVillageName().toString() : "");
				dataRow.createCell(1).setCellValue(dto.getKhataCount() != null ? dto.getKhataCount().toString() : "");
				dataRow.createCell(2).setCellValue(dto.getPlotCount() != null ? dto.getPlotCount().toString() : "");
				dataRow.createCell(3).setCellValue(dto.getSumAreaAcer() != null ? dto.getSumAreaAcer().toString() : "");
			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			log.info("inside error in ExcellHelper : !!!" + e.getMessage());
		}
		return null;
	}

	public static ByteArrayInputStream khataRecordToExcel(List<LandInformationReportDTO> recordForDistrict)
			throws IOException {
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet(KHATA_RECORD);

			// Create cell style for main header
			CellStyle mainHeaderStyle = workbook.createCellStyle();
			mainHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
			mainHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			Font font = workbook.createFont();
			font.setBold(true);
			font.setFontHeightInPoints((short) 14);
			mainHeaderStyle.setFont(font);
			mainHeaderStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
			mainHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			// Create header row
			Row headerRow = sheet.createRow(0);
			Cell mainHeaderCell = headerRow.createCell(0);
			mainHeaderCell.setCellValue(HEADER_MAIN_KHATA);
			mainHeaderCell.setCellStyle(mainHeaderStyle);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, HEADER_KHATA.length - 1)); // Merge cells for main
																							// header

			// Create header for the excel value name
			Row subHeaderRow = sheet.createRow(1);
			CellStyle headerStyle = workbook.createCellStyle();
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			Font subHeaderFont = workbook.createFont();
			subHeaderFont.setBold(true);
			headerStyle.setFont(subHeaderFont);
			for (int i = 0; i < HEADER_KHATA.length; i++) {
				Cell headerCell = subHeaderRow.createCell(i);
				headerCell.setCellValue(HEADER_KHATA[i]);
				headerCell.setCellStyle(headerStyle);
			}

			// Fill data rows
			int rowIndex = 2; // Start from row index 2
			for (LandInformationReportDTO dto : recordForDistrict) {
				Row dataRow = sheet.createRow(rowIndex++);
				dataRow.createCell(0).setCellValue(dto.getKhataNo() != null ? dto.getKhataNo().toString() : "");
				dataRow.createCell(1).setCellValue(dto.getOwnerName() != null ? dto.getOwnerName().toString() : "");
				dataRow.createCell(2)
						.setCellValue(dto.getMarfatdarName() != null ? dto.getMarfatdarName().toString() : "");
				dataRow.createCell(3).setCellValue(dto.getSotwar() != null ? dto.getSotwar().toString() : "");
				Date publicationDate = dto.getPublicationDate();
				dataRow.createCell(4).setCellValue(publicationDate != null ? publicationDate.toString() : "");
				dataRow.createCell(5).setCellValue(dto.getPlotCount() != null ? dto.getPlotCount().toString() : "");
				dataRow.createCell(6).setCellValue(dto.getSumAreaAcer() != null ? dto.getSumAreaAcer().toString() : "");
			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			log.info("inside error in ExcellHelper : !!!" + e.getMessage());
		}
		return null;
	}

	public static ByteArrayInputStream plotRecordToExcel(List<LandInformationReportDTO> recordForDistrict)
			throws IOException {
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet(PLOT_RECORD);

			// Create cell style for main header
			CellStyle mainHeaderStyle = workbook.createCellStyle();
			mainHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
			mainHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			Font font = workbook.createFont();
			font.setBold(true);
			font.setFontHeightInPoints((short) 14);
			mainHeaderStyle.setFont(font);
			mainHeaderStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
			mainHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			// Create header row
			Row headerRow = sheet.createRow(0);
			Cell mainHeaderCell = headerRow.createCell(0);
			mainHeaderCell.setCellValue(HEADER_MAIN_PLOT);
			mainHeaderCell.setCellStyle(mainHeaderStyle);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, HEADER_PLOT.length - 1)); // Merge cells for main
																							// header

			// Create header for the excel value name
			Row subHeaderRow = sheet.createRow(1);
			CellStyle headerStyle = workbook.createCellStyle();
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			Font subHeaderFont = workbook.createFont();
			subHeaderFont.setBold(true);
			headerStyle.setFont(subHeaderFont);
			for (int i = 0; i < HEADER_PLOT.length; i++) {
				Cell headerCell = subHeaderRow.createCell(i);
				headerCell.setCellValue(HEADER_PLOT[i]);
				headerCell.setCellStyle(headerStyle);
			}

			// Fill data rows
			int rowIndex = 2; // Start from row index 2
			for (LandInformationReportDTO dto : recordForDistrict) {
				Row dataRow = sheet.createRow(rowIndex++);
				dataRow.createCell(0).setCellValue(dto.getPlotNo() != null ? dto.getPlotNo().toString() : "");
				dataRow.createCell(1).setCellValue(dto.getKissam() != null ? dto.getKissam().toString() : "");
				dataRow.createCell(2).setCellValue(dto.getAreaAcer() != null ? dto.getAreaAcer().toString() : "");
			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			log.info("inside error in ExcellHelper : !!!" + e.getMessage());
		}
		return null;

	}

	public static ByteArrayInputStream landRecordToExcel(List<LandApplicationReportDTO> recordForLand)
			throws IOException {
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet(LAND_RECORD);

			CellStyle mainHeaderStyle = workbook.createCellStyle();
			mainHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
			mainHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			Font font = workbook.createFont();
			font.setBold(true);
			font.setFontHeightInPoints((short) 14);
			mainHeaderStyle.setFont(font);
			mainHeaderStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
			mainHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			Row headerRow = sheet.createRow(0);
			Cell mainHeaderCell = headerRow.createCell(0);
			mainHeaderCell.setCellValue(HEADER_MAIN_LAND);
			mainHeaderCell.setCellStyle(mainHeaderStyle);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, HEADER_LAND.length - 1));

			Row subHeaderRow = sheet.createRow(1);
			CellStyle headerStyle = workbook.createCellStyle();
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			Font subHeaderFont = workbook.createFont();
			subHeaderFont.setBold(true);
			headerStyle.setFont(subHeaderFont);
			for (int i = 0; i < HEADER_LAND.length; i++) {
				Cell headerCell = subHeaderRow.createCell(i);
				headerCell.setCellValue(HEADER_LAND[i]);
				headerCell.setCellStyle(headerStyle);
			}

			int rowIndex = 2;
			for (LandApplicationReportDTO dto : recordForLand) {
				Row dataRow = sheet.createRow(rowIndex++);
				dataRow.createCell(0).setCellValue(dto.getDistrictName());
				dataRow.createCell(1).setCellValue(dto.getAreaApplied() != null ? dto.getAreaApplied().toString() : "");
				dataRow.createCell(2).setCellValue(dto.getTotal().toString());
				dataRow.createCell(3).setCellValue(dto.getPending().toString());
				dataRow.createCell(4).setCellValue(dto.getApproved().toString());
				dataRow.createCell(5).setCellValue(dto.getRejected().toString());
				dataRow.createCell(6).setCellValue(dto.getReverted().toString());

			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			log.info("inside error in ExcellHelper : !!!" + e.getMessage());
		}
		return null;

	}

	/*
	 * public static ByteArrayInputStream
	 * landRecordToExcel(List<LandApplicationReportDTO> recordForLand) throws
	 * IOException { try (Workbook workbook = new XSSFWorkbook();
	 * ByteArrayOutputStream out = new ByteArrayOutputStream()) { Sheet sheet =
	 * workbook.createSheet(LAND_RECORD);
	 * 
	 * CellStyle mainHeaderStyle = workbook.createCellStyle();
	 * mainHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
	 * mainHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER); Font font =
	 * workbook.createFont(); font.setBold(true); font.setFontHeightInPoints((short)
	 * 14); mainHeaderStyle.setFont(font);
	 * mainHeaderStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex())
	 * ; mainHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	 * 
	 * Row headerRow = sheet.createRow(0); for (int i = 0; i < HEADER_LAND.length;
	 * i++) { Cell headerCell = headerRow.createCell(i);
	 * headerCell.setCellValue(HEADER_LAND[i]);
	 * headerCell.setCellStyle(mainHeaderStyle); }
	 * 
	 * Row subHeaderRow = sheet.createRow(1); CellStyle subHeaderStyle =
	 * workbook.createCellStyle();
	 * subHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
	 * subHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER); Font
	 * subHeaderFont = workbook.createFont(); subHeaderFont.setBold(true);
	 * subHeaderFont.setFontHeightInPoints((short) 12);
	 * subHeaderStyle.setFont(subHeaderFont); for (int i = 0; i <
	 * SUB_HEADER_LAND.length; i++) { Cell subHeaderCell = subHeaderRow.createCell(i
	 * + 3); // Start from the 4th cell (index 3)
	 * subHeaderCell.setCellValue(SUB_HEADER_LAND[i]);
	 * subHeaderCell.setCellStyle(subHeaderStyle); }
	 * 
	 * int rowIndex = 2; for (LandApplicationReportDTO dto : recordForLand) { Row
	 * dataRow = sheet.createRow(rowIndex++);
	 * dataRow.createCell(0).setCellValue(dto.getDistrictName());
	 * dataRow.createCell(1).setCellValue(dto.getAreaApplied() != null ?
	 * dto.getAreaApplied().toString() : "");
	 * dataRow.createCell(2).setCellValue(""); // Placeholder for
	 * "No of Application"
	 * dataRow.createCell(3).setCellValue(dto.getTotal().toString());
	 * dataRow.createCell(4).setCellValue(dto.getPending().toString());
	 * dataRow.createCell(5).setCellValue(dto.getApproved().toString());
	 * dataRow.createCell(6).setCellValue(dto.getRejected().toString());
	 * dataRow.createCell(7).setCellValue(dto.getReverted().toString()); }
	 * 
	 * workbook.write(out); return new ByteArrayInputStream(out.toByteArray()); }
	 * catch (IOException e) { throw e; } }
	 */

	public static ByteArrayInputStream landRecordDistrictWiseToExcel(List<LandApplicationReportDTO> recordForLand)
			throws IOException {
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet(DISTRICT_WISE_LAND_RECORD);

			CellStyle mainHeaderStyle = workbook.createCellStyle();
			mainHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
			mainHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			Font font = workbook.createFont();
			font.setBold(true);
			font.setFontHeightInPoints((short) 14);
			mainHeaderStyle.setFont(font);
			mainHeaderStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
			mainHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			Row headerRow = sheet.createRow(0);
			Cell mainHeaderCell = headerRow.createCell(0);
			mainHeaderCell.setCellValue(HEADER_MAIN_LAND_DISTRICT);
			mainHeaderCell.setCellStyle(mainHeaderStyle);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, HEADER_LAND_DISTRICT.length - 1));

			Row subHeaderRow = sheet.createRow(1);
			CellStyle headerStyle = workbook.createCellStyle();
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			Font subHeaderFont = workbook.createFont();
			subHeaderFont.setBold(true);
			headerStyle.setFont(subHeaderFont);
			for (int i = 0; i < HEADER_LAND_DISTRICT.length; i++) {
				Cell headerCell = subHeaderRow.createCell(i);
				headerCell.setCellValue(HEADER_LAND_DISTRICT[i]);
				headerCell.setCellStyle(headerStyle);
			}

			int rowIndex = 2;
			for (LandApplicationReportDTO dto : recordForLand) {
				Row dataRow = sheet.createRow(rowIndex++);
				dataRow.createCell(0).setCellValue(dto.getTahasilName());
				dataRow.createCell(1).setCellValue(dto.getAreaApplied().toString());
				dataRow.createCell(2).setCellValue(dto.getTotal().toString());
				dataRow.createCell(3).setCellValue(dto.getPending().toString());
				dataRow.createCell(4).setCellValue(dto.getApproved().toString());
				dataRow.createCell(5).setCellValue(dto.getRejected().toString());
				dataRow.createCell(6).setCellValue(dto.getReverted().toString());

			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			log.info("inside error in ExcellHelper : !!!" + e.getMessage());
		}
		return null;
	}

	public static ByteArrayInputStream landRecordTahasilWiseToExcel(List<LandApplicationReportDTO> recordForLand)
			throws IOException {
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet(TAHASIL_WISE_LAND_RECORD);

			CellStyle mainHeaderStyle = workbook.createCellStyle();
			mainHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
			mainHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			Font font = workbook.createFont();
			font.setBold(true);
			font.setFontHeightInPoints((short) 14);
			mainHeaderStyle.setFont(font);
			mainHeaderStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
			mainHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			Row headerRow = sheet.createRow(0);
			Cell mainHeaderCell = headerRow.createCell(0);
			mainHeaderCell.setCellValue(HEADER_MAIN_LAND_TAHASIL);
			mainHeaderCell.setCellStyle(mainHeaderStyle);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, HEADER_LAND_TAHASIL.length - 1));

			Row subHeaderRow = sheet.createRow(1);
			CellStyle headerStyle = workbook.createCellStyle();
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			Font subHeaderFont = workbook.createFont();
			subHeaderFont.setBold(true);
			headerStyle.setFont(subHeaderFont);
			for (int i = 0; i < HEADER_LAND_TAHASIL.length; i++) {
				Cell headerCell = subHeaderRow.createCell(i);
				headerCell.setCellValue(HEADER_LAND_TAHASIL[i]);
				headerCell.setCellStyle(headerStyle);
			}

			int rowIndex = 2;
			for (LandApplicationReportDTO dto : recordForLand) {
				Row dataRow = sheet.createRow(rowIndex++);
				dataRow.createCell(0).setCellValue(dto.getVillageName());
				dataRow.createCell(1).setCellValue(dto.getAreaApplied().toString());
				dataRow.createCell(2).setCellValue(dto.getTotal().toString());
				dataRow.createCell(3).setCellValue(dto.getPending().toString());
				dataRow.createCell(4).setCellValue(dto.getApproved().toString());
				dataRow.createCell(5).setCellValue(dto.getRejected().toString());
				dataRow.createCell(6).setCellValue(dto.getReverted().toString());

			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			log.info("inside error in ExcellHelper : !!!" + e.getMessage());
		}
		return null;
	}

	public static ByteArrayInputStream illegitimateLandDistrictWiseExcel(List<IllegitimateLandUseDTO> recordForLand)
			throws IOException {
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet(HEADER_DISTRICT_WISE_ILLEGITIMATE_LAND_DETAILS);

			CellStyle mainHeaderStyle = workbook.createCellStyle();
			mainHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
			mainHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			Font font = workbook.createFont();
			font.setBold(true);
			font.setFontHeightInPoints((short) 14);
			mainHeaderStyle.setFont(font);
			mainHeaderStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
			mainHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			Row headerRow = sheet.createRow(0);
			Cell mainHeaderCell = headerRow.createCell(0);
			mainHeaderCell.setCellValue(HEADER_DISTRICT_WISE_ILLEGITIMATE_LAND_DETAILS);
			mainHeaderCell.setCellStyle(mainHeaderStyle);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, HEADER_ILLEGITIMATE_DISTRICT.length - 1));

			Row subHeaderRow = sheet.createRow(1);
			CellStyle headerStyle = workbook.createCellStyle();
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			Font subHeaderFont = workbook.createFont();
			subHeaderFont.setBold(true);
			headerStyle.setFont(subHeaderFont);
			for (int i = 0; i < HEADER_ILLEGITIMATE_DISTRICT.length; i++) {
				Cell headerCell = subHeaderRow.createCell(i);
				headerCell.setCellValue(HEADER_ILLEGITIMATE_DISTRICT[i]);
				headerCell.setCellStyle(headerStyle);
			}

			int rowIndex = 2;
			for (IllegitimateLandUseDTO dto : recordForLand) {
				Row dataRow = sheet.createRow(rowIndex++);
				dataRow.createCell(0).setCellValue(dto.getDistrictName());
				dataRow.createCell(1).setCellValue(dto.getTotalApplicant().toString());
				dataRow.createCell(2).setCellValue(dto.getPending().toString());
				dataRow.createCell(3).setCellValue(dto.getAssigned().toString());
				dataRow.createCell(4).setCellValue(dto.getApplicationOnHold().toString());
				dataRow.createCell(5).setCellValue(dto.getInspectionCompleted().toString());
				dataRow.createCell(6).setCellValue(dto.getClosed().toString());
				dataRow.createCell(7).setCellValue(dto.getApplDiscarded().toString());

			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			log.info("inside error in ExcellHelper : !!!" + e.getMessage());
		}
		return null;
	}

	public static ByteArrayInputStream illegitimateLandTahasilWiseExcel(List<IllegitimateLandUseDTO> recordForLand)
			throws IOException {
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet(HEADER_DISTRICT_WISE_ILLEGITIMATE_LAND_DETAILS);

			CellStyle mainHeaderStyle = workbook.createCellStyle();
			mainHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
			mainHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			Font font = workbook.createFont();
			font.setBold(true);
			font.setFontHeightInPoints((short) 14);
			mainHeaderStyle.setFont(font);
			mainHeaderStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
			mainHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			Row headerRow = sheet.createRow(0);
			Cell mainHeaderCell = headerRow.createCell(0);
			mainHeaderCell.setCellValue(HEADER_TAHASIL_WISE_ILLEGITIMATE_LAND_DETAILS);
			mainHeaderCell.setCellStyle(mainHeaderStyle);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, HEADER_ILLEGITIMATE_TAHASIL.length - 1));

			Row subHeaderRow = sheet.createRow(1);
			CellStyle headerStyle = workbook.createCellStyle();
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			Font subHeaderFont = workbook.createFont();
			subHeaderFont.setBold(true);
			headerStyle.setFont(subHeaderFont);
			for (int i = 0; i < HEADER_ILLEGITIMATE_TAHASIL.length; i++) {
				Cell headerCell = subHeaderRow.createCell(i);
				headerCell.setCellValue(HEADER_ILLEGITIMATE_TAHASIL[i]);
				headerCell.setCellStyle(headerStyle);
			}

			int rowIndex = 2;
			for (IllegitimateLandUseDTO dto : recordForLand) {
				Row dataRow = sheet.createRow(rowIndex++);
				dataRow.createCell(0).setCellValue(dto.getTahasilName());
				dataRow.createCell(1).setCellValue(dto.getTotalApplicant().toString());
				dataRow.createCell(2).setCellValue(dto.getPending().toString());
				dataRow.createCell(3).setCellValue(dto.getAssigned().toString());
				dataRow.createCell(4).setCellValue(dto.getApplicationOnHold().toString());
				dataRow.createCell(5).setCellValue(dto.getInspectionCompleted().toString());
				dataRow.createCell(6).setCellValue(dto.getClosed().toString());
				dataRow.createCell(7).setCellValue(dto.getApplDiscarded().toString());

			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			log.info("inside error in ExcellHelper : !!!" + e.getMessage());
		}
		return null;
	}

	public static ByteArrayInputStream leaseCasedistrictRecordToExcel(List<LesaeCaseReportDTO> recordForDistrict)
			throws IOException {
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet(LEASE_CASE_DISTRICT_RECORD);

			// Create cell style for main header
			CellStyle mainHeaderStyle = workbook.createCellStyle();
			mainHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
			mainHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			Font font = workbook.createFont();
			font.setBold(true);
			font.setFontHeightInPoints((short) 14);
			mainHeaderStyle.setFont(font);
			mainHeaderStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
			mainHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			// Create header row
			Row headerRow = sheet.createRow(0);
			Cell mainHeaderCell = headerRow.createCell(0);
			mainHeaderCell.setCellValue(HEADER_MAIN_LEASE_CASE_DISTRICT);
			mainHeaderCell.setCellStyle(mainHeaderStyle);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, HEADER_LEASE_CASE_DISTRICT.length - 1)); // Merge cells
																											// for main
			// header
			// Create header for the excel value name
			Row subHeaderRow = sheet.createRow(1);
			CellStyle headerStyle = workbook.createCellStyle();
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			Font subHeaderFont = workbook.createFont();
			subHeaderFont.setBold(true);
			headerStyle.setFont(subHeaderFont);
			for (int i = 0; i < HEADER_LEASE_CASE_DISTRICT.length; i++) {
				Cell headerCell = subHeaderRow.createCell(i);
				headerCell.setCellValue(HEADER_LEASE_CASE_DISTRICT[i]);
				headerCell.setCellStyle(headerStyle);
			}

			// Fill data rows
			int rowIndex = 2; // Start from row index 2
			for (LesaeCaseReportDTO dto : recordForDistrict) {
				Row dataRow = sheet.createRow(rowIndex++);
				dataRow.createCell(0)
						.setCellValue(dto.getDistrictName() != null ? dto.getDistrictName().toString() : "");
				dataRow.createCell(1)
						.setCellValue(dto.getNoOfLeaseCseCount() != null ? dto.getNoOfLeaseCseCount().toString() : "");
				dataRow.createCell(2)
						.setCellValue(dto.getTotalPlotCount() != null ? dto.getTotalPlotCount().toString() : "");
				dataRow.createCell(3).setCellValue(dto.getDlscCount() != null ? dto.getDlscCount().toString() : "");
				dataRow.createCell(4).setCellValue(dto.getTlscCount() != null ? dto.getTlscCount().toString() : "");
				dataRow.createCell(5).setCellValue(dto.getMcCount() != null ? dto.getMcCount().toString() : "");
			}
			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			log.info("inside error in ExcellHelper : !!!" + e.getMessage());
		}
		return null;

	}

	public static ByteArrayInputStream leaseCaseTahasilRecordToExcel(List<LesaeCaseReportDTO> recordForDistrict)
			throws IOException {
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet(LEASE_CASE_TAHASIL_RECORD);

			// Create cell style for main header
			CellStyle mainHeaderStyle = workbook.createCellStyle();
			mainHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
			mainHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			Font font = workbook.createFont();
			font.setBold(true);
			font.setFontHeightInPoints((short) 14);
			mainHeaderStyle.setFont(font);
			mainHeaderStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
			mainHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			// Create header row
			Row headerRow = sheet.createRow(0);
			Cell mainHeaderCell = headerRow.createCell(0);
			mainHeaderCell.setCellValue(HEADER_MAIN_LEASE_CASE_TAHASIL);
			mainHeaderCell.setCellStyle(mainHeaderStyle);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, HEADER_LEASE_CASE_TAHASIL.length - 1)); // Merge cells
																										// for main
			// header
			// Create header for the excel value name
			Row subHeaderRow = sheet.createRow(1);
			CellStyle headerStyle = workbook.createCellStyle();
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			Font subHeaderFont = workbook.createFont();
			subHeaderFont.setBold(true);
			headerStyle.setFont(subHeaderFont);
			for (int i = 0; i < HEADER_LEASE_CASE_TAHASIL.length; i++) {
				Cell headerCell = subHeaderRow.createCell(i);
				headerCell.setCellValue(HEADER_LEASE_CASE_TAHASIL[i]);
				headerCell.setCellStyle(headerStyle);
			}

			// Fill data rows
			int rowIndex = 2; // Start from row index 2
			for (LesaeCaseReportDTO dto : recordForDistrict) {
				Row dataRow = sheet.createRow(rowIndex++);
				dataRow.createCell(0).setCellValue(dto.getTahasilName() != null ? dto.getTahasilName().toString() : "");
				dataRow.createCell(1)
						.setCellValue(dto.getNoOfLeaseCseCount() != null ? dto.getNoOfLeaseCseCount().toString() : "");
				dataRow.createCell(2)
						.setCellValue(dto.getTotalPlotCount() != null ? dto.getTotalPlotCount().toString() : "");
				dataRow.createCell(3).setCellValue(dto.getDlscCount() != null ? dto.getDlscCount().toString() : "");
				dataRow.createCell(4).setCellValue(dto.getTlscCount() != null ? dto.getTlscCount().toString() : "");
				dataRow.createCell(5).setCellValue(dto.getMcCount() != null ? dto.getMcCount().toString() : "");
			}
			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			log.info("inside error in ExcellHelper : !!!" + e.getMessage());
		}
		return null;

	}

	public static ByteArrayInputStream leaseCaseVillageRecordToExcel(List<LesaeCaseReportDTO> recordForDistrict)
			throws IOException {
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet(LEASE_CASE_VILLAGE_RECORD);

			// Create cell style for main header
			CellStyle mainHeaderStyle = workbook.createCellStyle();
			mainHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
			mainHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			Font font = workbook.createFont();
			font.setBold(true);
			font.setFontHeightInPoints((short) 14);
			mainHeaderStyle.setFont(font);
			mainHeaderStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
			mainHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			// Create header row
			Row headerRow = sheet.createRow(0);
			Cell mainHeaderCell = headerRow.createCell(0);
			mainHeaderCell.setCellValue(HEADER_MAIN_LEASE_CASE_VILLAGE);
			mainHeaderCell.setCellStyle(mainHeaderStyle);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, HEADER_LEASE_CASE_VILLAGE.length - 1)); // Merge cells
																										// for main
			// header
			// Create header for the excel value name
			Row subHeaderRow = sheet.createRow(1);
			CellStyle headerStyle = workbook.createCellStyle();
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			Font subHeaderFont = workbook.createFont();
			subHeaderFont.setBold(true);
			headerStyle.setFont(subHeaderFont);
			for (int i = 0; i < HEADER_LEASE_CASE_VILLAGE.length; i++) {
				Cell headerCell = subHeaderRow.createCell(i);
				headerCell.setCellValue(HEADER_LEASE_CASE_VILLAGE[i]);
				headerCell.setCellStyle(headerStyle);
			}

			// Fill data rows
			int rowIndex = 2; // Start from row index 2
			for (LesaeCaseReportDTO dto : recordForDistrict) {
				Row dataRow = sheet.createRow(rowIndex++);
				dataRow.createCell(0).setCellValue(dto.getVillageName() != null ? dto.getVillageName().toString() : "");
				dataRow.createCell(1)
						.setCellValue(dto.getNoOfLeaseCseCount() != null ? dto.getNoOfLeaseCseCount().toString() : "");
				dataRow.createCell(2)
						.setCellValue(dto.getTotalPlotCount() != null ? dto.getTotalPlotCount().toString() : "");
				dataRow.createCell(3).setCellValue(dto.getDlscCount() != null ? dto.getDlscCount().toString() : "");
				dataRow.createCell(4).setCellValue(dto.getTlscCount() != null ? dto.getTlscCount().toString() : "");
				dataRow.createCell(5).setCellValue(dto.getMcCount() != null ? dto.getMcCount().toString() : "");
			}
			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			log.info("inside error in ExcellHelper : !!!" + e.getMessage());
		}
		return null;

	}

	public static ByteArrayInputStream leaseCaseKhataRecordToExcel(List<LesaeCaseReportDTO> recordForDistrict)
			throws IOException {
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet(LEASE_CASE_KHATA_RECORD);

			// Create cell style for main header
			CellStyle mainHeaderStyle = workbook.createCellStyle();
			mainHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
			mainHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			Font font = workbook.createFont();
			font.setBold(true);
			font.setFontHeightInPoints((short) 14);
			mainHeaderStyle.setFont(font);
			mainHeaderStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
			mainHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			// Create header row
			Row headerRow = sheet.createRow(0);
			Cell mainHeaderCell = headerRow.createCell(0);
			mainHeaderCell.setCellValue(HEADER_MAIN_LEASE_CASE_KHATA);
			mainHeaderCell.setCellStyle(mainHeaderStyle);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, HEADER_LEASE_CASE_KHATA.length - 1)); // Merge cells
																										// for main
			// header
			// Create header for the excel value name
			Row subHeaderRow = sheet.createRow(1);
			CellStyle headerStyle = workbook.createCellStyle();
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			Font subHeaderFont = workbook.createFont();
			subHeaderFont.setBold(true);
			headerStyle.setFont(subHeaderFont);
			for (int i = 0; i < HEADER_LEASE_CASE_KHATA.length; i++) {
				Cell headerCell = subHeaderRow.createCell(i);
				headerCell.setCellValue(HEADER_LEASE_CASE_KHATA[i]);
				headerCell.setCellStyle(headerStyle);
			}

			// Fill data rows
			int rowIndex = 2; // Start from row index 2
			for (LesaeCaseReportDTO dto : recordForDistrict) {
				Row dataRow = sheet.createRow(rowIndex++);
				dataRow.createCell(0).setCellValue(dto.getKhataNo() != null ? dto.getKhataNo().toString() : "");
				dataRow.createCell(1)
						.setCellValue(dto.getNoOfLeaseCseCount() != null ? dto.getNoOfLeaseCseCount().toString() : "");
				dataRow.createCell(2)
						.setCellValue(dto.getTotalPlotCount() != null ? dto.getTotalPlotCount().toString() : "");
				dataRow.createCell(3).setCellValue(dto.getDlscCount() != null ? dto.getDlscCount().toString() : "");
				dataRow.createCell(4).setCellValue(dto.getTlscCount() != null ? dto.getTlscCount().toString() : "");
				dataRow.createCell(5).setCellValue(dto.getMcCount() != null ? dto.getMcCount().toString() : "");
			}
			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			log.info("inside error in ExcellHelper : !!!" + e.getMessage());
		}
		return null;

	}

	public static ByteArrayInputStream leaseCasePlotRecordToExcel(List<LesaeCaseReportDTO> recordForDistrict)
			throws IOException {
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet(LEASE_CASE_PLOT_RECORD);

			// Create cell style for main header
			CellStyle mainHeaderStyle = workbook.createCellStyle();
			mainHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
			mainHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			Font font = workbook.createFont();
			font.setBold(true);
			font.setFontHeightInPoints((short) 14);
			mainHeaderStyle.setFont(font);
			mainHeaderStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
			mainHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			// Create header row
			Row headerRow = sheet.createRow(0);
			Cell mainHeaderCell = headerRow.createCell(0);
			mainHeaderCell.setCellValue(HEADER_MAIN_LEASE_CASE_PLOT);
			mainHeaderCell.setCellStyle(mainHeaderStyle);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, HEADER_LEASE_CASE_PLOT.length - 1)); // Merge cells
																										// for main
			// header
			// Create header for the excel value name
			Row subHeaderRow = sheet.createRow(1);
			CellStyle headerStyle = workbook.createCellStyle();
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			Font subHeaderFont = workbook.createFont();
			subHeaderFont.setBold(true);
			headerStyle.setFont(subHeaderFont);
			for (int i = 0; i < HEADER_LEASE_CASE_PLOT.length; i++) {
				Cell headerCell = subHeaderRow.createCell(i);
				headerCell.setCellValue(HEADER_LEASE_CASE_PLOT[i]);
				headerCell.setCellStyle(headerStyle);
			}

			// Fill data rows
			int rowIndex = 2; // Start from row index 2
			for (LesaeCaseReportDTO dto : recordForDistrict) {
				Row dataRow = sheet.createRow(rowIndex++);
				dataRow.createCell(0).setCellValue(dto.getPlotNo() != null ? dto.getPlotNo().toString() : "");
				String fieldInqueryStatus = "";
				if (dto.getFieldInquery() == 1) {
					fieldInqueryStatus = "YES";
				} else if (dto.getFieldInquery() == 2) {
					fieldInqueryStatus = "NO";
				} else {
					fieldInqueryStatus = "N/A";
				}
				dataRow.createCell(1).setCellValue(fieldInqueryStatus != null ? fieldInqueryStatus.toString() : "");
				dataRow.createCell(2).setCellValue(dto.getDlsc() != null ? dto.getDlsc().toString() : "");
				dataRow.createCell(3).setCellValue(dto.getTlsc() != null ? dto.getTlsc().toString() : "");
				dataRow.createCell(4).setCellValue(dto.getMc() != null ? dto.getMc().toString() : "");
				String notIssue = "";
				if (dto.getNoticeIssue() == 1) {
					notIssue = "YES";
				} else if (dto.getNoticeIssue() == 2) {
					notIssue = "NO";
				} else {
					notIssue = "N/A";
				}
				dataRow.createCell(5).setCellValue(notIssue != null ? notIssue.toString() : "");
				String consignMony = "";
				if (dto.getConsiderMonyDeposite() == 1) {
					consignMony = "YES";
				} else if (dto.getConsiderMonyDeposite() == 2) {
					consignMony = "NO";
				} else {
					consignMony = "N/A";
				}
				dataRow.createCell(6).setCellValue(consignMony != null ? consignMony.toString() : "");
				dataRow.createCell(7).setCellValue(dto.getRemerk() != null ? dto.getRemerk().toString() : "");
			}
			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			log.info("inside error in ExcellHelper : !!!" + e.getMessage());
		}
		return null;

	}

	public static ByteArrayInputStream paymentReportToExcel(List<PaymentCollectionHistoryDTO> reportList) {
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet(PAYMENT_COLLECTION_REPORT);

			CellStyle mainHeaderStyle = workbook.createCellStyle();
			mainHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
			mainHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			Font font = workbook.createFont();
			font.setBold(true);
			font.setFontHeightInPoints((short) 14);
			mainHeaderStyle.setFont(font);
			mainHeaderStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
			mainHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			Row headerRow = sheet.createRow(0);
			Cell mainHeaderCell = headerRow.createCell(0);
			mainHeaderCell.setCellValue(PAYMENT_COLLECTION_HISTORY_REPORT);
			mainHeaderCell.setCellStyle(mainHeaderStyle);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, HEADER_PAYMENT_COLLECTION.length - 1));

			Row subHeaderRow = sheet.createRow(1);
			CellStyle headerStyle = workbook.createCellStyle();
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			Font subHeaderFont = workbook.createFont();
			subHeaderFont.setBold(true);
			headerStyle.setFont(subHeaderFont);
			for (int i = 0; i < HEADER_PAYMENT_COLLECTION.length; i++) {
				Cell headerCell = subHeaderRow.createCell(i);
				headerCell.setCellValue(HEADER_PAYMENT_COLLECTION[i]);
				headerCell.setCellStyle(headerStyle);
			}

			int rowIndex = 2;
			for (PaymentCollectionHistoryDTO dto : reportList) {
				Row dataRow = sheet.createRow(rowIndex++);
				dataRow.createCell(0).setCellValue(dto.getDistrictName());
				dataRow.createCell(1).setCellValue(dto.getJanuary() != null ? dto.getJanuary().toString() : "");
				dataRow.createCell(2).setCellValue(dto.getFebruary() != null ? dto.getFebruary().toString() : "");
				dataRow.createCell(3).setCellValue(dto.getMarch() != null ? dto.getMarch().toString() : "");
				dataRow.createCell(4).setCellValue(dto.getApril() != null ? dto.getApril().toString() : "");
				dataRow.createCell(5).setCellValue(dto.getMay() != null ? dto.getMay().toString() : "");
				dataRow.createCell(6).setCellValue(dto.getJune() != null ? dto.getJune().toString() : "");
				dataRow.createCell(7).setCellValue(dto.getJuly() != null ? dto.getJuly().toString() : "");
				dataRow.createCell(8).setCellValue(dto.getAugust() != null ? dto.getAugust().toString() : "");
				dataRow.createCell(9).setCellValue(dto.getSeptember() != null ? dto.getSeptember().toString() : "");
				dataRow.createCell(10).setCellValue(dto.getOctober() != null ? dto.getOctober().toString() : "");
				dataRow.createCell(11).setCellValue(dto.getNovember() != null ? dto.getNovember().toString() : "");
				dataRow.createCell(12).setCellValue(dto.getDecember() != null ? dto.getDecember().toString() : "");
				dataRow.createCell(13)
						.setCellValue(dto.getTotalAmount() != null ? dto.getTotalAmount().toString() : "");

			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			log.info("inside error in ExcellHelper : !!!" + e.getMessage());
		}
		return null;

	}

	public static ByteArrayInputStream tahasilPaymentReportToExcel(List<PaymentCollectionHistoryDTO> reportList) {
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet(PAYMENT_COLLECTION_REPORT);

			CellStyle mainHeaderStyle = workbook.createCellStyle();
			mainHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
			mainHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			Font font = workbook.createFont();
			font.setBold(true);
			font.setFontHeightInPoints((short) 14);
			mainHeaderStyle.setFont(font);
			mainHeaderStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
			mainHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			Row headerRow = sheet.createRow(0);
			Cell mainHeaderCell = headerRow.createCell(0);
			mainHeaderCell.setCellValue(PAYMENT_COLLECTION_HISTORY_REPORT);
			mainHeaderCell.setCellStyle(mainHeaderStyle);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, TAHASIL_PAYMENT_COLLECTION.length - 1));

			Row subHeaderRow = sheet.createRow(1);
			CellStyle headerStyle = workbook.createCellStyle();
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			Font subHeaderFont = workbook.createFont();
			subHeaderFont.setBold(true);
			headerStyle.setFont(subHeaderFont);
			for (int i = 0; i < TAHASIL_PAYMENT_COLLECTION.length; i++) {
				Cell headerCell = subHeaderRow.createCell(i);
				headerCell.setCellValue(TAHASIL_PAYMENT_COLLECTION[i]);
				headerCell.setCellStyle(headerStyle);
			}

			int rowIndex = 2;
			for (PaymentCollectionHistoryDTO dto : reportList) {
				Row dataRow = sheet.createRow(rowIndex++);
				dataRow.createCell(0).setCellValue(dto.getTahasilName());
				dataRow.createCell(1).setCellValue(dto.getJanuary() != null ? dto.getJanuary().toString() : "");
				dataRow.createCell(2).setCellValue(dto.getFebruary() != null ? dto.getFebruary().toString() : "");
				dataRow.createCell(3).setCellValue(dto.getMarch() != null ? dto.getMarch().toString() : "");
				dataRow.createCell(4).setCellValue(dto.getApril() != null ? dto.getApril().toString() : "");
				dataRow.createCell(5).setCellValue(dto.getMay() != null ? dto.getMay().toString() : "");
				dataRow.createCell(6).setCellValue(dto.getJune() != null ? dto.getJune().toString() : "");
				dataRow.createCell(7).setCellValue(dto.getJuly() != null ? dto.getJuly().toString() : "");
				dataRow.createCell(8).setCellValue(dto.getAugust() != null ? dto.getAugust().toString() : "");
				dataRow.createCell(9).setCellValue(dto.getSeptember() != null ? dto.getSeptember().toString() : "");
				dataRow.createCell(10).setCellValue(dto.getOctober() != null ? dto.getOctober().toString() : "");
				dataRow.createCell(11).setCellValue(dto.getNovember() != null ? dto.getNovember().toString() : "");
				dataRow.createCell(12).setCellValue(dto.getDecember() != null ? dto.getDecember().toString() : "");
				dataRow.createCell(13)
						.setCellValue(dto.getTotalAmount() != null ? dto.getTotalAmount().toString() : "");

			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			log.info("inside error in ExcellHelper : !!!" + e.getMessage());
		}
		return null;

	}

	public static ByteArrayInputStream villagePaymentReportToExcel(List<PaymentCollectionHistoryDTO> reportList) {
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet(PAYMENT_COLLECTION_REPORT);

			CellStyle mainHeaderStyle = workbook.createCellStyle();
			mainHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
			mainHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			Font font = workbook.createFont();
			font.setBold(true);
			font.setFontHeightInPoints((short) 14);
			mainHeaderStyle.setFont(font);
			mainHeaderStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
			mainHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			Row headerRow = sheet.createRow(0);
			Cell mainHeaderCell = headerRow.createCell(0);
			mainHeaderCell.setCellValue(PAYMENT_COLLECTION_HISTORY_REPORT);
			mainHeaderCell.setCellStyle(mainHeaderStyle);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, VILLAGE_PAYMENT_COLLECTION.length - 1));

			Row subHeaderRow = sheet.createRow(1);
			CellStyle headerStyle = workbook.createCellStyle();
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			Font subHeaderFont = workbook.createFont();
			subHeaderFont.setBold(true);
			headerStyle.setFont(subHeaderFont);
			for (int i = 0; i < VILLAGE_PAYMENT_COLLECTION.length; i++) {
				Cell headerCell = subHeaderRow.createCell(i);
				headerCell.setCellValue(VILLAGE_PAYMENT_COLLECTION[i]);
				headerCell.setCellStyle(headerStyle);
			}

			int rowIndex = 2;
			for (PaymentCollectionHistoryDTO dto : reportList) {
				Row dataRow = sheet.createRow(rowIndex++);
				dataRow.createCell(0).setCellValue(dto.getVillageName());
				dataRow.createCell(1).setCellValue(dto.getJanuary() != null ? dto.getJanuary().toString() : "");
				dataRow.createCell(2).setCellValue(dto.getFebruary() != null ? dto.getFebruary().toString() : "");
				dataRow.createCell(3).setCellValue(dto.getMarch() != null ? dto.getMarch().toString() : "");
				dataRow.createCell(4).setCellValue(dto.getApril() != null ? dto.getApril().toString() : "");
				dataRow.createCell(5).setCellValue(dto.getMay() != null ? dto.getMay().toString() : "");
				dataRow.createCell(6).setCellValue(dto.getJune() != null ? dto.getJune().toString() : "");
				dataRow.createCell(7).setCellValue(dto.getJuly() != null ? dto.getJuly().toString() : "");
				dataRow.createCell(8).setCellValue(dto.getAugust() != null ? dto.getAugust().toString() : "");
				dataRow.createCell(9).setCellValue(dto.getSeptember() != null ? dto.getSeptember().toString() : "");
				dataRow.createCell(10).setCellValue(dto.getOctober() != null ? dto.getOctober().toString() : "");
				dataRow.createCell(11).setCellValue(dto.getNovember() != null ? dto.getNovember().toString() : "");
				dataRow.createCell(12).setCellValue(dto.getDecember() != null ? dto.getDecember().toString() : "");
				dataRow.createCell(13)
						.setCellValue(dto.getTotalAmount() != null ? dto.getTotalAmount().toString() : "");

			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			log.info("inside error in ExcellHelper : !!!" + e.getMessage());
		}
		return null;

	}

	public static ByteArrayInputStream landAllotPaymentReportToExcel(List<PaymentCollectionHistoryDTO> reportList) {
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet(LAND_ALLOTMENT_PAYMENT_COLLECTION_REPORT);

			CellStyle mainHeaderStyle = workbook.createCellStyle();
			mainHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
			mainHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			Font font = workbook.createFont();
			font.setBold(true);
			font.setFontHeightInPoints((short) 14);
			mainHeaderStyle.setFont(font);
			mainHeaderStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
			mainHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			Row headerRow = sheet.createRow(0);
			Cell mainHeaderCell = headerRow.createCell(0);
			mainHeaderCell.setCellValue(LAND_ALLOTMENT_PAYMENT_COLLECTION_HISTORY_REPORT);
			mainHeaderCell.setCellStyle(mainHeaderStyle);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, HEADER_PAYMENT_COLLECTION.length - 1));

			Row subHeaderRow = sheet.createRow(1);
			CellStyle headerStyle = workbook.createCellStyle();
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			Font subHeaderFont = workbook.createFont();
			subHeaderFont.setBold(true);
			headerStyle.setFont(subHeaderFont);
			for (int i = 0; i < HEADER_PAYMENT_COLLECTION.length; i++) {
				Cell headerCell = subHeaderRow.createCell(i);
				headerCell.setCellValue(HEADER_PAYMENT_COLLECTION[i]);
				headerCell.setCellStyle(headerStyle);
			}

			int rowIndex = 2;
			for (PaymentCollectionHistoryDTO dto : reportList) {
				Row dataRow = sheet.createRow(rowIndex++);
				dataRow.createCell(0).setCellValue(dto.getDistrictName());
				dataRow.createCell(1).setCellValue(dto.getJanuary() != null ? dto.getJanuary().toString() : "");
				dataRow.createCell(2).setCellValue(dto.getFebruary() != null ? dto.getFebruary().toString() : "");
				dataRow.createCell(3).setCellValue(dto.getMarch() != null ? dto.getMarch().toString() : "");
				dataRow.createCell(4).setCellValue(dto.getApril() != null ? dto.getApril().toString() : "");
				dataRow.createCell(5).setCellValue(dto.getMay() != null ? dto.getMay().toString() : "");
				dataRow.createCell(6).setCellValue(dto.getJune() != null ? dto.getJune().toString() : "");
				dataRow.createCell(7).setCellValue(dto.getJuly() != null ? dto.getJuly().toString() : "");
				dataRow.createCell(8).setCellValue(dto.getAugust() != null ? dto.getAugust().toString() : "");
				dataRow.createCell(9).setCellValue(dto.getSeptember() != null ? dto.getSeptember().toString() : "");
				dataRow.createCell(10).setCellValue(dto.getOctober() != null ? dto.getOctober().toString() : "");
				dataRow.createCell(11).setCellValue(dto.getNovember() != null ? dto.getNovember().toString() : "");
				dataRow.createCell(12).setCellValue(dto.getDecember() != null ? dto.getDecember().toString() : "");
				dataRow.createCell(13)
						.setCellValue(dto.getTotalAmount() != null ? dto.getTotalAmount().toString() : "");

			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			log.info("inside error in ExcellHelper : !!!" + e.getMessage());
		}
		return null;

	}

	public static ByteArrayInputStream tahasilLandAllotPaymentReportToExcel(
			List<PaymentCollectionHistoryDTO> reportList) {

		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet(LAND_ALLOTMENT_PAYMENT_COLLECTION_REPORT);

			CellStyle mainHeaderStyle = workbook.createCellStyle();
			mainHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
			mainHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			Font font = workbook.createFont();
			font.setBold(true);
			font.setFontHeightInPoints((short) 14);
			mainHeaderStyle.setFont(font);
			mainHeaderStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
			mainHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			Row headerRow = sheet.createRow(0);
			Cell mainHeaderCell = headerRow.createCell(0);
			mainHeaderCell.setCellValue(LAND_ALLOTMENT_PAYMENT_COLLECTION_HISTORY_REPORT);
			mainHeaderCell.setCellStyle(mainHeaderStyle);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, TAHASIL_PAYMENT_COLLECTION.length - 1));

			Row subHeaderRow = sheet.createRow(1);
			CellStyle headerStyle = workbook.createCellStyle();
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			Font subHeaderFont = workbook.createFont();
			subHeaderFont.setBold(true);
			headerStyle.setFont(subHeaderFont);
			for (int i = 0; i < TAHASIL_PAYMENT_COLLECTION.length; i++) {
				Cell headerCell = subHeaderRow.createCell(i);
				headerCell.setCellValue(TAHASIL_PAYMENT_COLLECTION[i]);
				headerCell.setCellStyle(headerStyle);
			}

			int rowIndex = 2;
			for (PaymentCollectionHistoryDTO dto : reportList) {
				Row dataRow = sheet.createRow(rowIndex++);
				dataRow.createCell(0).setCellValue(dto.getTahasilName());
				dataRow.createCell(1).setCellValue(dto.getJanuary() != null ? dto.getJanuary().toString() : "");
				dataRow.createCell(2).setCellValue(dto.getFebruary() != null ? dto.getFebruary().toString() : "");
				dataRow.createCell(3).setCellValue(dto.getMarch() != null ? dto.getMarch().toString() : "");
				dataRow.createCell(4).setCellValue(dto.getApril() != null ? dto.getApril().toString() : "");
				dataRow.createCell(5).setCellValue(dto.getMay() != null ? dto.getMay().toString() : "");
				dataRow.createCell(6).setCellValue(dto.getJune() != null ? dto.getJune().toString() : "");
				dataRow.createCell(7).setCellValue(dto.getJuly() != null ? dto.getJuly().toString() : "");
				dataRow.createCell(8).setCellValue(dto.getAugust() != null ? dto.getAugust().toString() : "");
				dataRow.createCell(9).setCellValue(dto.getSeptember() != null ? dto.getSeptember().toString() : "");
				dataRow.createCell(10).setCellValue(dto.getOctober() != null ? dto.getOctober().toString() : "");
				dataRow.createCell(11).setCellValue(dto.getNovember() != null ? dto.getNovember().toString() : "");
				dataRow.createCell(12).setCellValue(dto.getDecember() != null ? dto.getDecember().toString() : "");
				dataRow.createCell(13)
						.setCellValue(dto.getTotalAmount() != null ? dto.getTotalAmount().toString() : "");

			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			log.info("inside error in ExcellHelper : !!!" + e.getMessage());
		}
		return null;

	}

	public static ByteArrayInputStream villageLandAllotPaymentReportToExcel(
			List<PaymentCollectionHistoryDTO> reportList) {
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet(LAND_ALLOTMENT_PAYMENT_COLLECTION_REPORT);

			CellStyle mainHeaderStyle = workbook.createCellStyle();
			mainHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
			mainHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			Font font = workbook.createFont();
			font.setBold(true);
			font.setFontHeightInPoints((short) 14);
			mainHeaderStyle.setFont(font);
			mainHeaderStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
			mainHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			Row headerRow = sheet.createRow(0);
			Cell mainHeaderCell = headerRow.createCell(0);
			mainHeaderCell.setCellValue(LAND_ALLOTMENT_PAYMENT_COLLECTION_HISTORY_REPORT);
			mainHeaderCell.setCellStyle(mainHeaderStyle);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, VILLAGE_PAYMENT_COLLECTION.length - 1));

			Row subHeaderRow = sheet.createRow(1);
			CellStyle headerStyle = workbook.createCellStyle();
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			Font subHeaderFont = workbook.createFont();
			subHeaderFont.setBold(true);
			headerStyle.setFont(subHeaderFont);
			for (int i = 0; i < VILLAGE_PAYMENT_COLLECTION.length; i++) {
				Cell headerCell = subHeaderRow.createCell(i);
				headerCell.setCellValue(VILLAGE_PAYMENT_COLLECTION[i]);
				headerCell.setCellStyle(headerStyle);
			}

			int rowIndex = 2;
			for (PaymentCollectionHistoryDTO dto : reportList) {
				Row dataRow = sheet.createRow(rowIndex++);
				dataRow.createCell(0).setCellValue(dto.getVillageName());
				dataRow.createCell(1).setCellValue(dto.getJanuary() != null ? dto.getJanuary().toString() : "");
				dataRow.createCell(2).setCellValue(dto.getFebruary() != null ? dto.getFebruary().toString() : "");
				dataRow.createCell(3).setCellValue(dto.getMarch() != null ? dto.getMarch().toString() : "");
				dataRow.createCell(4).setCellValue(dto.getApril() != null ? dto.getApril().toString() : "");
				dataRow.createCell(5).setCellValue(dto.getMay() != null ? dto.getMay().toString() : "");
				dataRow.createCell(6).setCellValue(dto.getJune() != null ? dto.getJune().toString() : "");
				dataRow.createCell(7).setCellValue(dto.getJuly() != null ? dto.getJuly().toString() : "");
				dataRow.createCell(8).setCellValue(dto.getAugust() != null ? dto.getAugust().toString() : "");
				dataRow.createCell(9).setCellValue(dto.getSeptember() != null ? dto.getSeptember().toString() : "");
				dataRow.createCell(10).setCellValue(dto.getOctober() != null ? dto.getOctober().toString() : "");
				dataRow.createCell(11).setCellValue(dto.getNovember() != null ? dto.getNovember().toString() : "");
				dataRow.createCell(12).setCellValue(dto.getDecember() != null ? dto.getDecember().toString() : "");
				dataRow.createCell(13)
						.setCellValue(dto.getTotalAmount() != null ? dto.getTotalAmount().toString() : "");

			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			log.info("inside error in ExcellHelper : !!!" + e.getMessage());
		}
		return null;

	}

	public static ByteArrayInputStream khatianLandAllotPaymentReportToExcel(
			List<PaymentCollectionHistoryDTO> reportList) {
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet(LAND_ALLOTMENT_PAYMENT_COLLECTION_REPORT);

			CellStyle mainHeaderStyle = workbook.createCellStyle();
			mainHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
			mainHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			Font font = workbook.createFont();
			font.setBold(true);
			font.setFontHeightInPoints((short) 14);
			mainHeaderStyle.setFont(font);
			mainHeaderStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
			mainHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			Row headerRow = sheet.createRow(0);
			Cell mainHeaderCell = headerRow.createCell(0);
			mainHeaderCell.setCellValue(LAND_ALLOTMENT_PAYMENT_COLLECTION_HISTORY_REPORT);
			mainHeaderCell.setCellStyle(mainHeaderStyle);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, KHATIAN_PAYMENT_COLLECTION.length - 1));

			Row subHeaderRow = sheet.createRow(1);
			CellStyle headerStyle = workbook.createCellStyle();
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			Font subHeaderFont = workbook.createFont();
			subHeaderFont.setBold(true);
			headerStyle.setFont(subHeaderFont);
			for (int i = 0; i < KHATIAN_PAYMENT_COLLECTION.length; i++) {
				Cell headerCell = subHeaderRow.createCell(i);
				headerCell.setCellValue(KHATIAN_PAYMENT_COLLECTION[i]);
				headerCell.setCellStyle(headerStyle);
			}

			int rowIndex = 2;
			for (PaymentCollectionHistoryDTO dto : reportList) {
				Row dataRow = sheet.createRow(rowIndex++);
				dataRow.createCell(0).setCellValue(dto.getKhataNo());
				dataRow.createCell(1).setCellValue(dto.getJanuary() != null ? dto.getJanuary().toString() : "");
				dataRow.createCell(2).setCellValue(dto.getFebruary() != null ? dto.getFebruary().toString() : "");
				dataRow.createCell(3).setCellValue(dto.getMarch() != null ? dto.getMarch().toString() : "");
				dataRow.createCell(4).setCellValue(dto.getApril() != null ? dto.getApril().toString() : "");
				dataRow.createCell(5).setCellValue(dto.getMay() != null ? dto.getMay().toString() : "");
				dataRow.createCell(6).setCellValue(dto.getJune() != null ? dto.getJune().toString() : "");
				dataRow.createCell(7).setCellValue(dto.getJuly() != null ? dto.getJuly().toString() : "");
				dataRow.createCell(8).setCellValue(dto.getAugust() != null ? dto.getAugust().toString() : "");
				dataRow.createCell(9).setCellValue(dto.getSeptember() != null ? dto.getSeptember().toString() : "");
				dataRow.createCell(10).setCellValue(dto.getOctober() != null ? dto.getOctober().toString() : "");
				dataRow.createCell(11).setCellValue(dto.getNovember() != null ? dto.getNovember().toString() : "");
				dataRow.createCell(12).setCellValue(dto.getDecember() != null ? dto.getDecember().toString() : "");
				dataRow.createCell(13)
						.setCellValue(dto.getTotalAmount() != null ? dto.getTotalAmount().toString() : "");

			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			log.info("inside error in ExcellHelper : !!!" + e.getMessage());
		}
		return null;

	}

	public static ByteArrayInputStream plotLandAllotPaymentReportToExcel(List<PaymentCollectionHistoryDTO> reportList) {
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet(LAND_ALLOTMENT_PAYMENT_COLLECTION_REPORT);

			CellStyle mainHeaderStyle = workbook.createCellStyle();
			mainHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
			mainHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			Font font = workbook.createFont();
			font.setBold(true);
			font.setFontHeightInPoints((short) 14);
			mainHeaderStyle.setFont(font);
			mainHeaderStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
			mainHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			Row headerRow = sheet.createRow(0);
			Cell mainHeaderCell = headerRow.createCell(0);
			mainHeaderCell.setCellValue(LAND_ALLOTMENT_PAYMENT_COLLECTION_HISTORY_REPORT);
			mainHeaderCell.setCellStyle(mainHeaderStyle);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, PLOT_PAYMENT_COLLECTION.length - 1));

			Row subHeaderRow = sheet.createRow(1);
			CellStyle headerStyle = workbook.createCellStyle();
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			Font subHeaderFont = workbook.createFont();
			subHeaderFont.setBold(true);
			headerStyle.setFont(subHeaderFont);
			for (int i = 0; i < PLOT_PAYMENT_COLLECTION.length; i++) {
				Cell headerCell = subHeaderRow.createCell(i);
				headerCell.setCellValue(PLOT_PAYMENT_COLLECTION[i]);
				headerCell.setCellStyle(headerStyle);
			}

			int rowIndex = 2;
			for (PaymentCollectionHistoryDTO dto : reportList) {
				Row dataRow = sheet.createRow(rowIndex++);
				dataRow.createCell(0).setCellValue(dto.getPlotNo());
				dataRow.createCell(1).setCellValue(dto.getJanuary() != null ? dto.getJanuary().toString() : "");
				dataRow.createCell(2).setCellValue(dto.getFebruary() != null ? dto.getFebruary().toString() : "");
				dataRow.createCell(3).setCellValue(dto.getMarch() != null ? dto.getMarch().toString() : "");
				dataRow.createCell(4).setCellValue(dto.getApril() != null ? dto.getApril().toString() : "");
				dataRow.createCell(5).setCellValue(dto.getMay() != null ? dto.getMay().toString() : "");
				dataRow.createCell(6).setCellValue(dto.getJune() != null ? dto.getJune().toString() : "");
				dataRow.createCell(7).setCellValue(dto.getJuly() != null ? dto.getJuly().toString() : "");
				dataRow.createCell(8).setCellValue(dto.getAugust() != null ? dto.getAugust().toString() : "");
				dataRow.createCell(9).setCellValue(dto.getSeptember() != null ? dto.getSeptember().toString() : "");
				dataRow.createCell(10).setCellValue(dto.getOctober() != null ? dto.getOctober().toString() : "");
				dataRow.createCell(11).setCellValue(dto.getNovember() != null ? dto.getNovember().toString() : "");
				dataRow.createCell(12).setCellValue(dto.getDecember() != null ? dto.getDecember().toString() : "");
				dataRow.createCell(13)
						.setCellValue(dto.getTotalAmount() != null ? dto.getTotalAmount().toString() : "");
				dataRow.createCell(14)
						.setCellValue(dto.getApplicantName() != null ? dto.getApplicantName().toString() : "");
				dataRow.createCell(15)
						.setCellValue(dto.getPurchaseArea() != null ? dto.getPurchaseArea().toString() : "");
				dataRow.createCell(16).setCellValue(dto.getTotalPrice() != null ? dto.getTotalPrice().toString() : "");
				dataRow.createCell(17)
						.setCellValue(dto.getLandOrderNo() != null ? dto.getLandOrderNo().toString() : "");
				Date landOrderDate = dto.getLandOrderDate();
				dataRow.createCell(18).setCellValue(dto.getLandOrderDate() != null ? landOrderDate.toString() : "");

			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			log.info("inside error in ExcellHelper : !!!" + e.getMessage());
		}
		return null;

	}

	public static ByteArrayInputStream getAuctionReport(List<AuctionReportDTO> recordForDistrict) throws IOException {
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet(AUCTION_RECORD);

			// Create cell style for main header
			CellStyle mainHeaderStyle = workbook.createCellStyle();
			mainHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
			mainHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			Font font = workbook.createFont();
			font.setBold(true);
			font.setFontHeightInPoints((short) 14);
			mainHeaderStyle.setFont(font);
			mainHeaderStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
			mainHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			// Create header row
			Row headerRow = sheet.createRow(0);
			Cell mainHeaderCell = headerRow.createCell(0);
			mainHeaderCell.setCellValue(AUCTION_REPORT);
			mainHeaderCell.setCellStyle(mainHeaderStyle);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, HEADER_AUCTION.length - 1)); // Merge cells for main
																								// header

			// Create header for the excel value name
			Row subHeaderRow = sheet.createRow(1);
			CellStyle headerStyle = workbook.createCellStyle();
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			Font subHeaderFont = workbook.createFont();
			subHeaderFont.setBold(true);
			headerStyle.setFont(subHeaderFont);
			for (int i = 0; i < HEADER_AUCTION.length; i++) {
				Cell headerCell = subHeaderRow.createCell(i);
				headerCell.setCellValue(HEADER_AUCTION[i]);
				headerCell.setCellStyle(headerStyle);
			}

			// Fill data rows
			int rowIndex = 2; // Start from row index 2
			for (AuctionReportDTO dto : recordForDistrict) {
				Row dataRow = sheet.createRow(rowIndex++);
				dataRow.createCell(0)
						.setCellValue(dto.getAuctionNumber() != null ? dto.getAuctionNumber().toString() : "");
				dataRow.createCell(1).setCellValue(dto.getLandDetails() != null ? dto.getLandDetails().toString() : "");
				dataRow.createCell(2).setCellValue(dto.getAuctionDate() != null ? dto.getAuctionDate().toString() : "");
				dataRow.createCell(3).setCellValue(dto.getWinnerName() != null ? dto.getWinnerName().toString() : "");
				dataRow.createCell(4)
						.setCellValue(dto.getWinnerBidAmount() != null ? dto.getWinnerBidAmount().toString() : "");
				dataRow.createCell(5).setCellValue(
						dto.getParticipantDetails() != null ? dto.getParticipantDetails().toString() : "");
			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			log.info("inside Excell helper TahasilRecordToExcel() !!! " + e.getMessage());

		}
		return null;
	}

}
