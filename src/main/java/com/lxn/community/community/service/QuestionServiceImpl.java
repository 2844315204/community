package com.lxn.community.community.service;

import com.lxn.community.community.bean.Question;
import com.lxn.community.community.bean.User;
import com.lxn.community.community.dto.QuestionDto;
import com.lxn.community.community.mapper.QuestionMapper;
import com.lxn.community.community.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired(required = false)
    QuestionMapper questionMapper;
    @Autowired(required = false)
    UserMapper userMapper;
    @Override
    public int insertQuestion(Question question) {
        return questionMapper.insertQuestion(question);
    }

    @Override
    public List<QuestionDto> listQuestion() {
        List<Question> questions = questionMapper.listQuestion();
        List<QuestionDto> questionDtoList=new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDto questionDto = new QuestionDto();
            BeanUtils.copyProperties(question,questionDto);
            questionDto.setUser(user);
            questionDtoList.add(questionDto);
        }
        return questionDtoList;
    }
}
