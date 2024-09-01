package com.csmtech.sjta.repositoryImpl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.dto.LandAllotementResponesDTO;
import com.csmtech.sjta.dto.LandAllotementResponesNewDTO;
import com.csmtech.sjta.dto.LandAllotementWinnerResultDto;
import com.csmtech.sjta.entity.LandAllotementAuctionEntity;
import com.csmtech.sjta.entity.LandAllotementEntity;
import com.csmtech.sjta.repository.LandAllertmentSlcCompliteRepository;

@Repository
public class LandAllertmentSlcCompliteRepositoryImpl implements LandAllertmentSlcCompliteRepository {

	@PersistenceContext
	@Autowired
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public List<Object[]> getMeetingSchedulesByMeetingLevelId(Integer limit, Integer offset) {
		String nativeQuery =  "SELECT  "
//				+ "ms.meeting_schedule_id, "
				+" DISTINCT    ON(ms.meeting_schedule_id) ms.meeting_schedule_id , "
				+ " ms.meeting_no, ms.meeting_level_id , "
				+ " (select count(*) from application.meeting_schedule_applicant where  "
				+ "	meeting_schedule_id=ms.meeting_schedule_id) as land_meeting_count," 
				+ " ms.venue, "
				+ "	CAST(TO_CHAR(ms.meeting_date, 'YYYY-MM-DD') as varchar) as meeting_date, " 
				+ "	ms.meeting_purpose , msa.bidder_form_m_application_id, msa.land_application_id "
				+ " FROM application.meeting_schedule ms JOIN application.meeting_schedule_applicant msa ON(msa.meeting_schedule_id=ms.meeting_schedule_id) "
				+ " WHERE ms.meeting_level_id = 4 AND ms.upload_mom IS NOT NULL AND msa.status='0' AND ms.status='0' "
				+ " AND msa.land_allotement_complited_flag=0 AND msa.approval_status=1 ORDER BY ms.meeting_schedule_id DESC  OFFSET :offset  limit :limit";
		return (List<Object[]>) entityManager.createNativeQuery(nativeQuery).setParameter("limit", limit)
				.setParameter("offset", offset).getResultList();
	}

	@Transactional
	@Override
	public BigInteger countMeetingSchedulesByMeetingLevelId() {
		String nativeQuery = "select count(*) from ( SELECT "
				+ "        DISTINCT     "
				+ "            ON(ms.meeting_schedule_id) ms.meeting_schedule_id , "
				+ "            ms.meeting_no, "
				+ "            ms.meeting_level_id , "
				+ "            (select "
				+ "                count(*)  "
				+ "        from "
				+ "            application.meeting_schedule_applicant  "
				+ "        where "
				+ "            meeting_schedule_id=ms.meeting_schedule_id) as land_meeting_count, "
				+ "        ms.venue, "
				+ "        CAST(TO_CHAR(ms.meeting_date, "
				+ "        'YYYY-MM-DD') as varchar) as meeting_date, "
				+ "        ms.meeting_purpose , "
				+ "        msa.bidder_form_m_application_id, "
				+ "        msa.land_application_id   "
				+ "    FROM "
				+ "        application.meeting_schedule ms  "
				+ "    JOIN "
				+ "        application.meeting_schedule_applicant msa  "
				+ "            ON( "
				+ "                msa.meeting_schedule_id=ms.meeting_schedule_id "
				+ "            )   "
				+ "    WHERE "
				+ "        ms.meeting_level_id = 4  "
				+ "        AND ms.upload_mom IS NOT NULL  "
				+ "        AND msa.status='0'  "
				+ "        AND ms.status='0'   "
				+ "        AND msa.land_allotement_complited_flag=0  "
				+ "        AND msa.approval_status=1  "
				+ "    ORDER BY "
				+ "        ms.meeting_schedule_id DESC ) as subquery ";

		return (BigInteger) entityManager.createNativeQuery(nativeQuery).getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public List<Map<String, Object>> getDistinctPlotNumbers(BigInteger meetingScheduleId,Integer flagStatus) {
//		String nativeQuery="";
//		if(flagStatus==1) {
//			nativeQuery="";
//		}else {
			String nativeQuery = "SELECT DISTINCT   "
				 		+ "pi.plot_no , "
				 		+ "pi.plot_code   "
				 		+ "FROM application.meeting_schedule ms   "
				 		+ "JOIN application.meeting_schedule_applicant msa ON(msa.meeting_schedule_id = ms.meeting_schedule_id) "
				 		+ "JOIN land_bank.plot_information pi ON(msa.plot_no=pi.plot_code)  "
//				 		+ "LEFT JOIN application.land_allotement la ON(msa.plot_no=la.plot_code)"
				 		+ "WHERE  ms.meeting_schedule_id = :meetingScheduleId AND  ms.status='0' AND msa.status='0' "
				 		+ "AND msa.status='0' AND msa.land_allotement_complited_flag=0 AND msa.approval_status=1 ";
//		}
		
		List<Object[]> resultList = entityManager.createNativeQuery(nativeQuery)
				.setParameter("meetingScheduleId", meetingScheduleId).getResultList();

		List<Map<String, Object>> keyValuePairs = new ArrayList<>();
		for (Object[] res : resultList) {
			Map<String, Object> keyValueMap = new HashMap<>();
			keyValueMap.put("PlotNo", res[0]);
			keyValueMap.put("plotCode", res[1]);
			keyValuePairs.add(keyValueMap);
		}

		return keyValuePairs;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public List<Object[]> executeCustomQueryGetLandAllertmentAlRequiredDetails(String plotNo,
			BigInteger meetingScheduleId,Integer flagStatus) {
		String nativeQuery="";
		List<Object[]> response=null;
		if(flagStatus==1) {
			nativeQuery="SELECT msa.bidder_form_m_application_id, "
					+ "((SELECT (select full_name from public.citizen_profile_details where citizen_profile_details_id=bfma.user_id)  "
					+ "FROM application.bidder_form_m_application bfma  "
					+ "WHERE msa.bidder_form_m_application_id = bfma.bidder_form_m_application_id) || ' - ( ' || (SELECT "
					+ " bidder_form_m_application_number FROM application.bidder_form_m_application bfma  "
					+ "WHERE msa.bidder_form_m_application_id = bfma.bidder_form_m_application_id) || ' )' ) AS land_app_name_and_uniqueno  , "
					+ "CAST(pi.area_acre  as varchar) as totalArea, "
					+ "CAST(pi.area_acre as varchar) as purchaseArea, "
					+ "CAST(CAST(la.highest_bid_price AS NUMERIC(12, 2)) as varchar) as pricePerAcer, "
					+ "CAST(CAST((pi.area_acre*la.highest_bid_price) AS NUMERIC(12, 2)) as varchar) as totalPrice "
					+ "FROM application.meeting_schedule ms  "
					+ "JOIN application.meeting_schedule_applicant msa  ON (msa.meeting_schedule_id = ms.meeting_schedule_id)  "
					+ "JOIN land_bank.plot_information pi  ON (pi.plot_code = msa.plot_no)  "
					+ "JOIN application.auction_plot_details apd ON (msa.plot_no = apd.plot_code) "
					+ "JOIN application.auction_plot ap ON (ap.auction_plot_id = apd.auction_plot_id) "
					+ "JOIN application.tender_auction ta ON (ta.auction_plot_id = apd.auction_plot_details_id)  "
					+ "JOIN application.live_auction la ON (la.tender_auction_id = ta.tender_auction_id) "
					+ "WHERE msa.status='0' AND ms.status='0' AND ms.meeting_schedule_id=:meeting_schedule_id "
					+ "AND apd.deleted_flag='0' AND apd.deleted_flag='0' AND ta.deleted_flag='0' AND la.deleted_flag='0' AND msa.plot_no=:plotCode AND msa.approval_status=1 ";
			response=entityManager.createNativeQuery(nativeQuery)
				      .setParameter("meeting_schedule_id", meetingScheduleId).setParameter("plotCode", plotNo).getResultList();
		}else {
		nativeQuery = "SELECT DISTINCT ms.meeting_schedule_id,msa.land_application_id,ms.meeting_no, "
				+ "(SELECT plot_no FROM land_bank.plot_information WHERE plot_code = ls.plot_code) AS plot_no, "
				+ "(SELECT applicant_name FROM public.land_application WHERE msa.land_application_id = land_application_id) AS land_app_name, "
				+ "CAST(CAST(pv.price_per_acre AS NUMERIC(12, 2)) AS VARCHAR) AS price_per_acer, "
				+ "CAST(pi.area_acre AS VARCHAR) AS total_area, "
				+ "CAST(ls.purchase_area AS VARCHAR) AS purchase_area, "
				+ "CAST(CAST(pv.price_per_acre * ls.purchase_area AS NUMERIC(12, 2)) AS VARCHAR) AS total_price , "
				+ " ((SELECT applicant_name FROM public.land_application WHERE msa.land_application_id = land_application_id) || ' - ( ' || "
				+ "(SELECT  application_no  FROM  public.land_application  WHERE  msa.land_application_id = land_application_id) || ' )' ) AS land_app_name_and_uniqueno, "
				+ "pi.plot_code  "
				+ "FROM application.meeting_schedule ms "
				+ "JOIN application.meeting_schedule_applicant msa ON (msa.meeting_schedule_id = ms.meeting_schedule_id) "
				+ "JOIN public.land_schedule ls ON (ls.land_application_id = msa.land_application_id) "
				+ "JOIN land_bank.plot_information pi ON (pi.plot_code = ls.plot_code) "
				+ "JOIN application.plot_valuation pv ON (pv.plot_code = pi.plot_code)  "
//				+ "JOIN application.land_allotment_configuration lac ON (lac.plot_code = pi.plot_code) "
				+ "WHERE pi.plot_code = :plot_no "  
				+ "AND ms.meeting_schedule_id = :meeting_schedule_id " 
				+ "AND ls.deleted_flag = '0' " 
				+ "AND msa.status = '0' " 
				+ "AND pv.deleted_flag = '0' "
				+ "AND msa.status = '0'  ";
		      response=entityManager.createNativeQuery(nativeQuery).setParameter("plot_no", plotNo)
		      .setParameter("meeting_schedule_id", meetingScheduleId).getResultList();
		}

		return response;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public List<Object[]> executeCustomQueryGetLandAllertmentAlRequiredDetailsNativeQuery(
			BigInteger meetingScheduleIdParam) {
		String nativeQuery = "WITH SecondQuery AS ( " + "   SELECT DISTINCT " + "       (SELECT plot_no "
				+ "        FROM land_bank.plot_information " + "        WHERE plot_code = ls.plot_code) AS plot_no "
				+ "   FROM " + "       application.meeting_schedule ms " + "   JOIN "
				+ "       application.meeting_schedule_applicant msa ON (msa.meeting_schedule_id = ms.meeting_schedule_id) "
				+ "   JOIN " + "       public.land_schedule ls ON (ls.land_application_id = msa.land_application_id) "
				+ "   WHERE " + "       ms.meeting_schedule_id = :meetingScheduleIdParam "
				+ "       AND ls.deleted_flag = '0' " + "       AND msa.status = '0' " + ") " + "SELECT DISTINCT "
				+ "   ms.meeting_schedule_id, " + "   msa.land_application_id, " + "   ms.meeting_no, "
				+ "   SecondQuery.plot_no, " + "   (SELECT applicant_name " + "    FROM public.land_application "
				+ "    WHERE msa.land_application_id = land_application_id) AS land_app_name, "
				+ "   CAST(CAST(lac.total_amount AS NUMERIC(12, 2)) AS VARCHAR) AS price_per_acer, "
				+ "   CAST(pi.area_acre AS VARCHAR) AS total_area, "
				+ "   CAST(ls.purchase_area AS VARCHAR) AS purchase_area, "
				+ "   CAST(CAST((lac.total_amount * ls.purchase_area) AS NUMERIC(12, 2)) AS VARCHAR) AS total_price "
				+ "FROM " + "   application.meeting_schedule ms " + "JOIN "
				+ "   application.meeting_schedule_applicant msa ON (msa.meeting_schedule_id = ms.meeting_schedule_id) "
				+ "JOIN " + "   public.land_schedule ls ON (ls.land_application_id = msa.land_application_id) "
				+ "JOIN " + "   land_bank.plot_information pi ON (pi.plot_code = ls.plot_code) " + "JOIN "
				+ "   application.land_allotment_configuration lac ON (lac.plot_code = pi.plot_code) " + "JOIN "
				+ "   SecondQuery ON SecondQuery.plot_no = pi.plot_no " + "WHERE "
				+ "   pi.plot_no IN (SELECT plot_no FROM SecondQuery) "
				+ "   AND ms.meeting_schedule_id = :meetingScheduleIdParam " + "   AND ls.deleted_flag = '0' "
				+ "   AND msa.status = '0' " + "   AND lac.deleted_flag = '0' " + "   AND msa.status = '0'";

		Query query = entityManager.createNativeQuery(nativeQuery);
		query.setParameter("meetingScheduleIdParam", meetingScheduleIdParam);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public List<LandAllotementResponesNewDTO> getLandAllotmentDetails(Integer limit, Integer offset) {
		String nativeQuery = "SELECT " + "land_allotement_id as landAllotmentId , " 
	            + "pi.plot_no as plotNo  , "
				+ "total_area as totalArea , " + "purchase_area as purchaseArea , "
				+ "price_per_acer as pricePerAcer , " + "total_price_in_purchase_area as totalPriceInPurchaseArea ,"
				+ " CASE WHEN la.land_application_id IS NOT NULL THEN "
				+ "(SELECT applicant_name FROM public.land_application WHERE land_application_id = la.land_application_id LIMIT 1) "
				+ "ELSE "
				+ "(SELECT (SELECT full_name FROM public.citizen_profile_details ud WHERE ud.citizen_profile_details_id = bfma.user_id) "
				+ "FROM application.bidder_form_m_application bfma WHERE bidder_form_m_application_id = la.form_m_application_id  LIMIT 1) "
				+ "END as fullName, " + "CAST(la.form_16_flag as varchar) as from16Flag ,"
				+ " form_16_docs as form16Docs  " 
				+ "FROM application.land_allotement la "
				+ "JOIN land_bank.plot_information pi ON(la.plot_code=pi.plot_code) "
				+ "WHERE deleted_flag = '0' ORDER BY la.land_allotement_id DESC offset :offset limit :limit ";

		return entityManager.createNativeQuery(nativeQuery, LandAllotementResponesNewDTO
				.class)
				.setParameter("limit", limit).setParameter("offset", offset).getResultList();
	}

	@Transactional
	@Override
	public BigInteger countLandAllortUser() {
		String nativeQuery = "SELECT COUNT(*) " + "FROM application.land_allotement " + "WHERE deleted_flag='0'";

		return (BigInteger) entityManager.createNativeQuery(nativeQuery).getSingleResult();
	}

	@Transactional
	@Override
	public Integer updateLandAllotementFlag(BigInteger landAlloId) {
		String nativeQuery = "UPDATE application.land_allotement " + "SET deleted_flag = '1' "
				+ "WHERE land_allotement_id = :landAlloId ";
		return entityManager.createNativeQuery(nativeQuery).setParameter("landAlloId", landAlloId).executeUpdate();
	}

	@Transactional
	@Override
	public BigInteger goForAuctionCount() {
		String nativeQuery = "SELECT COUNT(*) " + "FROM application.land_allotement_for_auction "
				+ "WHERE deleted_flag='0'";
		return (BigInteger) entityManager.createNativeQuery(nativeQuery).getSingleResult();
	}

	@Transactional
	@Override
	public Integer updateGoForAyction(BigInteger landAlloId) {
		String nativeQuery = "UPDATE application.land_allotement_for_auction " + "SET deleted_flag = '1' "
				+ "WHERE land_allotement_for_auction_id = :landAlloId ";
		return entityManager.createNativeQuery(nativeQuery).setParameter("landAlloId", landAlloId).executeUpdate();
	}

	@Transactional
	@Override
	public Integer updateLandAllotementFrom16Record(BigInteger landAllotementId, String form16Docs,
			String form16Remark) {
		String nativeQuery = "UPDATE application.land_allotement " + "SET form_16_docs = :form16Docs, "
				+ "form_16_remark = :form16Remark, " + "form_16_flag = B'1' "
				+ "WHERE land_allotement_id = :landAllotementId ";

		Query query = entityManager.createNativeQuery(nativeQuery);
		query.setParameter("form16Docs", form16Docs);
		query.setParameter("form16Remark", form16Remark);
		query.setParameter("landAllotementId", landAllotementId);
		return query.executeUpdate();
	}

	@Transactional
	@Override
	public List<LandAllotementWinnerResultDto> getLandAllotmentWnnerDetails(BigInteger createdBy, Integer limit,
			Integer offset) {
		String nativeQuery = " SELECT la1.land_allotement_id as landAllotementId,la1.land_application_id as landId,bfma.created_by as ctreatedBy ,"
				+ "la1.form_16_docs as docs,la1.form_16_remark as remark , "
				+ "(SELECT full_name FROM public.citizen_profile_details ud WHERE citizen_profile_details_id = bfma.created_by AND ud.status = '0' LIMIT 1) as fullName , "
				+ "(select plot_no from land_bank.plot_information where plot_code=la1.plot_code) as plotNo, "  
				+ "la1.total_area as totalArea,la1.purchase_area as purchaseArea, la1.price_per_acer as pricePerAcer, "
				+ "la1.total_price_in_purchase_area as totalPricePurchaseArea, "
				+ "(select(select ki.khata_no from land_bank.khatian_information ki where  ki.khatian_code=pii.khatian_code LIMIT 1) from "
				+ "land_bank.plot_information pii  where pii.plot_code=la1.plot_code LIMIT 1) as kahataNo, "
				+ "(select (select (select vm.village_name from land_bank.village_master vm where vm.village_code=ki.village_code LIMIT 1) from "
				+ "land_bank.khatian_information ki where ki.khatian_code=pii.khatian_code LIMIT 1) from land_bank.plot_information pii "
				+ "where pii.plot_code=la1.plot_code LIMIT 1) as villageName,(select (select (select (select "
				+ "tm.tahasil_name from land_bank.tahasil_master tm where tm.tahasil_code=vm.tahasil_code LIMIT 1)   "
				+ "from land_bank.village_master vm  where vm.village_code=ki.village_code LIMIT 1) from land_bank.khatian_information ki  "
				+ "where ki.khatian_code=pii.khatian_code LIMIT 1)  from land_bank.plot_information pii  where pii.plot_code=la1.plot_code LIMIT 1) as tahasilName, "
				+ "(select (select (select (select (select district_name from land_bank.district_master dm   where dm.district_code=tm.district_code LIMIT 1)  from "
				+ "land_bank.tahasil_master tm  where tm.tahasil_code=vm.tahasil_code LIMIT 1) from land_bank.village_master vm  "
				+ "where vm.village_code=ki.village_code LIMIT 1) from land_bank.khatian_information ki  where ki.khatian_code=pii.khatian_code LIMIT 1)  "
				+ "from  land_bank.plot_information pii  where  pii.plot_code=la1.plot_code LIMIT 1) as districtName, "
				+ "(select (select (select (select (select (select state_name  from land_bank.state_master sm  where sm.state_code=dm.state_code LIMIT 1) "
				+ "from  land_bank.district_master dm  where dm.district_code=tm.district_code LIMIT 1) from land_bank.tahasil_master tm  where "
				+ "tm.tahasil_code=vm.tahasil_code LIMIT 1) from land_bank.village_master vm  where vm.village_code=ki.village_code LIMIT 1) "
				+ "from land_bank.khatian_information ki  where ki.khatian_code=pii.khatian_code LIMIT 1)   "
				+ "from land_bank.plot_information pii   where pii.plot_code=la1.plot_code LIMIT 1) as stateName, CAST(form_register_flag as varchar) as landRegistFlag,"
				+ "(SELECT CASE WHEN la2.total_price_in_purchase_area = COALESCE(SUM(COALESCE(lap.transaction_paid_amount, 0)), 0) THEN 'true' "
				+ "ELSE 'false' END AS is_amount_equal FROM application.land_allotement la2 LEFT JOIN "
				+ "application.land_allotement_payment lap ON la2.land_allotement_id = lap.land_allotement_id "
				+ "WHERE la2.land_allotement_id =la1.land_allotement_id AND la2.deleted_flag = '0' "
				+ "AND (lap.deleted_flag = '0' OR lap.deleted_flag IS NULL) GROUP BY la2.total_price_in_purchase_area) as paymentFlag ,"
				+ "la1.form_register_docs as registerDocs " + "FROM application.land_allotement la1 "
				+ "JOIN application.bidder_form_m_application bfma ON (la1.form_m_application_id = bfma.bidder_form_m_application_id) "
				+ "WHERE la1.deleted_flag = '0'  AND bfma.deleted_flag = '0' AND (la1.form_m_application_id IS NOT NULL AND bfma.created_by= :createdBy)  "
				+ "UNION "
				+ "SELECT la1.land_allotement_id as landAllotementId,la1.land_application_id as landId,la2.created_by as ctreatedBy ,la1.form_16_docs as docs, "
				+ "la1.form_16_remark as remark ,(SELECT applicant_name FROM public.land_application WHERE land_application_id =la1.land_application_id  "
				+ "AND deleted_flag = B'0' LIMIT 1) as fullName , "
				+ "(select plot_no from land_bank.plot_information where plot_code=la1.plot_code) as plotNo, "
				+ "la1.total_area as totalArea,la1.purchase_area as purchaseArea, "
				+ "la1.price_per_acer as pricePerAcer,la1.total_price_in_purchase_area as totalPricePurchaseArea,(select (select ki.khata_no  from "
				+ "land_bank.khatian_information ki where ki.khatian_code=pii.khatian_code LIMIT 1) from land_bank.plot_information pii "
				+ "where pii.plot_code=la1.plot_code LIMIT 1) as kahataNo,(select (select (select vm.village_name from land_bank.village_master vm  "
				+ "where vm.village_code=ki.village_code LIMIT 1) from land_bank.khatian_information ki where ki.khatian_code=pii.khatian_code LIMIT 1)  "
				+ "from land_bank.plot_information pii where pii.plot_code=la1.plot_code LIMIT 1) as villageName, "
				+ "(select (select (select (select tm.tahasil_name from land_bank.tahasil_master tm where tm.tahasil_code=vm.tahasil_code LIMIT 1) from "
				+ "land_bank.village_master vm  where vm.village_code=ki.village_code LIMIT 1)  "
				+ "from land_bank.khatian_information ki  where ki.khatian_code=pii.khatian_code LIMIT 1) from land_bank.plot_information pii "
				+ "where  pii.plot_code=la1.plot_code LIMIT 1) as tahasilName, (select (select (select (select (select "
				+ "district_name from land_bank.district_master dm  where dm.district_code=tm.district_code LIMIT 1) from land_bank.tahasil_master tm  "
				+ "where tm.tahasil_code=vm.tahasil_code LIMIT 1) from land_bank.village_master vm   where vm.village_code=ki.village_code LIMIT 1)  "
				+ "from land_bank.khatian_information ki  where ki.khatian_code=pii.khatian_code LIMIT 1) from land_bank.plot_information pii where "
				+ "pii.plot_code=la1.plot_code LIMIT 1) as districtName, (select (select (select (select (select (select state_name  from land_bank.state_master sm  "
				+ "where sm.state_code=dm.state_code LIMIT 1) from land_bank.district_master dm  where dm.district_code=tm.district_code LIMIT 1)   "
				+ "from land_bank.tahasil_master tm  where tm.tahasil_code=vm.tahasil_code LIMIT 1)  from land_bank.village_master vm  where "
				+ "vm.village_code=ki.village_code LIMIT 1) from land_bank.khatian_information ki where ki.khatian_code=pii.khatian_code LIMIT 1)   "
				+ "from land_bank.plot_information pii where pii.plot_code=la1.plot_code LIMIT 1) as stateName ,CAST(form_register_flag as varchar) as landRegistFlag , "
				+ "(SELECT CASE WHEN la2.total_price_in_purchase_area = COALESCE(SUM(COALESCE(lap.transaction_paid_amount, 0)), 0) THEN 'true' "
				+ "ELSE 'false' END AS is_amount_equal FROM application.land_allotement la2 LEFT JOIN "
				+ "application.land_allotement_payment lap ON la2.land_allotement_id = lap.land_allotement_id "
				+ "WHERE la2.land_allotement_id =la1.land_allotement_id AND la2.deleted_flag = '0' "
				+ "AND (lap.deleted_flag = '0' OR lap.deleted_flag IS NULL) GROUP BY la2.total_price_in_purchase_area) as paymentFlag,"
				+ "la1.form_register_docs as registerDocs  "
				+ "FROM application.land_allotement la1 JOIN  public.land_application la2 ON (la1.land_application_id = la2.land_application_id) "
				+ "WHERE la1.deleted_flag = '0' AND la2.deleted_flag = '0' "
				+ "AND(la1.land_application_id IS NOT NULL AND la2.created_by= :createdBy)  "
				+ " offset :offset limit :limit ";
		Query query = entityManager.createNativeQuery(nativeQuery, LandAllotementWinnerResultDto.class);
		query.setParameter("createdBy", createdBy);
		query.setParameter("limit", limit);
		query.setParameter("offset", offset);
		@SuppressWarnings("unchecked")
		List<LandAllotementWinnerResultDto> resultList = query.getResultList();
		return resultList;
	}

	@Transactional
	@Override
	public BigDecimal getLandAllotmentWnnerDetailsCount(BigInteger createdBy) {
		String nativeQuery = " SELECT SUM(cnt) AS total_count FROM (SELECT COUNT(*) AS cnt "
				+ "FROM application.land_allotement la1  JOIN application.bidder_form_m_application bfma ON (la1.form_m_application_id = bfma.bidder_form_m_application_id) "
				+ "WHERE la1.deleted_flag = '0' AND bfma.deleted_flag = '0' AND la1.form_m_application_id IS NOT NULL AND bfma.created_by = :createdBy  "
				+ "UNION " + "SELECT COUNT(*) AS cnt FROM application.land_allotement la1  "
				+ "JOIN public.land_application la2 ON (la1.land_application_id = la2.land_application_id) "
				+ "WHERE la1.deleted_flag = '0' AND la2.deleted_flag = '0' AND la1.land_application_id IS NOT NULL AND la2.created_by = :createdBy) AS combined_counts";
		return (BigDecimal) entityManager.createNativeQuery(nativeQuery).setParameter("createdBy", createdBy)
				.getSingleResult();
	}
	
	@Transactional
	@Override
	public Integer updateformFlag(BigInteger id,BigInteger meetingId,String plotCode) { 
		String updateQuery="update application.land_allotement set  form_16_flag=B'1', form_16_remark= 'OK' WHERE land_allotement_id=:id ";
		String updateFlagmeeting="update application.meeting_schedule_applicant set land_allotement_complited_flag=1 where meeting_schedule_id=:meetingId "
				               + " AND plot_no=:plotCode ";
		Integer meetingUpdateCount=null;
		Integer updateflagCount=entityManager.createNativeQuery(updateQuery).setParameter("id", id).executeUpdate();
		if(updateflagCount>0) {
			meetingUpdateCount=entityManager.createNativeQuery(updateFlagmeeting).setParameter("meetingId", meetingId)
			.setParameter("plotCode", plotCode).executeUpdate();
		}
		return meetingUpdateCount;
	}
	
	
	@Transactional
	@Override
	public BigInteger insertLandAllotment(LandAllotementEntity landAllotment) {
		 String sql = "INSERT INTO application.land_allotement "
		            + "(meeting_schedule_id, "
		            + "total_area, "
		            + "purchase_area, "
		            + "price_per_acer, "
		            + "total_price_in_purchase_area, "
		            + "form_16_docs, "
		            + "form_16_remark, "
		            + "form_16_flag, "
		            + "land_allotement_flag, "
		            + "form_m_application_id, "
		            + "plot_code, "
		            + "land_order_number, "
		            + "land_order_date,created_by) "
		            + "VALUES "
		            + "(:meetingScheduleId, "
		            + ":totalArea,  "
		            + ":purchaseArea, "
		            + ":pricePerAcer, "
		            + ":totalPriceInPurchaseArea, "
		            + ":form16Docs, "
		            + " 'AUCTION' , "
		            + "B'1' , "
		            + "1, "
		            + ":formMApplicationId, "
		            + ":plotCode, "
		            + ":landOrderNumber, "
		            + ":landOrderDate,:createdBy) RETURNING land_allotement_id";

		 return (BigInteger) entityManager.createNativeQuery(sql)
				.setParameter("meetingScheduleId", landAllotment.getMeetingScheduleId())
				.setParameter("totalArea", landAllotment.getTotalArea())
				.setParameter("purchaseArea", landAllotment.getPurchaseArea())
				.setParameter("pricePerAcer", landAllotment.getPiceInPerAcer())
				.setParameter("totalPriceInPurchaseArea", landAllotment.getTotalPriceInPurchaseArea())
				.setParameter("form16Docs", landAllotment.getFileDocument())
				.setParameter("formMApplicationId", landAllotment.getApplicantName())
				.setParameter("plotCode", landAllotment.getSelPlotNo())
				.setParameter("landOrderNumber", landAllotment.getOrderNumber())
				.setParameter("landOrderDate", landAllotment.getOrderDate())
				.setParameter("createdBy",landAllotment.getCreatedBy())
				.getSingleResult();

	}
	
}
