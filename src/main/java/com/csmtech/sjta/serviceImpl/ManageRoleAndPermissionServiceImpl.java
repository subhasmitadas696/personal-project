package com.csmtech.sjta.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.ModuleMenuDTO;
import com.csmtech.sjta.dto.RoleDTO;
import com.csmtech.sjta.dto.SetPermissionRespones;
import com.csmtech.sjta.entity.Department;
import com.csmtech.sjta.entity.RoleEntity;
import com.csmtech.sjta.repository.DepartmentRepository;
import com.csmtech.sjta.repository.ManageRoleAndPermissionReposiory;
import com.csmtech.sjta.repository.RoleRepository;
import com.csmtech.sjta.service.ManageRoleAndPermissionService;

@Service
public class ManageRoleAndPermissionServiceImpl implements ManageRoleAndPermissionService {

	@Autowired
	private ManageRoleAndPermissionReposiory repo;

	@Autowired
	private DepartmentRepository deptRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Override
	public List<RoleDTO> getRoles() {
		return repo.getRoles();
	}

	@Override
	public List<ModuleMenuDTO> getModulesWithMenus(Integer roleId) {
		return repo.getModulesWithMenus(roleId);
	}

	@Override
	public Integer batchInsertOrUpdateSetPermissionTestR(List<SetPermissionRespones> dataToInsertOrUpdate) {
		return repo.batchInsertOrUpdateSetPermissionTestR(dataToInsertOrUpdate);
	}

	@Override
	public Integer batchUpdateSetPermissionTestR(List<SetPermissionRespones> dataToUpdate) {
		return repo.batchUpdateSetPermissionTestR(dataToUpdate);
	}

	@Override
	public List<Department> getAllDept() {
		return deptRepo.findAll();
	}

	@Override
	public List<RoleEntity> getAllRoleByDepartment(Short departmentId) {
		return roleRepo.findAllByDepartmentId(departmentId);
	}

}
