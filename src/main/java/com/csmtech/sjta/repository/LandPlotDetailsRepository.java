package com.csmtech.sjta.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.dto.ApplicantNumberAndMobileDTO;
import com.csmtech.sjta.dto.LandIndividualAppDTO;
import com.csmtech.sjta.dto.PlotDetails;
import com.csmtech.sjta.util.CommonUtil;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class LandPlotDetailsRepository {

	@PersistenceContext
	@Autowired
	private EntityManager entity;

	@Transactional
	public Integer updateApplicantRecord(LandIndividualAppDTO dto) {
		String queryString = "" + "update land_application " + "set district_code = :plot_district_id , "
				+ "    tehsil_code = :plot_tehsil_id , " + "    village_code = :plot_mouza_id , "
				+ "    khatian_code = :plot_khata_no_id  " + "where land_application_id = :applicantId";

		Query query = entity.createNativeQuery(queryString).setParameter("plot_district_id", dto.getSelDistrictName())
				.setParameter("plot_tehsil_id", dto.getSelTehsilName()).setParameter("plot_mouza_id", dto.getSelMouza())
				.setParameter("plot_khata_no_id", dto.getSelKhataNo())
				.setParameter("applicantId", dto.getIntLandApplicantId());

		entity.close();
		return query.executeUpdate();
	}

	@Transactional
	public Integer batchUpdateDeleteNocPlots(LandIndividualAppDTO nocPlot) {

		String queryString = "update public.land_schedule set deleted_flag = '1' where land_application_id = :applicantId";
		entity.createNativeQuery(queryString).setParameter("applicantId", nocPlot.getIntLandApplicantId())
				.executeUpdate();

		String insertQuery = "insert into public.land_schedule "
				+ " (plot_code,total_area,purchase_area,created_by,land_application_id)  " + " values "
				+ "(:plot_no_id,:total_area,:purchase_area,:created_by,:land_applicant_id)";

		int batchSize = 50;
		List<PlotDetails> plotDetailsList = nocPlot.getPlot_details();
		for (int i = 0; i < plotDetailsList.size(); i++) {
			PlotDetails plotDetails = plotDetailsList.get(i);
			BigDecimal totalArea = new BigDecimal(plotDetails.getTotal_area());
			BigDecimal purchaseArea = new BigDecimal(plotDetails.getPurchase_area());
			entity.createNativeQuery(insertQuery).setParameter("plot_no_id", plotDetails.getPlot_id())
					.setParameter("total_area", totalArea).setParameter("purchase_area", purchaseArea)
					.setParameter("created_by", nocPlot.getIntCreatedBy())
					.setParameter("land_applicant_id", nocPlot.getIntLandApplicantId()).executeUpdate();
			if (i % batchSize == 0) {
				entity.flush();
				entity.clear();

			}
		}

		entity.close();
		return batchSize;

	}

	@Transactional
	public LandIndividualAppDTO getLandIndividualAppDTOByLandApplicantId(Integer landApplicantId) {
		String sqlQuery = "SELECT p.district_code as selDistrictName, p.tehsil_code as selTehsilName, "
				+ "p.village_code as selMouza, p.khatian_code as selKhataNo, pp.plot_code as selPlotNo, "
				+ "pp.total_area as txtTotalRakba, pp.purchase_area as txtPurchaseRakba, "
				+ "mp.plot_no as plot_name, (select ki.khata_no from land_bank.plot_information pii  "
				+ "join land_bank.khatian_information ki on(ki.khatian_code=pii.khatian_code) \r\n"
				+ "where pii.plot_code=pp.plot_code),"
				+ "(select (select (select (select (select \r\n"
				+ " district_name from land_bank.district_master dm  where dm.district_code=tm.district_code LIMIT 1) from land_bank.tahasil_master tm   \r\n"
				+ " where tm.tahasil_code=vm.tahasil_code LIMIT 1) from land_bank.village_master vm   where vm.village_code=ki.village_code LIMIT 1)  \r\n"
				+ " from land_bank.khatian_information ki  where ki.khatian_code=pii.khatian_code LIMIT 1) from land_bank.plot_information pii where  \r\n"
				+ " pii.plot_code=pp.plot_code "
				+ " limit 1) as district,\r\n"
				+ "   (select (select (select (select tahasil_name from land_bank.tahasil_master tm   \r\n"
				+ " where tm.tahasil_code=vm.tahasil_code LIMIT 1) from land_bank.village_master vm   where vm.village_code=ki.village_code LIMIT 1)  \r\n"
				+ " from land_bank.khatian_information ki  where ki.khatian_code=pii.khatian_code LIMIT 1) from land_bank.plot_information pii where  \r\n"
				+ " pii.plot_code=pp.plot_code limit 1) as tahasil,\r\n"
				+ "  (select (select (select village_name from land_bank.village_master vm   where vm.village_code=ki.village_code LIMIT 1)  \r\n"
				+ " from land_bank.khatian_information ki  where ki.khatian_code=pii.khatian_code LIMIT 1) from land_bank.plot_information pii where  \r\n"
				+ " pii.plot_code=pp.plot_code limit 1) as mouza "
				+ "FROM public.land_application p  "
				+ "JOIN public.land_schedule pp ON ( pp.land_application_id = p.land_application_id ) \r\n"
				+ "JOIN land_bank.plot_information mp ON ( mp.plot_code = pp.plot_code ) \r\n"
				+ "WHERE pp.deleted_flag = '0' and p.land_application_id =:landApplicantId";

		@SuppressWarnings("unchecked")
		List<Object[]> resultList = entity.createNativeQuery(sqlQuery).setParameter("landApplicantId", landApplicantId)
				.getResultList();
		LandIndividualAppDTO landIndividualAppDTO = new LandIndividualAppDTO();
		List<PlotDetails> plotDetailsList = new ArrayList<>();

		for (Object[] result : resultList) {
			landIndividualAppDTO.setSelDistrictName(((String) result[0]));
			landIndividualAppDTO.setSelTehsilName(((String) result[1]));
			landIndividualAppDTO.setSelMouza(((String) result[2]));
			landIndividualAppDTO.setSelKhataNo(((String) result[3]));
			PlotDetails plotDetails = new PlotDetails();
			plotDetails.setPlot_id(((String) result[4]));
			plotDetails.setPlot_name((String) result[7]);
			plotDetails.setTotal_area(((BigDecimal) result[5]).toString());
			plotDetails.setPurchase_area(((BigDecimal) result[6]).toString());
			plotDetails.setKhataNo((String) result[8]);
			plotDetails.setDistrict((String) result[9]);
			plotDetails.setTahasil((String) result[10]);
			plotDetails.setMouza((String) result[11]);
			plotDetailsList.add(plotDetails);
		}

		landIndividualAppDTO.setPlot_details(plotDetailsList);

		entity.close();
		return landIndividualAppDTO;
	}

	@Transactional
	public Integer updateApplicantName(BigInteger applicantId, String newAppName) {
		String queryy = "select application_status_id from land_application_approval WHERE land_application_id = "
				+ applicantId;

		Object applicationsatus = 0;
		try {
			applicationsatus = CommonUtil.getDynSingleData(entity, queryy);
		} catch (Exception ex) {
			applicationsatus = 0;
		}

		if (applicationsatus.toString().equalsIgnoreCase("8") || applicationsatus.toString().equalsIgnoreCase("9")
				|| applicationsatus.toString().equalsIgnoreCase("10")
				|| applicationsatus.toString().equalsIgnoreCase("18")) {
			return 1;
		} else {
			String nativeUpdateQuery = "UPDATE land_application SET application_no = :newAppName WHERE land_application_id = :applicantId";

			Integer status = entity.createNativeQuery(nativeUpdateQuery).setParameter("newAppName", newAppName)
					.setParameter("applicantId", applicantId).executeUpdate();

			entity.close();
			return status;
		}

	}

	// returive applicant no or mobile no
	@Transactional
	public List<ApplicantNumberAndMobileDTO> fetchApplicantInfoById(BigInteger i) {
		String nativeQuery = "SELECT application_no, mobile_no FROM land_application WHERE land_application_id = :applicantId";

		Query query = entity.createNativeQuery(nativeQuery);
		query.setParameter("applicantId", i);

		@SuppressWarnings("unchecked")
		List<Object[]> results = query.getResultList();
		List<ApplicantNumberAndMobileDTO> applicantInfoList = mapToDTOList(results);
		entity.close();
		return applicantInfoList;
	}

	private List<ApplicantNumberAndMobileDTO> mapToDTOList(List<Object[]> resultList) {
		// Create a list to hold DTO objects
		List<ApplicantNumberAndMobileDTO> applicantInfoList = new ArrayList<>();

		for (Object[] result : resultList) {
			String applicantNo = (String) result[0];
			String mobileNo = (String) result[1];

			ApplicantNumberAndMobileDTO dto = new ApplicantNumberAndMobileDTO();
			dto.setApplicantNo(applicantNo);
			dto.setMobileNo(mobileNo);

			applicantInfoList.add(dto);
		}

		return applicantInfoList;
	}

	@Transactional
	public Integer insertPaymentTransaction(BigInteger landApplicantId, String orderId, String paymentSignature,
			String paymentId, String paymentStatus,BigDecimal amount,String tranctionUniqueNo) {
		String nativeQuery = " INSERT INTO public.land_application_payment_tranction_details "
				+ " (land_application_id, order_id, payment_signature, payment_id, payment_status, created_by,tranction_amount,receipt_no) "
				+ " VALUES (?, ?, ?, ?, ?,  "
				+ " currval(pg_get_serial_sequence('public.land_application_payment_tranction_details', 'land_application_payment_tranction_details_id')),?,?)";

		log.info(":: insertPaymentTransaction() method insert the tranction record Success.!!");
		return entity.createNativeQuery(nativeQuery).setParameter(1, landApplicantId).setParameter(2, orderId)
				.setParameter(3, paymentSignature).setParameter(4, paymentId).setParameter(5, paymentStatus).setParameter(6, amount)
				.setParameter(7, tranctionUniqueNo)
				.executeUpdate();
	}

	@Transactional
	public Integer updateAppStatusForLandApplicationId(BigInteger landApplicationId, Integer newAppStatus) {
		String sqlQuery = "UPDATE public.land_application " + " SET app_status = :newAppStatus "
				+ " WHERE land_application_id = :landApplicationId";

		return entity.createNativeQuery(sqlQuery).setParameter("newAppStatus", newAppStatus)
				.setParameter("landApplicationId", landApplicationId).executeUpdate();
	}

	@Transactional
	public String getCurrentStage(Integer landApplicationId) {
		String nativeQuery = "SELECT app_status FROM land_application WHERE land_application_id = :applicantId";
		Object appStage = entity.createNativeQuery(nativeQuery).setParameter("applicantId", landApplicationId)
				.getSingleResult();
		return appStage.toString();
	}

}
