package com.lxn.community.community.mapper;

import com.lxn.community.community.bean.Question;
import com.lxn.community.community.bean.QuestionExample;
import com.lxn.community.community.dto.QuestionQueryDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

@Mapper
public interface QuestionExMapper {

    int incView(Question record);
    int extCommentCount(Question record);
    List<Question> selectRelated(Question question);

    Integer countBySearch(QuestionQueryDto questionQueryDto);

    List<Question> selectBySearch(QuestionQueryDto questionQueryDto);
}