package com.forum.service;

import com.forum.model.admin.dto.QuestionListDTO;
import com.forum.model.admin.dto.ReviewDTO;
import com.forum.model.admin.vo.AnswerSummaryVO;
import com.forum.model.entity.UserAnswerList;
import com.forum.utils.PageResult;

import java.util.List;

public interface ReviewAnswersService {

    void addQuestion(QuestionListDTO questionListDTO);

    PageResult<AnswerSummaryVO> getAnswersList(String status, Integer pageNum, Integer pageSize);

    PageResult<UserAnswerList> getAnswersByUser(Long userId);

    //在此之前还要用前端程序算出总分和能否通过并显示出来
    void submitReviewing(ReviewDTO reviewDTO);

    //批改的流程就是：进入目录界面，获取按照时间顺序排列的entrytest表单缩略栏，点击任一缩略栏后进入新页面，
    // 在这个页面自动发送请求获取一个表单（由随机抽取的问题和用户的作答组成），在每道题的右侧输入数字/点击按键以进行批改，
    // 最后点击页面最下方的提交，将输入的数字/点击的按钮合成为一个新表单去提交。
    //我还需要blocklist来记录未通过测试的用户的邮箱。这些邮箱若想进行第二次注册，会失败。
    //那么应该给通过entrytest前的用户分配用户id吗？若没通过应该连着这个id一起删掉还是将其留在数据库里，但标记为"non activated"？
    //目前先采用分配id的方案吧。
}
