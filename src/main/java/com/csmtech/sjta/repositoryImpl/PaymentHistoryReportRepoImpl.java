package com.csmtech.sjta.repositoryImpl;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.repository.PaymentHistoryRepository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class PaymentHistoryReportRepoImpl implements PaymentHistoryRepository {

	@PersistenceContext
	@Autowired
	private EntityManager entity;

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> fetchPaymentHistoryReport(LocalDate financialYearStart, LocalDate financialYearEnd) {
		String nativeQuery = "SELECT district_code, district_name, "
				+ "    SUM(CASE WHEN month = '01' THEN total_amount ELSE NULL END) AS January, "
				+ "    SUM(CASE WHEN month = '02' THEN total_amount ELSE NULL END) AS February, "
				+ "    SUM(CASE WHEN month = '03' THEN total_amount ELSE NULL END) AS March, "
				+ "    SUM(CASE WHEN month = '04' THEN total_amount ELSE NULL END) AS April, "
				+ "    SUM(CASE WHEN month = '05' THEN total_amount ELSE NULL END) AS May, "
				+ "    SUM(CASE WHEN month = '06' THEN total_amount ELSE NULL END) AS June, "
				+ "    SUM(CASE WHEN month = '07' THEN total_amount ELSE NULL END) AS July, "
				+ "    SUM(CASE WHEN month = '08' THEN total_amount ELSE NULL END) AS August, "
				+ "    SUM(CASE WHEN month = '09' THEN total_amount ELSE NULL END) AS September, "
				+ "    SUM(CASE WHEN month = '10' THEN total_amount ELSE NULL END) AS October, "
				+ "    SUM(CASE WHEN month = '11' THEN total_amount ELSE NULL END) AS November, "
				+ "    SUM(CASE WHEN month = '12' THEN total_amount ELSE NULL END) AS December, "
				+ " SUM(total_amount) AS total_amount FROM ( SELECT "
				+ "        TO_CHAR(DATE_TRUNC('month', payment_date_time), 'mm') AS month, "
				+ "        SUM(tranction_amount) AS total_amount,  la.district_code, "
				+ "        dm.district_name  FROM "
				+ "        public.land_application_payment_tranction_details lapd  JOIN "
				+ "        public.land_application la ON (lapd.land_application_id = la.land_application_id) "
				+ "    JOIN  land_bank.district_master dm ON (la.district_code = dm.district_code)"
				+ " WHERE payment_date_time >= DATE(:financialYearStart) AND payment_date_time <= DATE(:financialYearEnd)"
				+ "  GROUP BY  DATE_TRUNC('month', payment_date_time),  la.district_code, "
				+ "        district_name UNION ALL  SELECT "
				+ "        TO_CHAR(DATE_TRUNC('month', payment_date_time), 'mm') AS month, "
				+ "        SUM(tranction_amount) AS total_amount,  ap.district_code, "
				+ "        dm.district_name FROM  application.from_m_application_payment_details fmpd  JOIN "
				+ "        application.bidder_form_m_application fm ON (fmpd.from_m__application_id = fm.bidder_form_m_application_id) "
				+ "    JOIN  application.tender_auction ta ON (fm.tender_auction_id = ta.tender_auction_id) "
				+ "    JOIN application.auction_plot_details apd ON (ta.auction_plot_id = apd.auction_plot_details_id) "
				+ "    JOIN  application.auction_plot ap ON (apd.auction_plot_id = ap.auction_plot_id) "
				+ "    JOIN land_bank.district_master dm ON (ap.district_code = dm.district_code) "
				+ " WHERE payment_date_time >= DATE(:financialYearStart) AND payment_date_time <= DATE(:financialYearEnd)"
				+ "    GROUP BY  DATE_TRUNC('month', payment_date_time),  ap.district_code, "
				+ "        district_name ) AS combined_results GROUP BY  district_code, "
				+ "    district_name ORDER BY district_name ASC; ";

		return entity.createNativeQuery(nativeQuery).setParameter("financialYearStart", financialYearStart)
				.setParameter("financialYearEnd", financialYearEnd).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> fetchPaymentHistoryReportOfTahasil(String districtCode, LocalDate financialYearStart,
			LocalDate financialYearEnd) {

		String nativeQuery = "SELECT   tehsil_code,   tahasil_name,  SUM(CASE  "
				+ "            WHEN month = '01' THEN total_amount   ELSE NULL  "
				+ "        END) AS January,  SUM(CASE  "
				+ "            WHEN month = '02' THEN total_amount  ELSE NULL  "
				+ "        END) AS February,  SUM(CASE  "
				+ "            WHEN month = '03' THEN total_amount   ELSE NULL END) AS March, "
				+ "        SUM(CASE    WHEN month = '04' THEN total_amount   ELSE NULL  "
				+ "        END) AS April,    SUM(CASE  "
				+ "            WHEN month = '05' THEN total_amount    ELSE NULL   END) AS May, "
				+ "        SUM(CASE WHEN month = '06' THEN total_amount  ELSE NULL  "
				+ "        END) AS June,  SUM(CASE  WHEN month = '07' THEN total_amount  "
				+ "            ELSE NULL  END) AS July,  SUM(CASE  "
				+ "            WHEN month = '08' THEN total_amount  ELSE NULL END) AS August, SUM(CASE  "
				+ "            WHEN month = '09' THEN total_amount   ELSE NULL  "
				+ "        END) AS September,  SUM(CASE  "
				+ "            WHEN month = '10' THEN total_amount  ELSE NULL  "
				+ "        END) AS October,  SUM(CASE  "
				+ "            WHEN month = '11' THEN total_amount   ELSE NULL  "
				+ "        END) AS November,  SUM(CASE  "
				+ "            WHEN month = '12' THEN total_amount  ELSE NULL  "
				+ "        END) AS December, SUM(total_amount) AS total_amount, "
				+ "        district_code,  district_name   FROM (     SELECT "
				+ "            TO_CHAR(DATE_TRUNC('month',   payment_date_time), "
				+ "            'mm') AS month,    SUM(tranction_amount) AS total_amount, "
				+ "            la.tehsil_code,   tm.tahasil_name,  la.district_code, "
				+ "            dm.district_name  FROM "
				+ "            public.land_application_payment_tranction_details lapd JOIN "
				+ "            public.land_application la  ON ( "
				+ "                    lapd.land_application_id = la.land_application_id  )  JOIN "
				+ "            land_bank.district_master dm    ON ( "
				+ "                    la.district_code = dm.district_code   )     JOIN "
				+ "            land_bank.tahasil_master tm  ON( "
				+ "                    la.tehsil_code = tm.tahasil_code  )   WHERE "
				+ "            la.district_code =:districtCode "
				+ "            AND payment_date_time >= DATE(:financialYearStart)  "
				+ "            AND payment_date_time <= DATE(:financialYearEnd)    GROUP BY "
				+ "            DATE_TRUNC('month',  payment_date_time), "
				+ "            la.tehsil_code,    tahasil_name,  la.district_code, "
				+ "            district_name  UNION ALL     SELECT "
				+ "            TO_CHAR(DATE_TRUNC('month',    payment_date_time), "
				+ "            'mm') AS month,  SUM(tranction_amount) AS total_amount, "
				+ "            ap.tahasil_code,  tm.tahasil_name,  ap.district_code, "
				+ "            dm.district_name    FROM "
				+ "            application.from_m_application_payment_details fmpd   JOIN "
				+ "            application.bidder_form_m_application fm    ON ( "
				+ "                    fmpd.from_m__application_id = fm.bidder_form_m_application_id "
				+ "                )       JOIN     application.tender_auction ta  "
				+ "                ON (   fm.tender_auction_id = ta.tender_auction_id "
				+ "                )           JOIN   application.auction_plot_details apd  "
				+ "                ON (   ta.auction_plot_id = apd.auction_plot_details_id "
				+ "                )          JOIN   application.auction_plot ap  "
				+ "                ON (   apd.auction_plot_id = ap.auction_plot_id "
				+ "                )      JOIN    land_bank.district_master dm  "
				+ "                ON (   ap.district_code = dm.district_code "
				+ "                )    JOIN  land_bank.tahasil_master tm  "
				+ "                ON( ap.tahasil_code = tm.tahasil_code "
				+ "                )       WHERE   ap.district_code =:districtCode  "
				+ "           AND payment_date_time >= DATE(:financialYearStart)  "
				+ "            AND payment_date_time <= DATE(:financialYearEnd)  GROUP BY "
				+ "            DATE_TRUNC('month',   payment_date_time), "
				+ "            ap.tahasil_code,   tahasil_name,   ap.district_code, district_name  "
				+ "    ) AS combined_results GROUP BY    tehsil_code, "
				+ "    tahasil_name, district_code,district_name  ORDER BY tahasil_name ASC; ";

		return entity.createNativeQuery(nativeQuery).setParameter("districtCode", districtCode)
				.setParameter("financialYearStart", financialYearStart)
				.setParameter("financialYearEnd", financialYearEnd).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> fetchPaymentHistoryReportOfVillage(String tahasilCode, LocalDate financialYearStart,
			LocalDate financialYearEnd) {

		String nativeQuery = "SELECT  village_code,village_name, "
				+ " SUM(CASE WHEN month = '01' THEN total_amount ELSE NULL END) AS January, "
				+ "    SUM(CASE WHEN month = '02' THEN total_amount ELSE NULL END) AS February, "
				+ "    SUM(CASE WHEN month = '03' THEN total_amount ELSE NULL END) AS March, "
				+ "    SUM(CASE WHEN month = '04' THEN total_amount ELSE NULL END) AS April, "
				+ "    SUM(CASE WHEN month = '05' THEN total_amount ELSE NULL END) AS May, "
				+ "    SUM(CASE WHEN month = '06' THEN total_amount ELSE NULL END) AS June, "
				+ "    SUM(CASE WHEN month = '07' THEN total_amount ELSE NULL END) AS July, "
				+ "    SUM(CASE WHEN month = '08' THEN total_amount ELSE NULL END) AS August, "
				+ "    SUM(CASE WHEN month = '09' THEN total_amount ELSE NULL END) AS September, "
				+ "    SUM(CASE WHEN month = '10' THEN total_amount ELSE NULL END) AS October, "
				+ "    SUM(CASE WHEN month = '11' THEN total_amount ELSE NULL END) AS November, "
				+ "    SUM(CASE WHEN month = '12' THEN total_amount ELSE NULL END) AS December, "
				+ " SUM(total_amount) AS total_amount, district_code, district_name, tehsil_code, tahasil_name "
				+ " FROM ( SELECT TO_CHAR(DATE_TRUNC('month', payment_date_time), 'mm') AS month, "
				+ " SUM(tranction_amount) AS total_amount, la.village_code, vm.village_name,la.tehsil_code, "
				+ " tm.tahasil_name, la.district_code, dm.district_name  "
				+ " FROM public.land_application_payment_tranction_details lapd "
				+ " JOIN public.land_application la ON(lapd.land_application_id = la.land_application_id) "
				+ " JOIN land_bank.district_master dm ON(la.district_code = dm.district_code) "
				+ " JOIN land_bank.tahasil_master tm ON(la.tehsil_code = tm.tahasil_code) "
				+ " JOIN land_bank.village_master vm ON(la.village_code = vm.village_code) "
				+ " WHERE la.tehsil_code =:tahasilCode AND payment_date_time >= DATE(:financialYearStart) AND payment_date_time <= DATE(:financialYearEnd) "
				+ " GROUP BY DATE_TRUNC('month', payment_date_time),la.village_code, village_name, la.tehsil_code, "
				+ " tahasil_name, la.district_code, district_name  "
				+ " UNION ALL SELECT TO_CHAR(DATE_TRUNC('month', payment_date_time), 'mm') AS month,  "
				+ " SUM(tranction_amount) AS total_amount, ap.village_code, vm.village_name,ap.tahasil_code, "
				+ " tm.tahasil_name, ap.district_code, dm.district_name  "
				+ "FROM application.from_m_application_payment_details fmpd "
				+ " JOIN application.bidder_form_m_application fm ON(fmpd.from_m__application_id = fm.bidder_form_m_application_id) "
				+ " JOIN application.tender_auction ta ON(fm.tender_auction_id = ta.tender_auction_id) "
				+ " JOIN application.auction_plot_details apd ON(ta.auction_plot_id = apd.auction_plot_details_id) "
				+ " JOIN application.auction_plot ap ON(apd.auction_plot_id = ap.auction_plot_id) "
				+ " JOIN land_bank.district_master dm ON(ap.district_code = dm.district_code) "
				+ " JOIN land_bank.tahasil_master tm ON(ap.tahasil_code = tm.tahasil_code) "
				+ " JOIN land_bank.village_master vm ON(ap.village_code = vm.village_code) "
				+ " WHERE ap.tahasil_code =:tahasilCode AND payment_date_time >= DATE(:financialYearStart) AND payment_date_time <= DATE(:financialYearEnd)"
				+ " GROUP BY DATE_TRUNC('month', payment_date_time),ap.village_code, village_name,ap.tahasil_code, "
				+ " tahasil_name, ap.district_code, district_name ) "
				+ " combined_results GROUP BY village_code,village_name, tehsil_code, tahasil_name, "
				+ " district_code, district_name  ORDER BY tahasil_name ASC";

		return entity.createNativeQuery(nativeQuery).setParameter("tahasilCode", tahasilCode)
				.setParameter("financialYearStart", financialYearStart)
				.setParameter("financialYearEnd", financialYearEnd).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> fetchLandAllotmentPaymentHistory(LocalDate financialYearStart, LocalDate financialYearEnd) {

		String nativeQuery = "SELECT district_code,district_name, "
				+ "SUM(CASE WHEN month = '01' THEN total_amount ELSE NULL END) AS January, "
				+ "SUM(CASE WHEN month = '02' THEN total_amount ELSE NULL END) AS February, "
				+ "SUM(CASE WHEN month = '03' THEN total_amount ELSE NULL END) AS March, "
				+ "SUM(CASE WHEN month = '04' THEN total_amount ELSE NULL END) AS April, "
				+ "SUM(CASE WHEN month = '05' THEN total_amount ELSE NULL END) AS May, "
				+ "SUM(CASE WHEN month = '06' THEN total_amount ELSE NULL END) AS June, "
				+ "SUM(CASE WHEN month = '07' THEN total_amount ELSE NULL END) AS July, "
				+ "SUM(CASE WHEN month = '08' THEN total_amount ELSE NULL END) AS August, "
				+ "SUM(CASE WHEN month = '09' THEN total_amount ELSE NULL END) AS September, "
				+ "SUM(CASE WHEN month = '10' THEN total_amount ELSE NULL END) AS October, "
				+ "SUM(CASE WHEN month = '11' THEN total_amount ELSE NULL END) AS November, "
				+ "SUM(CASE WHEN month = '12' THEN total_amount ELSE NULL END) AS December, "
				+ "SUM(total_amount) AS total_amount " + "FROM "
				+ "(SELECT TO_CHAR(DATE_TRUNC('month', payment_date_time), 'mm') AS month,   "
				+ "SUM(transaction_paid_amount) AS total_amount, " + "dm.district_code,dm.district_name FROM "
				+ "application.land_allotement_payment lap  "
				+ "JOIN application.land_allotement la ON(lap.land_allotement_id = la.land_allotement_id)  "
				+ "JOIN land_bank.plot_information pi ON(la.plot_code = pi.plot_code)  "
				+ "JOIN land_bank.khatian_information ki ON(pi.khatian_code = ki.khatian_code)  "
				+ "JOIN land_bank.village_master vm USING(village_code)  "
				+ "JOIN land_bank.tahasil_master tm USING(tahasil_code)  "
				+ "JOIN land_bank.district_master dm USING(district_code)  "
				+ " WHERE payment_date_time >= DATE(:financialYearStart) AND payment_date_time <= DATE(:financialYearEnd)"
				+ "GROUP BY DATE_TRUNC('month', payment_date_time),dm.district_code,dm.district_name "
				+ ") AS combined_results " + "GROUP BY district_code,district_name " + "ORDER BY district_name ASC";

		return entity.createNativeQuery(nativeQuery).setParameter("financialYearStart", financialYearStart)
				.setParameter("financialYearEnd", financialYearEnd).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> fetchLandAllotmentPaymentHistoryOfTahasil(String districtCode, LocalDate financialYearStart,
			LocalDate financialYearEnd) {

		String nativeQuery = "SELECT tahasil_code,tahasil_name, "
				+ "SUM(CASE WHEN month = '01' THEN total_amount ELSE NULL END) AS January, "
				+ "SUM(CASE WHEN month = '02' THEN total_amount ELSE NULL END) AS February, "
				+ "SUM(CASE WHEN month = '03' THEN total_amount ELSE NULL END) AS March, "
				+ "SUM(CASE WHEN month = '04' THEN total_amount ELSE NULL END) AS April, "
				+ "SUM(CASE WHEN month = '05' THEN total_amount ELSE NULL END) AS May, "
				+ "SUM(CASE WHEN month = '06' THEN total_amount ELSE NULL END) AS June, "
				+ "SUM(CASE WHEN month = '07' THEN total_amount ELSE NULL END) AS July, "
				+ "SUM(CASE WHEN month = '08' THEN total_amount ELSE NULL END) AS August, "
				+ "SUM(CASE WHEN month = '09' THEN total_amount ELSE NULL END) AS September, "
				+ "SUM(CASE WHEN month = '10' THEN total_amount ELSE NULL END) AS October, "
				+ "SUM(CASE WHEN month = '11' THEN total_amount ELSE NULL END) AS November, "
				+ "SUM(CASE WHEN month = '12' THEN total_amount ELSE NULL END) AS December, "
				+ "SUM(total_amount) AS total_amount, " + "district_code,district_name " + "FROM "
				+ "(SELECT TO_CHAR(DATE_TRUNC('month', payment_date_time), 'mm') AS month,   "
				+ "SUM(transaction_paid_amount) AS total_amount, "
				+ "dm.district_code,dm.district_name, tm.tahasil_code,tm.tahasil_name FROM "
				+ "application.land_allotement_payment lap  "
				+ "JOIN application.land_allotement la ON(lap.land_allotement_id = la.land_allotement_id)  "
				+ "JOIN land_bank.plot_information pi ON(la.plot_code = pi.plot_code)  "
				+ "JOIN land_bank.khatian_information ki ON(pi.khatian_code = ki.khatian_code)  "
				+ "JOIN land_bank.village_master vm USING(village_code)  "
				+ "JOIN land_bank.tahasil_master tm USING(tahasil_code)  "
				+ "JOIN land_bank.district_master dm USING(district_code)  " + "WHERE dm.district_code =:districtCode "
				+ "AND payment_date_time >= DATE(:financialYearStart) AND payment_date_time <= DATE(:financialYearEnd) "
				+ "GROUP BY DATE_TRUNC('month', payment_date_time),tm.tahasil_code,tm.tahasil_name, dm.district_code,dm.district_name "
				+ ") AS combined_results " + "GROUP BY tahasil_code,tahasil_name,district_code,district_name "
				+ "ORDER BY tahasil_name ASC";

		return entity.createNativeQuery(nativeQuery).setParameter("districtCode", districtCode)
				.setParameter("financialYearStart", financialYearStart)
				.setParameter("financialYearEnd", financialYearEnd).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> fetchLandAllotmentPaymentHistoryOfVillage(String tahasilCode, LocalDate financialYearStart,
			LocalDate financialYearEnd) {

		String nativeQuery = "SELECT village_code,village_name, "
				+ "SUM(CASE WHEN month = '01' THEN total_amount ELSE NULL END) AS January, "
				+ "SUM(CASE WHEN month = '02' THEN total_amount ELSE NULL END) AS February, "
				+ "SUM(CASE WHEN month = '03' THEN total_amount ELSE NULL END) AS March, "
				+ "SUM(CASE WHEN month = '04' THEN total_amount ELSE NULL END) AS April, "
				+ "SUM(CASE WHEN month = '05' THEN total_amount ELSE NULL END) AS May, "
				+ "SUM(CASE WHEN month = '06' THEN total_amount ELSE NULL END) AS June, "
				+ "SUM(CASE WHEN month = '07' THEN total_amount ELSE NULL END) AS July, "
				+ "SUM(CASE WHEN month = '08' THEN total_amount ELSE NULL END) AS August, "
				+ "SUM(CASE WHEN month = '09' THEN total_amount ELSE NULL END) AS September, "
				+ "SUM(CASE WHEN month = '10' THEN total_amount ELSE NULL END) AS October, "
				+ "SUM(CASE WHEN month = '11' THEN total_amount ELSE NULL END) AS November, "
				+ "SUM(CASE WHEN month = '12' THEN total_amount ELSE NULL END) AS December, "
				+ "SUM(total_amount) AS total_amount, " + "district_code,district_name,tahasil_code,tahasil_name "
				+ "FROM " + "(SELECT TO_CHAR(DATE_TRUNC('month', payment_date_time), 'mm') AS month,   "
				+ "SUM(transaction_paid_amount) AS total_amount, "
				+ "vm.village_code,vm.village_name,dm.district_code,dm.district_name, tm.tahasil_code,tm.tahasil_name FROM "
				+ "application.land_allotement_payment lap  "
				+ "JOIN application.land_allotement la ON(lap.land_allotement_id = la.land_allotement_id)  "
				+ "JOIN land_bank.plot_information pi ON(la.plot_code = pi.plot_code)  "
				+ "JOIN land_bank.khatian_information ki ON(pi.khatian_code = ki.khatian_code)  "
				+ "JOIN land_bank.village_master vm USING(village_code)  "
				+ "JOIN land_bank.tahasil_master tm USING(tahasil_code)  "
				+ "JOIN land_bank.district_master dm USING(district_code)  " + "WHERE tm.tahasil_code =:tahasilCode "
				+ "AND payment_date_time >= DATE(:financialYearStart) AND payment_date_time <= DATE(:financialYearEnd)"
				+ "GROUP BY DATE_TRUNC('month', payment_date_time),vm.village_code,vm.village_name,tm.tahasil_code,tm.tahasil_name, dm.district_code,dm.district_name "
				+ ") AS combined_results "
				+ "GROUP BY village_code,village_name,tahasil_code,tahasil_name,district_code,district_name "
				+ "ORDER BY village_name ASC";

		return entity.createNativeQuery(nativeQuery).setParameter("tahasilCode", tahasilCode)
				.setParameter("financialYearStart", financialYearStart)
				.setParameter("financialYearEnd", financialYearEnd).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> fetchLandAllotmentPaymentHistoryOfKhatian(String villageCode, LocalDate financialYearStart,
			LocalDate financialYearEnd) {

		String nativeQuery = "SELECT khatian_code,khata_no, "
				+ "SUM(CASE WHEN month = '01' THEN total_amount ELSE NULL END) AS January, "
				+ "SUM(CASE WHEN month = '02' THEN total_amount ELSE NULL END) AS February, "
				+ "SUM(CASE WHEN month = '03' THEN total_amount ELSE NULL END) AS March, "
				+ "SUM(CASE WHEN month = '04' THEN total_amount ELSE NULL END) AS April, "
				+ "SUM(CASE WHEN month = '05' THEN total_amount ELSE NULL END) AS May, "
				+ "SUM(CASE WHEN month = '06' THEN total_amount ELSE NULL END) AS June, "
				+ "SUM(CASE WHEN month = '07' THEN total_amount ELSE NULL END) AS July, "
				+ "SUM(CASE WHEN month = '08' THEN total_amount ELSE NULL END) AS August, "
				+ "SUM(CASE WHEN month = '09' THEN total_amount ELSE NULL END) AS September, "
				+ "SUM(CASE WHEN month = '10' THEN total_amount ELSE NULL END) AS October, "
				+ "SUM(CASE WHEN month = '11' THEN total_amount ELSE NULL END) AS November, "
				+ "SUM(CASE WHEN month = '12' THEN total_amount ELSE NULL END) AS December, "
				+ "SUM(total_amount) AS total_amount, "
				+ "district_code,district_name,tahasil_code,tahasil_name,village_code,village_name " + "FROM "
				+ "(SELECT TO_CHAR(DATE_TRUNC('month', payment_date_time), 'mm') AS month,   "
				+ "SUM(transaction_paid_amount) AS total_amount, "
				+ "ki.khatian_code,ki.khata_no,vm.village_code,vm.village_name,dm.district_code,dm.district_name, tm.tahasil_code,tm.tahasil_name FROM "
				+ "application.land_allotement_payment lap  "
				+ "JOIN application.land_allotement la ON(lap.land_allotement_id = la.land_allotement_id)  "
				+ "JOIN land_bank.plot_information pi ON(la.plot_code = pi.plot_code)  "
				+ "JOIN land_bank.khatian_information ki ON(pi.khatian_code = ki.khatian_code)  "
				+ "JOIN land_bank.village_master vm USING(village_code)  "
				+ "JOIN land_bank.tahasil_master tm USING(tahasil_code)  "
				+ "JOIN land_bank.district_master dm USING(district_code)  " + "WHERE vm.village_code =:villageCode "
				+ "AND payment_date_time >= DATE(:financialYearStart) AND payment_date_time <= DATE(:financialYearEnd)"
				+ "GROUP BY DATE_TRUNC('month', payment_date_time),ki.khatian_code,ki.khata_no,vm.village_code,vm.village_name,tm.tahasil_code,tm.tahasil_name, dm.district_code,dm.district_name "
				+ ") AS combined_results "
				+ "GROUP BY khatian_code,khata_no,village_code,village_name,tahasil_code,tahasil_name,district_code,district_name "
				+ "ORDER BY khata_no ASC";

		return entity.createNativeQuery(nativeQuery).setParameter("villageCode", villageCode)
				.setParameter("financialYearStart", financialYearStart)
				.setParameter("financialYearEnd", financialYearEnd).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> fetchLandAllotmentPaymentHistoryOfPlot(String khatianCode, LocalDate financialYearStart,
			LocalDate financialYearEnd) {

		String nativeQuery = "SELECT plot_code,plot_no, "
				+ "SUM(CASE WHEN month = '01' THEN total_amount ELSE NULL END) AS January, "
				+ "SUM(CASE WHEN month = '02' THEN total_amount ELSE NULL END) AS February, "
				+ "SUM(CASE WHEN month = '03' THEN total_amount ELSE NULL END) AS March, "
				+ "SUM(CASE WHEN month = '04' THEN total_amount ELSE NULL END) AS April, "
				+ "SUM(CASE WHEN month = '05' THEN total_amount ELSE NULL END) AS May, "
				+ "SUM(CASE WHEN month = '06' THEN total_amount ELSE NULL END) AS June, "
				+ "SUM(CASE WHEN month = '07' THEN total_amount ELSE NULL END) AS July, "
				+ "SUM(CASE WHEN month = '08' THEN total_amount ELSE NULL END) AS August, "
				+ "SUM(CASE WHEN month = '09' THEN total_amount ELSE NULL END) AS September, "
				+ "SUM(CASE WHEN month = '10' THEN total_amount ELSE NULL END) AS October, "
				+ "SUM(CASE WHEN month = '11' THEN total_amount ELSE NULL END) AS November, "
				+ "SUM(CASE WHEN month = '12' THEN total_amount ELSE NULL END) AS December, "
				+ "SUM(total_amount) AS total_amount, "
				+ "district_code,district_name,tahasil_code,tahasil_name,village_code,village_name,khatian_code,khata_no, "
				+ "applicant_name AS applicant_name, " + "purchase_area AS purchase_area, "
				+ "total_price_in_purchase_area AS total_price_in_purchase_area, "
				+ "land_order_number AS land_order_number, " + "land_order_date AS land_order_date " + "FROM "
				+ "(SELECT TO_CHAR(DATE_TRUNC('month', payment_date_time), 'mm') AS month,   "
				+ "SUM(transaction_paid_amount) AS total_amount,lapp.applicant_name,la.purchase_area,la.total_price_in_purchase_area, "
				+ "la.land_order_number,la.land_order_date, "
				+ "pi.plot_code,pi.plot_no,ki.khatian_code,ki.khata_no,vm.village_code,vm.village_name,dm.district_code,"
				+ "dm.district_name, tm.tahasil_code,tm.tahasil_name FROM "
				+ "application.land_allotement_payment lap  "
				+ "JOIN application.land_allotement la ON(lap.land_allotement_id = la.land_allotement_id)  "
				+ "JOIN public.land_application AS lapp ON la.land_application_id = lapp.land_application_id "
				+ "JOIN land_bank.plot_information pi ON(la.plot_code = pi.plot_code)  "
				+ "JOIN land_bank.khatian_information ki ON(pi.khatian_code = ki.khatian_code)  "
				+ "JOIN land_bank.village_master vm ON(ki.village_code = vm.village_code)  "
				+ "JOIN land_bank.tahasil_master tm ON(vm.tahasil_code = tm.tahasil_code)  "
				+ "JOIN land_bank.district_master dm ON(tm.district_code = dm.district_code)  "
				+ "WHERE ki.khatian_code =:khatianCode AND payment_date_time >= DATE(:financialYearStart) AND payment_date_time <= DATE(:financialYearEnd)"
				+ "GROUP BY DATE_TRUNC('month', payment_date_time),lapp.applicant_name,la.purchase_area,"
				+ "la.total_price_in_purchase_area,la.land_order_number,la.land_order_date,pi.plot_code,pi.plot_no,ki.khatian_code,"
				+ "ki.khata_no,vm.village_code,vm.village_name,tm.tahasil_code,tm.tahasil_name, dm.district_code,dm.district_name "
				+ ") AS combined_results "
				+ "GROUP BY plot_code,plot_no,khatian_code,khata_no,village_code,village_name,tahasil_code,tahasil_name,district_code,"
				+ "district_name,applicant_name,purchase_area,total_price_in_purchase_area,land_order_number,land_order_date "
				+ "ORDER BY plot_no ASC";

		return entity.createNativeQuery(nativeQuery).setParameter("khatianCode", khatianCode)
				.setParameter("financialYearStart", financialYearStart)
				.setParameter("financialYearEnd", financialYearEnd).getResultList();
	}

}
