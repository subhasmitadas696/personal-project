package com.csmtech.sjta.service;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

public interface PaymentHistoryService {

	List<Map<String, Object>> fetchPaymentHistory(String data);
	
	List<Map<String, Object>> fetchPaymentHistoryReportOfTahasil(String data);

	List<Map<String, Object>> fetchPaymentHistoryReportOfVillage(String data);

	List<Map<String, Object>> fetchLandAllotmentPaymentHistory(String data);

	List<Map<String, Object>> fetchLandAllotmentPaymentHistoryOfTahasil(String data);

	List<Map<String, Object>> fetchLandAllotmentPaymentHistoryOfVillage(String data);

	List<Map<String, Object>> fetchLandAllotmentPaymentHistoryOfKhatian(String data);

	List<Map<String, Object>> fetchLandAllotmentPaymentHistoryOfPlot(String data);

	ByteArrayInputStream fetchPaymentHistoryExcel(String fiscalYear);

	ByteArrayInputStream fetchTahasilPaymentHistoryExcel(String districtCode);

	ByteArrayInputStream fetchVillagePaymentHistoryExcel(String tahasilCode);

	ByteArrayInputStream fetchDistLandAllotmentPaymentHistoryExcel(String fiscalYear);

	ByteArrayInputStream fetchTahasilLandAllotmentPaymentHistoryExcel(String districtCode);

	ByteArrayInputStream fetchVillageLandAllotmentPaymentHistoryExcel(String tahasilCode);

	ByteArrayInputStream fetchKhatianLandAllotmentPaymentHistoryExcel(String villageCode);

	ByteArrayInputStream fetchPlotLandAllotmentPaymentHistoryExcel(String khatianCode);


}
