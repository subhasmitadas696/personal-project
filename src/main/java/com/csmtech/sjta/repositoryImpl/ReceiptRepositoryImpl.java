package com.csmtech.sjta.repositoryImpl;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.dto.TransactionGenerateDTO;
import com.csmtech.sjta.repository.ReceiptRepository;

@Repository
public class ReceiptRepositoryImpl implements ReceiptRepository {

	@PersistenceContext
	@Autowired
	private EntityManager entity;

	@SuppressWarnings("unchecked")
	@Transactional	
	@Override
	public List<Object[]> fetchFeePayment(BigInteger id) {
		String nativeQuery = "SELECT la.application_no, la.applicant_name, d.district_name, t.tahasil_name, m.village_name, k.khata_no, "
				+ "CAST((SELECT STRING_AGG((select plot_no from land_bank.plot_information where plot_code=ls.plot_code), ',') AS plot_no"
				+ " FROM public.land_schedule ls WHERE ls.land_application_id = la.land_application_id AND deleted_flag='0' )as varchar) "
				+ "as plot_no, lapt.payment_id, TO_CHAR(lapt.payment_date_time, 'DD-MON-YYYY HH:MM '), lapt.tranction_amount, lapt.payment_type, lapt.receipt_no "
				+ "FROM public.land_application la "
				+ "INNER JOIN land_bank.district_master d ON la.district_code = d.district_code "
				+ "INNER JOIN land_bank.tahasil_master t ON la.tehsil_code = t.tahasil_code "
				+ "INNER JOIN land_bank.village_master m ON la.village_code = m.village_code "
				+ "INNER JOIN land_bank.khatian_information k ON la.khatian_code = k.khatian_code "
				+ "INNER JOIN public.land_application_payment_tranction_details lapt on la.land_application_id = lapt.land_application_id "
				+ "WHERE la.land_application_id = :landId AND la.deleted_flag = '0'";
		Query query = entity.createNativeQuery(nativeQuery).setParameter("landId", id);
		return  query.getResultList();
	}

	@Override
	public List<Object[]> fetchFormMFeePayment(BigInteger id) {
		String nativeQuery = "SELECT receipt_no, TO_CHAR(payment_date_time, 'DD-MON-YYYY HH:MM '), tranction_amount,apd.payment_id, cpd.full_name, dm.district_name, tm.tahasil_name, vm.village_name, ki.khata_no, pi.plot_no "
				+ "FROM application.from_m_application_payment_details as apd "
				+ "JOIN application.bidder_form_m_application as bfm ON bfm.bidder_form_m_application_id = apd.from_m__application_id "
				+ "JOIN application.tender_auction ta ON bfm.tender_auction_id = ta.tender_auction_id "
				+ "JOIN application.auction_plot_details ad ON ta.auction_plot_id = ad.auction_plot_details_id "
				+ "JOIN application.auction_plot ap ON ap.auction_plot_id = ad.auction_plot_id "
				+ "JOIN public.citizen_profile_details cpd ON bfm.created_by = cpd.citizen_profile_details_id "
				+ "JOIN land_bank.plot_information pi ON pi.plot_code = ad.plot_code "
				+ "JOIN land_bank.khatian_information ki ON ki.khatian_code = ap.khatian_code "
				+ "JOIN land_bank.district_master dm ON dm.district_code = ap.district_code "
				+ "JOIN land_bank.tahasil_master tm ON tm.tahasil_code = ap.tahasil_code "
				+ "JOIN land_bank.village_master vm ON vm.village_code = ap.village_code "
				+ "WHERE apd.from_m__application_id = :formId";
		Query query = entity.createNativeQuery(nativeQuery).setParameter("formId", id);
		return  query.getResultList();
	}

	@Override
	public List<Object[]> fetchFinalFeePayment(BigInteger id) {
		String nativeQuery = "SELECT lap.receipt_no, TO_CHAR(lap.payment_date_time, 'DD-MON-YYYY HH:MM '), lap.transaction_paid_amount, lap.razorpay_payment_id, lapp.applicant_name, dm.district_name, tm.tahasil_name, vm.village_name, ki.khata_no, pi.plot_no "
				+ "FROM application.land_allotement_payment AS lap "
				+ "JOIN application.land_allotement AS la ON lap.land_allotement_id = la.land_allotement_id "
				+ "JOIN public.land_application AS lapp ON la.land_application_id = lapp.land_application_id "
				+ "JOIN land_bank.plot_information pi ON pi.plot_code = la.plot_code "
				+ "JOIN land_bank.khatian_information ki ON ki.khatian_code = pi.khatian_code "
				+ "JOIN land_bank.village_master vm ON vm.village_code = ki.village_code "
				+ "JOIN land_bank.tahasil_master tm ON tm.tahasil_code = vm.tahasil_code "
				+ "JOIN land_bank.district_master dm ON dm.district_code = tm.district_code "
				+ "WHERE lap.land_allotement_payment_id = :payId";
		Query query = entity.createNativeQuery(nativeQuery).setParameter("payId", id);
		return  query.getResultList();
	}

}
