package com.csmtech.sjta.mobile.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.mobile.dto.ApplicationFlowDto;
import com.csmtech.sjta.mobile.entity.ApplicationFlowEntity;
import com.csmtech.sjta.mobile.repository.ApplicationFlowRepository;

@Service
public class CommonService {
	
	@Autowired
	ApplicationFlowRepository applicationFlowRepository;

	public void saveApplicationFlow(ApplicationFlowDto applicationFlowDto ) {
		ApplicationFlowEntity entity = new ApplicationFlowEntity();
		entity.setLandApplicationId(applicationFlowDto.getLandApplicationId());
		entity.setApplicationFlowId(applicationFlowDto.getApplicationFlowId());
		entity.setActionRoleId(applicationFlowDto.getActionRoleId());
		entity.setActionDateTime(applicationFlowDto.getActionDateTime());
		applicationFlowRepository.save(entity);
		
	}
}
