package com.csmtech.sjta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csmtech.sjta.entity.StateMaster;

public interface StateRepository extends JpaRepository<StateMaster, String> {

}
