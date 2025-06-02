package com.forum.service.impl;

import com.forum.event.NotificationEvent;
import com.forum.exception.BusinessException;
import com.forum.mapper.PostMapper;
import com.forum.model.entity.Post;
import com.forum.model.user.dto.PostDTO;
import com.forum.model.user.query.PostQueryDTO;
import com.forum.model.user.vo.PostDetailVO;
import com.forum.model.user.vo.PostItemVO;
import com.forum.service.PostService;
import com.forum.utils.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;
    private final ApplicationEventPublisher eventPublisher;

    private static final int REPORT_THRESHOLD = 5;

    @Override
    public PageResult<List<PostItemVO>> getPostsByCategory(Long categoryId, PostQueryDTO postQueryDTO) {
        // 计算分页参数
        int offset = (postQueryDTO.getPageNo() - 1) * postQueryDTO.getPageSize();

        // 查询帖子列表
        List<Post> posts = postMapper.selectByCategory(categoryId, offset, postQueryDTO.getPageSize());
        Long total = postMapper.countByCategory(categoryId);

        // 转换为VO
        List<PostItemVO> voList = posts.stream()
                .map(this::convertToItemVO)
                .collect(Collectors.toList());

        // Wrap voList in another list to match PageResult<List<PostItemVO>>
        return new PageResult<List<PostItemVO>>(List.of(voList), total, postQueryDTO.getPageNo(), postQueryDTO.getPageSize());
    }

    @Override
    @Transactional
    public void createPost(PostDTO postDTO) {
        Post post = new Post();
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setCategoryId(postDTO.getCategoryId());
        post.setUserId(postDTO.getUserId());
        post.setFloorCount(0);
        post.setReportCount(0);
        post.setViewCount(0);
        post.setIsHidden(false);
        post.setStatus(1);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        postMapper.insert(post);
        log.info("帖子创建成功: postId={}, userId={}", post.getId(), post.getUserId());
    }

    @Override
    @Transactional
    public void reportPost(Long postId) {
        Post post = postMapper.selectById(postId);
        if (post == null) {
            throw new BusinessException("帖子不存在");
        }

        // 增加举报次数
        post.setReportCount(post.getReportCount() + 1);

        // 检查是否需要折叠
        if (post.getReportCount() >= REPORT_THRESHOLD && !post.getIsHidden()) {
            post.setIsHidden(true);
            log.info("帖子因举报次数过多被折叠: postId={}", postId);
        }

        postMapper.update(post);
    }

    @Override
    @Transactional
    public PostItemVO updatePost(Long postId, PostDTO postDTO) {
        Post post = postMapper.selectById(postId);
        if (post == null) {
            throw new BusinessException("帖子不存在");
        }

        // 检查权限
        if (!post.getUserId().equals(postDTO.getUserId())) {
            throw new BusinessException("无权修改此帖子");
        }

        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setUpdatedAt(LocalDateTime.now());

        postMapper.update(post);

        return convertToItemVO(post);
    }

    @Override
    public PageResult<PostDetailVO> getPostDetail(Long postId, Integer pageNum, Integer pageSize) {
        Post post = postMapper.selectById(postId);
        if (post == null) {
            throw new BusinessException("帖子不存在");
        }

        // 增加浏览次数
        postMapper.incrementViewCount(postId);

        PostDetailVO detailVO = convertToDetailVO(post);

        // For getPostDetail, if it returns PageResult<PostDetailVO>,
        // List.of(detailVO) is correct for List<PostDetailVO> as the first argument.
        return new PageResult<PostDetailVO>(List.of(detailVO), 1L, pageNum, pageSize);
    }

    private PostItemVO convertToItemVO(Post post) {
        PostItemVO vo = new PostItemVO();
        vo.setPostId(post.getId());
        vo.setTitle(post.getTitle());
        vo.setAuthorName(post.getAuthorName());
        vo.setCategoryName(post.getCategoryName());
        vo.setCommentCount(post.getFloorCount());
        vo.setViewCount(post.getViewCount());
        vo.setIsHidden(post.getIsHidden());
        vo.setCreatedAt(post.getCreatedAt());
        vo.setUpdatedAt(post.getUpdatedAt());
        return vo;
    }

    private PostDetailVO convertToDetailVO(Post post) {
        PostDetailVO vo = new PostDetailVO();
        vo.setPostId(post.getId());
        vo.setTitle(post.getTitle());
        vo.setContent(post.getContent());
        vo.setAuthorName(post.getAuthorName());
        vo.setCategoryName(post.getCategoryName());
        vo.setCommentCount(post.getFloorCount());
        vo.setViewCount(post.getViewCount());
        vo.setIsHidden(post.getIsHidden());
        vo.setCreatedAt(post.getCreatedAt());
        vo.setUpdatedAt(post.getUpdatedAt());
        // Populate other fields as necessary, e.g., comments if fetched
        return vo;
    }
}
