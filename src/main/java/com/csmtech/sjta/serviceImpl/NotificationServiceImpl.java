package com.csmtech.sjta.serviceImpl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.NotificationDTO;
import com.csmtech.sjta.entity.Notification;
import com.csmtech.sjta.repository.NotificationRepository;
import com.csmtech.sjta.repository.NotificationsNativeRepository;
import com.csmtech.sjta.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private NotificationRepository notificationRepository;
	
	@Autowired
	private NotificationsNativeRepository notiRepo;

	Date currentDateTime = new Date();

	@Override
	public List<NotificationDTO> getAllNotifications() {
		List<Notification> notifications = notificationRepository.findAllByStatusFalse();
		List<NotificationDTO> notificationDTOs = new ArrayList<>();

		for (Notification notification : notifications) {
			NotificationDTO dto = new NotificationDTO();
			dto.setNotificationId(notification.getNotificationId());
			dto.setTitle(notification.getTitle());
			dto.setDescription(notification.getDescription());
			dto.setUploadDoc(notification.getUploadDoc());
			dto.setCreatedOn(notification.getCreatedOn());

			notificationDTOs.add(dto);
		}

		return notificationDTOs;
	}

	@Override
	public NotificationDTO getNotificationById(BigInteger notificationId) {
		Optional<Notification> notificationOptional = notificationRepository.findById(notificationId);
		if (notificationOptional.isPresent()) {
			return new NotificationDTO(notificationOptional.get());
		}
		return null;
	}

	@Override
	public void addNotification(NotificationDTO notificationDTO) {
		Notification notification = new Notification();
		notification.setTitle(notificationDTO.getTitle());
		notification.setDescription(notificationDTO.getDescription());
		notification.setUploadDoc(notificationDTO.getUploadDoc());
		notification.setStatus(false);
		notification.setCreatedBy(notificationDTO.getCreatedBy());
		notificationRepository.save(notification);
	}

	@Override
	public void updateNotification(NotificationDTO notificationDTO) {

		Optional<Notification> notificationOptional = notificationRepository
				.findById(notificationDTO.getNotificationId());
		if (notificationOptional.isPresent()) {
			Notification notification = notificationOptional.get();
			notification.setTitle(notificationDTO.getTitle());
			notification.setDescription(notificationDTO.getDescription());
			notification.setUploadDoc(notificationDTO.getUploadDoc());
			notification.setUpdatedBy(notificationDTO.getUpdatedBy());
			notification.setUpdatedOn(currentDateTime);

			notificationRepository.save(notification);
		}
	}

	@Override
	public void softDeleteNotification(NotificationDTO notificationDTO) {
		Optional<Notification> notificationOptional = notificationRepository
				.findById(notificationDTO.getNotificationId());
		if (notificationOptional.isPresent()) {
			Notification notification = notificationOptional.get();
			notification.setUpdatedBy(notificationDTO.getUpdatedBy());
			notification.setUpdatedOn(currentDateTime);
			notification.setStatus(true);
			notificationRepository.save(notification);
		}
	}

	@Override
	public List<NotificationDTO> getAllNotification(String description,Date startDate, Date endDate) {
		return notiRepo.findNotificationsByTitleAndCreatedDateRange(description,startDate,endDate);
	}

}