package com.csmtech.sjta.repository;

import java.time.LocalDate;
import java.util.List;

public interface PaymentHistoryRepository {

	List<Object[]> fetchPaymentHistoryReport(LocalDate financialYearStart, LocalDate financialYearEnd);

	List<Object[]> fetchPaymentHistoryReportOfTahasil(String districtCode, LocalDate financialYearStart, LocalDate financialYearEnd);

	List<Object[]> fetchPaymentHistoryReportOfVillage(String tahasilCode, LocalDate financialYearStart, LocalDate financialYearEnd);

	List<Object[]> fetchLandAllotmentPaymentHistory(LocalDate financialYearStart, LocalDate financialYearEnd);

	List<Object[]> fetchLandAllotmentPaymentHistoryOfTahasil(String districtCode, LocalDate financialYearStart, LocalDate financialYearEnd);

	List<Object[]> fetchLandAllotmentPaymentHistoryOfVillage(String tahasilCode, LocalDate financialYearStart, LocalDate financialYearEnd);

	List<Object[]> fetchLandAllotmentPaymentHistoryOfKhatian(String villageCode,  LocalDate financialYearStart, LocalDate financialYearEnd);

	List<Object[]> fetchLandAllotmentPaymentHistoryOfPlot(String khatianCode, LocalDate financialYearStart, LocalDate financialYearEnd);

}
