package com.csmtech.sjta.serviceImpl;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.entity.CitizenProfileEntity;
import com.csmtech.sjta.repository.CitizenProfileRepository;
import com.csmtech.sjta.service.CitizenService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CitizenServiceImpl implements CitizenService {

	@Autowired
	private CitizenProfileRepository profileRepo;

	@Override
	public CitizenProfileEntity saveDetails(CitizenProfileEntity profile) {
		log.info("inside service of savedetails ");
		log.info("profile" + profile);

		if (profile.getCitizenProfileDetailsId() != null) {
			CitizenProfileEntity existingProfile = profileRepo.findByCitizenId(profile.getCitizenProfileDetailsId());

			if (existingProfile == null) {
				log.info("before save");
				return profileRepo.save(profile);
			} else {
				return profileRepo.save(existingProfile);
			}
		}
		return null;
	}

	@Override
	public CitizenProfileEntity findByCitizenId(BigInteger userId) {
		return profileRepo.findByCitizenId(userId);
	}

}
