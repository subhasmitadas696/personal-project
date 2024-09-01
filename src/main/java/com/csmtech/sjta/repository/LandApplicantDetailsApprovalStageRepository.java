package com.csmtech.sjta.repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.dto.LandApplicantDTO;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class LandApplicantDetailsApprovalStageRepository {

	private static final Logger logger = LoggerFactory.getLogger(LandApplicantDetailsApprovalStageRepository.class);

	@PersistenceContext
	@Autowired
	private EntityManager entityManager;

	@Transactional
	public BigInteger getUnerProcessUseLike(BigInteger roleId, String districtCode, String tahasilCode,
			String villageCode, String khatianCode) {

		String sql = "SELECT count(*) FROM public.land_application A inner join land_bank.district_master B "
				+ "ON A.district_code = B.district_code inner join land_bank.tahasil_master C ON A.tehsil_code =C.tahasil_code "
				+ "inner join land_bank.village_master D ON A.village_code = D.village_code inner join "
				+ "land_bank.khatian_information E ON A.khatian_code = E.khatian_code "
				+ "inner join land_application_approval F ON A.land_application_id = F.land_application_id inner join m_application_status G "
				+ "ON F.application_status_id=G.application_status_id where  F.approval_action_id in (1,5) "
				+ " AND pending_at_role_id !=:roleId AND a.deleted_flag = '0' AND "
				+ "(b.district_code = :districtCode OR :districtCode IS NULL) AND "
				+ "(c.tahasil_code = :tahasilCode OR :tahasilCode IS NULL) AND "
				+ "(d.village_code = :villageCode OR :villageCode IS NULL) AND "
				+ "(e.khatian_code = :khatianCode OR :khatianCode IS NULL)";

		BigInteger status = (BigInteger) entityManager.createNativeQuery(sql).setParameter("roleId", roleId)
				.setParameter("districtCode", districtCode + "%").setParameter("tahasilCode", tahasilCode + "%")
				.setParameter("villageCode", villageCode + "%").setParameter("khatianCode", khatianCode)
				.getSingleResult();

		entityManager.close();
		return status;
	}

	@Transactional
	public BigInteger getUnerProcess(BigInteger roleId) {

		String sql = "SELECT count(*)  FROM public.land_application A inner join "
				+ "land_bank.district_master B ON A.district_code = B.district_code "
				+ "inner join land_bank.tahasil_master C ON A.tehsil_code =C.tahasil_code inner join "
				+ "land_bank.village_master D ON A.village_code = D.village_code "
				+ "inner join land_bank.khatian_information E  ON A.khatian_code = E.khatian_code inner join "
				+ "land_application_approval F ON A.land_application_id =F.land_application_id inner join "
				+ "m_application_status G on F.application_status_id=G.application_status_id where  F.approval_action_id in (  1,5  ) AND pending_at_role_id !=:roleId ";

		BigInteger status = (BigInteger) entityManager.createNativeQuery(sql).setParameter("roleId", roleId)
				.getSingleResult();

		entityManager.close();
		return status;
	}

	@Transactional
	public BigInteger getRevertToCitizenUseLike(Integer revertToCitiZen, String districtCode, String tahasilCode,
			String villageCode, String khatianCode) {

		String sql = "SELECT count(*) FROM public.land_application A inner join land_bank.district_master B "
				+ "ON A.district_code = B.district_code inner join land_bank.tahasil_master C "
				+ "ON A.tehsil_code =C.tahasil_code inner join land_bank.village_master D "
				+ "ON A.village_code = D.village_code inner join  land_bank.khatian_information E "
				+ "ON A.khatian_code = E.khatian_code inner join land_application_approval F "
				+ "ON A.land_application_id =F.land_application_id inner join m_application_status G "
				+ "ON F.application_status_id=G.application_status_id where  F.approval_action_id =:revertToCitiZen AND "
				+ "(b.district_code = :districtCode OR :districtCode IS NULL) AND "
				+ "(c.tahasil_code = :tahasilCode OR :tahasilCode IS NULL) AND "
				+ "(d.village_code = :villageCode OR :villageCode IS NULL) AND "
				+ "(e.khatian_code = :khatianCode OR :khatianCode IS NULL)";

		BigInteger status = (BigInteger) entityManager.createNativeQuery(sql)
				.setParameter("revertToCitiZen", revertToCitiZen).setParameter("districtCode", districtCode + "%")
				.setParameter("tahasilCode", tahasilCode + "%").setParameter("villageCode", villageCode + "%")
				.setParameter("khatianCode", khatianCode).getSingleResult();

		entityManager.close();
		return status;
	}

	@Transactional
	public BigInteger getRevertToCitizen(Integer revertToCitiZen) {

		String sql = "SELECT count(*) FROM public.land_application A inner join land_bank.district_master B "
				+ "ON A.district_code = B.district_code inner join land_bank.tahasil_master C "
				+ "ON A.tehsil_code =C.tahasil_code inner join land_bank.village_master D "
				+ "ON A.village_code = D.village_code inner join land_bank.khatian_information E "
				+ "ON A.khatian_code = E.khatian_code inner join land_application_approval F "
				+ "ON A.land_application_id =F.land_application_id inner join m_application_status G "
				+ "ON F.application_status_id=G.application_status_id where"
				+ " F.approval_action_id =:revertToCitiZen";

		BigInteger status = (BigInteger) entityManager.createNativeQuery(sql)
				.setParameter("revertToCitiZen", revertToCitiZen).getSingleResult();

		entityManager.close();
		return status;
	}

	@Transactional
	public BigInteger getApproveUseLike(Integer approvedUser, String districtCode, String tahasilCode,
			String villageCode, String khatianCode) {

		String sql = "SELECT count(*) FROM public.land_application A inner join land_bank.district_master B "
				+ "ON A.district_code = B.district_code inner join land_bank.tahasil_master C "
				+ "ON A.tehsil_code =C.tahasil_code inner join land_bank.village_master D "
				+ "ON A.village_code = D.village_code inner join land_bank.khatian_information E "
				+ "ON A.khatian_code = E.khatian_code inner join land_application_approval F "
				+ "ON A.land_application_id =F.land_application_id inner join m_application_status G "
				+ "on F.application_status_id=G.application_status_id where F.approval_action_id =:approvedUser AND "
				+ "(b.district_code = :districtCode OR :districtCode IS NULL) AND "
				+ "(c.tahasil_code = :tahasilCode OR :tahasilCode IS NULL) AND "
				+ "(d.village_code = :villageCode OR :villageCode IS NULL) AND "
				+ "(e.khatian_code = :khatianCode OR :khatianCode IS NULL)";

		BigInteger status = (BigInteger) entityManager.createNativeQuery(sql).setParameter("approvedUser", approvedUser)
				.setParameter("districtCode", districtCode + "%").setParameter("tahasilCode", tahasilCode + "%")
				.setParameter("villageCode", villageCode + "%").setParameter("khatianCode", khatianCode)
				.getSingleResult();

		entityManager.close();
		return status;
	}

	@Transactional
	public BigInteger getApprove(Integer approvedUser) {

		String sql = "SELECT count(*) FROM public.land_application A inner join land_bank.district_master B "
				+ "ON A.district_code = B.district_code inner join land_bank.tahasil_master C "
				+ "ON A.tehsil_code = C.tahasil_code inner join land_bank.village_master D "
				+ "ON A.village_code = D.village_code inner join land_bank.khatian_information E "
				+ "ON A.khatian_code = E.khatian_code inner join land_application_approval F "
				+ "ON A.land_application_id = F.land_application_id inner join	m_application_status G "
				+ "on F.application_status_id= G.application_status_id where  F.approval_action_id =:approvedUser ";

		BigInteger status = (BigInteger) entityManager.createNativeQuery(sql).setParameter("approvedUser", approvedUser)
				.getSingleResult();

		entityManager.close();
		return status;
	}

	@Transactional
	public BigInteger getRejectUseLike(Integer approvalActionId, String districtCode, String tahasilCode,
			String villageCode, String khatianCode) {

		String sql = "SELECT count(*) FROM public.land_application A inner join land_bank.district_master B "
				+ "ON A.district_code = B.district_code inner join land_bank.tahasil_master C "
				+ "ON A.tehsil_code = C.tahasil_code inner join land_bank.village_master D "
				+ "ON A.village_code = D.village_code inner join land_bank.khatian_information E "
				+ "ON A.khatian_code = E.khatian_code inner join land_application_approval F "
				+ "ON A.land_application_id = F.land_application_id inner join "
				+ "m_application_status G on F.application_status_id = G.application_status_id "
				+ "WHERE F.approval_action_id =:approvalActionId AND "
				+ "(b.district_code = :districtCode OR :districtCode IS NULL) AND "
				+ "(c.tahasil_code = :tahasilCode OR :tahasilCode IS NULL) AND "
				+ "(d.village_code = :villageCode OR :villageCode IS NULL) AND "
				+ "(e.khatian_code = :khatianCode OR :khatianCode IS NULL)";

		BigInteger status = (BigInteger) entityManager.createNativeQuery(sql)
				.setParameter("approvalActionId", approvalActionId).setParameter("districtCode", districtCode + "%")
				.setParameter("tahasilCode", tahasilCode + "%").setParameter("villageCode", villageCode + "%")
				.setParameter("khatianCode", khatianCode).getSingleResult();

		entityManager.close();
		return status;
	}

	@Transactional
	public BigInteger getReject(Integer approvalActionId) {

		String sql = " SELECT count(*) FROM public.land_application A inner join land_bank.district_master B ON A.district_code = B.district_code "
				+ " inner join land_bank.tahasil_master C " + " ON A.tehsil_code =C.tahasil_code inner join "
				+ " land_bank.village_master D ON A.village_code = D.village_code inner join "
				+ " land_bank.khatian_information E ON A.khatian_code = E.khatian_code inner join land_application_approval F "
				+ " ON A.land_application_id =F.land_application_id  inner join  m_application_status G on F.application_status_id=G.application_status_id "
				+ " where  F.approval_action_id =:approvalActionId ";

		BigInteger status = (BigInteger) entityManager.createNativeQuery(sql)
				.setParameter("approvalActionId", approvalActionId).getSingleResult();

		entityManager.close();
		return status;
	}

	@Transactional
	public List<LandApplicantDTO> getLandApplicantsWithDetailsSearchFunction(BigInteger roleId, String districtCode,
			int pageNumber, int pageSize, String tahasilCode, String villageCode, String khatianCode) {
		String query = "SELECT A.land_application_id, A.application_no, A.applicant_name, A.mobile_no, B.district_name, C.tahasil_name,D.village_name, E.khata_no, G.application_status, F.action_on, F.remark, (select CAST(st_extent(ST_Transform(spb.geom,3857)) AS character varying) as extent from public.land_schedule ls join land_bank.plot_information pi on( ls.plot_code=pi.plot_code)"
				+ " join land_bank.sjta_plot_boundary spb on(spb.plot_code=pi.plot_code) JOIN land_bank.sjta_plot_boundary sj  ON sj.plot_code = k.khatian_code  where A.land_application_id=land_application_id) , CAST((SELECT st_extent(ST_Transform(geom,3857)) as extent FROM land_bank.sjta_plot_boundary_view where plot_code in (SELECT STRING_AGG(ls.plot_code, ',') AS plot_code "
				+ " FROM public.land_schedule ls WHERE ls.land_application_id = A.land_application_id AND ls.deleted_flag='0' )  group by village_code)as varchar) as plot_extend,CAST((SELECT STRING_AGG((select plot_no from land_bank.plot_information where plot_code=ls.plot_code), ',') AS plot_no FROM public.land_schedule ls WHERE ls.land_application_id = A.land_application_id AND deleted_flag='0') as varchar) as plot_no, F.created_on, B.district_code, C.tahasil_code, D.village_code "
				+ " FROM public.land_application A inner join land_bank.district_master B ON A.district_code = B.district_code inner join "
				+ " land_bank.tahasil_master C ON A.tehsil_code = C.tahasil_code inner join land_bank.village_master D "
				+ "	ON A.village_code = D.village_code inner join land_bank.khatian_information E ON A.khatian_code = E.khatian_code inner join land_application_approval F"
				+ " ON A.land_application_id = F.land_application_id inner join m_application_status G on F.application_status_id= G.application_status_id  INNER JOIN land_bank.khatian_information k "
				+ "	ON A.khatian_code = k.khatian_code where F.approval_action_id in ( 1,5 ) AND pending_at_role_id !=:roleId  AND b.district_code =:districtCode  ";

		if (!tahasilCode.equals("0")) {
			query += "AND c.tahasil_code =:tahasilCode ";
		}

		if (!villageCode.equals("0")) {
			query += "AND d.village_code =:villageCode ";
		}

		if (!khatianCode.equals("0")) {
			query += "AND e.khatian_code =:khatianCode ";
		}

		query += "AND a.deleted_flag = '0' ORDER BY a.created_on, a.land_application_id DESC LIMIT :pageSize OFFSET :offset";

		Integer offset = (pageNumber - 1) * pageSize;

		@SuppressWarnings("unchecked")
		Query resultList = entityManager.createNativeQuery(query).setParameter("roleId", roleId)
				.setParameter("districtCode", districtCode).setParameter("pageSize", pageSize)
				.setParameter("offset", offset);

		if (!tahasilCode.equals("0")) {
			resultList.setParameter("tahasilCode", tahasilCode);
		}

		if (!villageCode.equals("0")) {
			resultList.setParameter("villageCode", villageCode);
		}

		if (!khatianCode.equals("0")) {
			resultList.setParameter("khatianCode", khatianCode);
		}

		log.info(":: getLandApplicantsWithDetailsSearchFunction()  Query Execution Sucess..!!");

//		int offset = (pageNumber - 1) * pageSize;
//
//		Query query = entityManager.createNativeQuery(sql).setParameter("roleId", roleId)
//				.setParameter("districtCode", districtCode + "%").setParameter("tahasilCode", tahasilCode + "%")
//				.setParameter("villageCode", villageCode + "%").setParameter("khatianCode", khatianCode)
//				.setParameter("pageSize", pageSize).setParameter("offset", offset);

		List<LandApplicantDTO> result = new ArrayList<>();
		@SuppressWarnings("unchecked")
		List<Object[]> rows = resultList.getResultList();
		for (Object[] row : rows) {
			LandApplicantDTO dto = new LandApplicantDTO();
			dto.setLandApplicantId((BigInteger) row[0]);
			dto.setApplicantNo((String) row[1]);
			dto.setApplicantName((String) row[2]);
			dto.setMobileNo((String) row[3]);
			dto.setDistrictName((String) row[4]);
			dto.setTehsilName((String) row[5]);
			dto.setMouzaName((String) row[6]);
			dto.setKhataNo((String) row[7]);
			dto.setApplicationStatus((String) row[8]);
			dto.setActionOn((Date) row[9]);
			dto.setRemark((String) row[10]);
			dto.setExtent((String) row[11]);
			dto.setPlotExtend((String) row[12]);
			dto.setPlotNo((String) row[13]);
			dto.setCreatedOn((Date) row[14]);
			dto.setDistrictCode((String) row[15]);
			dto.setTahasilCode((String) row[16]);
			dto.setVillageCode((String) row[17]);

			result.add(dto);
		}

		entityManager.close();
		log.info(":: getLandApplicantsWithDetailsSearchFunction() --   Result Return Scucess..!!");
		return result;
	}

	@Transactional
	public List<LandApplicantDTO> getLandApplicantsWithDetails(BigInteger roleId, int pageNumber, int pageSize) {
		String sql = " SELECT  A.land_application_id, A.application_no,  A.applicant_name, A.mobile_no,   B.district_name, C.tahasil_name,   D.village_name, "
				+ " E.khata_no, G.application_status, F.action_on,  F.remark , (select  CAST(st_extent(ST_Transform(spb.geom,3857))AS character varying) as extent	"
				+ " from public.land_schedule ls  "
				+ " join  land_bank.plot_information pi  on(ls.plot_code=pi.plot_code)  "
				+ " join land_bank.sjta_plot_boundary spb on(spb.plot_code=pi.plot_code) "
				+ " where A.land_application_id=land_application_id),(select pending_at_role_id  from "
				+ " public.land_application_approval	where land_application_id=A.land_application_id) as role_id , "
				+ " CAST((SELECT  st_extent(ST_Transform(geom, 3857)) as extent	FROM land_bank.sjta_plot_boundary_view	where plot_code in "
				+ " (SELECT  STRING_AGG(ls.plot_code, ',') AS plot_code	FROM public.land_schedule ls  "
				+ " WHERE  ls.land_application_id = A.land_application_id  "
				+ " AND ls.deleted_flag='0' ) group by village_code)as varchar) as plot_extend,  CAST((SELECT "
				+ " STRING_AGG((select   plot_no from   land_bank.plot_information	where plot_code=ls.plot_code), "
				+ " ',') AS plot_no	" + "FROM public.land_schedule ls  "
				+ " WHERE  ls.land_application_id = A.land_application_id  AND deleted_flag='0') as varchar) as plot_no, F.created_on,"
				+ " B.district_code, C.tahasil_code, D.village_code   " + " FROM  public.land_application A  "
				+ " inner join  land_bank.district_master B  ON A.district_code = B.district_code  "
				+ " inner join  land_bank.tahasil_master C   ON A.tehsil_code = C.tahasil_code  "
				+ " inner join  land_bank.village_master D   ON A.village_code = D.village_code  "
				+ " inner join  land_bank.khatian_information E    ON A.khatian_code = E.khatian_code  "
				+ " inner join  land_application_approval F  ON A.land_application_id =F.land_application_id  "
				+ " inner join  m_application_status G on F.application_status_id=G.application_status_id  "
				+ " where  F.approval_action_id in ( 1,5 ) AND pending_at_role_id !=:roleId "
				+ " ORDER BY  F.created_on DESC LIMIT :pageSize OFFSET :offset ";

		log.info(":: getLandApplicantsWithDetailsSearchFunction()  Query Execution Sucess..!!");
		int offset = (pageNumber - 1) * pageSize;

		Query query = entityManager.createNativeQuery(sql).setParameter("roleId", roleId)
				.setParameter("pageSize", pageSize).setParameter("offset", offset);

		List<LandApplicantDTO> resultList = new ArrayList<>();
		@SuppressWarnings("unchecked")
		List<Object[]> rows = query.getResultList();
		for (Object[] row : rows) {
			LandApplicantDTO dto = new LandApplicantDTO();
			dto.setLandApplicantId((BigInteger) row[0]);
			dto.setApplicantNo((String) row[1]);
			dto.setApplicantName((String) row[2]);
			dto.setMobileNo((String) row[3]);
			dto.setDistrictName((String) row[4]);
			dto.setTehsilName((String) row[5]);
			dto.setMouzaName((String) row[6]);
			dto.setKhataNo((String) row[7]);
			dto.setApplicationStatus((String) row[8]);
			dto.setActionOn((Date) row[9]);
			dto.setRemark((String) row[10]);
			dto.setExtent((String) row[11]);
			dto.setRoleId((Short) row[12]);
			dto.setPlotExtend((String) row[13]);
			dto.setPlotNo((String) row[14]);
			dto.setCreatedOn((Date) row[15]);
			dto.setDistrictCode((String) row[16]);
			dto.setTahasilCode((String) row[17]);
			dto.setVillageCode((String) row[18]);

			resultList.add(dto);
		}

		entityManager.close();
		log.info(":: getLandApplicantsWithDetails() --   Result Return Scucess..!!");
		return resultList;
	}

	@Transactional
	public List<LandApplicantDTO> getLandApplicantsWithApproveDetails(Integer approvedUser, int pageNumber,
			int pageSize) {
		String sql = "SELECT A.land_application_id, A.application_no, "
				+ "A.applicant_name,A.mobile_no, B.district_name , C.tahasil_name, D.village_name,E.khata_no, G.application_status, F.action_on, F.remark ,"
				+ "(select CAST(st_extent(ST_Transform(spb.geom,3857))AS character varying) as extent from public.land_schedule ls join  land_bank.plot_information pi on(ls.plot_code=pi.plot_code) "
				+ " join land_bank.sjta_plot_boundary spb on(spb.plot_code=pi.plot_code) "
				+ " where A.land_application_id=land_application_id), "
				+ " CAST((SELECT st_extent(ST_Transform(geom,3857)) as extent "
				+ " FROM land_bank.sjta_plot_boundary_view "
				+ " where plot_code in (SELECT STRING_AGG(ls.plot_code, ',') AS plot_code "
				+ " FROM public.land_schedule ls " + " WHERE ls.land_application_id = A.land_application_id "
				+ " AND ls.deleted_flag='0' ) group by village_code)as varchar) as plot_extend,"
				+ "CAST((SELECT STRING_AGG((select plot_no from land_bank.plot_information "
				+ "where plot_code=ls.plot_code), ',') AS plot_no FROM public.land_schedule ls "
				+ " WHERE ls.land_application_id = A.land_application_id AND deleted_flag='0' "
				+ " )as varchar) as plot_no, F.created_on, B.district_code, C.tahasil_code, D.village_code FROM public.land_application A inner join land_bank.district_master B  "
				+ " ON A.district_code = B.district_code  inner join land_bank.tahasil_master C "
				+ " ON A.tehsil_code = C.tahasil_code inner join land_bank.village_master D "
				+ " ON A.village_code = D.village_code inner join land_bank.khatian_information E ON A.khatian_code = E.khatian_code inner join "
				+ " land_application_approval F ON A.land_application_id =F.land_application_id inner join m_application_status G "
				+ " on F.application_status_id= G.application_status_id  where F.approval_action_id =:approvedUser "
				+ " ORDER BY F.created_on DESC LIMIT :pageSize OFFSET :offset ";

		log.info(":: getLandApplicantsWithDetailsSearchFunction()  Query Execution Sucess..!!");
		int offset = (pageNumber - 1) * pageSize;

		Query query = entityManager.createNativeQuery(sql).setParameter("approvedUser", approvedUser)
				.setParameter("pageSize", pageSize).setParameter("offset", offset);

		List<LandApplicantDTO> resultList = new ArrayList<>();
		@SuppressWarnings("unchecked")
		List<Object[]> rows = query.getResultList();
		for (Object[] row : rows) {
			LandApplicantDTO dto = new LandApplicantDTO();
			dto.setLandApplicantId((BigInteger) row[0]);
			dto.setApplicantNo((String) row[1]);
			dto.setApplicantName((String) row[2]);
			dto.setMobileNo((String) row[3]);
			dto.setDistrictName((String) row[4]);
			dto.setTehsilName((String) row[5]);
			dto.setMouzaName((String) row[6]);
			dto.setKhataNo((String) row[7]);
			dto.setApplicationStatus((String) row[8]);
			dto.setActionOn((Date) row[9]);
			dto.setRemark((String) row[10]);
			dto.setExtent((String) row[11]);
			dto.setPlotExtend((String) row[12]);
			dto.setPlotNo((String) row[13]);
			dto.setCreatedOn((Date) row[14]);
			dto.setDistrictCode((String) row[15]);
			dto.setTahasilCode((String) row[16]);
			dto.setVillageCode((String) row[17]);

			resultList.add(dto);
		}

		entityManager.close();
		log.info(":: getLandApplicantsWithApproveDetails() --   Result Return Scucess..!!");
		return resultList;

	}

	@Transactional
	public List<LandApplicantDTO> getLandApplicantsWithApproveDetailsSerchFunction(Integer approvedUser,
			String districtCode, int pageNumber, int pageSize, String tahasilCode, String villageCode,
			String khatianCode) {
		String query = " SELECT A.land_application_id, A.application_no, A.applicant_name,A.mobile_no, B.district_name, "
				+ " C.tahasil_name, D.village_name, E.khata_no, "
				+ " G.application_status, F.action_on, F.remark, k.khatian_code,"
				+ "(select CAST(st_extent(ST_Transform(spb.geom,3857))AS character varying) as extent "
				+ "from public.land_schedule ls join land_bank.plot_information pi on(ls.plot_code=pi.plot_code )join land_bank.sjta_plot_boundary spb "
				+ "on( spb.plot_code=pi.plot_code) JOIN land_bank.sjta_plot_boundary sj ON sj.plot_code = k.khatian_code where "
				+ " A.land_application_id=land_application_id) , "
				+ " CAST((SELECT st_extent(ST_Transform(geom,3857)) as extent "
				+ " FROM land_bank.sjta_plot_boundary_view where plot_code in (SELECT STRING_AGG(ls.plot_code, ',') AS plot_code FROM public.land_schedule ls WHERE ls.land_application_id = A.land_application_id AND ls.deleted_flag='0' ) group by village_code)as varchar) as plot_extend,"
				+ " CAST((SELECT STRING_AGG((select plot_no from land_bank.plot_information "
				+ "where plot_code=ls.plot_code), ',') AS plot_no FROM public.land_schedule ls "
				+ "WHERE ls.land_application_id = A.land_application_id AND deleted_flag='0')as varchar) as plot_no, F.created_on, B.district_code, C.tahasil_code, D.village_code FROM "
				+ " public.land_application A  inner join "
				+ " land_bank.district_master B ON A.district_code = B.district_code inner join land_bank.tahasil_master C ON A.tehsil_code =C.tahasil_code "
				+ "inner join land_bank.village_master D ON A.village_code = D.village_code inner join land_bank.khatian_information E "
				+ " ON A.khatian_code = E.khatian_code inner join land_application_approval F "
				+ "ON A.land_application_id =F.land_application_id inner join m_application_status G on F.application_status_id=G.application_status_id "
				+ " INNER JOIN land_bank.khatian_information k ON A.khatian_code = k.khatian_code  where "
				+ " F.approval_action_id = :approvedUser AND b.district_code =:districtCode  ";

		if (!tahasilCode.equals("0")) {
			query += "AND c.tahasil_code =:tahasilCode ";
		}

		if (!villageCode.equals("0")) {
			query += "AND d.village_code =:villageCode ";
		}

		if (!khatianCode.equals("0")) {
			query += "AND e.khatian_code =:khatianCode ";
		}

		query += "AND a.deleted_flag = '0'  ORDER BY F.created_on DESC LIMIT :pageSize OFFSET :offset";

		Integer offset = (pageNumber - 1) * pageSize;

		@SuppressWarnings("unchecked")
		Query resultList = entityManager.createNativeQuery(query).setParameter("approvedUser", approvedUser)
				.setParameter("districtCode", districtCode).setParameter("pageSize", pageSize)
				.setParameter("offset", offset);

		if (!tahasilCode.equals("0")) {
			resultList.setParameter("tahasilCode", tahasilCode);
		}

		if (!villageCode.equals("0")) {
			resultList.setParameter("villageCode", villageCode);
		}

		if (!khatianCode.equals("0")) {
			resultList.setParameter("khatianCode", khatianCode);
		}

		log.info(":: getLandApplicantsWithDetailsSearchFunction()  Query Execution Sucess..!!");

//		int offset = (pageNumber - 1) * pageSize;
//
//		Query query = entityManager.createNativeQuery(sql).setParameter("approvedUser", approvedUser)
//				.setParameter("districtCode", districtCode + "%").setParameter("tahasilCode", tahasilCode + "%")
//				.setParameter("villageCode", villageCode + "%").setParameter("khatianCode", khatianCode)
//				.setParameter("pageSize", pageSize).setParameter("offset", offset);

		List<LandApplicantDTO> result = new ArrayList<>();
		@SuppressWarnings("unchecked")
		List<Object[]> rows = resultList.getResultList();
		for (Object[] row : rows) {
			LandApplicantDTO dto = new LandApplicantDTO();
			dto.setLandApplicantId((BigInteger) row[0]);
			dto.setApplicantNo((String) row[1]);
			dto.setApplicantName((String) row[2]);
			dto.setMobileNo((String) row[3]);
			dto.setDistrictName((String) row[4]);
			dto.setTehsilName((String) row[5]);
			dto.setMouzaName((String) row[6]);
			dto.setKhataNo((String) row[7]);
			dto.setApplicationStatus((String) row[8]);
			dto.setActionOn((Date) row[9]);
			dto.setRemark((String) row[10]);
			dto.setKhatianCode((String) row[11]);
			dto.setExtent((String) row[12]);
			dto.setPlotExtend((String) row[13]);
			dto.setPlotNo((String) row[14]);
			dto.setCreatedOn((Date) row[15]);
			dto.setDistrictCode((String) row[16]);
			dto.setTahasilCode((String) row[17]);
			dto.setVillageCode((String) row[18]);

			result.add(dto);
		}

		entityManager.close();
		log.info(":: getLandApplicantsWithApproveDetailsSerchFunction() --   Result Return Scucess..!!");
		return result;

	}

	@Transactional
	public List<LandApplicantDTO> getLandApplicantsWithRejectDetails(Integer approvalActionId, int pageNumber,
			int pageSize) {
		String sql = "SELECT A.land_application_id, A.application_no, A.applicant_name, "
				+ " A.mobile_no, B.district_name, C.tahasil_name, D.village_name, "
				+ " E.khata_no, G.application_status, F.action_on, F.remark,(select CAST(st_extent(ST_Transform(spb.geom,3857))AS character varying) as extent "
				+ "from public.land_schedule ls join  land_bank.plot_information pi on(ls.plot_code=pi.plot_code)  "
				+ " join land_bank.sjta_plot_boundary spb on(spb.plot_code=pi.plot_code) "
				+ "where A.land_application_id=land_application_id), "
				+ " CAST((SELECT st_extent(ST_Transform(geom,3857)) as extent "
				+ " FROM land_bank.sjta_plot_boundary_view where plot_code in (SELECT STRING_AGG(ls.plot_code, ',') AS plot_code"
				+ "	FROM public.land_schedule ls WHERE ls.land_application_id = A.land_application_id "
				+ " AND ls.deleted_flag='0' ) group by village_code)as varchar) as plot_extend, "
				+ "	CAST((SELECT STRING_AGG((select plot_no from land_bank.plot_information"
				+ " where plot_code=ls.plot_code), ',') AS plot_no FROM public.land_schedule ls "
				+ "WHERE ls.land_application_id = A.land_application_id AND deleted_flag='0')as varchar) as plot_no, "
				+ "F.pending_at_role_id, F.approval_level, F.created_on, B.district_code, C.tahasil_code, D.village_code "
				+ "FROM public.land_application A inner join land_bank.district_master B "
				+ " ON A.district_code = B.district_code inner join land_bank.tahasil_master C "
				+ " ON A.tehsil_code =C.tahasil_code inner join land_bank.village_master D "
				+ " ON A.village_code = D.village_code inner join "
				+ " land_bank.khatian_information E ON A.khatian_code = E.khatian_code "
				+ " inner join land_application_approval F "
				+ "ON A.land_application_id =F.land_application_id inner join "
				+ " m_application_status G on F.application_status_id=G.application_status_id "
				+ " where F.approval_action_id =:approvalActionId ORDER BY F.created_on DESC"
				+ " LIMIT :pageSize OFFSET :offset ";

		log.info(":: getLandApplicantsWithDetailsSearchFunction()  Query Execution Sucess..!!");
		int offset = (pageNumber - 1) * pageSize;

		Query query = entityManager.createNativeQuery(sql).setParameter("approvalActionId", approvalActionId)
				.setParameter("pageSize", pageSize).setParameter("offset", offset);

		List<LandApplicantDTO> resultList = new ArrayList<>();
		@SuppressWarnings("unchecked")
		List<Object[]> rows = query.getResultList();
		for (Object[] row : rows) {
			LandApplicantDTO dto = new LandApplicantDTO();
			dto.setLandApplicantId((BigInteger) row[0]);
			dto.setApplicantNo((String) row[1]);
			dto.setApplicantName((String) row[2]);
			dto.setMobileNo((String) row[3]);
			dto.setDistrictName((String) row[4]);
			dto.setTehsilName((String) row[5]);
			dto.setMouzaName((String) row[6]);
			dto.setKhataNo((String) row[7]);
			dto.setApplicationStatus((String) row[8]);
			dto.setActionOn((Date) row[9]);
			dto.setRemark((String) row[10]);
			dto.setExtent((String) row[11]);
			dto.setPlotExtend((String) row[12]);
			dto.setPlotNo((String) row[13]);
			dto.setRejectPendingRoleId((Short) row[14]);
			dto.setApprovalLevleId((Short) row[15]);
			dto.setCreatedOn((Date) row[16]);
			dto.setDistrictCode((String) row[17]);
			dto.setTahasilCode((String) row[18]);
			dto.setVillageCode((String) row[19]);

			resultList.add(dto);
		}

		entityManager.close();
		log.info(":: getLandApplicantsWithRejectDetails() --   Result Return Scucess..!!");
		return resultList;

	}

	@Transactional
	public List<LandApplicantDTO> getLandApplicantsWithRejectDetailsSearchFunction(Integer approvalActionId,
			String districtCode, int pageNumber, int pageSize, String tahasilCode, String villageCode,
			String khatianCode) {
		String query = " SELECT A.land_application_id, A.application_no, A.applicant_name, A.mobile_no, B.district_name, C.tahasil_name, D.village_name,"
				+ " E.khata_no, G.application_status, F.action_on, F.remark , k.khatian_code , "
				+ " (select CAST(st_extent(ST_Transform(spb.geom, 3857))AS character varying) as extent from public.land_schedule ls "
				+ " join land_bank.plot_information pi on( ls.plot_code=pi.plot_code ) join land_bank.sjta_plot_boundary spb on( spb.plot_code=pi.plot_code ) "
				+ " where A.land_application_id=ls.land_application_id),  "
				+ " CAST((SELECT st_extent(ST_Transform(geom,3857)) as extent FROM land_bank.sjta_plot_boundary_view where plot_code in  "
				+ " (SELECT STRING_AGG(ls.plot_code, ',') AS plot_code FROM public.land_schedule ls WHERE ls.land_application_id = A.land_application_id "
				+ " AND ls.deleted_flag='0' ) group by village_code)as varchar) as plot_extend, "
				+ " CAST((SELECT STRING_AGG((select plot_no from land_bank.plot_information where plot_code=ls.plot_code), ',') AS plot_no "
				+ " FROM public.land_schedule ls WHERE ls.land_application_id = A.land_application_id AND deleted_flag='0' )as varchar) as plot_no , "
				+ " F.pending_at_role_id, F.approval_level, F.created_on, B.district_code, C.tahasil_code, D.village_code  "
				+ " FROM public.land_application A  "
				+ " inner join land_bank.district_master B ON A.district_code = B.district_code  "
				+ " inner join land_bank.tahasil_master C ON A.tehsil_code =C.tahasil_code  "
				+ " inner join land_bank.village_master D ON A.village_code = D.village_code  "
				+ " inner join land_bank.khatian_information E ON A.khatian_code = E.khatian_code  "
				+ " inner join land_application_approval F ON A.land_application_id =F.land_application_id  "
				+ " inner join m_application_status G on F.application_status_id=G.application_status_id  "
				+ " INNER JOIN land_bank.khatian_information k ON A.khatian_code = k.khatian_code  "
				+ " where F.approval_action_id = :approvalActionId AND b.district_code =:districtCode   ";

//				+ " ORDER BY F.created_on DESC LIMIT :pageSize OFFSET :offset";

		log.info(":: getLandApplicantsWithDetailsSearchFunction()  Query Execution Sucess..!!");

		if (!tahasilCode.equals("0")) {
			query += "AND c.tahasil_code =:tahasilCode ";
		}

		if (!villageCode.equals("0")) {
			query += "AND d.village_code =:villageCode ";
		}

		if (!khatianCode.equals("0")) {
			query += "AND e.khatian_code =:khatianCode ";
		}

		query += "AND a.deleted_flag = '0' ORDER BY F.created_on DESC LIMIT :pageSize OFFSET :offset";

		Integer offset = (pageNumber - 1) * pageSize;

		@SuppressWarnings("unchecked")
		Query resultList = entityManager.createNativeQuery(query).setParameter("approvalActionId", approvalActionId)
				.setParameter("districtCode", districtCode).setParameter("pageSize", pageSize)
				.setParameter("offset", offset);

		if (!tahasilCode.equals("0")) {
			resultList.setParameter("tahasilCode", tahasilCode);
		}

		if (!villageCode.equals("0")) {
			resultList.setParameter("villageCode", villageCode);
		}

		if (!khatianCode.equals("0")) {
			resultList.setParameter("khatianCode", khatianCode);
		}

		List<LandApplicantDTO> result = new ArrayList<>();
		@SuppressWarnings("unchecked")
		List<Object[]> rows = resultList.getResultList();
		for (Object[] row : rows) {
			LandApplicantDTO dto = new LandApplicantDTO();
			dto.setLandApplicantId((BigInteger) row[0]);
			dto.setApplicantNo((String) row[1]);
			dto.setApplicantName((String) row[2]);
			dto.setMobileNo((String) row[3]);
			dto.setDistrictName((String) row[4]);
			dto.setTehsilName((String) row[5]);
			dto.setMouzaName((String) row[6]);
			dto.setKhataNo((String) row[7]);
			dto.setApplicationStatus((String) row[8]);
			dto.setActionOn((Date) row[9]);
			dto.setRemark((String) row[10]);
			dto.setKhatianCode((String) row[11]);
			dto.setExtent((String) row[12]);
			dto.setPlotExtend((String) row[13]);
			dto.setPlotNo((String) row[14]);
			dto.setRejectPendingRoleId((Short) row[15]);
			dto.setApprovalLevleId((Short) row[16]);
			dto.setCreatedOn((Date) row[17]);
			dto.setDistrictCode((String) row[18]);
			dto.setTahasilCode((String) row[19]);
			dto.setVillageCode((String) row[20]);

			result.add(dto);
		}
		log.info(":: getLandApplicantsWithRejectDetailsSearchFunction() --   Result Return Scucess..!!");
		entityManager.close();
		return result;

	}

	@Transactional
	public List<LandApplicantDTO> getLandApplicantsRevertCitizenDetails(Integer revertToCitiZen, int pageNumber,
			int pageSize) {
		String sql = " SELECT A.land_application_id, A.application_no, A.applicant_name, A.mobile_no, B.district_name, C.tahasil_name, "
				+ " D.village_name, E.khata_no, G.application_status, F.action_on, F.remark , (select CAST(st_extent(ST_Transform(spb.geom, 3857))AS character varying) "
				+ " as extent from public.land_schedule ls  "
				+ " join land_bank.plot_information pi on(ls.plot_code=pi.plot_code) join land_bank.sjta_plot_boundary spb on(spb.plot_code=pi.plot_code) "
				+ " where A.land_application_id=land_application_id) , CAST((SELECT st_extent(ST_Transform(geom,3857)) as extent FROM land_bank.sjta_plot_boundary_view "
				+ " where plot_code in (SELECT STRING_AGG(ls.plot_code, ',') AS plot_code FROM public.land_schedule ls WHERE ls.land_application_id = A.land_application_id "
				+ " AND ls.deleted_flag='0' ) group by village_code)as varchar) as plot_extend, CAST((SELECT STRING_AGG((select plot_no from land_bank.plot_information "
				+ " where plot_code=ls.plot_code), ',') AS plot_no FROM public.land_schedule ls WHERE ls.land_application_id = A.land_application_id "
				+ " AND deleted_flag='0' )as varchar) as plot_no, F.created_on, B.district_code, C.tahasil_code, D.village_code FROM public.land_application A "
				+ " inner join land_bank.district_master B ON A.district_code = B.district_code  "
				+ " inner join land_bank.tahasil_master C ON A.tehsil_code =C.tahasil_code inner join land_bank.village_master D ON A.village_code = D.village_code  "
				+ " inner join land_bank.khatian_information E ON A.khatian_code = E.khatian_code  "
				+ " inner join land_application_approval F ON A.land_application_id =F.land_application_id  "
				+ " inner join m_application_status G on F.application_status_id=G.application_status_id "
				+ " where F.approval_action_id = :revertToCitiZen ORDER BY F.created_on DESC LIMIT :pageSize OFFSET :offset ;";

		log.info(":: getLandApplicantsWithDetailsSearchFunction()  Query Execution Sucess..!!");
		int offset = (pageNumber - 1) * pageSize;

		Query query = entityManager.createNativeQuery(sql).setParameter("revertToCitiZen", revertToCitiZen)
				.setParameter("pageSize", pageSize).setParameter("offset", offset);

		List<LandApplicantDTO> resultList = new ArrayList<>();
		@SuppressWarnings("unchecked")
		List<Object[]> rows = query.getResultList();
		for (Object[] row : rows) {
			LandApplicantDTO dto = new LandApplicantDTO();
			dto.setLandApplicantId((BigInteger) row[0]);
			dto.setApplicantNo((String) row[1]);
			dto.setApplicantName((String) row[2]);
			dto.setMobileNo((String) row[3]);
			dto.setDistrictName((String) row[4]);
			dto.setTehsilName((String) row[5]);
			dto.setMouzaName((String) row[6]);
			dto.setKhataNo((String) row[7]);
			dto.setApplicationStatus((String) row[8]);
			dto.setActionOn((Date) row[9]);
			dto.setRemark((String) row[10]);
			dto.setExtent((String) row[11]);
			dto.setPlotExtend((String) row[12]);
			dto.setPlotNo((String) row[13]);
			dto.setCreatedOn((Date) row[14]);
			dto.setDistrictCode((String) row[15]);
			dto.setTahasilCode((String) row[16]);
			dto.setVillageCode((String) row[17]);

			resultList.add(dto);
		}

		entityManager.close();
		log.info(":: getLandApplicantsRevertCitizenDetails() --   Result Return Scucess..!!");
		return resultList;

	}

	@Transactional
	public List<LandApplicantDTO> getLandApplicantsRevertCitizenDetailsSearchFunction(Integer revertToCitiZen,
			String districtCode, int pageNumber, int pageSize, String tahasilCode, String villageCode,
			String khatianCode) {
		String query = "SELECT A.land_application_id, A.application_no, A.applicant_name, A.mobile_no, B.district_name, C.tahasil_name, D.village_name, "
				+ " E.khata_no, G.application_status, F.action_on, F.remark ,(select CAST(st_extent(ST_Transform(spb.geom, 3857))AS character varying) as extent "
				+ " from public.land_schedule ls  "
				+ " join land_bank.plot_information pi on( ls.plot_code=pi.plot_code )  "
				+ " join land_bank.sjta_plot_boundary spb on( spb.plot_code=pi.plot_code )  "
				+ " JOIN land_bank.sjta_plot_boundary sj ON sj.plot_code = k.khatian_code where A.land_application_id=land_application_id), "
				+ " CAST((SELECT st_extent(ST_Transform(geom,3857)) as extent FROM land_bank.sjta_plot_boundary_view "
				+ " where plot_code in (SELECT STRING_AGG(ls.plot_code, ',') AS plot_code FROM public.land_schedule ls  "
				+ " WHERE ls.land_application_id = A.land_application_id AND ls.deleted_flag='0' ) group by village_code)as varchar) as plot_extend, "
				+ " CAST((SELECT STRING_AGG((select plot_no from land_bank.plot_information where plot_code=ls.plot_code), ',') AS plot_no FROM public.land_schedule ls  "
				+ " WHERE ls.land_application_id = A.land_application_id AND deleted_flag='0' )as varchar) as plot_no, F.created_on, B.district_code, C.tahasil_code, "
				+ " D.village_code FROM public.land_application A inner join land_bank.district_master B ON A.district_code = B.district_code  "
				+ " inner join land_bank.tahasil_master C ON A.tehsil_code =C.tahasil_code  "
				+ " inner join land_bank.village_master D ON A.village_code = D.village_code  "
				+ " inner join land_bank.khatian_information E ON A.khatian_code = E.khatian_code  "
				+ " inner join land_application_approval F ON A.land_application_id =F.land_application_id  "
				+ " inner join m_application_status G on F.application_status_id=G.application_status_id  "
				+ " INNER JOIN land_bank.khatian_information k ON A.khatian_code = k.khatian_code where F.approval_action_id =:revertToCitiZen  "
				+ " AND b.district_code =:districtCode ";

		log.info(":: getLandApplicantsWithDetailsSearchFunction()  Query Execution Sucess..!!");

		if (!tahasilCode.equals("0")) {
			query += "AND c.tahasil_code =:tahasilCode ";
		}

		if (!villageCode.equals("0")) {
			query += "AND d.village_code =:villageCode ";
		}

		if (!khatianCode.equals("0")) {
			query += "AND e.khatian_code =:khatianCode ";
		}

		query += "AND a.deleted_flag = '0' ORDER BY F.created_on DESC LIMIT :pageSize OFFSET :offset ";

		Integer offset = (pageNumber - 1) * pageSize;

		@SuppressWarnings("unchecked")
		Query resultList = entityManager.createNativeQuery(query).setParameter("revertToCitiZen", revertToCitiZen)
				.setParameter("districtCode", districtCode).setParameter("pageSize", pageSize)
				.setParameter("offset", offset);

		if (!tahasilCode.equals("0")) {
			resultList.setParameter("tahasilCode", tahasilCode);
		}

		if (!villageCode.equals("0")) {
			resultList.setParameter("villageCode", villageCode);
		}

		if (!khatianCode.equals("0")) {
			resultList.setParameter("khatianCode", khatianCode);
		}

//		int offset = (pageNumber - 1) * pageSize;
//
//		Query query = entityManager.createNativeQuery(sql).setParameter("revertToCitiZen", revertToCitiZen)
//				.setParameter("districtCode", districtCode + "%").setParameter("tahasilCode", tahasilCode + "%")
//				.setParameter("villageCode", villageCode + "%").setParameter("khatianCode", khatianCode)
//				.setParameter("pageSize", pageSize).setParameter("offset", offset);

		List<LandApplicantDTO> result = new ArrayList<>();
		@SuppressWarnings("unchecked")
		List<Object[]> rows = resultList.getResultList();
		for (Object[] row : rows) {
			LandApplicantDTO dto = new LandApplicantDTO();
			dto.setLandApplicantId((BigInteger) row[0]);
			dto.setApplicantNo((String) row[1]);
			dto.setApplicantName((String) row[2]);
			dto.setMobileNo((String) row[3]);
			dto.setDistrictName((String) row[4]);
			dto.setTehsilName((String) row[5]);
			dto.setMouzaName((String) row[6]);
			dto.setKhataNo((String) row[7]);
			dto.setApplicationStatus((String) row[8]);
			dto.setActionOn((Date) row[9]);
			dto.setRemark((String) row[10]);
			dto.setExtent((String) row[11]);
			dto.setPlotExtend((String) row[12]);
			dto.setPlotNo((String) row[13]);
			dto.setCreatedOn((Date) row[14]);
			dto.setDistrictCode((String) row[15]);
			dto.setTahasilCode((String) row[16]);
			dto.setVillageCode((String) row[17]);

			result.add(dto);
		}
		entityManager.close();
		log.info(":: getLandApplicantsRevertCitizenDetailsSearchFunction() --   Result Return Scucess..!!");
		return result;

	}

	public List<LandApplicantDTO> getLandApplicantsDetails(String plotCode, Short mettingLevleId) {
		String queryFireJoin = "";
		String queryFireCondiction = "";
		String queryFireJoinAdd = " JOIN application.meeting_schedule_applicant msa ON(msa.land_application_id=a.land_application_id) "
				+ " JOIN application.meeting_schedule ms ON(msa.meeting_schedule_id=ms.meeting_schedule_id) ";
		String querySetConstantWhere = "AND (select plot_code from land_bank.plot_information pi2 where msa.plot_no=pi2.plot_no)=:plotCode ";
		if (mettingLevleId == 2) {
			queryFireJoin = queryFireJoinAdd;
			queryFireCondiction = " AND msa.approval_status=1 AND msa.status='0' AND ms.meeting_level_id=1 AND ms.status='0' "
					+ querySetConstantWhere;
		} else if (mettingLevleId == 3) {
			queryFireJoin = queryFireJoinAdd;
			queryFireCondiction = " AND msa.approval_status=3 AND msa.status='0' AND ms.meeting_level_id=2 AND ms.status='0' "
					+ querySetConstantWhere;
		} else if (mettingLevleId == 4) {
			queryFireJoin = queryFireJoinAdd;
			queryFireCondiction = " AND msa.approval_status=5 AND msa.status='0' AND ms.meeting_level_id=3 AND ms.status='0' "
					+ querySetConstantWhere;
		} else {
			queryFireJoin = "";
			queryFireCondiction = "";
		}
		List<LandApplicantDTO> resultList = new ArrayList<>();
		try {
			String sql = "SELECT DISTINCT ON (a.application_no) a.application_no, a.applicant_name, a.mobile_no,"
					+ "d.district_name, t.tahasil_name, v.village_name, k.khata_no, STRING_AGG(DISTINCT p.plot_no, ', ') AS plot_numbers, "
					+ "a.land_application_id, a.email_address, b.created_on, ls.plot_code   "
					+ "FROM land_application a " + "JOIN land_application_approval b USING(land_application_id)  "
					+ "JOIN land_schedule ls USING(land_application_id)  "
					+ "JOIN land_bank.district_master d USING(district_code)  "
					+ "JOIN land_bank.tahasil_master t ON a.tehsil_code = t.tahasil_code  "
					+ "JOIN land_bank.village_master v USING(village_code)  "
					+ "JOIN land_bank.khatian_information k USING(khatian_code)  "
					+ "JOIN land_bank.plot_information p ON p.plot_code = ls.plot_code " + queryFireJoin
					+ "WHERE b.approval_action_id = 6 " + "	AND a.deleted_flag='0' " + "AND ls.plot_code=:plotCode "
					+ "AND ls.deleted_flag='0' AND ls.meeting_schedule_flag= '0' " + queryFireCondiction
					+ "GROUP BY a.application_no, a.applicant_name, a.mobile_no, d.district_name, "
					+ "t.tahasil_name, v.village_name, k.khata_no, a.land_application_id, a.email_address, b.created_on, ls.plot_code ";

			logger.info(":: getLandApplicantsWithDetailsWithoutSearchFunction() Query Execution Success..!!");
			Query query = entityManager.createNativeQuery(sql).setParameter("plotCode", plotCode);

			@SuppressWarnings("unchecked")
			List<Object[]> rows = query.getResultList();
			for (Object[] row : rows) {
				LandApplicantDTO dto = new LandApplicantDTO();
				dto.setApplicantNo((String) row[0]);
				dto.setApplicantName((String) row[1]);
				dto.setMobileNo((String) row[2]);
				dto.setDistrictName((String) row[3]);
				dto.setTehsilName((String) row[4]);
				dto.setMouzaName((String) row[5]);
				dto.setKhataNo((String) row[6]);
				dto.setPlotNo((String) row[7]);
				dto.setLandApplicantId((BigInteger) row[8]);
				dto.setEmailId((String) row[9]);
				dto.setCreatedOn((Date) row[10]);
				dto.setPlotCode((String) row[11]);
				resultList.add(dto);
			}
		} catch (Exception e) {
			logger.error("Error,  in getLandApplicantsDetails {} ", e.getMessage());
		} finally {
			entityManager.close();
		}

		return resultList;
	}

	public BigInteger getCount(String selDistrictName, String selTahasilName) {
		try {
			String sql = "SELECT COUNT(*) FROM land_application a "
					+ "JOIN land_application_approval b USING(land_application_id) "
					+ "JOIN land_bank.district_master d USING(district_code) "
					+ "JOIN land_bank.tahasil_master t ON a.tehsil_code = t.tahasil_code "
					+ "JOIN land_bank.village_master v USING(village_code) "
					+ "JOIN land_bank.khatian_information k USING(khatian_code) WHERE b.approval_action_id = 6";

			if (selDistrictName != null && !selDistrictName.isEmpty()) {
				sql += " AND d.district_code = :district_code ";
			}

			if (selTahasilName != null && !selTahasilName.isEmpty()) {
				sql += " AND t.tahasil_code = :tahasil_code ";
			}

			logger.info(":: getCount() Query Execution Success..!!");
			Query query = entityManager.createNativeQuery(sql);

			if (selDistrictName != null && !selDistrictName.isEmpty()) {
				query.setParameter("district_code", selDistrictName);
			}

			if (selTahasilName != null && !selTahasilName.isEmpty()) {
				query.setParameter("tahasil_code", selTahasilName);
			}

			return (BigInteger) query.getSingleResult();
		} catch (Exception e) {
			logger.error("Error in getCount: " + e.getMessage(), e);
			return BigInteger.ZERO;
		} finally {
			entityManager.close();
		}
	}

	public List<LandApplicantDTO> viewApplicationHistory(BigInteger landApplicantId) {
		List<LandApplicantDTO> resultList = new ArrayList<>();
		try {
			String sql = " select lal.land_application_id, la.application_no, mas.application_status,maa.approval_action,laa.remark,lal.action_on,ud.full_name,dm.district_name,tm.tahasil_name,vm.village_name, mas.application_status_id from land_application_approval_log lal "
					+ " LEFT JOIN land_application la USING(land_application_id) "
					+ " LEFT JOIN m_application_status mas ON lal.application_status_id = mas.application_status_id "
					+ " LEFT JOIN m_approval_action maa ON lal.approval_action_id = maa.approval_action_id "
					+ " LEFT JOIN land_application_approval laa ON lal.land_application_approval_id = laa.land_application_approval_id "
					+ " LEFT JOIN user_details ud ON lal.updated_by = ud.user_id "
					+ " LEFT JOIN land_bank.district_master dm ON la.district_code = dm.district_code "
					+ " LEFT JOIN land_bank.tahasil_master tm ON la.tehsil_code = tm.tahasil_code "
					+ " LEFT JOIN land_bank.village_master vm ON la.village_code = vm.village_code "
					+ " WHERE lal.land_application_id =:landApplicantId ORDER BY lal.action_on DESC";
			logger.info(":: viewApplicationHistory() Query Execution Success..!!");
			Query query = entityManager.createNativeQuery(sql).setParameter("landApplicantId", landApplicantId);

			@SuppressWarnings("unchecked")
			List<Object[]> rows = query.getResultList();
			for (Object[] row : rows) {
				LandApplicantDTO dto = new LandApplicantDTO();
				dto.setApplicantNo((String) row[1]);
				dto.setFullName((String) row[6]);
				dto.setDistrictName((String) row[7]);
				dto.setTehsilName((String) row[8]);
				dto.setMouzaName((String) row[9]);
				dto.setApplicationStatus((String) row[2]);
				dto.setActionOn((Date) row[5]);
				dto.setRemark((String) row[4]);
				dto.setApplicationStatusId((Short) row[10]);
				resultList.add(dto);
			}
		} catch (Exception e) {
			logger.error("Error,  in viewhistory {} ", e.getMessage());
		} finally {
			entityManager.close();
		}

		return resultList;

	}

	@Transactional
	public List<LandApplicantDTO> getlandForAfterMeetings(Short mettingLevleId, BigInteger meetingId,
			BigInteger meetingSheduleId, Integer auctionFlag) {

		List<LandApplicantDTO> resultList = new ArrayList<>();
		if (auctionFlag == 3) {
			String sqlForAuction = "  SELECT a.bidder_form_m_application_number, (select full_name from public.citizen_profile_details  "
					+ "where user_id=citizen_profile_details_id limit 1) as applicant_name, "
					+ "(select mobile_no  from  public.citizen_profile_details  "
					+ "where  user_id=citizen_profile_details_id limit 1) as mobile_no, "
					+ "(select (select (select (select (select dm.district_name from "
					+ "land_bank.district_master dm  where dm.district_code=tm.district_code LIMIT 1)   "
					+ "from  land_bank.tahasil_master tm where tm.tahasil_code=vm.tahasil_code LIMIT 1)   "
					+ "from  land_bank.village_master vm  where  vm.village_code=ki.village_code LIMIT 1)   "
					+ "from land_bank.khatian_information ki where ki.khatian_code=pii.khatian_code LIMIT 1)    "
					+ "from land_bank.plot_information pii  where "
					+ "pii.plot_code=msa.plot_no limit 1) as district_name, "
					+ "(select  (select (select (select tm.tahasil_name  from land_bank.tahasil_master tm where "
					+ "tm.tahasil_code=vm.tahasil_code LIMIT 1) from land_bank.village_master vm   "
					+ "where vm.village_code=ki.village_code LIMIT 1) from "
					+ "land_bank.khatian_information ki   where ki.khatian_code=pii.khatian_code LIMIT 1)    "
					+ "from land_bank.plot_information pii  where pii.plot_code=msa.plot_no limit 1)as tahasil_name,  "
					+ "(  select  (select (select  vm.village_name  from land_bank.village_master vm where "
					+ "vm.village_code=ki.village_code LIMIT 1) from land_bank.khatian_information ki   "
					+ "where  ki.khatian_code=pii.khatian_code LIMIT 1)    "
					+ "from land_bank.plot_information pii  where "
					+ "pii.plot_code=msa.plot_no limit 1)as village_name, (select "
					+ "(select ki.khata_no   from land_bank.khatian_information ki   "
					+ "where ki.khatian_code=pii.khatian_code LIMIT 1)  from land_bank.plot_information pii   "
					+ "where  pii.plot_code=msa.plot_no limit 1 )as khata_no,   "
					+ "(  select pii.plot_no from land_bank.plot_information pii   where pii.plot_code=msa.plot_no limit 1 "
					+ ")AS plot_numbers, a.bidder_form_m_application_id, ( "
					+ "select email_id  from public.citizen_profile_details  where user_id=citizen_profile_details_id limit 1) as email_address, "
					+ "a.created_on ,msa.plot_no    " + "FROM  application.bidder_form_m_application a  "
					+ "JOIN  application.tender_auction ta  " + "ON ta.tender_auction_id = a.tender_auction_id   "
					+ "JOIN application.meeting_schedule_applicant msa ON ( msa.bidder_form_m_application_id=a.bidder_form_m_application_id )  "
					+ "JOIN application.meeting_schedule ms  ON(ms.meeting_schedule_id = msa.meeting_schedule_id)  "
					+ "where  ms.meeting_schedule_id=:meetingSheduleId AND msa.approval_status=1  ";

			Query query = entityManager.createNativeQuery(sqlForAuction).setParameter("meetingSheduleId",
					meetingSheduleId);
			@SuppressWarnings("unchecked")
			List<Object[]> rows = query.getResultList();
			for (Object[] row : rows) {
				LandApplicantDTO dto = new LandApplicantDTO();
				dto.setApplicantNo((String) row[0]);
				dto.setApplicantName((String) row[1]);
				dto.setMobileNo((String) row[2]);
				dto.setDistrictName((String) row[3]);
				dto.setTehsilName((String) row[4]);
				dto.setMouzaName((String) row[5]);
				dto.setKhataNo((String) row[6]);
				dto.setPlotNo((String) row[7]);
				dto.setLandApplicantId((BigInteger) row[8]);
				dto.setEmailId((String) row[9]);
				dto.setCreatedOn((Date) row[10]);
				dto.setPlotCode((String) row[11]);
				resultList.add(dto);
			}

		} else {
			try {
				String sql = "";
				if (mettingLevleId != null && mettingLevleId > 0) {
					sql = " SELECT DISTINCT "
							+ "(SELECT application_no FROM public.land_application WHERE land_application_id = msa.land_application_id LIMIT 1) AS application_no, "
							+ "(SELECT applicant_name FROM public.land_application WHERE land_application_id = msa.land_application_id LIMIT 1) AS applicant_name, "
							+ "(SELECT mobile_no FROM public.land_application WHERE land_application_id = msa.land_application_id LIMIT 1) AS mobile_no, "
							+ "d.district_name, " + "t.tahasil_name, " + "v.village_name, " + "k.khata_no, "
							+ "msa.plot_no as plotcode, " + "msa.land_application_id, "
							+ "(SELECT email_address FROM public.land_application WHERE land_application_id = msa.land_application_id LIMIT 1) AS email_address, "
							+ "ms.created_on, " + "P.plot_no  " + "FROM  application.meeting_schedule ms "
							+ "JOIN application.meeting_schedule_applicant msa ON ms.meeting_schedule_id = msa.meeting_schedule_id "
							+ "JOIN land_bank.plot_information p ON p.plot_code = msa.plot_no "
							+ "JOIN land_bank.khatian_information k USING(khatian_code) "
							+ "JOIN land_bank.village_master v USING(village_code) "
							+ "JOIN land_bank.tahasil_master t USING(tahasil_code) "
							+ "JOIN land_bank.district_master d USING(district_code) " + "WHERE   "
							+ "msa.approval_status=1    " + "AND msa.status='0'    " + "AND ms.status='0'    "
							+ "AND ms.meeting_level_id=:meetingLevleId  " + "AND ms.meeting_id=:mettingId   "
							+ "AND ms.meeting_schedule_id=:meetingSheduleId  " + "AND msa.metting_auction_status=0 ";
				} else {
					sql = " SELECT  DISTINCT  "
							+ " ON (a.application_no) a.application_no, a.applicant_name, a.mobile_no, d.district_name, t.tahasil_name, v.village_name, "
							+ " k.khata_no, "
//							+ " STRING_AGG(DISTINCT p.plot_no, ', ') AS plot_numbers"
							+ "  msa.plot_no , "
							+ " a.land_application_id, a.email_address, b.created_on ,(select plot_no from land_bank.plot_information where plot_code=msa.plot_no)  as plot_code  "
							+ " FROM land_application a  "
							+ " JOIN  land_application_approval b USING(land_application_id)   "
							+ " JOIN  land_schedule ls USING(land_application_id)   "
							+ " JOIN  land_bank.district_master d USING(district_code)   "
							+ " JOIN  land_bank.tahasil_master t ON a.tehsil_code = t.tahasil_code   "
							+ " JOIN  land_bank.village_master v USING(village_code)   "
							+ " JOIN  land_bank.khatian_information k USING(khatian_code)   "
							+ " JOIN  land_bank.plot_information p ON p.plot_code = ls.plot_code   "
							+ " JOIN  application.meeting_schedule_applicant msa  ON( msa.land_application_id=a.land_application_id ) "
							+ " JOIN  application.meeting_schedule ms ON( msa.meeting_schedule_id=ms.meeting_schedule_id ) "
							+ "	JOIN application.meetings m ON(m.meeting_id=ms.meeting_id)		 "
							+ " WHERE  b.approval_action_id = 6   " + " AND a.deleted_flag='0'  "
							+ " AND ls.deleted_flag='0'  AND msa.approval_status=1  " + " AND msa.status='0'  "
							+ " AND ms.status='0' " + " AND m.status='0' "
							+ " AND ms.meeting_level_id=:meetingLevleId  " + "	AND m.meeting_id=:mettingId "
							+ "	AND ms.meeting_schedule_id=:meetingSheduleId AND msa.metting_auction_status=0 "
							+ " GROUP BY " + " a.application_no, " + " a.applicant_name, " + " a.mobile_no, "
							+ " d.district_name, " + " t.tahasil_name, " + " v.village_name, " + " k.khata_no, "
							+ " a.land_application_id, " + " a.email_address, " + " b.created_on ,msa.plot_no ";
				}

				logger.info(":: getLandApplicantsWithDetailsWithoutSearchFunction() Query Execution Success..!!");
				Query query = entityManager.createNativeQuery(sql).setParameter("meetingLevleId", mettingLevleId)
						.setParameter("mettingId", meetingId).setParameter("meetingSheduleId", meetingSheduleId);

				@SuppressWarnings("unchecked")
				List<Object[]> rows = query.getResultList();
				for (Object[] row : rows) {
					LandApplicantDTO dto = new LandApplicantDTO();
					dto.setApplicantNo((String) row[0]);
					dto.setApplicantName((String) row[1]);
					dto.setMobileNo((String) row[2]);
					dto.setDistrictName((String) row[3]);
					dto.setTehsilName((String) row[4]);
					dto.setMouzaName((String) row[5]);
					dto.setKhataNo((String) row[6]);
					dto.setPlotCode((String) row[7]);
					dto.setLandApplicantId((BigInteger) row[8]);
					dto.setEmailId((String) row[9]);
					dto.setCreatedOn((Date) row[10]);
					dto.setPlotNo((String) row[11]);
					resultList.add(dto);
				}
			} catch (Exception e) {
				logger.error("Error,  in getLandApplicantsDetails {} ", e.getMessage());
			} finally {
				entityManager.close();
			}

		}
		return resultList;
	}

	public List<LandApplicantDTO> getAuctionFlagData(String plotCode) {
		List<LandApplicantDTO> resultList = new ArrayList<>();
		try {
			String sql = " SELECT DISTINCT ON (a.bidder_form_m_application_number) a.bidder_form_m_application_number, "
					+ "(select full_name from public.citizen_profile_details where a.user_id=citizen_profile_details_id limit 1) as applicant_name, "
					+ "(select mobile_no from public.citizen_profile_details where a.user_id=citizen_profile_details_id limit 1) as mobile_no, "
					+ "(select (select (select (select (select dm.district_name  "
					+ "from land_bank.district_master dm where dm.district_code=tm.district_code LIMIT 1)  "
					+ "from land_bank.tahasil_master tm  where tm.tahasil_code=vm.tahasil_code LIMIT 1)  "
					+ "from land_bank.village_master vm  where vm.village_code=ki.village_code LIMIT 1)  "
					+ "from land_bank.khatian_information ki  where ki.khatian_code=pii.khatian_code LIMIT 1)   "
					+ "from land_bank.plot_information pii  where pii.plot_code=:plotCode limit 1) as district_name, "
					+ "(select (select (select (select  tm.tahasil_name "
					+ "from land_bank.tahasil_master tm  where tm.tahasil_code=vm.tahasil_code LIMIT 1)  "
					+ "from land_bank.village_master vm  where vm.village_code=ki.village_code LIMIT 1)  "
					+ "from land_bank.khatian_information ki  where ki.khatian_code=pii.khatian_code LIMIT 1)   "
					+ "from land_bank.plot_information pii  where pii.plot_code=:plotCode limit 1)as tahasil_name, "
					+ "(select (select (select  vm.village_name "
					+ "from land_bank.village_master vm  where vm.village_code=ki.village_code LIMIT 1)  "
					+ "from land_bank.khatian_information ki  where ki.khatian_code=pii.khatian_code LIMIT 1)   "
					+ "from land_bank.plot_information pii  where pii.plot_code=:plotCode limit 1)as village_name, "
					+ "(select (select ki.khata_no "
					+ "from land_bank.khatian_information ki  where ki.khatian_code=pii.khatian_code LIMIT 1)   "
					+ "from land_bank.plot_information pii  where pii.plot_code=:plotCode limit 1)as khata_no, "
					+ " (select   pii.plot_no "
					+ "from land_bank.plot_information pii  where pii.plot_code=:plotCode limit 1)AS plot_numbers, "
					+ "a.bidder_form_m_application_id, "
					+ "(select email_id from public.citizen_profile_details where a.user_id=citizen_profile_details_id limit 1) as email_address, "
					+ "a.created_on   ,apd.plot_code " + "FROM application.bidder_form_m_application a "
					+ "JOIN application.tender_auction ta ON ta.tender_auction_id = a.tender_auction_id "
//					+ "JOIN application.auction_plot ap ON ap.auction_plot_id = ta.auction_plot_id "
					+ "JOIN application.auction_plot_details apd ON ta.auction_plot_id = apd.auction_plot_details_id "
					+ "JOIN application.auction_plot ap ON ap.auction_plot_id = apd.auction_plot_id "
					+ "JOIN application.live_auction la ON(a.user_id=la.user_id and a.tender_auction_id=la.tender_auction_id) "
					+ "where apd.plot_code=:plotCode";

			logger.info(":: getLandApplicantsWithDetailsWithoutSearchFunction() Query Execution Success..!!");
			Query query = entityManager.createNativeQuery(sql).setParameter("plotCode", plotCode);

			@SuppressWarnings("unchecked")
			List<Object[]> rows = query.getResultList();
			for (Object[] row : rows) {
				LandApplicantDTO dto = new LandApplicantDTO();
				dto.setApplicantNo((String) row[0]);
				dto.setApplicantName((String) row[1]);
				dto.setMobileNo((String) row[2]);
				dto.setDistrictName((String) row[3]);
				dto.setTehsilName((String) row[4]);
				dto.setMouzaName((String) row[5]);
				dto.setKhataNo((String) row[6]);
				dto.setPlotNo((String) row[7]);
				dto.setLandApplicantId((BigInteger) row[8]);
				dto.setEmailId((String) row[9]);
				dto.setCreatedOn((Date) row[10]);
				dto.setPlotCode((String) row[11]);
				resultList.add(dto);
			}
		} catch (Exception e) {
			logger.error("Error,  in getLandApplicantsDetails {} ", e.getMessage());
		} finally {
			entityManager.close();
		}
		return resultList;
	}

	public List<LandApplicantDTO> viewCitizenApplicationHistory(BigInteger landApplicantId) {
		List<LandApplicantDTO> resultList = new ArrayList<>();
		try {
			String sql = "SELECT DISTINCT la.land_application_id,la.application_no,la.applicant_name,\r\n"
					+ "la.mobile_no,(select district_name from land_bank.district_master \r\n"
					+ "where la.district_code=district_code limit 1) as dist_name,(SELECT DISTINCT tm.tahasil_name\r\n"
					+ "FROM public.land_schedule ls JOIN land_bank.plot_information pi ON pi.plot_code = ls.plot_code \r\n"
					+ "JOIN land_bank.khatian_information ki ON ki.khatian_code = pi.khatian_code \r\n"
					+ "JOIN land_bank.village_master vm ON vm.village_code = ki.village_code \r\n"
					+ "JOIN land_bank.tahasil_master tm ON tm.tahasil_code = vm.tahasil_code \r\n"
					+ "WHERE ls.plot_code = ls.plot_code limit 1) as tahasil_name,\r\n"
					+ "(select village_name from land_bank.village_master  where la.village_code=village_code limit 1) as  village_name,\r\n"
					+ "(select khata_no from land_bank.khatian_information  where la.khatian_code=khatian_code limit 1) as  khata_no,\r\n"
					+ "G.application_status, maf.flow as application_flow,af.action_datetime, F.remark,\r\n"
					+ "F.application_status_id,pp.pending_at_role_id,la.district_code,la.tehsil_code,la.village_code FROM \r\n"
					+ "public.land_application la LEFT JOIN land_application_approval F  \r\n"
					+ "on la.land_application_id=F.land_application_id LEFT JOIN m_application_status G \r\n"
					+ "on F.application_status_id= G.application_status_id LEFT JOIN \r\n"
					+ "public.land_application_approval pp on la.land_application_id= pp.land_application_id  \r\n"
					+ "LEFT JOIN application.application_flow af ON la.land_application_id = af.land_application_id\r\n"
					+ "LEFT JOIN m_application_flow maf USING(application_flow_id)\r\n"
					+ "WHERE la.deleted_flag = '0' and la.land_application_id =:landApplicantId ORDER BY af.action_datetime ";
			logger.info(":: viewCitizenApplicationHistory() Query Execution Success..!!");
			Query query = entityManager.createNativeQuery(sql).setParameter("landApplicantId", landApplicantId);

			@SuppressWarnings("unchecked")
			List<Object[]> rows = query.getResultList();
			for (Object[] row : rows) {
				LandApplicantDTO dto = new LandApplicantDTO();
				dto.setLandApplicantId((BigInteger) row[0]);
				dto.setApplicantNo((String) row[1]);
				dto.setApplicantName((String) row[2]);
				dto.setMobileNo((String) row[3]);
				dto.setDistrictName((String) row[4]);
				dto.setTehsilName((String) row[5]);
				dto.setMouzaName((String) row[6]);
				dto.setKhataNo((String) row[7]);
				dto.setApplicationStatus((String) row[8]);
				dto.setApplicationFlow((String) row[9]);
				dto.setActionOn((Date) row[10]);
				dto.setRemark((String) row[11]);
				dto.setApplicationStatusId((Short) row[12]);
				dto.setPendingroleid((Short) row[13]);
				dto.setDistrictCode((String) row[14]);
				dto.setTahasilCode((String) row[15]);
				dto.setVillageCode((String) row[16]);

				resultList.add(dto);
			}
		} catch (Exception e) {
			logger.error("Error,  in citizen viewhistory {} ", e.getMessage());
		} finally {
			entityManager.close();
		}

		return resultList;
	}

	public List<LandApplicantDTO> getApplicationHistoryReport(Integer pageSize, Integer pageNumber) {
		List<LandApplicantDTO> resultList = new ArrayList<>();
		try {
			String sql = "SELECT DISTINCT la.land_application_id,la.application_no,la.applicant_name,"
					+ " la.mobile_no,(select district_name from land_bank.district_master "
					+ " where la.district_code=district_code limit 1) as dist_name,(SELECT DISTINCT tm.tahasil_name "
					+ " FROM public.land_schedule ls JOIN land_bank.plot_information pi ON pi.plot_code = ls.plot_code "
					+ " JOIN land_bank.khatian_information ki ON ki.khatian_code = pi.khatian_code "
					+ " JOIN land_bank.village_master vm ON vm.village_code = ki.village_code "
					+ " JOIN land_bank.tahasil_master tm ON tm.tahasil_code = vm.tahasil_code"
					+ " WHERE ls.plot_code = ls.plot_code limit 1) as tahasil_name,"
					+ " (select village_name from land_bank.village_master  where la.village_code=village_code limit 1) as  village_name,"
					+ " (select khata_no from land_bank.khatian_information  where la.khatian_code=khatian_code limit 1) as  khata_no,"
					+ " G.application_status, maf.flow as application_flow,F.action_on, F.remark,"
					+ " F.application_status_id,pp.pending_at_role_id,la.district_code,la.tehsil_code,la.village_code FROM "
					+ " public.land_application la LEFT JOIN land_application_approval F "
					+ " on la.land_application_id=F.land_application_id LEFT JOIN m_application_status G"
					+ " on F.application_status_id= G.application_status_id LEFT JOIN "
					+ " public.land_application_approval pp on la.land_application_id= pp.land_application_id "
					+ " LEFT JOIN application.application_flow af ON la.land_application_id = af.land_application_id"
					+ " LEFT JOIN m_application_flow maf USING(application_flow_id)"
					+ " WHERE la.deleted_flag = '0' ORDER BY la.mobile_no LIMIT :pageSize OFFSET :offset ";

			Integer offset = (pageNumber - 1) * pageSize;

			Query query = entityManager.createNativeQuery(sql).setParameter("pageSize", pageSize).setParameter("offset",
					offset);

			@SuppressWarnings("unchecked")
			List<Object[]> rows = query.getResultList();
			for (Object[] row : rows) {
				LandApplicantDTO dto = new LandApplicantDTO();

				dto.setLandApplicantId((BigInteger) row[0]);
				dto.setApplicantNo((String) row[1]);
				dto.setApplicantName((String) row[2]);
				dto.setMobileNo((String) row[3]);
				dto.setDistrictName((String) row[4]);
				dto.setTehsilName((String) row[5]);
				dto.setMouzaName((String) row[6]);
				dto.setKhataNo((String) row[7]);
				dto.setApplicationStatus((String) row[8]);
				dto.setApplicationFlow((String) row[9]);
				dto.setActionOn((Date) row[10]);
				dto.setRemark((String) row[11]);
				dto.setApplicationStatusId((Short) row[12]);
				dto.setPendingroleid((Short) row[13]);
				dto.setDistrictCode((String) row[14]);
				dto.setTahasilCode((String) row[15]);
				dto.setVillageCode((String) row[16]);

				resultList.add(dto);
			}
		} catch (DataAccessException e) {
			log.error("Error retrieving application history report: {}", e.getMessage());
			throw new DataAccessException("Error occurred while fetching application history report.");
		}

		return resultList;
	}

	public Integer getCountOfApplicationHistoryReport() {

		String sql = "SELECT COUNT(*) FROM ("
				+ " SELECT DISTINCT la.land_application_id, la.application_no, la.applicant_name,"
				+ " la.mobile_no,  (SELECT district_name FROM land_bank.district_master "
				+ " WHERE la.district_code = district_code LIMIT 1) AS dist_name,"
				+ " (SELECT DISTINCT tm.tahasil_name  FROM public.land_schedule ls "
				+ " JOIN land_bank.plot_information pi ON pi.plot_code = ls.plot_code "
				+ " JOIN land_bank.khatian_information ki ON ki.khatian_code = pi.khatian_code "
				+ "  JOIN land_bank.village_master vm ON vm.village_code = ki.village_code "
				+ " JOIN land_bank.tahasil_master tm ON tm.tahasil_code = vm.tahasil_code "
				+ " WHERE ls.plot_code = ls.plot_code LIMIT 1) AS tahasil_name,"
				+ " (SELECT village_name FROM land_bank.village_master  "
				+ " WHERE la.village_code = village_code LIMIT 1) AS village_name,"
				+ " (SELECT khata_no FROM land_bank.khatian_information  "
				+ " WHERE la.khatian_code = khatian_code LIMIT 1) AS khata_no,"
				+ " G.application_status, maf.flow AS application_flow, F.action_on, F.remark,"
				+ " F.application_status_id, pp.pending_at_role_id, la.district_code, la.tehsil_code, la.village_code "
				+ " FROM public.land_application la "
				+ " LEFT JOIN land_application_approval F ON la.land_application_id = F.land_application_id "
				+ " LEFT JOIN m_application_status G ON F.application_status_id = G.application_status_id"
				+ " LEFT JOIN public.land_application_approval pp ON la.land_application_id = pp.land_application_id "
				+ " LEFT JOIN application.application_flow af ON la.land_application_id = af.land_application_id "
				+ " LEFT JOIN m_application_flow maf USING(application_flow_id) "
				+ " WHERE la.deleted_flag = '0'   ORDER BY la.mobile_no) AS subquery; ";

		BigInteger count = (BigInteger) entityManager.createNativeQuery(sql).getSingleResult();

		return count.intValue();
	}

	public class DataAccessException extends RuntimeException {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public DataAccessException(String message) {
			super(message);
		}
	}

}
