package com.csmtech.sjta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csmtech.sjta.entity.ManageNotification;

public interface ManageNotificationRepository extends JpaRepository<ManageNotification, Integer> {
	@Query(" From ManageNotification where intId =:intId and status = '0' ")
	ManageNotification findByIntId(Integer intId);

	@Query("From ManageNotification where bitDeletedFlag=:bitDeletedFlag")
	List<ManageNotification> findAllByBitDeletedFlag(Boolean bitDeletedFlag);

	@Query(value = "select * from m_notification where status= '0'  limit :pageSize offset :offset", nativeQuery = true)
	List<ManageNotification> findAllByBitDeletedFlagWithPagination(Integer pageSize, Integer offset);

	@Query(value = "SELECT *  FROM m_notification where (:txttitle='0' or title ilike CONCAT('%',:txttitle,'%')) and status = '0'  limit :pageSize offset :offset ", nativeQuery = true)
	List<ManageNotification> findByTxttitle(String txttitle, Integer pageSize, Integer offset);

	@Query(value = "select count(1) from m_notification where status='0' ", nativeQuery = true)
	Integer getCount();

	@Query(value = "select count(1) from m_notification where status='0' and (:txttitle='0' or title ilike CONCAT('%',:txttitle,'%')) ", nativeQuery = true)
	Integer getCountOfTitle(String txttitle);

	@Query(value = "select count(1) from m_notification where status = '0' AND publish = 'Y' AND notification_id =:id ", nativeQuery = true)
	Integer checkPublish(Integer id);

}