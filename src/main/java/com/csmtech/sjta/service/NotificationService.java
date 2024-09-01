package com.csmtech.sjta.service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.csmtech.sjta.dto.NotificationDTO;

public interface NotificationService {
	List<NotificationDTO> getAllNotifications();

	NotificationDTO getNotificationById(BigInteger notificationId);

	void addNotification( NotificationDTO notificationDTO);

	void updateNotification(NotificationDTO notificationDTO);

	void softDeleteNotification(NotificationDTO notificationDTO);

	List<NotificationDTO> getAllNotification(String description, Date startDate, Date endDate);
}
