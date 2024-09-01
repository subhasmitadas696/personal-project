package com.csmtech.sjta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csmtech.sjta.entity.ApprovalConfigurationEntity;

public interface ApprovalConfigurationRepository extends JpaRepository<ApprovalConfigurationEntity, Short> {
}

