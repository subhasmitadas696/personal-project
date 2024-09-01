package com.csmtech.sjta.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.FaqDto;
import com.csmtech.sjta.repository.FaqRepository;

/**
 * @author prasanta.sethi
 */
public interface FaqService {

	public List<FaqDto> getAllQuestionsAndAnswers();
}
