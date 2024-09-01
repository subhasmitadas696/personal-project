package com.csmtech.sjta.mobile.service;

import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.NotificationDTO;
import com.csmtech.sjta.mobile.dto.NotificationResponseDto;

@Service
public interface NotificationDetailsService {

	NotificationResponseDto showNotificationCount(NotificationDTO notificationDto);

	NotificationResponseDto fetchNotification(NotificationDTO notificationDto);

	NotificationResponseDto updateNotification(NotificationDTO notificationDto);

	NotificationResponseDto submitNotification(NotificationDTO notificationDto);

	NotificationResponseDto notificationRerun(NotificationDTO notificationDto);



}
