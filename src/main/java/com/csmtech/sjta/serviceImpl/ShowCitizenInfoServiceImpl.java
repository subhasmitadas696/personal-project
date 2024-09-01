/**
 * 
 */
package com.csmtech.sjta.serviceImpl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.CitizenInfoDTO;
import com.csmtech.sjta.dto.UserDetailsDTO;
import com.csmtech.sjta.repository.ShowCitizenDetailsNativeRepository;
import com.csmtech.sjta.repository.ShowCitizenDetailsRepository;
import com.csmtech.sjta.service.ShowCitizenInfoService;

/**
 * @Auth Prasanta Kumar Sethi
 */
@Service
public class ShowCitizenInfoServiceImpl implements ShowCitizenInfoService {

	@Autowired
	private ShowCitizenDetailsNativeRepository showCitizenDetailsNativeRepository;

	@Autowired
	ShowCitizenDetailsRepository repo;

	@Override
	public List<CitizenInfoDTO> getCitizenInfo() {
		return showCitizenDetailsNativeRepository.getActiveUsers();
	}

	public List<UserDetailsDTO> searchUsers(String searchKeyword) {
		List<Object[]> resultList = repo
				.searchByMobileNoContainingIgnoreCaseOrFullNameContainingIgnoreCase(searchKeyword);
		List<UserDetailsDTO> userDtoList = new ArrayList<>();

		for (Object[] result : resultList) {
			UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
			userDetailsDTO.setUserId((BigInteger) result[0]);
			userDetailsDTO.setUserType((String) result[1]);
			userDetailsDTO.setFullName((String) result[2]);
			userDetailsDTO.setMobileNo((String) result[3]);
			userDetailsDTO.setEmailId((String) result[4]);
			userDtoList.add(userDetailsDTO);
		}

		return userDtoList;
	}

	@Override
	public Integer getTotalApplicantCount() {
		return showCitizenDetailsNativeRepository.getTotalUser();
	}

	@Override
	public List<UserDetailsDTO> getSearchUserDetailsPage(String searchKeyword, Integer pageNumber, Integer pageSize) {
		return showCitizenDetailsNativeRepository.getSearchUserDetails(searchKeyword, pageNumber, pageSize);
	}

	@Override
	public List<UserDetailsDTO> getUserDetailsPage(Integer pageNumber, Integer pageSize) {
		return showCitizenDetailsNativeRepository.getUserDetails(pageNumber, pageSize);
	}

	@Override
	public Integer getSearchedApplicantCount(String searchKeyword) {
		return showCitizenDetailsNativeRepository.getSearchedUser(searchKeyword);
	}

}
