package com.csmtech.sjta.mobile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.csmtech.sjta.dto.TahasilTeamUseRequestDto;
import com.csmtech.sjta.mobile.entity.TahasilLoginEntity;

public interface TahasilMobileRepository extends JpaRepository<TahasilLoginEntity, Integer> {
	@Query("select new com.csmtech.sjta.dto.TahasilTeamUseRequestDto(t.tahasilId,t.tahasilCode, t.password) from TahasilLoginEntity t where t.tahasilCode =:tahasilCode and status = '0'")
	TahasilTeamUseRequestDto findByTahasilCode(@Param("tahasilCode") String tahasilCode);

	@Query("select otp from TahasilLoginEntity t where t.tahasilCode =:tahasilCode and t.mobileNo =:mobileNo and status = '0'")
	String findOtpByTahasilCodeAndMobileNo(@Param("tahasilCode") String tahasilCode,
			@Param("mobileNo") String mobileNo);

}
