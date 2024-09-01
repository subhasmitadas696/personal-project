package com.csmtech.sjta.repository;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class BannerDataRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<Object[]> getDistrictCodes() {

		String query = "SELECT d.district_code,\r\n" + "COUNT(DISTINCT t.tahasil_code) AS tahasil,\r\n"
				+ "COUNT(DISTINCT v.village_code) AS mouza, \r\n" + "COUNT(DISTINCT p.plot_code) AS plot,\r\n"
				+ "SUM(area_acre) AS area " + "FROM land_bank.district_master d\r\n" + "LEFT JOIN\r\n"
				+ "land_bank.tahasil_master t ON t.district_code = d.district_code\r\n" + "LEFT JOIN\r\n"
				+ "land_bank.village_master v ON t.tahasil_code = v.tahasil_code\r\n" + "JOIN\r\n"
				+ "land_bank.khatian_information k ON v.village_code = k.village_code\r\n" + "JOIN\r\n"
				+ "land_bank.plot_information p ON k.khatian_code = p.khatian_code\r\n" + "GROUP BY\r\n"
				+ "d.district_code  ORDER BY d.district_name";

		List<Object[]> resultList = entityManager.createNativeQuery(query).getResultList();
		entityManager.close();
		return resultList;
	}

}
