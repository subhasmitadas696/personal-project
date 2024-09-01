package com.csmtech.sjta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csmtech.sjta.entity.TSetWorkFlow;

public interface TSetWorkFlowRepository extends JpaRepository<TSetWorkFlow, Integer> {

	@Query(value = "select count(*) FROM t_set_workflow where serviceId= :serviceId and vchDynFilter=:dynFilterValue and vchDynFilterCtrlId=:dynFilterctrlId and deletedFlag=0", nativeQuery = true)
	Integer countFilterData(Integer serviceId, String dynFilterValue, String dynFilterctrlId);

	@Query(value = "select * from t_set_workflow where serviceId=:processId and deletedFlag=0 ", nativeQuery = true)
	TSetWorkFlow getDetailsByProcessId(int processId);

}
