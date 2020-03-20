package com.lxn.community.community.service;

import com.lxn.community.community.bean.Question;
import com.lxn.community.community.dto.QuestionDto;

import java.util.List;

public interface QuestionService {
    int insertQuestion(Question question);


    List<QuestionDto> listQuestion();
}
