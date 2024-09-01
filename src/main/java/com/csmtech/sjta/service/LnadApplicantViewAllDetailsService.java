package com.csmtech.sjta.service;

import java.math.BigInteger;

import com.csmtech.sjta.dto.LandAppResponseStructureDTO;

public interface LnadApplicantViewAllDetailsService {

	public LandAppResponseStructureDTO getCombinedDataForApplicant(BigInteger applicantId);

}
