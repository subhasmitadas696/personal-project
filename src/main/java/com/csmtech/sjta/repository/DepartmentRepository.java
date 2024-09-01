package com.csmtech.sjta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csmtech.sjta.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

}
