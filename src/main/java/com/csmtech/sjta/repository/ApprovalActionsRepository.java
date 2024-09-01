package com.csmtech.sjta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csmtech.sjta.entity.ApprovalActionEntity;

public interface ApprovalActionsRepository extends JpaRepository<ApprovalActionEntity, Short> {
}
