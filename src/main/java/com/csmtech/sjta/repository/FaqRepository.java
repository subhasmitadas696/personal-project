/**
 * @author prasanta.sethi
 */
package com.csmtech.sjta.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csmtech.sjta.entity.Faq;

public interface FaqRepository extends JpaRepository<Faq, BigInteger> {
	@Query(value = "SELECT question, answer FROM m_faq where status='0'", nativeQuery = true)
	List<Object[]> findAllQuestionsAndAnswers();
}
