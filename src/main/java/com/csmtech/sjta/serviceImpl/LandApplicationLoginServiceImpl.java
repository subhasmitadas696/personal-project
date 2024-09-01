package com.csmtech.sjta.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.entity.LandAppRegistrationEntity;
import com.csmtech.sjta.repository.LandAppRegistrationRepository;
import com.csmtech.sjta.service.LandApplicationLoginService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LandApplicationLoginServiceImpl implements LandApplicationLoginService {

	@Autowired
	private LandAppRegistrationRepository repo;

	@Override
	public LandAppRegistrationEntity findByMobilenoAndUserType(String userName, String userType) {
		return repo.findByMobilenoAndUserType(userName, userType);
	}

	@Override
	public Integer findBymobilenoBlockOrNot(String username) {
		return repo.findBymobilenoBlockOrNot(username);
	}

	@Override
	public LandAppRegistrationEntity findByUsernameAndUserType(String userName, String userType) {
		return repo.findByUsernameAndUserType(userName, userType);
	}

	@Override
	public Integer findByusernameBlockOrNot(String userName) {
		return repo.findByusernameBlockOrNot(userName);
	}

	@Override
	public LandAppRegistrationEntity findByUsername(String userName) {
		return repo.findByUsernameNative(userName);
	}

	@Override
	public Integer findCitizenBlockOrNot(String username) {
		return repo.findCitizenBlockOrNot(username);
	}
	
	
	@Override
	public Integer updateUserBlockFlag(String username,Short value) {
		return repo.updateLoginFlagForCitizen(username,value);
	}
	
	@Override
	public Integer updateLoginFlagOfficer(String username,Short value) {
		return repo.updateLoginFlagOfficer(username,value);
	}

	@Override
	public Integer updateLoginFlagForCitizenDate(String userName) {
		return repo.updateLoginFlagForCitizenDate(userName);
	}

	@Override
	public Integer updateLoginFlagForCitizenDateForNull(String userName,Short validation) {
		return repo.updateLoginFlagForCitizenDateForNull(userName,validation);
	}

	@Override
	public Integer updateLoginFlagForOfficerDate(String userName) {
		return repo.updateLoginFlagForOfficerDate(userName);
	}

	@Override
	public Integer updateLoginFlagForOfficer(String userName, Short value) {
		return repo.updateLoginFlagForOfficer(userName, value);
	}

	@Override
	public Integer updateLoginFlagForOfficerDateForNull(String userName, Short validation) {
		return repo.updateLoginFlagForOfficerDateForNull(userName, validation);
	}
	
	

}
