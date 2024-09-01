package com.csmtech.sjta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import com.csmtech.sjta.entity.Noc_plot;

public interface NocPlotRepository extends JpaRepository<Noc_plot, Integer> {
	Noc_plot findByIntIdAndBitDeletedFlag(Integer intId, boolean bitDeletedFlag);

	@Query("From Noc_plot where deleted_flag=:bitDeletedFlag")
	List<Noc_plot> findAllByBitDeletedFlag(Boolean bitDeletedFlag);
}