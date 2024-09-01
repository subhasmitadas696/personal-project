package com.csmtech.sjta.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.csmtech.sjta.entity.SetWorkflow;

@Transactional
public interface SetWorkflowRepository extends JpaRepository<SetWorkflow, Integer> {
	@Query(value = "SELECT folderId,canvasData,createdBy AS workflowCreatedBy FROM set_workflow where folderId=:folderId AND deletedFlag=0 ", nativeQuery = true)
	List<Object[]> getAllWorkflowByFolderId(Integer folderId);

	@Modifying
	@Query(value = "UPDATE set_workflow SET deletedFlag = 1 WHERE folderId = :folderId", nativeQuery = true)
	Integer updateWorkflowByFolderId(@Param("folderId") Integer folderId);

	SetWorkflow findByFolderIdAndDeletedFlag(Integer FolderId, Boolean deletedFlag);

}
