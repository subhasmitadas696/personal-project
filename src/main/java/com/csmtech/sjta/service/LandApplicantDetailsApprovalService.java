package com.csmtech.sjta.service;

import java.util.List;

import com.csmtech.sjta.dto.LandApplicantDetailsUPDTO;

public interface LandApplicantDetailsApprovalService {
	
	public List<LandApplicantDetailsUPDTO> getLandApplicantsWithDetails(String actionId);

	
}
