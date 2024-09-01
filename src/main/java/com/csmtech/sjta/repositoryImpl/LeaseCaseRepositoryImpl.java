package com.csmtech.sjta.repositoryImpl;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.repository.LeaseCaseRepository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class LeaseCaseRepositoryImpl implements LeaseCaseRepository {
	
	@PersistenceContext
	@Autowired
	private EntityManager entity;

	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getleaseCaseAllRecord(Integer limit, Integer offset) {
		String queryforall="select lease_case_id,case_year,applicant_name,address,contact_no,document_submitted, "
				+ "case_no from application.lease_case WHERE deleted_flag='0'  OFFSET :offset  limit :limit ";
		Query query = entity.createNativeQuery(queryforall).setParameter("limit", limit).setParameter("offset", offset);
		log.info("return repo of getleaseCaseAllRecord method success..!!");
		return query.getResultList();
	}
	
	
	@Transactional
	@Override
	public BigInteger getcountFromgetleaseCaseAllRecord() {
		String sqlQuery = " select   count(*) from application.lease_case where deleted_flag='0'  ";
		Query nativeQuery = entity.createNativeQuery(sqlQuery);
		BigInteger result = (BigInteger) nativeQuery.getSingleResult();
		log.info("return repo of getcountFromgetleaseCaseAllRecord method success..!!");
		return result;
	}


	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getleaseCaseAllRecordWitjId(BigInteger caseId) {
		String withIdQuery = "select lc.lease_case_id,lc.case_year,lc.applicant_name,lc.address,lc.contact_no,lc.document_submitted,lc.case_no "
				+ ",lcp.lease_case_plot_id, "
				+ "(select district_name from land_bank.district_master dm where dm.district_code=lcp.district_code), "
				+ "(select tahasil_name from land_bank.tahasil_master tm where tm.tahasil_code=lcp.tahasil_code), "
				+ "(select village_name from land_bank.village_master vm where vm.village_code=lcp.mouza_code), "
				+ "lcp.ps_no, "
				+ "(select khata_no from land_bank.khatian_information ki  where ki.khatian_code=lcp.khata_code), "
				+ "(select plot_no from land_bank.plot_information pi where pi.plot_code=lcp.plot_code), "
				+ "total_area,applied_area,kissam,rsd_no,is_case_matter ,"
				+ "CAST((SELECT st_extent(ST_Transform(geom,3857))from land_bank.sjta_plot_boundary_view  "
				+ "where plot_code=lcp.plot_code) as varchar) as extent "
				+ "from application.lease_case lc "
				+ "JOIN application.lease_case_plot lcp ON(lcp.lease_case_id=lc.lease_case_id) "
				+ "WHERE lc.lease_case_id=:caseId ";
		Query query = entity.createNativeQuery(withIdQuery).setParameter("caseId", caseId);
		log.info("return repo of getleaseCaseAllRecordWitjId method success..!!");
		return query.getResultList();
	}


	@Transactional
	@Override
	public BigInteger getcountFromgetleaseCaseAllRecordWithId(BigInteger caseId) {
		String sqlQuery = " select count(*)\r\n" + "from application.lease_case lc\r\n"
				+ "JOIN application.lease_case_plot lcp ON(lcp.lease_case_id=lc.lease_case_id)\r\n"
				+ "JOIN application.lease_case_status lcs ON(lcs.lease_case_plot_id=lcp.lease_case_plot_id)\r\n"
				+ "WHERE lc.lease_case_id=:caseId  ";
		Query nativeQuery = entity.createNativeQuery(sqlQuery).setParameter("caseId", caseId);
		BigInteger result = (BigInteger) nativeQuery.getSingleResult();
		log.info("return repo of getcountFromgetleaseCaseAllRecordWithId method success..!!");
		return result;
	}


	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getleaseCaseAllRecordWitjLeaseCaseStatusDetails(BigInteger leaseCasePlotId) {
       String leaseCasePlotDetails="select lc.lease_case_id,lc.case_year,lc.applicant_name,lc.address,lc.contact_no,lc.document_submitted,lc.case_no\r\n"
       		+ ",lcp.lease_case_plot_id,\r\n"
       		+ "(select district_name from land_bank.district_master dm where dm.district_code=lcp.district_code),\r\n"
       		+ "(select tahasil_name from land_bank.tahasil_master tm where tm.tahasil_code=lcp.tahasil_code),\r\n"
       		+ "(select village_name from land_bank.village_master vm where vm.village_code=lcp.mouza_code),\r\n"
       		+ "-- (select police_station_name from public.m_police_station mps where mps.police_station_id=lcp.ps_no),\r\n"
       		+ "lcp.ps_no,\r\n"
       		+ "(select khata_no from land_bank.khatian_information ki  where ki.khatian_code=lcp.khata_code),\r\n"
       		+ "(select plot_no from land_bank.plot_information pi where pi.plot_code=lcp.plot_code),\r\n"
       		+ "total_area,applied_area,kissam,rsd_no,is_case_matter,\r\n"
       		+ "lcs.lease_case_status_id,field_enquiry,dlsc_date,tlsc_date,mc_date,notice_issued,consideration_money_deposited,status,remarks,slc_date \r\n"
       		+ "from application.lease_case lc\r\n"
       		+ "JOIN application.lease_case_plot lcp ON(lcp.lease_case_id=lc.lease_case_id)\r\n"
       		+ "JOIN application.lease_case_status lcs ON(lcs.lease_case_plot_id=lcp.lease_case_plot_id)\r\n"
       		+ "WHERE lcs.lease_case_plot_id=:leaseCasePlotId ";
       Query query = entity.createNativeQuery(leaseCasePlotDetails).setParameter("leaseCasePlotId", leaseCasePlotId);
		log.info("return repo of getleaseCaseAllRecordWitjId method success..!!");
		return query.getResultList();
	}
	
	
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public List<Object[]> getLeaseCaseStatus(BigInteger leaseCasePlotId) {
		String nativeQuery = "SELECT field_enquiry, dlsc_date, tlsc_date, mc_date,slc_date, notice_issued, "
				+ "consideration_money_deposited, status, remarks " 
				+ "FROM application.lease_case_status "
				+ "WHERE lease_case_plot_id = :leaseCasePlotId";
		Query query = entity.createNativeQuery(nativeQuery);
		query.setParameter("leaseCasePlotId", leaseCasePlotId);
		return query.getResultList();
	}
	
	

	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getleaseCaseAllRecordUseLikeOperator(Integer limit, Integer offset,String caseNo,String caseYear) {
		String queryforall=" select lease_case_id,case_year,applicant_name,address,contact_no,document_submitted,case_no  "
				+ "from application.lease_case   "
				+ "WHERE deleted_flag='0' AND case_year ILIKE '%"+caseYear+"%' AND case_no ILIKE '%"+caseNo+"%'   "
				+ " OFFSET :offset  limit :limit ";
		Query query = entity.createNativeQuery(queryforall).setParameter("limit", limit).setParameter("offset", offset);
		log.info("return repo of getleaseCaseAllRecord method success..!!");
		return query.getResultList();
	}
	
	@Transactional
	@Override
	public BigInteger getcountFromgetleaseCaseAllRecorduseLikeOperator(String caseNo,String caseYear) {
		String sqlQuery = " select count(*) from application.lease_case "
				+ "WHERE deleted_flag='0' AND case_year ILIKE '%"+caseYear+"%' AND case_no ILIKE '%"+caseNo+"%'   ";
		Query nativeQuery = entity.createNativeQuery(sqlQuery);
		BigInteger result = (BigInteger) nativeQuery.getSingleResult();
		log.info("return repo of getcountFromgetleaseCaseAllRecord method success..!!");
		return result;
	}

}
