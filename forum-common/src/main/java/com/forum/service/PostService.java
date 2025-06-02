package com.forum.service;

import com.forum.model.user.dto.PostDTO;
import com.forum.model.user.query.PostQueryDTO;
import com.forum.model.user.vo.PostDetailVO;
import com.forum.model.user.vo.PostItemVO;
import com.forum.utils.PageResult;

import java.util.List;

public interface PostService {

    PageResult<List<PostItemVO>> getPostsByCategory(Long categoryId, PostQueryDTO postQueryDTO);

    void createPost(PostDTO postDTO);

    void reportPost(Long postId);

    PostItemVO updatePost(Long postId, PostDTO postDTO);

    PageResult<PostDetailVO> getPostDetail(Long postId, Integer pageNum, Integer pageSize);

    //或许有些我本认为应该返回null的methods实际上应该返回pageResult或xxVO？比如说在帖子里添加一条评论or在home页面里添加一条帖子（这都会改变数据库里存储的东西）后应当重新发送getxx请求并用更新后的资源重新渲染页面。
    //但不管怎样，我还是先debug简单的问题好了，以及优化文件夹结构

}