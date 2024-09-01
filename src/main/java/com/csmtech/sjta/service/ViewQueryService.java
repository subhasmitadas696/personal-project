/**
 * @author prasanta.sethi
 */

package com.csmtech.sjta.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.repository.ViewQueryRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class ViewQueryService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ViewQueryRepository queryRepository;

    public List<Object[]> getQueryResults() {
        // Write your native SQL query here
        String sqlQuery = "SELECT name, mobile_no,query FROM raise_query";
        List<Object[]> dataList = entityManager.createNativeQuery(sqlQuery).getResultList();
        
        entityManager.close();
        return dataList;
    }
}

