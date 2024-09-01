package com.csmtech.sjta.serviceImpl;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.QueryDetailsDTO;
import com.csmtech.sjta.repository.QueryRepository;
import com.csmtech.sjta.service.QueryService;

/**
 * @Auth Prasanta Kumar Sethi
 */

@Service
public class QueryServiceImpl implements QueryService {

	@Autowired
	private QueryRepository queryRepository;

	@Override
	public String insertQuery(BigInteger id, String name, String mobileNo, String query) {

		return queryRepository.insertQuery(id, name, mobileNo, query);
	}

	@Override
	public List<QueryDetailsDTO> getQueryDetailsById(String searchKeyword, Integer pageNumber, Integer pageSize) {
		return queryRepository.getQueryDetailsSearch(searchKeyword, pageNumber, pageSize);
	}

	@Override
	public List<QueryDetailsDTO> getQueryDetailsSearch(Integer pageNumber, Integer pageSize) {
		return queryRepository.getQueryDetails(pageNumber, pageSize);
	}

	@Override
	public List<QueryDetailsDTO> getQueryDetailsBySearch() {
		return Collections.emptyList();
	}

	@Override
	public Integer getTotalQueryCount(String searchKeyword) {
		return queryRepository.queryCount(searchKeyword);
	}
	
	@Override
	public Integer getTotalQueryCountAll() {
		return queryRepository.queryCountAll();
	}

}
