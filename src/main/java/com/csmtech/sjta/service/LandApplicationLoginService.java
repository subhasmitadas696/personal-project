package com.csmtech.sjta.service;

import org.springframework.data.repository.query.Param;

import com.csmtech.sjta.entity.LandAppRegistrationEntity;

public interface LandApplicationLoginService {

	LandAppRegistrationEntity findByMobilenoAndUserType(String userName, String userType);

	Integer findBymobilenoBlockOrNot(String username);

	LandAppRegistrationEntity findByUsernameAndUserType(String userName, String userType);

	Integer findByusernameBlockOrNot(String userName);

	LandAppRegistrationEntity findByUsername(String userName);

	Integer findCitizenBlockOrNot(String username);
	
	public Integer updateUserBlockFlag(String username,Short value);
	
	public Integer updateLoginFlagOfficer(String username,Short value);
	
	public  Integer updateLoginFlagForCitizenDate(String userName);
	
	public  Integer updateLoginFlagForCitizenDateForNull(String userName,Short validation);
	
	public  Integer updateLoginFlagForOfficerDate(String userName);
	
	public  Integer updateLoginFlagForOfficer(String userName,Short value);
	
	public  Integer updateLoginFlagForOfficerDateForNull(String userName,Short validation );

}
