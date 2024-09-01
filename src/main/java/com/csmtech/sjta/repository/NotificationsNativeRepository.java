package com.csmtech.sjta.repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.dto.NotificationDTO;
import com.csmtech.sjta.entity.ManageNotification;

@Repository
public class NotificationsNativeRepository {

	@Autowired
	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public List<NotificationDTO> findNotificationsByTitleAndCreatedDateRange(String description, Date startDate,
			Date endDate) {

		String queryBuilder = "SELECT description, created_on, upload_doc, title FROM m_notification WHERE status = '0' AND publish = 'Y'";

		if (description != null) {
			queryBuilder += " AND (description ILIKE :description OR title ILIKE :description) ";
		}

		if (startDate != null && endDate != null) {
			queryBuilder += " AND (DATE(created_on) >= :startDate AND DATE(created_on) <= :endDate) ";
		}

		queryBuilder += "ORDER BY created_on DESC";
		Query query = entityManager.createNativeQuery(queryBuilder.toString());

		if (description != null) {
			query.setParameter("description", "%" + description + "%");
		}

		if (startDate != null && endDate != null) {
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
		}

		@SuppressWarnings("unchecked")
		List<Object[]> resultList = query.getResultList();

		entityManager.close();
		return transformResultList(resultList);
	}

	private List<NotificationDTO> transformResultList(List<Object[]> resultList) {
		List<NotificationDTO> roleInfoList = new ArrayList<>();
		for (Object[] row : resultList) {
			String description = (String) row[0];
			Date createdOn = (Date) row[1];
			String uploadDoc = (String) row[2];
			String title = (String) row[3];

			NotificationDTO roleInfo = new NotificationDTO();
			roleInfo.setDescription(description);
			roleInfo.setCreatedOn(createdOn);
			roleInfo.setUploadDoc(uploadDoc);
			roleInfo.setTitle(title);

			roleInfoList.add(roleInfo);

		}

		return roleInfoList;
	}

	public Integer saveRecord(ManageNotification m_notification) {
		String sqlQuery = "INSERT INTO public.m_notification ( " + " title, description, upload_doc, created_by "
				+ " ) VALUES (" + " :title, :description, :upload_doc, :createdBy " + " ) "
				+ " RETURNING notification_id";

		Query query = entityManager.createNativeQuery(sqlQuery).setParameter("title", m_notification.getTxttitle())
				.setParameter("description", m_notification.getTxtrDescription())
				.setParameter("upload_doc", m_notification.getFileUploadDocument())
				.setParameter("createdBy", m_notification.getIntCreatedBy().longValue());

		BigInteger result = (BigInteger) query.getSingleResult();

		entityManager.close();
		return result.intValue();
	}

	@Transactional
	public ManageNotification updateRecord(ManageNotification m_notification) {
		String nativeQuery = "UPDATE public.m_notification SET " + "title = :title, " + "description = :description, "
				+ "upload_doc = :upload_doc, " + "updated_by = :updatedBy " + "WHERE notification_id = :notificationId";

		entityManager.createNativeQuery(nativeQuery).setParameter("title", m_notification.getTxttitle())
				.setParameter("description", m_notification.getTxtrDescription())
				.setParameter("upload_doc", m_notification.getFileUploadDocument())
				.setParameter("updatedBy", m_notification.getIntUpdatedBy())
				.setParameter("notificationId", m_notification.getIntId()).executeUpdate();

		entityManager.close();
		return m_notification;
	}

	@Transactional
	public Integer deleteById(Integer id, Integer updatedby) {
		entityManager.createNativeQuery("SET CONSTRAINTS ALL DEFERRED").executeUpdate();

		Date currentDate = new Date();
		String nativeQuery = "UPDATE public.m_notification SET status = CAST(:setstatus AS BIT), updated_on =:updatedOn, updated_by =:updatedBy WHERE notification_id = :notificationId ";

		Integer status = entityManager.createNativeQuery(nativeQuery).setParameter("setstatus", 1)
				.setParameter("updatedOn", currentDate).setParameter("updatedBy", updatedby)
				.setParameter("notificationId", id).executeUpdate();

		entityManager.close();
		return status;
	}

	public Integer unpublishById(Integer id, Integer updatedby) {
		entityManager.createNativeQuery("SET CONSTRAINTS ALL DEFERRED").executeUpdate();

		Date currentDate = new Date();
		Character unpublish = 'N';
		String nativeQuery = "UPDATE public.m_notification SET publish = :setpublish, updated_on =:updatedOn, updated_by =:updatedBy WHERE notification_id = :notificationId ";
		Integer status = entityManager.createNativeQuery(nativeQuery).setParameter("setpublish", unpublish)
				.setParameter("updatedOn", currentDate).setParameter("updatedBy", updatedby)
				.setParameter("notificationId", id).executeUpdate();
		entityManager.close();
		return status;
	}

	public Integer publishById(Integer id, Integer updatedby) {
		entityManager.createNativeQuery("SET CONSTRAINTS ALL DEFERRED").executeUpdate();

		Date currentDate = new Date();
		Character unpublish = 'Y';
		String nativeQuery = "UPDATE public.m_notification SET publish = :setpublish, updated_on =:updatedOn, updated_by =:updatedBy WHERE notification_id = :notificationId ";
		Integer status = entityManager.createNativeQuery(nativeQuery).setParameter("setpublish", unpublish)
				.setParameter("updatedOn", currentDate).setParameter("updatedBy", updatedby)
				.setParameter("notificationId", id).executeUpdate();
		entityManager.close();
		return status;
	}

}
