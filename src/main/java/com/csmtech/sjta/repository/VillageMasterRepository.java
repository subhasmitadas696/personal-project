package com.csmtech.sjta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csmtech.sjta.entity.VillageMaster;

public interface VillageMasterRepository extends JpaRepository<VillageMaster, String> {

	List<VillageMaster> findAllByTahasilCodeOrderByVillageName(String tahasilCode);

}
