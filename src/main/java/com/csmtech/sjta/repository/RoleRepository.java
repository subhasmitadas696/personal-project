package com.csmtech.sjta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csmtech.sjta.entity.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, Short> {

	List<RoleEntity> findAllByDepartmentId(Short departmentId);

}
