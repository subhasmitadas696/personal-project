package com.csmtech.sjta.service;

import java.util.List;

import com.csmtech.sjta.dto.ModuleMenuDTO;
import com.csmtech.sjta.dto.RoleDTO;
import com.csmtech.sjta.dto.SetPermissionRespones;
import com.csmtech.sjta.entity.Department;
import com.csmtech.sjta.entity.RoleEntity;

public interface ManageRoleAndPermissionService {
	
	public List<RoleDTO> getRoles();
	
	public List<ModuleMenuDTO> getModulesWithMenus(Integer roleId);
	
	public Integer batchInsertOrUpdateSetPermissionTestR(List<SetPermissionRespones> dataToInsertOrUpdate);
	
	public Integer batchUpdateSetPermissionTestR(List<SetPermissionRespones> dataToUpdate);

	public List<Department> getAllDept();

	public List<RoleEntity> getAllRoleByDepartment(Short departmentId);

}
