package com.csmtech.sjta.serviceImpl;

/**
 * @Auth Rashmi Ranjan Jena
 */

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.csmtech.sjta.entity.LandAppRegistrationEntity;
import com.csmtech.sjta.repository.LandAppRegistrationRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LandAppLoginServiceImpl implements UserDetailsService {

	@Autowired
	private LandAppRegistrationRepository regrepository;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		log.info("::  execution start of loadUserByUsername method");
		LandAppRegistrationEntity entity = regrepository.findByUsernameNative(userName);
		log.info(":: execution end of loadUserByUsername method return to controller");
		return new org.springframework.security.core.userdetails.User(entity.getUsername(), entity.getPassword(),
				new ArrayList<>());
	}

}
