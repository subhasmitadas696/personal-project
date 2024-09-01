package com.csmtech.sjta.service;

import java.util.List;
import com.csmtech.sjta.dto.CitizenInfoDTO;
import com.csmtech.sjta.dto.UserDetailsDTO;
import com.csmtech.sjta.entity.LandAppRegistrationEntity;

/**
 * @Auth Prasanta Kumar Sethi
 */
public interface ShowCitizenInfoService {

	 List<CitizenInfoDTO> getCitizenInfo();


	
	public List<UserDetailsDTO> searchUsers(String searchKeyword);



	Integer getTotalApplicantCount();



	List<UserDetailsDTO> getSearchUserDetailsPage(String searchKeyword, Integer pageNumber, Integer pageSize);



	List<UserDetailsDTO> getUserDetailsPage(Integer pageNumber, Integer pageSize);



	Integer getSearchedApplicantCount(String searchKeyword);
}
