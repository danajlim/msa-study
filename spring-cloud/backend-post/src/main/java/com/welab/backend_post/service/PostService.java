package com.welab.backend_post.service;

import com.welab.backend_post.common.exception.NotFound;
import com.welab.backend_post.domain.Post;
import com.welab.backend_post.domain.PostComment;
import com.welab.backend_post.domain.dto.PostCommentCreateDto;
import com.welab.backend_post.domain.dto.PostCreateDto;
import com.welab.backend_post.domain.repository.PostCommentRepository;
import com.welab.backend_post.domain.repository.PostRepository;
import com.welab.backend_post.remote.noti.RemoteNotiService;
import com.welab.backend_post.remote.noti.dto.SendSmsDto;
import com.welab.backend_post.remote.user.RemoteUserService;
import com.welab.backend_post.remote.user.dto.SiteUserInfoDto;
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

    private final RemoteUserService remoteUserService;
    private final RemoteNotiService remoteNotiService;

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

        //알림톡 보내기 위해 사용자 정보 조회:  Remote User 서비스 호출
        SiteUserInfoDto userInfoDto = remoteUserService.userInfo(post.getUserId()).getData();

        //알림톡 전송 요청
        SendSmsDto.Request requestDto = new SendSmsDto.Request();
        requestDto.setUserId(userInfoDto.getUserId());
        requestDto.setPhoneNumber(userInfoDto.getPhoneNumber());
        requestDto.setTitle("댓글 알림");
        requestDto.setMessage("댓글이 달렸습니다");

        remoteNotiService.sendSms(requestDto);
    }
}
