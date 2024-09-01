package com.csmtech.sjta.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.ModuleMenuDataDTO;
import com.csmtech.sjta.repository.MenuBindingRepository;
import com.csmtech.sjta.service.MenuBindingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MenuBindingServiceImpl implements MenuBindingService {

	@Autowired
	private MenuBindingRepository menurepo;

	@Override
	public List<ModuleMenuDataDTO> getModuleAndMenuByUserId(short roleId)
			throws JsonMappingException, JsonProcessingException {
		log.info(" ::  getModuleAndMenuByUserId() Execution Are Start And End !! ");
		return menurepo.getModuleAndMenuByUserId(roleId);
	}

}
