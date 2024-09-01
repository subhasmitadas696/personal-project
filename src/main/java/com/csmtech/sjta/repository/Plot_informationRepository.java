package com.csmtech.sjta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import com.csmtech.sjta.entity.Plot_information;

public interface Plot_informationRepository extends JpaRepository<Plot_information, String> {

	@Query("From Plot_information where (:selKhatianCode='0' or selKhatianCode like CONCAT('%',:selKhatianCode,'%')) and (:txtPlotCode='0' or txtPlotCode like CONCAT('%',:txtPlotCode,'%'))  and (:txtPlotNo='0' or txtPlotNo like CONCAT('%',:txtPlotNo,'%')) ORDER BY txtPlotNo ")
	List<Plot_information> findAll(String selKhatianCode, String txtPlotCode, String txtPlotNo);

	@Query("From Plot_information where (:selKhatianCode='0' or selKhatianCode like CONCAT('%',:selKhatianCode,'%')) ORDER BY txtPlotNo ")
	List<Plot_information> findBySelKhatianCode(String selKhatianCode);

	@Query("From Plot_information where(:txtPlotNo='0' or txtPlotNo like CONCAT('%',:txtPlotNo,'%')) and selKhatianCode=:selKhatianCode ORDER BY txtPlotNo ")
	List<Plot_information> findByTxtPlotNo(String txtPlotNo, String selKhatianCode);

	@Query(value = "select count(*) from land_bank.plot_information", nativeQuery = true)
	Integer countByAll();

	Plot_information findByTxtPlotCode(String txtPlotCode);

	@Query(value = "Select plot_code,plot_no,kissam,area_acre from land_bank.plot_information where khatian_code =:txtKhatianCode", nativeQuery = true)
	List<Object[]> findBytxtKhatianCode(String txtKhatianCode);

	@Query(value = "SELECT distinct(kissam) from land_bank.plot_information Order By kissam", nativeQuery = true)
	List<String> getKissam();

	@Query(value = "select count(plot_no) as total_plot,sum(area_acre) as total_area from land_bank.plot_information where khatian_code =:txtKhatianCode", nativeQuery = true)
	List<Object[]> findTotal(String txtKhatianCode);

	@Query(value = "Select plot_code,plot_no,kissam,area_acre from land_bank.plot_information where khatian_code =:txtKhatianCode AND  plot_no LIKE concat ('%',:txtPlotNo,'%') ", nativeQuery = true)
	List<Object[]> findBytxtKhatianCodeOfPlotNo(String txtKhatianCode, String txtPlotNo);

}