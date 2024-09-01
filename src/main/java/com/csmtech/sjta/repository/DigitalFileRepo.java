package com.csmtech.sjta.repository;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class DigitalFileRepo {

	private static final Logger logger = LoggerFactory.getLogger(DigitalFileRepo.class);

	@PersistenceContext
	EntityManager entityManager;

	@Transactional
	public Integer saveFile(BigInteger digitalInformationId, String txtKhatianCode, String txtKhataNo,
			String selDistrictCode, String selVillageCode, String selTahasilCode, Integer createdBy, Date createdOn,
			String newFileDoc) {
		String sqlQuery = "INSERT INTO land_bank.khatian_digital_information (digital_information_id, khatian_code, khata_no, district_code, village_code, tahasil_code, created_by, created_date_time,  "
				+ "digital_file) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ";

		Date currentDate = new Date();

		try {

			return entityManager.createNativeQuery(sqlQuery).setParameter(1, digitalInformationId)
					.setParameter(2, txtKhatianCode).setParameter(3, txtKhataNo).setParameter(4, selDistrictCode)
					.setParameter(5, selVillageCode).setParameter(6, selTahasilCode).setParameter(7, createdBy)
					.setParameter(8, currentDate).setParameter(9, newFileDoc).executeUpdate();

		} catch (Exception e) {
			logger.error(e.getMessage());
			return -1;
		} finally {
			if (entityManager.isOpen()) {
				entityManager.close();
			}
		}
	}
}
