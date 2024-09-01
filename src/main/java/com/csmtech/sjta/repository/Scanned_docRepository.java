package com.csmtech.sjta.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.csmtech.sjta.entity.Scanned_doc;

public interface Scanned_docRepository extends JpaRepository<Scanned_doc, Integer> {
	@Query(value = "select * from scanned_doc where deleted_flag = '0' and scanned_doc_id = :intId ", nativeQuery = true)
	Scanned_doc findByIntIdAndBitDeletedFlag(Integer intId);

//	@Query("From Scanned_doc where bitDeletedFlag=:bitDeletedFlag")
	@Query(value = "select * from scanned_doc where district_code = :districtCode and  deleted_flag = '0' order by created_on desc", nativeQuery = true)
	List<Scanned_doc> findAllByBitDeletedFlag(Pageable pageRequest, String districtCode);

	@Query(value = "select * from scanned_doc where deleted_flag = '0' order by created_on desc", nativeQuery = true)
	List<Scanned_doc> findAllByBitDeletedFlag(Pageable pageRequest);

	@Query(value = "select count(*) from scanned_doc where deleted_flag = '0' ", nativeQuery = true)
	Integer countByBitDeletedFlag();

	boolean existsByFileTypeAndSelDistrict12AndSelTehsil13AndFileNo(Integer filetype, String district, String tehsil,
			String fileno);

	@Modifying(clearAutomatically = true)
	@Query(value = "update scanned_doc set deleted_flag = '1' where scanned_doc_id = :intId ", nativeQuery = true)
	void deleteFlagById(Integer intId);

	@Modifying(clearAutomatically = true)
	@Query(value = "update scanned_doc set upload_csv = :csvFileNew where file_type = :fileType and deleted_flag = '0' ", nativeQuery = true)
	void saveMetadata(Integer fileType, String csvFileNew);

	@Query(value = "SELECT * FROM ( SELECT *, ROW_NUMBER() OVER (PARTITION BY tehsil_code, file_no \r\n"
			+ "ORDER BY created_on DESC) AS row_num \r\n"
			+ "FROM scanned_doc WHERE deleted_flag = '0' ) ranked_docs \r\n"
			+ "WHERE row_num <= 3 ORDER BY created_on DESC", nativeQuery = true)
	List<Scanned_doc> findAllData(Pageable pageRequest);

	@Query(value = "SELECT *\n" + "FROM (\n" + " SELECT *,\n"
			+ " ROW_NUMBER() OVER (PARTITION BY tehsil_code, file_no ORDER BY created_on DESC) AS row_num\n"
			+ " FROM scanned_doc\n" + " WHERE deleted_flag = '0' and district_code = :districtCode \n"
			+ ") ranked_docs\n" + "WHERE row_num <= 3\n" + "ORDER BY created_on DESC", nativeQuery = true)
	List<Scanned_doc> findAllData(Pageable pageRequest, String districtCode);

}