package com.csmtech.sjta.serviceImpl;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.entity.User;
import com.csmtech.sjta.repository.OfficerRegistrationNativeRepo;
import com.csmtech.sjta.repository.UserDetailsNativeRepository;
import com.csmtech.sjta.repository.UserRepository;
import com.csmtech.sjta.service.UserService;

/*
 * @Auth  GuruPrasad
 */

@Service
public class UserServiceImpl implements UserService {

	@Value("${sjta.bcryptpassword.secretKey}")
	private String secretkey;

	@Autowired
	private UserRepository userDetailsRepository;

	@Autowired
	private UserDetailsNativeRepository usernativeRepo;

	@Autowired
	private OfficerRegistrationNativeRepo officeRegisterRepo;

	@Override
	public User findByUserName(String userName) {
		return userDetailsRepository.findByUserName(userName);
	}

	@Override
	public User save(User user) {
		return userDetailsRepository.save(user);
	}

	@Override
	public User findBymobileNo(String mobileNo) {
		return userDetailsRepository.findBymobileNo(mobileNo);
	}

	@Override
	public User findByuserId(BigInteger userId) {
		return officeRegisterRepo.findDataById(userId);
	}

	@Override
	public Integer updateDetails(String newMobileNo, String newFullName, String newemailId, BigInteger newUpdatedBy,
			BigInteger userId) {
		return usernativeRepo.updateDetails(newMobileNo, newFullName, newemailId, newUpdatedBy, userId);
	}

	@Override
	public Integer updateUser(String encodedPassword, String newUpdatedBy, BigInteger userId, String userName) {
		return usernativeRepo.updateUser(encodedPassword, newUpdatedBy,userId, userName);
	}

	@Override
	public User findByuserIdFalse(BigInteger userId) {
		return userDetailsRepository.findByuserIdFalse(userId);
	}

	@Override
	public User findByuserNameFalse(String userName) {
		return userDetailsRepository.findByuserNameFalse(userName);
	}

	@Override
	public Integer updateCitizen(String encodedPassword, String newUpdatedBy, BigInteger userId, String userName) {
		return usernativeRepo.updateCitizen(encodedPassword, newUpdatedBy,userId, userName);
	}

}
