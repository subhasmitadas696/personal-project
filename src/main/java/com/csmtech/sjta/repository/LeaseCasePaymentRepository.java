package com.csmtech.sjta.repository;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.csmtech.sjta.entity.LeaseCasePaymantEntity;

public interface LeaseCasePaymentRepository extends JpaRepository<LeaseCasePaymantEntity, Serializable> {
	
	@Query(value = "select lease_case_payment_id ,lease_case_plot_id,mr_no,mr_date,amount,rsd_no ,rsd_date,created_by "
	+ "from application.lease_case_payment where lease_case_plot_id=:leaseCasePlotId ORDER BY lease_case_payment_id DESC OFFSET :offset  limit :limit ",nativeQuery = true)
	public List<LeaseCasePaymantEntity> getPaymantWithPlotId(@Param("leaseCasePlotId") BigInteger leaseCasePlotId,@Param("limit") Integer limit,@Param("offset") Integer offset);

	@Query(value = "select count(*) from application.lease_case_payment where lease_case_plot_id=:leaseCasePlotId ",nativeQuery = true)
	public BigInteger countPaymantRecord(@Param("leaseCasePlotId") BigInteger leaseCasePlotId);
}
