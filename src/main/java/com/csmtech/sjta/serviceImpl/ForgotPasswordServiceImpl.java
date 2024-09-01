package com.csmtech.sjta.serviceImpl;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.NotificationDTO;
import com.csmtech.sjta.entity.CitizenProfileEntity;
import com.csmtech.sjta.entity.LandAppRegistrationEntity;
import com.csmtech.sjta.entity.User;
import com.csmtech.sjta.mobile.service.NotificationDetailsServiceImpl;
import com.csmtech.sjta.repository.ForgotPasswordNativeRepository;
import com.csmtech.sjta.repository.ForgotPasswordRepository;
import com.csmtech.sjta.service.ForgotPasswordService;

@Service
public class ForgotPasswordServiceImpl implements ForgotPasswordService {
	@Autowired
	private ForgotPasswordRepository forgotPasswordRepository;
	@Autowired
	private ForgotPasswordNativeRepository nativerepo;	
	@Autowired
	NotificationDetailsServiceImpl notificationDetailsServiceImpl;
	

	@Value("${sjta.bcryptpassword.secretKey}")
	private String SECRET_KEY;

	@Override
	public LandAppRegistrationEntity findByMobileNo(String mobileNo) {

		return forgotPasswordRepository.findByUsername(mobileNo);
	}

	@Override
	public Integer updateOtp(LandAppRegistrationEntity user) {
		return nativerepo.updateOtp(user.getUsername(), "123456");
	}

	@Override
	public Integer updatepassword(String newPassword, String mobileNo) {

		BigInteger count = nativerepo.countMobileUser(mobileNo);
		if(count.intValue() > 0) {
			LandAppRegistrationEntity entity = forgotPasswordRepository.mobileOfficerUser(mobileNo);
			NotificationDTO notificationDto = new NotificationDTO();
			notificationDto.setNotification("Password has been updated");
			notificationDto.setUserId(new BigInteger(entity.getId().toString()));
			notificationDto.setUserType("O");
			notificationDto.setCreatedBy(new BigInteger(entity.getId().toString()));
			notificationDetailsServiceImpl.submitNotification(notificationDto);
		}else {
			LandAppRegistrationEntity entity = forgotPasswordRepository.mobileCitizenUser(mobileNo);
			NotificationDTO notificationDto = new NotificationDTO();
			notificationDto.setNotification("Password has been updated");
			notificationDto.setUserId(new BigInteger(entity.getId().toString()));
			notificationDto.setUserType("CI");
			notificationDto.setCreatedBy(new BigInteger(entity.getId().toString()));
			notificationDetailsServiceImpl.submitNotification(notificationDto);
		}
		
		return nativerepo.updatepassword(newPassword, mobileNo);
	}

	@Override
	public String checkPassword(String newPassword, String mobileNo) {
		return forgotPasswordRepository.passwordValidate(mobileNo);
	}

}
