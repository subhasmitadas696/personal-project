package com.csmtech.sjta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csmtech.sjta.entity.TahasilMaster;

public interface TahasilMasterRepository extends JpaRepository<TahasilMaster, String> {


	List<TahasilMaster> findAllByTxtDistrictCodeOrderByTahasilName(String txtDistrictCode);

}
