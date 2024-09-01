package com.csmtech.sjta.repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.dto.QueryDetailsDTO;
import com.csmtech.sjta.dto.UserDetailsDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @Auth Prasanta Kumar Sethi
 */

@Repository
@Slf4j
public class QueryRepository {

	@Autowired
	private EntityManager entityManager;

	@Transactional
	public String insertQuery(BigInteger id, String name, String mobileNo, String query) {
		String nativeQuery = "INSERT INTO raise_query (name, mobile_no, query, status) "
				+ "VALUES (:name, :mobileNo, :query, '0')";
		entityManager.createNativeQuery(nativeQuery).setParameter("name", name).setParameter("mobileNo", mobileNo)
				.setParameter("query", query).executeUpdate();
		entityManager.close();
		return nativeQuery;
	}

	public List<QueryDetailsDTO> getQueryDetailsSearch(String searchKeyword, Integer pageNumber, Integer pageSize) {

		String nativeQuery = "SELECT name, mobile_no,query FROM raise_query  "
				+ "WHERE (LOWER(mobile_no) LIKE LOWER(CONCAT('%', :searchKeyword, '%')) OR "
				+ "       LOWER(name) LIKE LOWER(CONCAT('%', :searchKeyword, '%'))) " + "AND status='0' "
				+ "ORDER BY name " + "LIMIT :pageSize " + "OFFSET :offset ";

		int offset = (pageNumber - 1) * pageSize;

		@SuppressWarnings("unchecked")
		List<Object[]> resultList = entityManager.createNativeQuery(nativeQuery)
				.setParameter("searchKeyword", "%" + searchKeyword + "%").setParameter("pageSize", pageSize)
				.setParameter("offset", offset).getResultList();

		entityManager.close();
		return transformResultList(resultList);

	}

	public List<QueryDetailsDTO> getQueryDetails(Integer pageNumber, Integer pageSize) {
		String nativeQuery = "SELECT name, mobile_no,query FROM raise_query  " + "WHERE  status='0' " + "ORDER BY name "
				+ "LIMIT :pageSize " + "OFFSET :offset ";

		int offset = (pageNumber - 1) * pageSize;

		@SuppressWarnings("unchecked")
		List<Object[]> resultList = entityManager.createNativeQuery(nativeQuery).setParameter("pageSize", pageSize)
				.setParameter("offset", offset).getResultList();

		entityManager.close();
		return transformResultList(resultList);

	}

	private List<QueryDetailsDTO> transformResultList(List<Object[]> resultList) {
		List<QueryDetailsDTO> roleInfoList = new ArrayList<>();
		for (Object[] row : resultList) {
			String fullName = (String) row[0];
			String mobileNo = (String) row[1];
			String query = (String) row[2];

			QueryDetailsDTO queryDetails = new QueryDetailsDTO();
			queryDetails.setName(fullName);
			queryDetails.setMobileno(mobileNo);
			queryDetails.setQuery(query);

			roleInfoList.add(queryDetails);

		}

		return roleInfoList;
	}

	public List<QueryDetailsDTO> getQueryDetailsSearch() {
		String nativeQuery = "SELECT name, mobile_no,query FROM raise_query where status='0' ";

		@SuppressWarnings("unchecked")
		List<Object[]> results = entityManager.createNativeQuery(nativeQuery).getResultList();

		List<QueryDetailsDTO> queryDetailsList = new ArrayList<>();
		for (Object[] result : results) {
			QueryDetailsDTO queryDetails = new QueryDetailsDTO();
			queryDetails.setName((String) result[0]);
			queryDetails.setMobileno((String) result[1]);
			queryDetails.setQuery((String) result[2]);
			queryDetailsList.add(queryDetails);
		}
		entityManager.close();
		return queryDetailsList;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<QueryDetailsDTO> getPendingQueriesSearch(String name) {
		String sqlQuery = "SELECT name, mobile_no, query FROM raise_query "
				+ "WHERE status = '0' AND name ILIKE :name  OR mobile_no ILIKE :mobileNO";

		List<Object[]> results = entityManager.createNativeQuery(sqlQuery).setParameter("name", "%" + name + "%")
				.setParameter("mobileNO", "%" + name + "%").getResultList();

		List<QueryDetailsDTO> queryDetailsList = new ArrayList<>();
		for (Object[] result : results) {
			QueryDetailsDTO queryDetails = new QueryDetailsDTO();
			queryDetails.setName((String) result[0]);
			queryDetails.setMobileno((String) result[1]);
			queryDetails.setQuery((String) result[2]);
			queryDetailsList.add(queryDetails);
		}
		entityManager.close();
		return queryDetailsList;
	}

	public Integer queryCountAll() {
		String query = "SELECT COUNT(*) FROM raise_query WHERE status = '0' ";
		BigInteger count = (BigInteger) entityManager.createNativeQuery(query).getSingleResult();
		UserDetailsDTO dto = new UserDetailsDTO();
		dto.setCountint(count);

		entityManager.close();
		return count.intValue();
	}

	public Integer queryCount(String searchKeyword) {
		String query = "SELECT COUNT(*) FROM raise_query WHERE "
				+ "(LOWER(mobile_no) LIKE LOWER(CONCAT('%', :searchKeyword, '%')) OR  LOWER(name) LIKE LOWER(CONCAT('%', :searchKeyword, '%'))) AND status='0' ";
		BigInteger count = (BigInteger) entityManager.createNativeQuery(query)
				.setParameter("searchKeyword", "%" + searchKeyword + "%").getSingleResult();
		UserDetailsDTO dto = new UserDetailsDTO();
		dto.setCountint(count);

		entityManager.close();
		return count.intValue();
	}

}
