package com.csmtech.sjta.repositoryImpl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.dto.LandAllotedPaymentRecordListDTO;
import com.csmtech.sjta.dto.LandAllotementPaymantRecordDTO;
import com.csmtech.sjta.dto.LandAllotementResponesDTO;
import com.csmtech.sjta.repository.LandAllotedPaymentUserRepository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class LandAllotedPaymentUserRepositoryImpl implements LandAllotedPaymentUserRepository {

	@PersistenceContext
	@Autowired
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public List<LandAllotementPaymantRecordDTO> getLandAllotementDetails(BigInteger landAllotementId) {
		String nativeQuery = "SELECT " + "land_allotement_id as landAllotementId , "
				+ "meeting_schedule_id as meetingId, " 
				+ "(select plot_no from land_bank.plot_information where plot_code=la.plot_code) as plotNo,  "
				+ "CAST(total_area AS VARCHAR) as totalArea, " + "CAST(purchase_area AS VARCHAR) as purchaseArea, "
				+ "CAST(price_per_acer AS VARCHAR) as pricePerAcer, "
				+ "CAST(total_price_in_purchase_area AS VARCHAR) as totalPricePerAcer , "
				+ "form_m_application_id as fromMid, " + "land_application_id as landId,"
				+ "(select sum(transaction_paid_amount) as paid_amount from " + "application.land_allotement_payment "
				+ "where land_allotement_id=la.land_allotement_id " + "AND deleted_flag='0' "
				+ "AND payment_status='1') as paidAmount,"
				+ "form_register_docs as registerDocs,CAST(form_register_flag as varchar) as registerFlag, "
				+ "(SELECT CASE WHEN la2.total_price_in_purchase_area = COALESCE(SUM(COALESCE(lap.transaction_paid_amount, 0)), 0) THEN 'true' "
				+ "ELSE 'false' END AS is_amount_equal FROM application.land_allotement la2 "
				+ "LEFT JOIN application.land_allotement_payment lap ON la2.land_allotement_id = lap.land_allotement_id "
				+ "WHERE la2.land_allotement_id = la.land_allotement_id AND la2.deleted_flag = '0' AND (lap.deleted_flag = '0' OR lap.deleted_flag IS NULL)  "
				+ "GROUP BY la2.total_price_in_purchase_area) as paymentFlag ,la.plot_code as plotCode "
				+ "FROM application.land_allotement la   " + "WHERE land_allotement_id =:landAllotementId "
				+ "AND form_16_flag = '1' " + "AND deleted_flag = '0'";
		Query query = entityManager.createNativeQuery(nativeQuery, LandAllotementPaymantRecordDTO.class)
				.setParameter("landAllotementId", landAllotementId);
		List<LandAllotementPaymantRecordDTO> resultList = query.getResultList();
		log.info("getLandAllotementDetails() execute and return the result :: !!");
		return resultList;
	}

	@Transactional
	@Override
	public Integer insertPaymentTransaction(String orderId, String paymentSignature, String paymentId,
			BigDecimal tranctionAmount, BigInteger userId, BigInteger landAllotedId,String reciptNo) {
		LocalDateTime currentDateTime = LocalDateTime.now();
		String nativeQuery = " INSERT INTO application.land_allotement_payment(land_allotement_id,transaction_paid_amount,payment_type,payment_status,user_id, "
				+ "razorpay_payment_id,razor_pay_signature,razor_pay_orderid,payment_date_time,receipt_no ) "
				+ " VALUES (?, ?, 1, B'1', ?, ?, ? , ?, ?,? ) ";
		log.info(":: insertPaymentTransaction() method insert the tranction record Success.!!");
		return entityManager.createNativeQuery(nativeQuery).setParameter(1, landAllotedId)
				.setParameter(2, tranctionAmount).setParameter(3, userId).setParameter(4, paymentId)
				.setParameter(5, paymentSignature).setParameter(6, orderId).setParameter(7, currentDateTime)
				.setParameter(8, reciptNo)
				.executeUpdate();
	}

	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public List<LandAllotedPaymentRecordListDTO> getPayments(BigInteger landAllotedId) {
		String nativeQuery = "SELECT land_allotement_payment_id as paymentId,CAST(transaction_paid_amount as varchar) as paymentAmount, "
				+ "payment_date_time as dateTime, payment_type as paymentType,razorpay_payment_id as paymentRId ,razor_pay_signature as paymentRsign , "
				+ "razor_pay_orderid as paymentROrderId ,payment_status as paymentStatus "
				+ "FROM application.land_allotement_payment "
				+ "WHERE land_allotement_id = :landAllotedId AND deleted_flag = '0' AND payment_status = '1' ";

		Query query = entityManager.createNativeQuery(nativeQuery, LandAllotedPaymentRecordListDTO.class)
				.setParameter("landAllotedId", landAllotedId);
		log.info("getPayments() execute and return the result :: !!");
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public List<LandAllotementResponesDTO> getLandAllotementDetails(Integer limit, Integer offset) {
		String nativeQuery = "SELECT " 
	            + "land_allotement_id as landAllotmentId, " 
				+ "(select plot_no from land_bank.plot_information where plot_code=la.plot_code) as plotNo , "
				+ "total_area as totalArea, " + "purchase_area as purchaseArea, " + "price_per_acer as pricePerAcer, "
				+ "total_price_in_purchase_area as totalPriceInPurchaseArea, "
				+ "CASE WHEN la.land_application_id IS NOT NULL THEN (SELECT applicant_name  "
				+ "FROM  public.land_application WHERE land_application_id = la.land_application_id LIMIT 1) "
				+ "ELSE (SELECT (SELECT full_name   FROM public.citizen_profile_details ud  WHERE "
				+ "ud.citizen_profile_details_id = bfma.user_id) FROM application.bidder_form_m_application bfma  "
				+ "WHERE bidder_form_m_application_id = la.form_m_application_id LIMIT 1)  " + "END as fullName, "
				+ "CAST(la.form_16_flag as varchar) as from16Flag, " + "form_16_docs as form16Docs, "
				+ "CAST(form_register_flag as varchar) as registerFlag, "
				+ "(SELECT CASE WHEN la2.total_price_in_purchase_area = COALESCE(SUM(COALESCE(lap.transaction_paid_amount, 0)), 0) THEN 'true' "
				+ "ELSE 'false' END AS is_amount_equal FROM application.land_allotement la2 "
				+ "LEFT JOIN application.land_allotement_payment lap ON la2.land_allotement_id = lap.land_allotement_id "
				+ "WHERE la2.land_allotement_id = la.land_allotement_id AND la2.deleted_flag = '0' AND (lap.deleted_flag = '0' OR lap.deleted_flag IS NULL)  "
				+ "GROUP BY la2.total_price_in_purchase_area) as paymentFlag " 
				+ "FROM application.land_allotement la "
				+ "WHERE deleted_flag = '0' ORDER BY la.land_allotement_id DESC  offset :offset limit :limit  ";
		log.info("getLandAllotementDetails() execute and return the result :: !!");
		return entityManager.createNativeQuery(nativeQuery, LandAllotementResponesDTO.class)
				.setParameter("limit", limit).setParameter("offset", offset).getResultList();
	}

	@Transactional
	@Override
	public BigInteger countLandAllortUser() {
		String nativeQuery = "SELECT COUNT(*) " + "FROM application.land_allotement la " + "WHERE deleted_flag='0' ";
		log.info("countLandAllortUser() execute and return the result :: !!");
		return (BigInteger) entityManager.createNativeQuery(nativeQuery).getSingleResult();
	}

	@Transactional
	@Override
	public Integer updateLandAllotementFormRegisterDocsAndFlag(BigInteger landAllotementId, String formRegisterDocs) {
		String nativeUpdateQuery = "UPDATE application.land_allotement "
				+ "SET form_register_docs = :formRegisterDocs, " + "form_register_flag = B'1' "
				+ "WHERE land_allotement_id = :landAllotementId ";

		Query query = entityManager.createNativeQuery(nativeUpdateQuery)
				.setParameter("formRegisterDocs", formRegisterDocs).setParameter("landAllotementId", landAllotementId);
		log.info("updateLandAllotementFormRegisterDocsAndFlag() execute and return the result :: !!");
		return query.executeUpdate();
	}
}
