package com.forum.service;


import com.forum.model.entity.Question;
import com.forum.model.user.dto.TestAnswerDTO;
import com.forum.model.user.vo.QuestionListVO;
import com.forum.utils.PageResult;

import java.util.List;

public interface TestService {

    //questionBank是数据库中的一张表，映射到java中就是存入表中的所有question实体的集合

    //要如何表示这种概念呢？怎样的程序能在用户一点进这个页面时，就自动开始查询所有的实体，并随机选取30道题，以随机顺序打包组装成一个DTO/VO渲染到页面上？
    //同样地，postBank也是数据库中的一张表，怎样的组织架构可以让人每次获得40条postBank表中的数据也是一个好问题

    List<Question> generateQuestions();
    PageResult<QuestionListVO> getOneQuestionInQuery(int pageNumber, List<Question> questions);
    void submitAnswers(TestAnswerDTO testAnswerDTO, Long questionId);

    //至于怎么实现右边展示progress和倒计时，左边显示当前题目，这个就交由前端实现了，反正后端只需要根据一次请求来返回一整个questions的有序列表就行，当前是第几题，每题的后面是哪一题她全都不管
}