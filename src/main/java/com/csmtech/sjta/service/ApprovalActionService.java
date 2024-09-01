package com.csmtech.sjta.service;

import java.math.BigInteger;
import java.util.List;

import com.csmtech.sjta.dto.ApprovalActionResultDTO;
import com.csmtech.sjta.dto.ApprovalActionUpdateDTO;
import com.csmtech.sjta.dto.ApprovalDocumentDTO;

public interface ApprovalActionService {
	
	public List<ApprovalActionResultDTO> findApprovalActionsForRoleId(Long roleId);
	
	public Short updateApprovalProcess(ApprovalActionUpdateDTO approvalDto);
	
	public String messageData(Integer approvalActionId );

	public List<ApprovalDocumentDTO> findApprovaldocumentForRoleId(Long roleId);


}
