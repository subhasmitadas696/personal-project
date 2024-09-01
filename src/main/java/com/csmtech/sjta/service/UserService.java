package com.csmtech.sjta.service;

import java.math.BigInteger;
import java.util.Date;

import com.csmtech.sjta.dto.UserDetails;
import com.csmtech.sjta.entity.User;

/*
 * @Auth  GuruPrasad
 */

public interface UserService {

	User findByUserName(String userName);

	User save(User user);

	User findBymobileNo(String mobileNo);

	User findByuserId(BigInteger userId);

	Integer updateDetails(String newMobileNo, String newFullName, String newemailId, BigInteger newUpdatedBy,
			BigInteger userId);

	Integer updateUser(String encodedPassword, String newUpdatedBy, BigInteger userId, String userName);

	User findByuserIdFalse(BigInteger userId);

	User findByuserNameFalse(String userName);

	Integer updateCitizen(String encodedPassword, String newUpdatedBy, BigInteger userId, String userName);

}
