package com.welab.backend_post.service;

import com.welab.backend_post.common.exception.NotFound;
import com.welab.backend_post.domain.Post;
import com.welab.backend_post.domain.PostComment;
import com.welab.backend_post.domain.dto.PostCommentCreateDto;
import com.welab.backend_post.domain.dto.PostCreateDto;
import com.welab.backend_post.domain.repository.PostCommentRepository;
import com.welab.backend_post.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;

    //게시글 생성
    public void createPost(PostCreateDto createDto) {
        Post post = createDto.toEntity(); // DTO → Entity 변환 (요청 데이터 + 유저 ID 포함)
        postRepository.save(post); //DB에 저장
    }

    //댓글 생성
    public void addPostComment(PostCommentCreateDto createDto) {
        //댓글 달 게시글 DB에서 조회
        Post post = postRepository.findById(createDto.getPostId())
                .orElseThrow(() -> new NotFound("포스팅 글을 찾을 수 없습니다"));

        //댓글 DTO -> Entity변환
        PostComment postComment = createDto.toEntity();
        postCommentRepository.save(postComment); //댓글 DB에 저장

        //Post 엔티티에 comment 추가
        post.addComment(postComment);
    }
}
