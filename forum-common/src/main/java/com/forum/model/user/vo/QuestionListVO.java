package com.forum.model.user.vo;

import lombok.Data;

import java.util.List;

@Data
public class QuestionListVO {
    @Data
    public static class Question {
        private Long id;
        private String description;      // 题目描述
        private String type;            // 题目类型
        private List<String> options;   // 选项（选择题才有）
    }
    private int questionNumber = 30;
    private List<Question> questions;
    //倒计时&倒计时结束时自动提交的功能可以全由前端实现！不用在这里写


}
