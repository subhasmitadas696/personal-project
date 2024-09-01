package com.csmtech.sjta.serviceImpl;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.PaymentCollectionHistoryDTO;
import com.csmtech.sjta.repository.PaymentHistoryRepository;
import com.csmtech.sjta.service.PaymentHistoryService;
import com.csmtech.sjta.util.ExcelHelper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaymentHistoryReportServiceImpl implements PaymentHistoryService {

	@Autowired
	private PaymentHistoryRepository payHistoryRepo;

	LocalDate financialYearStart;
	LocalDate financialYearEnd;

	public static class FinancialYearDates {
		private LocalDate start;
		private LocalDate end;

		public FinancialYearDates(LocalDate start, LocalDate end) {
			this.start = start;
			this.end = end;
		}

		public LocalDate getStart() {
			return start;
		}

		public LocalDate getEnd() {
			return end;
		}
	}

//	public PaymentHistoryReportServiceImpl(LocalDate start, LocalDate end) {
//		this.financialYearStart = start;
//		this.financialYearEnd = end;
//	}
//
//	public PaymentHistoryReportServiceImpl getFinancialYearDates(String financialYear) {
//		String[] years = financialYear.split("-");
//		int startYear = Integer.parseInt(years[0]);
//		int endYear = Integer.parseInt(years[1]);
//		LocalDate financialYearStart = LocalDate.of(startYear, 4, 1);
//		LocalDate financialYearEnd = LocalDate.of(endYear, 3, 31);
//		return new PaymentHistoryReportServiceImpl(financialYearStart, financialYearEnd);
//	}

	@Override
	public List<Map<String, Object>> fetchPaymentHistory(String data) {
		JSONObject jsonData = new JSONObject(data);

		String financialYear = jsonData.getString("financialYear");
		String[] years = financialYear.split("-");
		int startYear = Integer.parseInt(years[0]);
		int endYear = Integer.parseInt(years[1]);
		LocalDate financialYearStart = LocalDate.of(startYear, 4, 1);
		LocalDate financialYearEnd = LocalDate.of(endYear, 3, 31);

		List<Map<String, Object>> keyValueList = new ArrayList<>();
		List<Object[]> resultList = payHistoryRepo.fetchPaymentHistoryReport(financialYearStart, financialYearEnd);
		for (Object[] row : resultList) {
			Map<String, Object> keyValueMap = new HashMap<>();

			keyValueMap.put("districtCode", row[0]);
			keyValueMap.put("districtName", row[1]);
			keyValueMap.put("january", row[2]);
			keyValueMap.put("february", row[3]);
			keyValueMap.put("march", row[4]);
			keyValueMap.put("april", row[5]);
			keyValueMap.put("may", row[6]);
			keyValueMap.put("june", row[7]);
			keyValueMap.put("july", row[8]);
			keyValueMap.put("august", row[9]);
			keyValueMap.put("september", row[10]);
			keyValueMap.put("october", row[11]);
			keyValueMap.put("november", row[12]);
			keyValueMap.put("december", row[13]);
			keyValueMap.put("totalAmount",
					String.format("%.2f", (row[14] != null ? Double.parseDouble(row[14].toString()) : 0.00)));
			keyValueList.add(keyValueMap);
		}
		return keyValueList;
	}

	@Override
	public List<Map<String, Object>> fetchPaymentHistoryReportOfTahasil(String data) {
		JSONObject jsonData = new JSONObject(data);
		String districtCode = jsonData.getString("districtCode");
		String financialYear = jsonData.getString("financialYear");
		String[] years = financialYear.split("-");
		int startYear = Integer.parseInt(years[0]);
		int endYear = Integer.parseInt(years[1]);
		LocalDate financialYearStart = LocalDate.of(startYear, 4, 1);
		LocalDate financialYearEnd = LocalDate.of(endYear, 3, 31);
		List<Map<String, Object>> keyValueList = new ArrayList<>();
		List<Object[]> resultList = payHistoryRepo.fetchPaymentHistoryReportOfTahasil(districtCode, financialYearStart,
				financialYearEnd);
		for (Object[] row : resultList) {
			Map<String, Object> keyValueMap = new HashMap<>();

			keyValueMap.put("tahasilCode", row[0]);
			keyValueMap.put("tahasilName", row[1]);
			keyValueMap.put("january", row[2]);
			keyValueMap.put("february", row[3]);
			keyValueMap.put("march", row[4]);
			keyValueMap.put("april", row[5]);
			keyValueMap.put("may", row[6]);
			keyValueMap.put("june", row[7]);
			keyValueMap.put("july", row[8]);
			keyValueMap.put("august", row[9]);
			keyValueMap.put("september", row[10]);
			keyValueMap.put("october", row[11]);
			keyValueMap.put("november", row[12]);
			keyValueMap.put("december", row[13]);
			keyValueMap.put("totalAmount",
					String.format("%.2f", (row[14] != null ? Double.parseDouble(row[14].toString()) : 0.00)));
			keyValueMap.put("districtCode", row[15]);
			keyValueMap.put("districtName", row[16]);
			keyValueList.add(keyValueMap);
		}
		return keyValueList;
	}

	@Override
	public List<Map<String, Object>> fetchPaymentHistoryReportOfVillage(String data) {
		JSONObject jsonData = new JSONObject(data);
		String tahasilCode = jsonData.getString("tahasilCode");
		String financialYear = jsonData.getString("financialYear");
		String[] years = financialYear.split("-");
		int startYear = Integer.parseInt(years[0]);
		int endYear = Integer.parseInt(years[1]);
		LocalDate financialYearStart = LocalDate.of(startYear, 4, 1);
		LocalDate financialYearEnd = LocalDate.of(endYear, 3, 31);
		List<Map<String, Object>> keyValueList = new ArrayList<>();
		List<Object[]> resultList = payHistoryRepo.fetchPaymentHistoryReportOfVillage(tahasilCode, financialYearStart,
				financialYearEnd);
		for (Object[] row : resultList) {
			Map<String, Object> keyValueMap = new HashMap<>();

			keyValueMap.put("villageCode", row[0]);
			keyValueMap.put("villageName", row[1]);
			keyValueMap.put("january", row[2]);
			keyValueMap.put("february", row[3]);
			keyValueMap.put("march", row[4]);
			keyValueMap.put("april", row[5]);
			keyValueMap.put("may", row[6]);
			keyValueMap.put("june", row[7]);
			keyValueMap.put("july", row[8]);
			keyValueMap.put("august", row[9]);
			keyValueMap.put("september", row[10]);
			keyValueMap.put("october", row[11]);
			keyValueMap.put("november", row[12]);
			keyValueMap.put("december", row[13]);
			keyValueMap.put("totalAmount",
					String.format("%.2f", (row[14] != null ? Double.parseDouble(row[14].toString()) : 0.00)));
			keyValueMap.put("districtCode", row[15]);
			keyValueMap.put("districtName", row[16]);
			keyValueMap.put("tahasilCode", row[17]);
			keyValueMap.put("tahasilName", row[18]);
			keyValueList.add(keyValueMap);
		}
		return keyValueList;
	}

	@Override
	public List<Map<String, Object>> fetchLandAllotmentPaymentHistory(String data) {
		JSONObject jsonData = new JSONObject(data);

		String financialYear = jsonData.getString("financialYear");
		String[] years = financialYear.split("-");
		int startYear = Integer.parseInt(years[0]);
		int endYear = Integer.parseInt(years[1]);
		LocalDate financialYearStart = LocalDate.of(startYear, 4, 1);
		LocalDate financialYearEnd = LocalDate.of(endYear, 3, 31);

		List<Map<String, Object>> keyValueList = new ArrayList<>();
		List<Object[]> resultList = payHistoryRepo.fetchLandAllotmentPaymentHistory(financialYearStart,
				financialYearEnd);
		for (Object[] row : resultList) {
			Map<String, Object> keyValueMap = new HashMap<>();

			keyValueMap.put("districtCode", row[0]);
			keyValueMap.put("districtName", row[1]);
			keyValueMap.put("january", row[2]);
			keyValueMap.put("february", row[3]);
			keyValueMap.put("march", row[4]);
			keyValueMap.put("april", row[5]);
			keyValueMap.put("may", row[6]);
			keyValueMap.put("june", row[7]);
			keyValueMap.put("july", row[8]);
			keyValueMap.put("august", row[9]);
			keyValueMap.put("september", row[10]);
			keyValueMap.put("october", row[11]);
			keyValueMap.put("november", row[12]);
			keyValueMap.put("december", row[13]);
			keyValueMap.put("totalAmount",
					String.format("%.2f", (row[14] != null ? Double.parseDouble(row[14].toString()) : 0.00)));
			keyValueList.add(keyValueMap);
		}
		return keyValueList;
	}

	@Override
	public List<Map<String, Object>> fetchLandAllotmentPaymentHistoryOfTahasil(String data) {
		JSONObject jsonData = new JSONObject(data);
		String districtCode = jsonData.getString("districtCode");
		String financialYear = jsonData.getString("financialYear");
		String[] years = financialYear.split("-");
		int startYear = Integer.parseInt(years[0]);
		int endYear = Integer.parseInt(years[1]);
		LocalDate financialYearStart = LocalDate.of(startYear, 4, 1);
		LocalDate financialYearEnd = LocalDate.of(endYear, 3, 31);

		List<Map<String, Object>> keyValueList = new ArrayList<>();
		List<Object[]> resultList = payHistoryRepo.fetchLandAllotmentPaymentHistoryOfTahasil(districtCode,
				financialYearStart, financialYearEnd);
		for (Object[] row : resultList) {
			Map<String, Object> keyValueMap = new HashMap<>();

			keyValueMap.put("tahasilCode", row[0]);
			keyValueMap.put("tahasilName", row[1]);
			keyValueMap.put("january", row[2]);
			keyValueMap.put("february", row[3]);
			keyValueMap.put("march", row[4]);
			keyValueMap.put("april", row[5]);
			keyValueMap.put("may", row[6]);
			keyValueMap.put("june", row[7]);
			keyValueMap.put("july", row[8]);
			keyValueMap.put("august", row[9]);
			keyValueMap.put("september", row[10]);
			keyValueMap.put("october", row[11]);
			keyValueMap.put("november", row[12]);
			keyValueMap.put("december", row[13]);
			keyValueMap.put("totalAmount",
					String.format("%.2f", (row[14] != null ? Double.parseDouble(row[14].toString()) : 0.00)));
			keyValueMap.put("districtCode", row[15]);
			keyValueMap.put("districtName", row[16]);
			keyValueList.add(keyValueMap);
		}
		return keyValueList;
	}

	@Override
	public List<Map<String, Object>> fetchLandAllotmentPaymentHistoryOfVillage(String data) {
		JSONObject jsonData = new JSONObject(data);

		String tahasilCode = jsonData.getString("tahasilCode");
		String financialYear = jsonData.getString("financialYear");
		String[] years = financialYear.split("-");
		int startYear = Integer.parseInt(years[0]);
		int endYear = Integer.parseInt(years[1]);
		LocalDate financialYearStart = LocalDate.of(startYear, 4, 1);
		LocalDate financialYearEnd = LocalDate.of(endYear, 3, 31);
		List<Map<String, Object>> keyValueList = new ArrayList<>();
		List<Object[]> resultList = payHistoryRepo.fetchLandAllotmentPaymentHistoryOfVillage(tahasilCode,
				financialYearStart, financialYearEnd);
		for (Object[] row : resultList) {
			Map<String, Object> keyValueMap = new HashMap<>();

			keyValueMap.put("villageCode", row[0]);
			keyValueMap.put("villageName", row[1]);
			keyValueMap.put("january", row[2]);
			keyValueMap.put("february", row[3]);
			keyValueMap.put("march", row[4]);
			keyValueMap.put("april", row[5]);
			keyValueMap.put("may", row[6]);
			keyValueMap.put("june", row[7]);
			keyValueMap.put("july", row[8]);
			keyValueMap.put("august", row[9]);
			keyValueMap.put("september", row[10]);
			keyValueMap.put("october", row[11]);
			keyValueMap.put("november", row[12]);
			keyValueMap.put("december", row[13]);
			keyValueMap.put("totalAmount",
					String.format("%.2f", (row[14] != null ? Double.parseDouble(row[14].toString()) : 0.00)));
			keyValueMap.put("districtCode", row[15]);
			keyValueMap.put("districtName", row[16]);
			keyValueMap.put("tahasilCode", row[17]);
			keyValueMap.put("tahasilName", row[18]);
			keyValueList.add(keyValueMap);
		}
		return keyValueList;
	}

	@Override
	public List<Map<String, Object>> fetchLandAllotmentPaymentHistoryOfKhatian(String data) {

		JSONObject jsonData = new JSONObject(data);
		String villageCode = jsonData.getString("villageCode");
		String financialYear = jsonData.getString("financialYear");
		String[] years = financialYear.split("-");
		int startYear = Integer.parseInt(years[0]);
		int endYear = Integer.parseInt(years[1]);
		LocalDate financialYearStart = LocalDate.of(startYear, 4, 1);
		LocalDate financialYearEnd = LocalDate.of(endYear, 3, 31);
		List<Map<String, Object>> keyValueList = new ArrayList<>();
		List<Object[]> resultList = payHistoryRepo.fetchLandAllotmentPaymentHistoryOfKhatian(villageCode,
				financialYearStart, financialYearEnd);
		for (Object[] row : resultList) {
			Map<String, Object> keyValueMap = new HashMap<>();

			keyValueMap.put("khatianCode", row[0]);
			keyValueMap.put("khataNo", row[1]);
			keyValueMap.put("january", row[2]);
			keyValueMap.put("february", row[3]);
			keyValueMap.put("march", row[4]);
			keyValueMap.put("april", row[5]);
			keyValueMap.put("may", row[6]);
			keyValueMap.put("june", row[7]);
			keyValueMap.put("july", row[8]);
			keyValueMap.put("august", row[9]);
			keyValueMap.put("september", row[10]);
			keyValueMap.put("october", row[11]);
			keyValueMap.put("november", row[12]);
			keyValueMap.put("december", row[13]);
			keyValueMap.put("totalAmount",
					String.format("%.2f", (row[14] != null ? Double.parseDouble(row[14].toString()) : 0.00)));
			keyValueMap.put("districtCode", row[15]);
			keyValueMap.put("districtName", row[16]);
			keyValueMap.put("tahasilCode", row[17]);
			keyValueMap.put("tahasilName", row[18]);
			keyValueMap.put("villageCode", row[19]);
			keyValueMap.put("villageName", row[20]);

			keyValueList.add(keyValueMap);
		}
		return keyValueList;
	}

	@Override
	public List<Map<String, Object>> fetchLandAllotmentPaymentHistoryOfPlot(String data) {
		JSONObject jsonData = new JSONObject(data);
		String khatianCode = jsonData.getString("khatianCode");
		String financialYear = jsonData.getString("financialYear");
		String[] years = financialYear.split("-");
		int startYear = Integer.parseInt(years[0]);
		int endYear = Integer.parseInt(years[1]);
		LocalDate financialYearStart = LocalDate.of(startYear, 4, 1);
		LocalDate financialYearEnd = LocalDate.of(endYear, 3, 31);
		List<Map<String, Object>> keyValueList = new ArrayList<>();
		List<Object[]> resultList = payHistoryRepo.fetchLandAllotmentPaymentHistoryOfPlot(khatianCode,
				financialYearStart, financialYearEnd);
		for (Object[] row : resultList) {
			Map<String, Object> keyValueMap = new HashMap<>();

			keyValueMap.put("plotCode", row[0]);
			keyValueMap.put("plotNo", row[1]);
			keyValueMap.put("january", row[2]);
			keyValueMap.put("february", row[3]);
			keyValueMap.put("march", row[4]);
			keyValueMap.put("april", row[5]);
			keyValueMap.put("may", row[6]);
			keyValueMap.put("june", row[7]);
			keyValueMap.put("july", row[8]);
			keyValueMap.put("august", row[9]);
			keyValueMap.put("september", row[10]);
			keyValueMap.put("october", row[11]);
			keyValueMap.put("november", row[12]);
			keyValueMap.put("december", row[13]);
			keyValueMap.put("totalAmount",
					String.format("%.2f", (row[14] != null ? Double.parseDouble(row[14].toString()) : 0.00)));
			keyValueMap.put("districtCode", row[15]);
			keyValueMap.put("districtName", row[16]);
			keyValueMap.put("tahasilCode", row[17]);
			keyValueMap.put("tahasilName", row[18]);
			keyValueMap.put("villageCode", row[19]);
			keyValueMap.put("villageName", row[20]);
			keyValueMap.put("khatianCode", row[21]);
			keyValueMap.put("khataNo", row[22]);
			keyValueMap.put("applicantName", row[23]);
			keyValueMap.put("purchaseArea", row[24]);
			keyValueMap.put("totalPriceOfPurchaseArea", row[25]);
			keyValueMap.put("landOrderNumber", row[26]);
			keyValueMap.put("landOrderDate", row[27]);
			keyValueList.add(keyValueMap);
		}
		return keyValueList;
	}

	@Override
	public ByteArrayInputStream fetchPaymentHistoryExcel(String fiscalYear) {
		String[] years = fiscalYear.split("-");
		int startYear = Integer.parseInt(years[0]);
		int endYear = Integer.parseInt(years[1]);
		LocalDate financialYearStart = LocalDate.of(startYear, 4, 1);
		LocalDate financialYearEnd = LocalDate.of(endYear, 3, 31);
		List<PaymentCollectionHistoryDTO> reportList = new ArrayList<>();
		List<Object[]> resultList = payHistoryRepo.fetchPaymentHistoryReport(financialYearStart, financialYearEnd);
		for (Object[] row : resultList) {
			PaymentCollectionHistoryDTO dto = new PaymentCollectionHistoryDTO();

			dto.setDistrictCode((String) row[0]);
			dto.setDistrictName((String) row[1]);
			dto.setJanuary((BigDecimal) row[2]);
			dto.setFebruary((BigDecimal) row[3]);
			dto.setMarch((BigDecimal) row[4]);
			dto.setApril((BigDecimal) row[5]);
			dto.setMay((BigDecimal) row[6]);
			dto.setJune((BigDecimal) row[7]);
			dto.setJuly((BigDecimal) row[8]);
			dto.setAugust((BigDecimal) row[9]);
			dto.setSeptember((BigDecimal) row[10]);
			dto.setOctober((BigDecimal) row[11]);
			dto.setNovember((BigDecimal) row[12]);
			dto.setDecember((BigDecimal) row[13]);
			dto.setTotalAmount((BigDecimal) row[14]);
			reportList.add(dto);
		}
		return ExcelHelper.paymentReportToExcel(reportList);
	}

	@Override
	public ByteArrayInputStream fetchTahasilPaymentHistoryExcel(String districtCode) {
		List<PaymentCollectionHistoryDTO> reportList = new ArrayList<>();
		List<Object[]> resultList = payHistoryRepo.fetchPaymentHistoryReportOfTahasil(districtCode, financialYearStart,
				financialYearEnd);
		for (Object[] row : resultList) {
			PaymentCollectionHistoryDTO dto = new PaymentCollectionHistoryDTO();

			dto.setTahasilCode((String) row[0]);
			dto.setTahasilName((String) row[1]);
			dto.setJanuary((BigDecimal) row[2]);
			dto.setFebruary((BigDecimal) row[3]);
			dto.setMarch((BigDecimal) row[4]);
			dto.setApril((BigDecimal) row[5]);
			dto.setMay((BigDecimal) row[6]);
			dto.setJune((BigDecimal) row[7]);
			dto.setJuly((BigDecimal) row[8]);
			dto.setAugust((BigDecimal) row[9]);
			dto.setSeptember((BigDecimal) row[10]);
			dto.setOctober((BigDecimal) row[11]);
			dto.setNovember((BigDecimal) row[12]);
			dto.setDecember((BigDecimal) row[13]);
			dto.setTotalAmount((BigDecimal) row[14]);
			reportList.add(dto);
		}
		return ExcelHelper.tahasilPaymentReportToExcel(reportList);
	}

	@Override
	public ByteArrayInputStream fetchVillagePaymentHistoryExcel(String tahasilCode) {
		List<PaymentCollectionHistoryDTO> reportList = new ArrayList<>();
		List<Object[]> resultList = payHistoryRepo.fetchPaymentHistoryReportOfVillage(tahasilCode, financialYearStart,
				financialYearEnd);
		for (Object[] row : resultList) {
			PaymentCollectionHistoryDTO dto = new PaymentCollectionHistoryDTO();

			dto.setVillageCode((String) row[0]);
			dto.setVillageName((String) row[1]);
			dto.setJanuary((BigDecimal) row[2]);
			dto.setFebruary((BigDecimal) row[3]);
			dto.setMarch((BigDecimal) row[4]);
			dto.setApril((BigDecimal) row[5]);
			dto.setMay((BigDecimal) row[6]);
			dto.setJune((BigDecimal) row[7]);
			dto.setJuly((BigDecimal) row[8]);
			dto.setAugust((BigDecimal) row[9]);
			dto.setSeptember((BigDecimal) row[10]);
			dto.setOctober((BigDecimal) row[11]);
			dto.setNovember((BigDecimal) row[12]);
			dto.setDecember((BigDecimal) row[13]);
			dto.setTotalAmount((BigDecimal) row[14]);
			reportList.add(dto);
		}
		return ExcelHelper.villagePaymentReportToExcel(reportList);
	}

	@Override
	public ByteArrayInputStream fetchDistLandAllotmentPaymentHistoryExcel(String fiscalYear) {

		String[] years = fiscalYear.split("-");
		int startYear = Integer.parseInt(years[0]);
		int endYear = Integer.parseInt(years[1]);
		LocalDate financialYearStart = LocalDate.of(startYear, 4, 1);
		LocalDate financialYearEnd = LocalDate.of(endYear, 3, 31);

		List<PaymentCollectionHistoryDTO> reportList = new ArrayList<>();
		List<Object[]> resultList = payHistoryRepo.fetchLandAllotmentPaymentHistory(financialYearStart,
				financialYearEnd);
		for (Object[] row : resultList) {
			PaymentCollectionHistoryDTO dto = new PaymentCollectionHistoryDTO();

			dto.setDistrictCode((String) row[0]);
			dto.setDistrictName((String) row[1]);
			dto.setJanuary((BigDecimal) row[2]);
			dto.setFebruary((BigDecimal) row[3]);
			dto.setMarch((BigDecimal) row[4]);
			dto.setApril((BigDecimal) row[5]);
			dto.setMay((BigDecimal) row[6]);
			dto.setJune((BigDecimal) row[7]);
			dto.setJuly((BigDecimal) row[8]);
			dto.setAugust((BigDecimal) row[9]);
			dto.setSeptember((BigDecimal) row[10]);
			dto.setOctober((BigDecimal) row[11]);
			dto.setNovember((BigDecimal) row[12]);
			dto.setDecember((BigDecimal) row[13]);
			dto.setTotalAmount((BigDecimal) row[14]);
			reportList.add(dto);
		}
		return ExcelHelper.landAllotPaymentReportToExcel(reportList);
	}

	@Override
	public ByteArrayInputStream fetchTahasilLandAllotmentPaymentHistoryExcel(String districtCode) {
		List<PaymentCollectionHistoryDTO> reportList = new ArrayList<>();
		List<Object[]> resultList = payHistoryRepo.fetchLandAllotmentPaymentHistoryOfTahasil(districtCode,
				financialYearEnd, financialYearEnd);
		for (Object[] row : resultList) {
			PaymentCollectionHistoryDTO dto = new PaymentCollectionHistoryDTO();

			dto.setTahasilCode((String) row[0]);
			dto.setTahasilName((String) row[1]);
			dto.setJanuary((BigDecimal) row[2]);
			dto.setFebruary((BigDecimal) row[3]);
			dto.setMarch((BigDecimal) row[4]);
			dto.setApril((BigDecimal) row[5]);
			dto.setMay((BigDecimal) row[6]);
			dto.setJune((BigDecimal) row[7]);
			dto.setJuly((BigDecimal) row[8]);
			dto.setAugust((BigDecimal) row[9]);
			dto.setSeptember((BigDecimal) row[10]);
			dto.setOctober((BigDecimal) row[11]);
			dto.setNovember((BigDecimal) row[12]);
			dto.setDecember((BigDecimal) row[13]);
			dto.setTotalAmount((BigDecimal) row[14]);
			reportList.add(dto);
		}
		return ExcelHelper.tahasilLandAllotPaymentReportToExcel(reportList);
	}

	@Override
	public ByteArrayInputStream fetchVillageLandAllotmentPaymentHistoryExcel(String tahasilCode) {
		List<PaymentCollectionHistoryDTO> reportList = new ArrayList<>();
		List<Object[]> resultList = payHistoryRepo.fetchLandAllotmentPaymentHistoryOfVillage(tahasilCode,
				financialYearStart, financialYearEnd);
		for (Object[] row : resultList) {
			PaymentCollectionHistoryDTO dto = new PaymentCollectionHistoryDTO();

			dto.setVillageCode((String) row[0]);
			dto.setVillageName((String) row[1]);
			dto.setJanuary((BigDecimal) row[2]);
			dto.setFebruary((BigDecimal) row[3]);
			dto.setMarch((BigDecimal) row[4]);
			dto.setApril((BigDecimal) row[5]);
			dto.setMay((BigDecimal) row[6]);
			dto.setJune((BigDecimal) row[7]);
			dto.setJuly((BigDecimal) row[8]);
			dto.setAugust((BigDecimal) row[9]);
			dto.setSeptember((BigDecimal) row[10]);
			dto.setOctober((BigDecimal) row[11]);
			dto.setNovember((BigDecimal) row[12]);
			dto.setDecember((BigDecimal) row[13]);
			dto.setTotalAmount((BigDecimal) row[14]);
			reportList.add(dto);
		}
		return ExcelHelper.villageLandAllotPaymentReportToExcel(reportList);
	}

	@Override
	public ByteArrayInputStream fetchKhatianLandAllotmentPaymentHistoryExcel(String villageCode) {
		List<PaymentCollectionHistoryDTO> reportList = new ArrayList<>();
		List<Object[]> resultList = payHistoryRepo.fetchLandAllotmentPaymentHistoryOfKhatian(villageCode,
				financialYearStart, financialYearEnd);
		for (Object[] row : resultList) {
			PaymentCollectionHistoryDTO dto = new PaymentCollectionHistoryDTO();

			dto.setKhataCode((String) row[0]);
			dto.setKhataNo((String) row[1]);
			dto.setJanuary((BigDecimal) row[2]);
			dto.setFebruary((BigDecimal) row[3]);
			dto.setMarch((BigDecimal) row[4]);
			dto.setApril((BigDecimal) row[5]);
			dto.setMay((BigDecimal) row[6]);
			dto.setJune((BigDecimal) row[7]);
			dto.setJuly((BigDecimal) row[8]);
			dto.setAugust((BigDecimal) row[9]);
			dto.setSeptember((BigDecimal) row[10]);
			dto.setOctober((BigDecimal) row[11]);
			dto.setNovember((BigDecimal) row[12]);
			dto.setDecember((BigDecimal) row[13]);
			dto.setTotalAmount((BigDecimal) row[14]);
			reportList.add(dto);
		}
		return ExcelHelper.khatianLandAllotPaymentReportToExcel(reportList);
	}

	@Override
	public ByteArrayInputStream fetchPlotLandAllotmentPaymentHistoryExcel(String khatianCode) {
		List<PaymentCollectionHistoryDTO> reportList = new ArrayList<>();
		Date parsedDate = new Date();

		List<Object[]> resultList = payHistoryRepo.fetchLandAllotmentPaymentHistoryOfPlot(khatianCode,
				financialYearStart, financialYearEnd);
		for (Object[] row : resultList) {
			PaymentCollectionHistoryDTO dto = new PaymentCollectionHistoryDTO();

			dto.setPlotCode((String) row[0]);
			dto.setPlotNo((String) row[1]);
			dto.setJanuary((BigDecimal) row[2]);
			dto.setFebruary((BigDecimal) row[3]);
			dto.setMarch((BigDecimal) row[4]);
			dto.setApril((BigDecimal) row[5]);
			dto.setMay((BigDecimal) row[6]);
			dto.setJune((BigDecimal) row[7]);
			dto.setJuly((BigDecimal) row[8]);
			dto.setAugust((BigDecimal) row[9]);
			dto.setSeptember((BigDecimal) row[10]);
			dto.setOctober((BigDecimal) row[11]);
			dto.setNovember((BigDecimal) row[12]);
			dto.setDecember((BigDecimal) row[13]);
			dto.setTotalAmount((BigDecimal) row[14]);

			dto.setApplicantName((String) row[23]);
			dto.setPurchaseArea((BigDecimal) row[24]);
			dto.setTotalPrice((BigDecimal) row[25]);
			dto.setLandOrderNo((String) row[26]);
//			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//			try {
//				parsedDate = dateFormat.parse((String) row[27]);
//			} catch (ParseException e) {
//				log.error("date error: " + e.getMessage());
//			}
			dto.setLandOrderDate((Date) row[27]);
			reportList.add(dto);
		}
		return ExcelHelper.plotLandAllotPaymentReportToExcel(reportList);
	}

}
