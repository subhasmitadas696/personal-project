package com.csmtech.sjta.service;

import java.math.BigInteger;

import com.csmtech.sjta.entity.CitizenProfileEntity;

public interface CitizenService {


	CitizenProfileEntity saveDetails(CitizenProfileEntity profile);

	CitizenProfileEntity findByCitizenId(BigInteger citizenId);

}
