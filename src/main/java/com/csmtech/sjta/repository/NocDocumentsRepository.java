package com.csmtech.sjta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import com.csmtech.sjta.entity.Noc_documents;

public interface NocDocumentsRepository extends JpaRepository<Noc_documents, Integer> {
	Noc_documents findByIntIdAndBitDeletedFlag(Integer intId, boolean bitDeletedFlag);

	@Query("From Noc_documents where deleted_flag=:bitDeletedFlag")
	List<Noc_documents> findAllByBitDeletedFlag(Boolean bitDeletedFlag);
}