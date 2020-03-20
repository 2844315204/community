package com.lxn.community.community.mapper;

import com.lxn.community.community.bean.Question;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface QuestionMapper {
    int insertQuestion(Question question);

    List<Question> listQuestion();
}
