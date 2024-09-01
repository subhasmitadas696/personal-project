/**
 * 
 */
package com.csmtech.sjta.serviceImpl;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.AppliedLandApplicationDTO;
import com.csmtech.sjta.dto.AppliedLandPaginationDTO;
import com.csmtech.sjta.dto.LandAppResponseStructureDTO;
import com.csmtech.sjta.mobile.dto.ApplicationFlowDto;
import com.csmtech.sjta.mobile.service.CommonService;
import com.csmtech.sjta.repository.AppliedLandApplicationNativeRepository;
import com.csmtech.sjta.service.AppliedLandApplicationService;

/**
 * 
 */
@Service
public class AppliedLandApplicationServiceImpl implements AppliedLandApplicationService {

	@Autowired
	private AppliedLandApplicationNativeRepository appliedLandApplicationRepo;
	
	@Autowired
	private CommonService commonService;

	@Override
	public AppliedLandPaginationDTO getLandApplicantDetailsPage(Integer createdBy, Integer pageNumber,
			Integer pageSize) {
		List<AppliedLandApplicationDTO> respones = appliedLandApplicationRepo
				.getAppliedLandApplicantDetailsPage(createdBy, pageNumber, pageSize);
		Integer count = appliedLandApplicationRepo.getTotalLandApplicantCount(createdBy);
		return new AppliedLandPaginationDTO(respones, count);
	}

	@Override
	public LandAppResponseStructureDTO findAllDetailsBylandApplicantId(BigInteger landApplicantId) {
		return null;
	}

	// change rashmi
	@Override
	public Integer cancelApplication(BigInteger landApplicantId, String remark,Integer userRoleId) {
		//added for inserting in db only(application flow) Land Application Cancelled
		ApplicationFlowDto dto = new ApplicationFlowDto();
		dto.setLandApplicationId(landApplicantId);
		dto.setApplicationFlowId(BigInteger.valueOf(2));
		dto.setActionDateTime(new Date());
		dto.setActionRoleId(BigInteger.valueOf(userRoleId));
		commonService.saveApplicationFlow(dto);

		return appliedLandApplicationRepo.cancelLandApplication(landApplicantId, remark);
	}

}
