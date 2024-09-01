package com.csmtech.sjta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import com.csmtech.sjta.entity.QuarryLandSchedule;

public interface QuarryLandScheduleRepository extends JpaRepository<QuarryLandSchedule, Integer> {
	QuarryLandSchedule findByIntIdAndBitDeletedFlag(Integer intId, boolean bitDeletedFlag);

	@Query("From QuarryLandSchedule where deleted_flag=:bitDeletedFlag")
	List<QuarryLandSchedule> findAllByBitDeletedFlag(Boolean bitDeletedFlag);
}