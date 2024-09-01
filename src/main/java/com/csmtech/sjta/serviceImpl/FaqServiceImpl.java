/**
 * 
 */
package com.csmtech.sjta.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.FaqDto;
import com.csmtech.sjta.repository.FaqRepository;
import com.csmtech.sjta.service.FaqService;

/**
 * @author prasanta.sethi
 */
@Service
public class FaqServiceImpl implements FaqService {

	@Autowired
	private FaqRepository faqRepository;

	@Override
	public List<FaqDto> getAllQuestionsAndAnswers() {
		List<Object[]> results = faqRepository.findAllQuestionsAndAnswers();
		List<FaqDto> faqDtoList = new ArrayList<>();

		for (Object[] row : results) {
			String question = (String) row[0];
			String answer = (String) row[1];
			FaqDto faqDto = new FaqDto(question, answer);
			faqDtoList.add(faqDto);
		}

		return faqDtoList;

	}

}
