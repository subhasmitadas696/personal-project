/**
 * 
 */
package com.csmtech.sjta.service;

import java.math.BigInteger;

import com.csmtech.sjta.dto.AppliedLandPaginationDTO;
import com.csmtech.sjta.dto.LandAppResponseStructureDTO;

/**
 * @author prasanta.sethi
 */
public interface AppliedLandApplicationService {

	AppliedLandPaginationDTO getLandApplicantDetailsPage(Integer createdBy, Integer pageNumber, Integer pageSize);

	LandAppResponseStructureDTO findAllDetailsBylandApplicantId(BigInteger landApplicantId);

	Integer cancelApplication(BigInteger landApplicantId, String remark, Integer userRoleId);

}
