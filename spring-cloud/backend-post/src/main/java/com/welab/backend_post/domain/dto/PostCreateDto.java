package com.welab.backend_post.domain.dto;

import com.welab.backend_post.common.web.context.GatewayRequestHeaderUtils;
import com.welab.backend_post.domain.Post;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

//게시글 생성 요청 데이터 DTO
@Getter @Setter
public class PostCreateDto {

    @NotBlank(message = "타이틀 입력하셈")
    private String title;

    @NotBlank(message = "본문 입력하셈")
    private String content;

    public Post toEntity() {
        Post post = new Post();

        post.setUserId(GatewayRequestHeaderUtils.getUserIdOrThrowException());
        post.setPost(this.title, this.content);

        return post;
    }
}
