package com.csmtech.sjta.repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.dto.AddOfficerDTO;
import com.csmtech.sjta.dto.TenderAdvertisementDTO;
import com.csmtech.sjta.entity.TenderAndAdvertizeEntity;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class TenderAndAdvertizeClassRepository {

	@Autowired
	@PersistenceContext
	private EntityManager entity;

	@Transactional
	public Integer saveRecord(Integer tenderTypeId, String title, String filepath, Date startDate, Date closeDate,
			Integer districtId, Integer tehsilId, Integer mouzaId, Integer khataNoId, Integer plotNoId,
			Integer createdBy) {
		String insertQuery = "INSERT INTO public.tender_advertisement "
				+ "(tender_type_id, title, upload_doc, start_date, close_date, district_id, tehsil_id, mouza_id, katha_no_id, plot_no_id,  created_by ) "
				+ "VALUES (:tenderTypeId, :title, :filepath, :startDate,"
				+ " :closedate, :districtId, :tehsilId, :mouzaId, :khataNoId, :plotNoId, :createdBy )";

		Integer save = entity.createNativeQuery(insertQuery).setParameter("tenderTypeId", tenderTypeId)
				.setParameter("title", title).setParameter("filepath", filepath).setParameter("startDate", startDate)
				.setParameter("closedate", closeDate).setParameter("districtId", districtId)
				.setParameter("tehsilId", tehsilId).setParameter("mouzaId", mouzaId)
				.setParameter("khataNoId", khataNoId).setParameter("plotNoId", plotNoId)
				.setParameter("createdBy", createdBy).executeUpdate();
		entity.close();
		return save;
	}

	@Transactional
	public Integer updateRecord(Integer tenderTypeId, String title, String filepath, Date startDate, Date closeDate,
			Integer districtId, Integer tehsilId, Integer mouzaId, Integer khataNoId, Integer plotNoId,
			Integer updatedBy, BigInteger tenderAdvertisementId) {
		String updateQuery = "UPDATE public.tender_advertisement SET "
				+ "tender_type_id = :tenderTypeId, title = :title, upload_doc = :filepath, start_date = :startDate, close_date = :closeDate, district_id = :districtId, tehsil_id = :tehsilId, "
				+ "mouza_id = :mouzaId, katha_no_id = :khataNoId, plot_no_id = :plotNoId, updated_by = :updatedBy "
				+ "WHERE tender_advertiesment_id = :tenderAdvertisementId";

		Integer status = entity.createNativeQuery(updateQuery).setParameter("tenderTypeId", tenderTypeId)
				.setParameter("title", title).setParameter("filepath", filepath).setParameter("startDate", startDate)
				.setParameter("closeDate", closeDate).setParameter("districtId", districtId)
				.setParameter("tehsilId", tehsilId).setParameter("mouzaId", mouzaId)
				.setParameter("khataNoId", khataNoId).setParameter("plotNoId", plotNoId)
				.setParameter("updatedBy", updatedBy).setParameter("tenderAdvertisementId", tenderAdvertisementId)
				.executeUpdate();

		entity.close();

		return status;

	}

	@Transactional
	public Integer deleteRecord(Integer createdBy, BigInteger tenderAdvertisementId) {
		entity.createNativeQuery("SET CONSTRAINTS ALL DEFERRED").executeUpdate();
		Date currentDateTime = new Date();
		Boolean status = true;

		Integer delete = entity.createNativeQuery(
				"UPDATE public.tender_advertisement SET status = :setstatus, updated_by = :setupdatedby, updated_on = :setupdatedon WHERE tender_advertiesment_id = :giventenderid")
				.setParameter("setstatus", status).setParameter("setupdatedby", createdBy.longValue())
				.setParameter("setupdatedon", currentDateTime).setParameter("giventenderid", tenderAdvertisementId)
				.executeUpdate();

		entity.close();
		return delete;
	}

	@Transactional
	public List<TenderAdvertisementDTO> findAllByTitle(Integer pageNumber, Integer pageSize, String title) {
		String query = "SELECT a.tender_advertiesment_id, t.tender_type, a.title, a.start_date, a.letter_no \r\n"
				+ "FROM tender_advertisement a JOIN m_tender_type t ON a.tender_type_id = t.tender_type_id\r\n"
				+ "where a.status = false ";

		if (title != null) {
			query = query + " and a.title ILIKE :title \r\n";
		}
		query += " LIMIT :pageSize OFFSET :offset";

		int offset = (pageNumber - 1) * pageSize;

		@SuppressWarnings("unchecked")
		List<Object[]> resultList = entity.createNativeQuery(query).setParameter("pageSize", pageSize)
				.setParameter("offset", offset).setParameter("title", "%" + title + "%").getResultList();
		entity.close();

		return transformResultList(resultList);

	}

	private List<TenderAdvertisementDTO> transformResultList(List<Object[]> resultList) {
		List<TenderAdvertisementDTO> roleInfoList = new ArrayList<>();
		for (Object[] row : resultList) {
			BigInteger tenderAdvertisementId = (BigInteger) row[0];
			String tenderType = (String) row[1];
			String title = (String) row[2];
			Date startDate = (Date) row[3];
			String letterNo = (String) row[4];

			TenderAdvertisementDTO roleInfo = new TenderAdvertisementDTO();
			roleInfo.setTenderAdvertisementId(tenderAdvertisementId);
			roleInfo.setTenderType(tenderType);
			roleInfo.setTitle(title);
			roleInfo.setStartDate(startDate);
			roleInfo.setLetterNo(letterNo);

			roleInfoList.add(roleInfo);

		}

		return roleInfoList;
	}

	public TenderAndAdvertizeEntity findByTenderAdvertisementId(BigInteger tenderAdvertisementId) {

		String query = "SELECT t.tender_type,  a.title, a.start_date, a.close_date, d.district_name,te.tahasil_name,mo.village_name,ka.khata_no,pl.plot_no, a.upload_doc,a.tender_type_id, a.district_code , a.tahasil_code, a.village_code, a.khatian_code, a.plot_code, a.letter_no  \r\n"
				+ "FROM tender_advertisement a JOIN m_tender_type t ON a.tender_type_id = t.tender_type_id\r\n"
				+ "LEFT JOIN land_bank.district_master d USING(district_code)\r\n"
				+ "LEFT JOIN land_bank.tahasil_master te USING(tahasil_code)\r\n"
				+ "LEFT JOIN land_bank.village_master mo USING(village_code)\r\n"
				+ "LEFT JOIN land_bank.khatian_information ka USING(khatian_code)\r\n"
				+ "LEFT JOIN land_bank.plot_information pl USING(plot_code)\r\n"
				+ "WHERE a.tender_advertiesment_id= :tenderAdvertisementId \r\n";
		try {
			Object[] result = (Object[]) entity.createNativeQuery(query)
					.setParameter("tenderAdvertisementId", tenderAdvertisementId).getSingleResult();

			TenderAndAdvertizeEntity tender = new TenderAndAdvertizeEntity();
			tender.setTenderTypeVal((String) result[0]);
			tender.setTxtTitle((String) result[1]);
			tender.setTxtStartDate((Date) result[2]);
			tender.setTxtCloseDate((Date) result[3]);
			tender.setDistrictVal((String) result[4]);
			tender.setTehsilVal((String) result[5]);
			tender.setMouzaVal((String) result[6]);
			tender.setKathaNoVal((String) result[7]);
			tender.setPlotNoVal((String) result[8]);
			tender.setFileUploadTenderDocument((String) result[9]);
			tender.setSelTenderType((Short) result[10]);
			tender.setSelDistrict((String) result[11]);
			tender.setSelTehsil((String) result[12]);
			tender.setSelMouza((String) result[13]);
			tender.setSelKhataNo((String) result[14]);
			tender.setSelPlotNo((String) result[15]);
			tender.setLetterNo((String) result[16]);

			return tender;
		} catch (NoResultException e) {
			return null;
		} finally {
			entity.close();
		}
	}

	@Transactional
	public List<TenderAdvertisementDTO> findAllByTitleStatusFalse(String title, Date startDate) {
		StringBuilder queryBuilder = new StringBuilder("SELECT main_query.tender_advertiesment_id, main_query.title, "
				+ "main_query.start_date, main_query.close_date, main_query.letter_no, main_query.upload_doc  "
				+ "FROM ( SELECT a.tender_advertiesment_id, a.title, a.start_date, a.close_date, a.letter_no, "
				+ "a.upload_doc FROM public.tender_advertisement a " + "WHERE a.status = false "
				+ "UNION SELECT tender_auction_id, auction_name, bid_document_downlode_start_date, bid_document_downlode_end_date, "
				+ "auction_name, null upload_doc FROM application.tender_auction  "
				+ "WHERE publish_status = '1' AND deleted_flag = false ) AS main_query WHERE 1=1 ");
		if (title != null) {
			queryBuilder.append(" AND main_query.title ILIKE :title");
		}
		if (startDate != null) {
			queryBuilder.append(" AND DATE(main_query.start_date) =:startDate");
		}
		queryBuilder.append(" ORDER BY close_date");
		Query query = entity.createNativeQuery(queryBuilder.toString());
		if (title != null) {
			query.setParameter("title", "%" + title + "%");

		}
		if (startDate != null) {
			query.setParameter("startDate", startDate);
		}
		@SuppressWarnings("unchecked")
		List<Object[]> resultList = query.getResultList();
		return transformToResultList(resultList);
	}

	private List<TenderAdvertisementDTO> transformToResultList(List<Object[]> resultList) {
		List<TenderAdvertisementDTO> roleInfoList = new ArrayList<>();
		for (Object[] row : resultList) {
			BigInteger tenderAdvertisementId = (BigInteger) row[0];
			String title = (String) row[1];
			Date startDate = (Date) row[2];
			Date closeDate = (Date) row[3];
			String letterNo = (String) row[4];
			String uploadDoc = (String) row[5];

			TenderAdvertisementDTO roleInfo = new TenderAdvertisementDTO();
			roleInfo.setTenderAdvertisementId(tenderAdvertisementId);
			roleInfo.setTitle(title);
			roleInfo.setStartDate(startDate);
			roleInfo.setCloseDate(closeDate);
			roleInfo.setLetterNo(letterNo);
			roleInfo.setUploadDoc(uploadDoc);

			roleInfoList.add(roleInfo);

		}

		return roleInfoList;
	}

	public Integer getCount(String title) {
		String query = "SELECT COUNT(*) FROM tender_advertisement WHERE status = false ";
		if (title != null) {
			query += " AND title ILIKE :title ";
		}
		BigInteger count = (BigInteger) entity.createNativeQuery(query).setParameter("title", "%" + title + "%")
				.getSingleResult();
		AddOfficerDTO dto = new AddOfficerDTO();
		dto.setCountint(count);
		entity.close();

		return count.intValue();
	}

}
