package com.forum.service;

import com.forum.model.user.dto.CommentDTO;
import com.forum.model.user.vo.CommentVO;
import com.forum.utils.PageResult;

public interface CommentService {

    PageResult<CommentVO> getCommentsByPost(Long postId, Integer pageNum, Integer pageSize);

    //PageResult is a class, while "getCommentByPost" is a method defined inside the class/interface
    //如何看待像PageResult这样的VO class与service层的class之间的关系？
    // 一个是要表现的数据的集合，是物体的集合（近似于“entity”），一个是方法的集合。方法是什么？方法是工厂流水线，是化工流程，是将物体进行处理的工序和工具
    //只是将处理材料和处理工具之间的关系变为处理材料的集合和处理工具的集合之间的关系，这就是object oriented programming

    CommentVO createComment(CommentDTO commentDTO); //呀所以说DTO和VO都是长这样的东西，都是【处理材料】的格式/集合

    void reportComment(Long postId, Long commentId); //这又是什么东西？我不会要给每一个service层方法写一个DTO吧

    void collapseComment(Long commentId); //看来只有要传数据的请求/功能才需要创建DTO啊，创建评论，创建帖子都是需要创建DTO的
    //这个collapse的业务与请求不相关，它对一条评论/帖子，在每次举报之后都会检查一次report值是否大等于5.若大等于5则将该条评论/帖子的isCollapsed状态标为true

}