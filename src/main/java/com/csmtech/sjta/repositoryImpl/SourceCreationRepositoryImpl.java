package com.csmtech.sjta.repositoryImpl;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.dto.SourceCreationPlotsRecord;
import com.csmtech.sjta.dto.SourcerCreationDto;
import com.csmtech.sjta.repository.SourceCreationRepository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class SourceCreationRepositoryImpl implements SourceCreationRepository {

	@PersistenceContext
	@Autowired
	EntityManager entity;

	// Rashmi Changes
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public List<Object[]> auctionPlotDetrails(Integer limit, Integer offset) {
		String sqlQuery = "  SELECT  DISTINCT ON(ap.auction_plot_id) ap.auction_plot_id,ap.khatian_code as khata ,  "
				+ " (select district_name from land_bank.district_master where district_code=ap.district_code LIMIT 1) as dist_code, "
				+ " (select tahasil_name from land_bank.tahasil_master where tahasil_code=ap.tahasil_code LIMIT 1) as taha_code,  "
				+ " (select village_name from land_bank.village_master where village_code=ap.village_code LIMIT 1) as vill_code,  "
				+ " (select khata_no from  land_bank.khatian_information where khatian_code=ap.khatian_code LIMIT 1)  as khatian_code ,"
				+ " CAST(auction_flag AS INTEGER) AS auction_flag_number , " + " approve_status ,"
				+ "(select STRING_AGG((pi.plot_no),',') from  application.auction_plot_details apd1 JOIN land_bank.plot_information pi "
				+ " ON(pi.plot_code=apd1.plot_code) where apd1.auction_plot_id=ap.auction_plot_id) as plotno , "
				+ "CAST((select st_extent(ST_Transform(spb.geom,3857)) " + "from land_bank.sjta_plot_boundary  spb "
				+ "where  spb.plot_code=apd.plot_code)as varchar),"
				+ "ap.approve_remark, ap.district_code,ap.tahasil_code,ap.village_code "
				+ "FROM application.auction_plot ap "
				+ "JOIN application.auction_plot_details apd ON(ap.auction_plot_id=apd.auction_plot_id) "
				+ "WHERE ap.deleted_flag='0' AND apd.deleted_flag='0'  AND apd.go_to_auction_flag=0 "
				+ "GROUP BY ap.khatian_code,ap.auction_plot_id,apd.plot_code  "
				+ " ORDER BY ap.auction_plot_id DESC   " 
				+ "offset :offset limit :limit ";
		Query query = entity.createNativeQuery(sqlQuery).setParameter("limit", limit).setParameter("offset", offset);
		List<Object[]> resultList = query.getResultList();
		entity.close();
		return resultList;
	}

	public BigInteger countAuctionPlots() {
		String nativeQuery = "select count(*) from (  SELECT "
				+ "  Distinct ON(ap.auction_plot_id) ap.auction_plot_id, "
				+ "        ap.khatian_code as khata , "
				+ "        (select "
				+ "            district_name  "
				+ "        from "
				+ "            land_bank.district_master  "
				+ "        where "
				+ "            district_code=ap.district_code LIMIT 1) as dist_code, "
				+ "        (select "
				+ "            tahasil_name  "
				+ "        from "
				+ "            land_bank.tahasil_master  "
				+ "        where "
				+ "            tahasil_code=ap.tahasil_code LIMIT 1) as taha_code, "
				+ "        (select "
				+ "            village_name  "
				+ "        from "
				+ "            land_bank.village_master  "
				+ "        where "
				+ "            village_code=ap.village_code LIMIT 1) as vill_code, "
				+ "        (select "
				+ "            khata_no  "
				+ "        from "
				+ "            land_bank.khatian_information  "
				+ "        where "
				+ "            khatian_code=ap.khatian_code LIMIT 1)  as khatian_code , "
				+ "        CAST(auction_flag AS INTEGER) AS auction_flag_number , "
				+ "        approve_status , "
				+ "		CAST((select "
				+ "            st_extent(ST_Transform(spb.geom, "
				+ "            3857))  "
				+ "        from "
				+ "            land_bank.sjta_plot_boundary  spb  "
				+ "        where "
				+ "            spb.plot_code=apd.plot_code)as varchar), "
				+ "        ap.approve_remark, "
				+ "        ap.district_code, "
				+ "        ap.tahasil_code, "
				+ "        ap.village_code  "
				+ "    FROM "
				+ "        application.auction_plot ap  "
				+ "    JOIN "
				+ "        application.auction_plot_details apd  "
				+ "            ON( "
				+ "                ap.auction_plot_id=apd.auction_plot_id "
				+ "            ) "
				+ "	JOIN land_bank.plot_information pi ON(pi.plot_code=apd.plot_code) "
				+ "    WHERE "
				+ "        ap.deleted_flag='0'  "
				+ "        AND apd.deleted_flag='0'   "
				+ "        AND apd.go_to_auction_flag=0  "
				+ "    GROUP BY "
				+ "        ap.khatian_code, "
				+ "        ap.auction_plot_id, "
				+ "        apd.plot_code    "
				+ "    ORDER BY "
				+ "        ap.auction_plot_id ASC) as subquery  ";
		Query query = entity.createNativeQuery(nativeQuery);
		entity.close();
		return ((BigInteger) query.getSingleResult());
	}

	// Retrieve data auction_plot_id through
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public List<Object[]> auctionPlotIdDetrails(BigInteger auctionPlotId) {
		String sqlQuery = "SELECT " + "        apd.auction_plot_details_id, " + "		pi.plot_code, "
				+ "        pi.plot_no, " + "        pi.kissam, " + "        pi.area_acre  " + "    FROM "
				+ "        application.auction_plot_details apd  " + "    join "
				+ "        land_bank.plot_information pi  " + "            on apd.plot_code = pi.plot_code  "
				+ "    WHERE " + "        apd.auction_plot_id = :auctionPlotId " + "  AND deleted_flag='0'";

		Query query = entity.createNativeQuery(sqlQuery);
		query.setParameter("auctionPlotId", auctionPlotId);
		List<Object[]> resultList = query.getResultList();
		entity.close();
		return resultList;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public List<Object[]> auctionPlotDetrailsThroughId(BigInteger initId) {
		String sqlQuery = "  SELECT  auction_plot_id, "
				+ " (select district_name from land_bank.district_master where district_code=ap.district_code LIMIT 1) as district_name, "
				+ " (select tahasil_name from land_bank.tahasil_master where tahasil_code=ap.tahasil_code LIMIT 1) as tahasil_name,  "
				+ " (select village_name from land_bank.village_master where village_code=ap.village_code LIMIT 1) as village_name,  "
				+ " (select khata_no from  land_bank.khatian_information where khatian_code=ap.khatian_code LIMIT 1)  as khata_no, "
				+ " district_code, " + "	tahasil_code, " + "	village_code, " + "	khatian_code," + " approve_status  "
				+ " FROM application.auction_plot ap " + " WHERE auction_plot_id=:auction_plot_id "
				+ "  AND deleted_flag='0'";

		// Create a native query
		Query query = entity.createNativeQuery(sqlQuery);

		// Execute the query and retrieve the results
		List<Object[]> resultList = query.setParameter("auction_plot_id", initId).getResultList();
		entity.close();
		return resultList;
	}

	@Override
	@Transactional
	public Integer insertAuctionPlotDetails(SourcerCreationDto sourcerCreationDto, BigInteger auctionPlotId) {
//		List<PlotDetails> detailsList = sourcerCreationDto.getPlot_details();
//		Integer count = null;
//		for (PlotDetails details : detailsList) {
//			String nativeQuery = "INSERT INTO application.auction_plot_details (auction_plot_id, plot_code,created_by) "
//					+ "VALUES (?, ?,  "
//					+ " currval(pg_get_serial_sequence('application.auction_plot_details','auction_plot_details_id')) )";
//
//			Query query = entity.createNativeQuery(nativeQuery);
//			query.setParameter(1, auctionPlotId);
//			query.setParameter(2, details.getPlot_id());
//
//			count = query.executeUpdate();
//		}
		String nativeQuery = "INSERT INTO application.auction_plot_details (auction_plot_id, plot_code,created_by,go_for_auction_process_flag) "
				+ "VALUES (?, ?, ?,1) ";
//				+ " currval(pg_get_serial_sequence('application.auction_plot_details','auction_plot_details_id')) )";
		Query query = entity.createNativeQuery(nativeQuery);
		query.setParameter(1, auctionPlotId);
		query.setParameter(2, sourcerCreationDto.getSelPlotNo());
		query.setParameter(3, sourcerCreationDto.getIntCreatedBy());
		return query.executeUpdate();
	}

	@Transactional
	@Override
	public BigInteger insertAuctionPlot(SourcerCreationDto dto) {
		String nativeQuery = " INSERT INTO application.auction_plot (district_code, tahasil_code, village_code, khatian_code,created_by) "
				+ " VALUES (?, ?, ?, ?, ?)" + " RETURNING auction_plot_id ";
		if (dto.getSaveFlag() == 1) {
			String plotQuery = "update application.land_allotement_for_auction " + "set auction_save_status=B'1' "
					+ "where land_allotement_for_auction_id=:landId ";
			Query queryPlots = entity.createNativeQuery(plotQuery);
			queryPlots.setParameter("landId", dto.getLandAllotementId());
			queryPlots.executeUpdate();
		}
		Query query = entity.createNativeQuery(nativeQuery);
		query.setParameter(1, dto.getSelDistrictName());
		query.setParameter(2, dto.getSelTehsilName());
		query.setParameter(3, dto.getSelMouza());
		query.setParameter(4, dto.getSelKhataNo());
		query.setParameter(5, dto.getIntCreatedBy());
		return (BigInteger) query.getSingleResult();
	}

	// update
	@Override
	@Transactional
	public Integer auctionUpdate(SourcerCreationDto dto) {
		String nativeQuery = " UPDATE application.auction_plot "
				+ "SET district_code = ?, tahasil_code = ?, village_code = ?, khatian_code = ? "
				+ "WHERE auction_plot_id = ?";
		Query query = entity.createNativeQuery(nativeQuery);
		query.setParameter(1, dto.getSelDistrictName());
		query.setParameter(2, dto.getSelTehsilName());
		query.setParameter(3, dto.getSelMouza());
		query.setParameter(4, dto.getSelKhataNo());
		query.setParameter(5, dto.getIntId());
		return query.executeUpdate();
	}

	@Transactional
	public Integer deleteAuctionPlotDetails(BigInteger detailsList) {
		String nativeQuery = "UPDATE application.auction_plot_details " + "SET deleted_flag = B'1' "
				+ "WHERE auction_plot_id = ? ";

		Query query = entity.createNativeQuery(nativeQuery);
		query.setParameter(1, detailsList);
		return query.executeUpdate();

	}
	
	@Transactional
	@Override
	public BigInteger gerAuctionThroughMeetingId(BigInteger auctionId) {
		String nativeQuery = "select meeting_schedule_id from application.auction_plot_details where auction_plot_id=:auctionId LIMIT 1 ";
		Query query = entity.createNativeQuery(nativeQuery);
		query.setParameter("auctionId", auctionId);
		return (BigInteger)query.getSingleResult();

	}
	
	@Override
	@Transactional
	public Integer updateDeletedFladForTheMeetings(BigInteger meetingId) {
		String meetingApplicantQuery = "update application.meeting_schedule_applicant set status='1' where meeting_schedule_id=:meetingId";
		String meetingSheduleQuery="update application.meeting_schedule set status='1' where meeting_schedule_id=:meetingId ";
		Query query = entity.createNativeQuery(meetingApplicantQuery);
		query.setParameter("meetingId", meetingId);
		Integer meetingApplicant=query.executeUpdate();
		Integer meetingSheduleCount=null;
		if(meetingApplicant>0) {
			Query query1 = entity.createNativeQuery(meetingSheduleQuery);
			query1.setParameter("meetingId", meetingId);
			meetingSheduleCount=query1.executeUpdate();
			log.info("meeting shedulle error ...!!");
		}
		if(meetingSheduleCount>0) {
			return 1;
		}
		return 0;

	}

	@Transactional
	@Override
	public Integer markAuctionPlotAsDeleted(BigInteger auctionPlotId) {
		String nativeQuery = " UPDATE application.auction_plot " + " SET deleted_flag = B'1' "
				+ " WHERE auction_plot_id = ?";

		Query query = entity.createNativeQuery(nativeQuery);
		query.setParameter(1, auctionPlotId);

		return query.executeUpdate();

	}

	@Transactional
	@Override
	public Integer updateAddAuctionPlotDetails(BigInteger auctionPlotId, String auctionDocument) {
		String nativeQuery = "UPDATE application.auction_plot " + "SET auction_document = ?, auction_flag = 1 "
				+ "WHERE auction_plot_id = ?";
		Query query = entity.createNativeQuery(nativeQuery);
		query.setParameter(1, auctionDocument);
		query.setParameter(2, auctionPlotId);

		return query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public List<Object[]> auctionPlotDetrailsViewByAuctionFlag(Integer limit, Integer offset, String approvalStatus) {
		String sqlQuery = "SELECT   DISTINCT   ON(ap.auction_plot_id) ap.auction_plot_id ,ap.khatian_code  as khata,  "
				+ "(select district_name from land_bank.district_master where district_code=ap.district_code LIMIT 1) as district_code, "
				+ "(select tahasil_name from land_bank.tahasil_master where tahasil_code=ap.tahasil_code LIMIT 1) as tahasil_code,  "
				+ "(select village_name from land_bank.village_master where village_code=ap.village_code LIMIT 1) as village_code,  "
				+ "(select khata_no from  land_bank.khatian_information where khatian_code=ap.khatian_code LIMIT 1)  as khatian_code , "
				+ "CAST(auction_flag AS INTEGER) AS auction_flag_number,"
				+ "(select plot_no from land_bank.plot_information where plot_code=apd.plot_code LIMIT 1) as plot_no, "
				+ "ap.approve_remark  " + "FROM application.auction_plot ap "
				+ "JOIN application.auction_plot_details apd ON(apd.auction_plot_id=ap.auction_plot_id)  "
				+ "WHERE ap.deleted_flag='0' AND apd.deleted_flag='0'  "
				+ " AND approve_status=:approvalStatus GROUP BY ap.khatian_code,ap.auction_plot_id,apd.plot_code ORDER BY ap.auction_plot_id offset :offset limit :limit";
		Query query = entity.createNativeQuery(sqlQuery).setParameter("limit", limit).setParameter("offset", offset)
				.setParameter("approvalStatus", approvalStatus);
		List<Object[]> resultList = query.getResultList();
		entity.close();
		return resultList;
	}

	@Transactional
	@Override
	public BigInteger countAuctionPlotsAuctionFlag(String approvalStatus) {
//		String nativeQuery = "SELECT count(*) " + " FROM application.auction_plot ap "
//				+ "  JOIN  application.auction_plot_details apd  " + "ON(apd.auction_plot_id=ap.auction_plot_id)  "
//				+ "WHERE ap.deleted_flag='0' " + " AND apd.deleted_flag='0'  AND approve_status=:approvalStatus ";
		String nativeQuery = "select count (*) from (SELECT   DISTINCT   ON(ap.auction_plot_id) ap.auction_plot_id ,ap.khatian_code  as khata,  "
				+ "(select district_name from land_bank.district_master where district_code=ap.district_code LIMIT 1) as district_code, "
				+ "(select tahasil_name from land_bank.tahasil_master where tahasil_code=ap.tahasil_code LIMIT 1) as tahasil_code,  "
				+ "(select village_name from land_bank.village_master where village_code=ap.village_code LIMIT 1) as village_code,  "
				+ "(select khata_no from  land_bank.khatian_information where khatian_code=ap.khatian_code LIMIT 1)  as khatian_code , "
				+ "CAST(auction_flag AS INTEGER) AS auction_flag_number,"
				+ "(select plot_no from land_bank.plot_information where plot_code=apd.plot_code LIMIT 1) as plot_no, "
				+ "ap.approve_remark  " + "FROM application.auction_plot ap "
				+ "JOIN application.auction_plot_details apd ON(apd.auction_plot_id=ap.auction_plot_id)  "
				+ "WHERE ap.deleted_flag='0' AND apd.deleted_flag='0'  "
				+ " AND approve_status=:approvalStatus GROUP BY ap.khatian_code,ap.auction_plot_id,apd.plot_code ORDER BY ap.auction_plot_id ) as subquery";
		Query query = entity.createNativeQuery(nativeQuery).setParameter("approvalStatus", approvalStatus);
		entity.close();
		return ((BigInteger) query.getSingleResult());
	}

	@Transactional
	@Override
	public Integer updateAuctionPlot(Integer auctionPlotId, String status, String remark) {
		String nativeQuery = "UPDATE application.auction_plot " + "SET approve_status = :status, "
				+ "approve_remark = :remark, " + "approve_date_time = CURRENT_TIMESTAMP," + "auction_flag = 2 "
				+ "WHERE auction_plot_id = :auctionPlotId";

		Query query = entity.createNativeQuery(nativeQuery);
		query.setParameter("auctionPlotId", auctionPlotId).setParameter("status", status).setParameter("remark",
				remark);

		return query.executeUpdate();
	}

	@Transactional
	@Override
	public BigInteger countAuctionPlotsAuctionFlagValidate(String plotCode) {
		String nativeQuery = " select count(*) " + " from application.auction_plot_details "
				+ " where plot_code= :plotCode " + " AND deleted_flag=B'0' ";
		Query query = entity.createNativeQuery(nativeQuery).setParameter("plotCode", plotCode);
		entity.close();
		return ((BigInteger) query.getSingleResult());
	}

	@Transactional
	@Override
	public List<SourceCreationPlotsRecord> getSelectedPlotsInfo(Integer limit, Integer offset) {
		String nativeQuery = "WITH SelectedPlots AS "
				+ "(SELECT DISTINCT plot_no FROM application.land_allotement_for_auction WHERE deleted_flag='0') "
				+ "SELECT lafa.land_allotement_for_auction_id as landAllotementId , lafa.plot_no as plotNo , "
				+ "ki.khata_no as kathaNo , vm.village_name as villageName, tm.tahasil_name as tahasilName , dm.district_name as districtName "
				+ ",pi.plot_code as plotCode ,ki.khatian_code as khataCode ,vm.village_code as villageCode ,tm.tahasil_code as tahasilCode ,dm.district_code as districtCode , "
				+ "pi.kissam as kissam,pi.area_acre as areaInAcer , "
				+ "CAST(lafa.auction_save_status as varchar) as saveFlag , "
				+ "CAST((select(select st_extent(ST_Transform(spb.geom,3857)) as extent from land_bank.sjta_plot_boundary  "
				+ "spb where pii.plot_code=spb.plot_code LIMIT 1) as plotExtend "
				+ "from land_bank.plot_information pii where pii.plot_no=lafa.plot_no LIMIT 1) as varchar) "
				+ "FROM application.land_allotement_for_auction lafa "
				+ "JOIN land_bank.plot_information pi ON pi.plot_no = lafa.plot_no "
				+ "JOIN land_bank.khatian_information ki ON ki.khatian_code = pi.khatian_code "
				+ "JOIN land_bank.village_master vm ON vm.village_code = ki.village_code "
				+ "JOIN land_bank.tahasil_master tm ON tm.tahasil_code = vm.tahasil_code "
				+ "JOIN land_bank.district_master dm ON dm.district_code = tm.district_code "
				+ "JOIN SelectedPlots sp ON sp.plot_no = lafa.plot_no  "
				+ "LEFT JOIN application.auction_plot_details apd ON(apd.plot_code=lafa.plot_code) "
//				+ "JOIN application.auction_plot ap ON(ap.auction_plot_id=apd.auction_plot_id) "
				+ "WHERE lafa.plot_no IN (SELECT plot_no FROM SelectedPlots) AND lafa.deleted_flag='0' "
				+ "AND apd.plot_code IS NULL "
//				+ "OR ap.deleted_flag='1'   "
				+ "ORDER BY lafa.land_allotement_for_auction_id  DESC  "
				+ "offset :offset limit :limit";
		Query query = entity.createNativeQuery(nativeQuery, SourceCreationPlotsRecord.class)
				.setParameter("limit", limit).setParameter("offset", offset);
		@SuppressWarnings("unchecked")
		List<SourceCreationPlotsRecord> resultList = query.getResultList();
		return resultList;
	}

	@Transactional
	@Override
	public BigInteger getSelectedPlotsInfoCount() {
		String nativeQuery = "select count(*) from (WITH SelectedPlots AS "
				+ "(SELECT DISTINCT plot_no FROM application.land_allotement_for_auction WHERE deleted_flag='0') "
				+ "SELECT lafa.land_allotement_for_auction_id as landAllotementId , lafa.plot_no as plotNo , "
				+ "ki.khata_no as kathaNo , vm.village_name as villageName, tm.tahasil_name as tahasilName , dm.district_name as districtName "
				+ ",pi.plot_code as plotCode ,ki.khatian_code as khataCode ,vm.village_code as villageCode ,tm.tahasil_code as tahasilCode ,dm.district_code as districtCode , "
				+ "pi.kissam as kissam,pi.area_acre as areaInAcer , "
				+ "CAST(lafa.auction_save_status as varchar) as saveFlag , "
				+ "CAST((select(select st_extent(ST_Transform(spb.geom,3857)) as extent from land_bank.sjta_plot_boundary  "
				+ "spb where pii.plot_code=spb.plot_code LIMIT 1) as plotExtend "
				+ "from land_bank.plot_information pii where pii.plot_no=lafa.plot_no LIMIT 1) as varchar) "
				+ "FROM application.land_allotement_for_auction lafa "
				+ "JOIN land_bank.plot_information pi ON pi.plot_no = lafa.plot_no "
				+ "JOIN land_bank.khatian_information ki ON ki.khatian_code = pi.khatian_code "
				+ "JOIN land_bank.village_master vm ON vm.village_code = ki.village_code "
				+ "JOIN land_bank.tahasil_master tm ON tm.tahasil_code = vm.tahasil_code "
				+ "JOIN land_bank.district_master dm ON dm.district_code = tm.district_code "
				+ "JOIN SelectedPlots sp ON sp.plot_no = lafa.plot_no  "
				+ "LEFT JOIN application.auction_plot_details apd ON(apd.plot_code=lafa.plot_code) "
				+ "WHERE lafa.plot_no IN (SELECT plot_no FROM SelectedPlots) AND lafa.deleted_flag='0' "
				+ "AND apd.plot_code IS NULL "
				+ "ORDER BY lafa.land_allotement_for_auction_id  DESC) as subquery  ";
		Query query = entity.createNativeQuery(nativeQuery);
		entity.close();
		return ((BigInteger) query.getSingleResult());
	}

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public List<Object[]> auctionPlotDetrailsUseLike(Integer limit, Integer offset, String plotNo, String auctionFlag) {
		String auctionFlag1 = "";
		String addAuctionFlag="";
		if (auctionFlag.equalsIgnoreCase("A")) {
			auctionFlag1 = "AND ap.approve_status= 'A' ";
		} else if (auctionFlag.equalsIgnoreCase("R")) {
			auctionFlag1 = "AND ap.approve_status= 'R' ";
		} else if (auctionFlag.equalsIgnoreCase("N")) {
			auctionFlag1 = "AND ap.approve_status= 'N' ";
			addAuctionFlag="AND ap.auction_flag=0 ";
		} else if (auctionFlag.equalsIgnoreCase("1")) {
			auctionFlag1 = "AND ap.auction_flag=" + 1;
		}

		String sqlQuery = "  SELECT DISTINCT ON(ap.auction_plot_id) ap.auction_plot_id ,ap.khatian_code as khata ,  "
				+ " (select district_name from land_bank.district_master where district_code=ap.district_code LIMIT 1) as dist_code, "
				+ " (select tahasil_name from land_bank.tahasil_master where tahasil_code=ap.tahasil_code LIMIT 1) as taha_code,  "
				+ " (select village_name from land_bank.village_master where village_code=ap.village_code LIMIT 1) as vill_code,  "
				+ " (select khata_no from  land_bank.khatian_information where khatian_code=ap.khatian_code LIMIT 1)  as khatian_code ,"
				+ " CAST(auction_flag AS INTEGER) AS auction_flag_number , " + " approve_status ,"
				+ " (select STRING_AGG((pi.plot_no),',') from  application.auction_plot_details apd1 JOIN land_bank.plot_information pi  "
				+ " ON(pi.plot_code=apd1.plot_code) where apd1.auction_plot_id=ap.auction_plot_id) as plotno ,  "
				+ "CAST((select st_extent(ST_Transform(spb.geom,3857)) " + "from land_bank.sjta_plot_boundary  spb "
				+ "where  spb.plot_code=apd.plot_code)as varchar),"
				+ "ap.approve_remark , ap.district_code,ap.tahasil_code,ap.village_code  "
				+ "FROM application.auction_plot ap "
				+ "JOIN application.auction_plot_details apd ON(ap.auction_plot_id=apd.auction_plot_id) "
				+ "WHERE ap.deleted_flag='0' AND apd.deleted_flag='0' AND (select p1.plot_no from land_bank.plot_information p1 where p1.plot_code=apd.plot_code) ILIKE :plotNo "
				+ " " + auctionFlag1 + addAuctionFlag+ " AND apd.go_to_auction_flag=0  "
				+ "GROUP BY ap.khatian_code,ap.auction_plot_id,apd.plot_code "
				+ " ORDER BY ap.auction_plot_id DESC " + "offset :offset limit :limit ";
		Query query = entity.createNativeQuery(sqlQuery).setParameter("limit", limit).setParameter("offset", offset)
				.setParameter("plotNo", "%" + plotNo + "%");
		List<Object[]> resultList = query.getResultList();
		entity.close();
		return resultList;
	}

	@Override
	public BigInteger countAuctionPlotsLikeCont(String plotNo, String auctionFlag) {
		String auctionFlag1 = "";
		String addAuctionFlag="";
		if (auctionFlag.equalsIgnoreCase("A")) {
			auctionFlag1 = "AND ap.approve_status= 'A' ";
		} else if (auctionFlag.equalsIgnoreCase("R")) {
			auctionFlag1 = "AND ap.approve_status= 'R' ";
		} else if (auctionFlag.equalsIgnoreCase("N")) {
			auctionFlag1 = "AND ap.approve_status= 'N' ";
			addAuctionFlag="AND ap.auction_flag=0 ";
		} else if (auctionFlag.equalsIgnoreCase("1")) {
			auctionFlag1 = "AND ap.auction_flag=" + 1;
		}
		String nativeQuery = "  select count(*) from (SELECT DISTINCT ON(ap.auction_plot_id) ap.auction_plot_id ,ap.khatian_code as khata ,  "
				+ " (select district_name from land_bank.district_master where district_code=ap.district_code LIMIT 1) as dist_code, "
				+ " (select tahasil_name from land_bank.tahasil_master where tahasil_code=ap.tahasil_code LIMIT 1) as taha_code,  "
				+ " (select village_name from land_bank.village_master where village_code=ap.village_code LIMIT 1) as vill_code,  "
				+ " (select khata_no from  land_bank.khatian_information where khatian_code=ap.khatian_code LIMIT 1)  as khatian_code ,"
				+ " CAST(auction_flag AS INTEGER) AS auction_flag_number , " + " approve_status ,"
				+ " (select pi.plot_no from land_bank.plot_information pi   "
				+ "where pi.plot_code=apd.plot_code),  "
				+ "CAST((select st_extent(ST_Transform(spb.geom,3857)) " + "from land_bank.sjta_plot_boundary  spb "
				+ "where  spb.plot_code=apd.plot_code)as varchar),"
				+ "ap.approve_remark , ap.district_code,ap.tahasil_code,ap.village_code  "
				+ "FROM application.auction_plot ap "
				+ "JOIN application.auction_plot_details apd ON(ap.auction_plot_id=apd.auction_plot_id) "
				+ "WHERE ap.deleted_flag='0' AND apd.deleted_flag='0' AND (select p1.plot_no from land_bank.plot_information p1 where p1.plot_code=apd.plot_code) ILIKE :plotNo "
				+ " " + auctionFlag1 + addAuctionFlag+ " AND apd.go_to_auction_flag=0  "
				+ "GROUP BY ap.khatian_code,ap.auction_plot_id,apd.plot_code "
				+ " ORDER BY ap.auction_plot_id DESC) as subquery " ;
		Query query = entity.createNativeQuery(nativeQuery).setParameter("plotNo", "%" + plotNo + "%");
		entity.close();
		return ((BigInteger) query.getSingleResult());
	}
	
	@Transactional
	@Override
	public BigInteger createMeeting(BigInteger createdBy) {
		Object result = entity
				.createNativeQuery("INSERT INTO application.meetings (created_by) VALUES (?) RETURNING meeting_id")
				.setParameter(1, createdBy).getSingleResult();
		return (BigInteger) result;
	}
	
	
	@Transactional
	public BigInteger createMeetingSchedule(String venue, Date meetingDate, String meetingPurpose, Short meetingLevelId,
			BigInteger createdBy, BigInteger meetingId) {
		Object result = entity.createNativeQuery(
				"INSERT INTO application.meeting_schedule (venue, meeting_date, meeting_purpose, meeting_level_id, created_by, meeting_id) VALUES (?, ?, ?, ?, ?, ?) RETURNING meeting_schedule_id")
				.setParameter(1, venue).setParameter(2, meetingDate).setParameter(3, meetingPurpose)
				.setParameter(4, meetingLevelId).setParameter(5, createdBy).setParameter(6, meetingId)
				.getSingleResult();
		return (BigInteger) result;
	}
	
	@Transactional
	@Override
	public Integer createMeetingScheduleApplicant(BigInteger meetingScheduleId, BigInteger createdBy,
			String plotNo) {
		Object result = entity.createNativeQuery(
				"INSERT INTO application.meeting_schedule_applicant (meeting_schedule_id, created_by, plot_no,direct_meeting_auction_flag) VALUES (?, ?, ?,1) RETURNING meeting_schedule_applicant_id")
				.setParameter(1, meetingScheduleId).setParameter(2, createdBy).setParameter(3, plotNo)
				.getSingleResult();
		return (Integer) result;
	}
	
	@Transactional
	@Override
	public BigInteger createAuctionPlot(String districtCode, String tahasilCode, String villageCode, String khatianCode,
			BigInteger createdBy) {
		Object result = entity.createNativeQuery(
				"INSERT INTO application.auction_plot (district_code, tahasil_code, village_code, khatian_code, created_by) VALUES (?, ?, ?, ?, ?) RETURNING auction_plot_id")
				.setParameter(1, districtCode).setParameter(2, tahasilCode).setParameter(3, villageCode)
				.setParameter(4, khatianCode).setParameter(5, createdBy).getSingleResult();
		return (BigInteger) result;
	}
	
	@Transactional
	@Override
	public BigInteger createAuctionPlotDetails(BigInteger auctionPlotId, String plotCode, BigInteger createdBy,BigInteger meetingSheduleId) {
		Object result = entity.createNativeQuery(
				"INSERT INTO application.auction_plot_details (auction_plot_id, plot_code, created_by,meeting_schedule_id,go_to_auction_flag) VALUES (?, ?, ?, ?, 1) RETURNING auction_plot_details_id")
				.setParameter(1, auctionPlotId).setParameter(2, plotCode).setParameter(3, createdBy).setParameter(4, meetingSheduleId).getSingleResult();
		return (BigInteger) result;
	}
	
	@Transactional
	@Override
	public Integer createMeetingScheduleMailBcc(BigInteger meetingScheduleId, Integer userId, BigInteger createdBy) {
		return entity.createNativeQuery(
				"INSERT INTO application.meeting_schedule_mail_bcc (meeting_schedule_id, user_id, created_by) VALUES (?, ?, ?)")
				.setParameter(1, meetingScheduleId).setParameter(2, userId).setParameter(3, createdBy).executeUpdate();
	}
	
	@Transactional
	@Override
	public Integer createMeetingScheduleMailCc(BigInteger meetingScheduleId, Integer userId, BigInteger createdBy) {
		return entity.createNativeQuery(
				"INSERT INTO application.meeting_schedule_mail_cc (meeting_schedule_id, user_id, created_by) VALUES (?, ?, ?)")
				.setParameter(1, meetingScheduleId).setParameter(2, userId).setParameter(3, createdBy).executeUpdate();
	}
	
}
