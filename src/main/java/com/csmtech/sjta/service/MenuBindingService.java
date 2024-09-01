package com.csmtech.sjta.service;

import java.util.List;

import com.csmtech.sjta.dto.ModuleMenuDataDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface MenuBindingService {

//	public List<ModuleMenuDataDTO> getModuleAndMenuByUserId(long userId, String userType);
	public List<ModuleMenuDataDTO> getModuleAndMenuByUserId(short roleId) throws JsonMappingException, JsonProcessingException;

}
