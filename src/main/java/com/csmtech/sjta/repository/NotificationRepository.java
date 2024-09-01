package com.csmtech.sjta.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csmtech.sjta.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, BigInteger> {

	@Query(value = "SELECT * from m_notification where status = '0' AND publish = 'Y' ORDER BY created_on DESC  LIMIT 5", nativeQuery = true)
	List<Notification> findAllByStatusFalse();

}
