/**
 * @author prasanta.sethi
 */
package com.csmtech.sjta.service;

import java.math.BigInteger;
import java.util.List;

import com.csmtech.sjta.dto.QueryDetailsDTO;

public interface QueryService {

	public String insertQuery(BigInteger id, String name, String mobileNo, String query);

	public List<QueryDetailsDTO> getQueryDetailsById(String searchKeyword, Integer pageNumber, Integer pageSize);

	List<QueryDetailsDTO> getQueryDetailsBySearch();

	public List<QueryDetailsDTO> getQueryDetailsSearch(Integer pageNumber, Integer pageSize);

	public Integer getTotalQueryCount(String searchKeyword);

	public Integer getTotalQueryCountAll();
}
