package com.csmtech.sjta.serviceImpl;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.AddOfficerDTO;
import com.csmtech.sjta.dto.UserDetails;
import com.csmtech.sjta.entity.Department;
import com.csmtech.sjta.entity.RoleEntity;
import com.csmtech.sjta.entity.User;
import com.csmtech.sjta.repository.DepartmentRepository;
import com.csmtech.sjta.repository.OfficerRegistrationNativeRepo;
import com.csmtech.sjta.repository.OfficerRegistrationRepository;
import com.csmtech.sjta.repository.RoleRepository;
import com.csmtech.sjta.repository.UserRepository;
import com.csmtech.sjta.service.OfficerRegistrationService;
import com.csmtech.sjta.util.OtpGenerateCommonUtil;

@Service

public class OfficerRegistrationServiceImpl implements OfficerRegistrationService {

	@Value("${sjta.bcryptpassword.secretKey}")
	private String secretkey;

	@Autowired
	private OfficerRegistrationNativeRepo officeRegisterRepo;

	@Autowired
	private OfficerRegistrationRepository officerRepo;

	@Autowired
	private DepartmentRepository deptRepo;
	
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RoleRepository roleRepo;

	private static final Logger log = LoggerFactory.getLogger(OfficerRegistrationServiceImpl.class);

	@Override
	public Integer checkExistingRecord(String userName, String mobileNo) {
		log.info(userName + " "+ mobileNo);
		return officeRegisterRepo.checkExistingRecord(userName, mobileNo);
	}

	@Override
	public Integer registerOfficer(UserDetails officerDTO) throws NoSuchAlgorithmException {

		String username = officerDTO.getUserName();
		String password = officerDTO.getPassword();
		String fullName = officerDTO.getFullName();
		String mobileNo = officerDTO.getMobileNo();
		String email = officerDTO.getEmailId();
		String otp = OtpGenerateCommonUtil.generateOTP();
		BigInteger createdBy = officerDTO.getCreatedBy();
		BigInteger departmentId = officerDTO.getDepartmentId();
		BigInteger roleId = officerDTO.getRoleId();

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodedValue = encoder.encode(secretkey + password);

		String userType = null;
		if (roleId.intValue() == 12) {
			userType = "T";
		} else if (roleId.intValue() == 13) {
			userType = "C";
		} else {
			userType = "O";
		}
		return officeRegisterRepo.insertUserWithRole(username, encodedValue, fullName, mobileNo, email, otp, userType,
				createdBy, departmentId, roleId);

	}

	@Override
	public List<RoleEntity> getAll() {
		return officerRepo.findAll();
	}

	@Override
	public List<User> viewOfficer() {
		return officerRepo.viewOfficer();
	}

	@Override
	public Integer updateDetails(String newfullName, String newmobileNo, String newemailId, String newuserName,
			BigInteger departmentId, BigInteger roleId, BigInteger newupdatedBy, BigInteger userId,
			BigInteger createdBy) {

		return officeRegisterRepo.updatedetails(newfullName, newmobileNo, newemailId, newuserName, departmentId, roleId,
				newupdatedBy, userId, createdBy);
	}

	@Override
	public Integer deleteRecord(BigInteger createdBy, BigInteger userId) {
		return officeRegisterRepo.deleteRecord(createdBy, userId);
	}

	@Override
	public Integer getTotalOfficerCount(String fullName) {
		return officeRegisterRepo.getTotalOfficerCount(fullName);
	}

	@Override
	public List<AddOfficerDTO> getOfficerInfo(Integer pageNumber, Integer pageSize, String fullName) {
		return officeRegisterRepo.getOfficers(pageNumber, pageSize, fullName);
	}

	@Override
	public List<Department> getAllDept() {
		return deptRepo.findAll();
	}

	@Override
	public List<RoleEntity> getAllRoleByDepartment(Short departmentId) {
		return roleRepo.findAllByDepartmentId(departmentId);
	}

	@Override
	public List<AddOfficerDTO> getOfficerDetails() {
		return officeRegisterRepo.getOfficerDetails();
	}

	@Override
	public List<User> checkOffficerRecord(String userName, String mobileNo) {
		return userRepo.findByUserNameOrMobileNo(userName, mobileNo);
	}

}
