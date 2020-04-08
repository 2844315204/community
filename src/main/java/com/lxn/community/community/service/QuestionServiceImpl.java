package com.lxn.community.community.service;

import com.lxn.community.community.bean.Question;
import com.lxn.community.community.bean.QuestionExample;
import com.lxn.community.community.bean.User;
import com.lxn.community.community.dto.PageDto;
import com.lxn.community.community.dto.QuestionDto;
import com.lxn.community.community.dto.QuestionQueryDto;
import com.lxn.community.community.exception.CustomizeException;
import com.lxn.community.community.exception.ICustomizeErrorCode;
import com.lxn.community.community.mapper.QuestionExMapper;
import com.lxn.community.community.mapper.QuestionMapper;
import com.lxn.community.community.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl {

    @Autowired(required = false)
    QuestionMapper questionMapper;
    @Autowired(required = false)
    QuestionExMapper questionExMapper;
    @Autowired(required = false)
    UserMapper userMapper;

//    首页展示
    public PageDto listQuestion(String search, Integer page, Integer size) {
        if (StringUtils.isNotBlank(search)){
            String[] searchs = StringUtils.split(search, " ");
            search=Arrays.stream(searchs).collect(Collectors.joining("|"));
        }
        if (search==""){
            search=null;
        }
        PageDto pageDto = new PageDto();
        Integer totalPage;
        QuestionQueryDto questionQueryDto = new QuestionQueryDto();
        questionQueryDto.setSearch(search);
        Integer totalcount = questionExMapper.countBySearch(questionQueryDto);//数据总数
        //        判断多少页
        totalPage=totalcount % size==0 ? totalcount / size : totalcount / size+1;


//      判断页面数是否越界
        if (page<1){
            page=1;
        }
        if (totalPage>0){
            if (page>totalPage){
                page=totalPage;
            }
        }

        pageDto.setPagination(totalPage,page);

        //行偏移的数量  limit 2,3  检索 3-5行  mysql初始下标0
        Integer offset=size*(page-1);
        QuestionExample example = new QuestionExample();
        example.setOrderByClause("gmt_create desc");
        questionQueryDto.setSize(size);
        questionQueryDto.setPage(offset);
//        List<Question> questions = questionMapper.selectByExampleWithRowbounds(example,new RowBounds(offset,size));
        List<Question> questions = questionExMapper.selectBySearch(questionQueryDto);

        List<QuestionDto> questionDtoList=new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDto questionDto = new QuestionDto();
            BeanUtils.copyProperties(question,questionDto);
            questionDto.setUser(user);
            questionDtoList.add(questionDto);
        }
        pageDto.setData(questionDtoList);
        return pageDto ;
    }


    //  个人页面展示
    public PageDto   selectId(Long userId, Integer page, Integer size) {

        PageDto pageDto = new PageDto();
        Integer totalPage;
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userId);
        Integer totalcount = (int)questionMapper.countByExample(questionExample);//数据总数
        //        判断多少页
        totalPage=totalcount % size==0 ? totalcount / size : totalcount / size+1;

//      判断页面数是否越界
        if (page<1){
            page=1;
        }
        if (totalPage>0){
            if (page>totalPage){
                page=totalPage;
            }
        }
        pageDto.setPagination(totalPage,page);

        Integer offset=size*(page-1);//行偏移的数量  limit 2,3  检索 3-5行  mysql初始下标0
        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        List<Question> questions =
                questionMapper.selectByExampleWithRowbounds(example,new RowBounds(offset,size));

        List<QuestionDto> questionDtoList=new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDto questionDto = new QuestionDto();
            BeanUtils.copyProperties(question,questionDto);
            questionDto.setUser(user);
            questionDtoList.add(questionDto);
        }
        pageDto.setData(questionDtoList);

        return pageDto;
    }

    public QuestionDto questionId(Long id) {
        Question question= questionMapper.selectByPrimaryKey(id);
        if (question==null){
            throw new CustomizeException(ICustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDto questionDto = new QuestionDto();
        BeanUtils.copyProperties(question,questionDto);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDto.setUser(user);
        return questionDto;
    }

    public void createOrUpdate(Question question) {
        if (question.getId()==null){
//            创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setViewCount(0);
            question.setLikeCount(0);
            question.setCommentCount(0);
            questionMapper.insert(question);
        }else {
//            修改
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            QuestionExample example = new QuestionExample();
            example.createCriteria().andIdEqualTo(question.getId());
            int update = questionMapper.updateByExampleSelective(updateQuestion, example);
            if (update!=1){
                throw new CustomizeException(ICustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void incView(Long id) {
        Question record = new Question();
        record.setId(id);
        record.setViewCount(1);
        questionExMapper.incView(record);
    }

    public List<QuestionDto> selectRelated(QuestionDto questionDto) {
        if (StringUtils.isBlank(questionDto.getTag())){
            return new ArrayList<>();
        }

        String[] tags = StringUtils.split(questionDto.getTag(), ",");
//        遍历出来的tags以“|”分割包装
        String regxTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(questionDto.getId());
        question.setTag(regxTag);
        List<Question> questions = questionExMapper.selectRelated(question);
        List<QuestionDto> questionDtos = questions.stream().map(q -> {
            QuestionDto question1 = new QuestionDto();
            BeanUtils.copyProperties(q,question1);
            return question1;
        }).collect(Collectors.toList());
        return questionDtos;
    }
}
