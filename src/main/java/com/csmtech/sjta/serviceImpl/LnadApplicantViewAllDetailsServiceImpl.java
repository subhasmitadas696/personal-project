package com.csmtech.sjta.serviceImpl;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.LandAppResponseStructureDTO;
import com.csmtech.sjta.repository.LandApplicantViewAllDetailsClassRepository;
import com.csmtech.sjta.service.LnadApplicantViewAllDetailsService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LnadApplicantViewAllDetailsServiceImpl implements LnadApplicantViewAllDetailsService {

	@Autowired
	private LandApplicantViewAllDetailsClassRepository repo;
	
	@Override
	public LandAppResponseStructureDTO getCombinedDataForApplicant(BigInteger applicantId) {
		log.info(" :: getCombinedDataForApplicant() method are return the result  ..!!");
		return repo.getCombinedDataForApplicant(applicantId);
	}

}
