package com.csmtech.sjta.service;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.csmtech.sjta.dto.AddOfficerDTO;
import com.csmtech.sjta.dto.UserDetails;
import com.csmtech.sjta.entity.Department;
import com.csmtech.sjta.entity.RoleEntity;
import com.csmtech.sjta.entity.User;

public interface OfficerRegistrationService {
	Integer checkExistingRecord(String mobileNo, String userName);

	Integer registerOfficer(UserDetails officerDTO) throws NoSuchAlgorithmException;

	List<RoleEntity> getAll();

	List<User> viewOfficer();

	List<AddOfficerDTO> getOfficerInfo(Integer pageNumber, Integer pageSize, String fullName);

	Integer updateDetails(String newfullName, String newmobileNo, String newemailId, String newuserName,
			BigInteger departmentId, BigInteger roleId, BigInteger newupdatedBy, BigInteger createdBy,
			BigInteger userId);

	Integer deleteRecord(BigInteger createdBy, BigInteger userId);

	Integer getTotalOfficerCount(String fullName);

	List<Department> getAllDept();

	List<RoleEntity> getAllRoleByDepartment(Short departmentId);

	List<AddOfficerDTO> getOfficerDetails();

	List<User> checkOffficerRecord(String userName, String mobileNo);

}
