package com.csmtech.sjta.repository;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csmtech.sjta.entity.LandDocuments;

public interface LandDocumentsRepository extends JpaRepository<LandDocuments, Serializable> {

	List<LandDocuments> findByLandApplicantId(BigInteger landApplicantId);

}
