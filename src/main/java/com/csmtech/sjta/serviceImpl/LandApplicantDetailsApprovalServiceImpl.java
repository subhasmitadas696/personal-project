package com.csmtech.sjta.serviceImpl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.LandApplicantDetailsUPDTO;
import com.csmtech.sjta.repository.LandApplicantDetailsApprovalStageRepository;
import com.csmtech.sjta.service.LandApplicantDetailsApprovalService;

@Service
public class LandApplicantDetailsApprovalServiceImpl implements LandApplicantDetailsApprovalService {

	@Autowired
	private LandApplicantDetailsApprovalStageRepository repo;

	@Override
	public List<LandApplicantDetailsUPDTO> getLandApplicantsWithDetails(String actionId) {
		return Collections.emptyList();
	}

}
