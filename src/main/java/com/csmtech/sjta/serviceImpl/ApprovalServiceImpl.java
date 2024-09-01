package com.csmtech.sjta.serviceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.ApprovalDTO;
import com.csmtech.sjta.entity.ApprovalActionEntity;
import com.csmtech.sjta.entity.ApprovalConfigurationEntity;
import com.csmtech.sjta.entity.RoleEntity;
import com.csmtech.sjta.repository.ApprovalActionsRepository;
import com.csmtech.sjta.repository.ApprovalConfigurationRepository;
import com.csmtech.sjta.repository.RoleRepository;
import com.csmtech.sjta.service.ApprovalService;

@Service
public class ApprovalServiceImpl implements ApprovalService {

	@Autowired
	private ApprovalConfigurationRepository configRepository;

	@Autowired
	private RoleRepository rolesRepository;

	@Autowired
	private ApprovalActionsRepository actionsRepository;

	@Override
	public List<ApprovalDTO> getApprovalData() {
		List<ApprovalConfigurationEntity> configList = configRepository.findAll();

		List<ApprovalDTO> result = new ArrayList<>();

		for (ApprovalConfigurationEntity config : configList) {
			List<Short> actionIds = Arrays.stream(config.getApprovalActionIds().split(",")).map(Short::valueOf)
					.collect(Collectors.toList());

			List<ApprovalActionEntity> actions = actionsRepository.findAllById(actionIds);

			for (ApprovalActionEntity action : actions) {
				ApprovalDTO dto = new ApprovalDTO();
				dto.setApprovalType(config.getApprovalType());
				dto.setRoleName(rolesRepository.findById(config.getRoleId()).orElse(new RoleEntity()).getRoleName());
				dto.setApprovalAction(action.getApprovalAction());
				result.add(dto);
			}
		}

		return result;
	}
}
