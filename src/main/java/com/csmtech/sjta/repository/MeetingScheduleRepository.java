package com.csmtech.sjta.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csmtech.sjta.entity.MeetingSchedule;

public interface MeetingScheduleRepository extends JpaRepository<MeetingSchedule, Integer> {
	@Query(" From MeetingSchedule where intId =:intId and status = '0' ")
	MeetingSchedule findByIntId(Integer intId);

	@Query(value = "From MeetingSchedule where status= '0' ")
	List<MeetingSchedule> findAllByBitDeletedFlag(Pageable pageRequest);

	@Query(value = "Select count(*) from application.meeting_schedule where status = '0' ", nativeQuery = true)
	Integer countByBitDeletedFlag();
}