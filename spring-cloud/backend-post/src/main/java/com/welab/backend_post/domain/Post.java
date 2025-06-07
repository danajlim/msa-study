package com.welab.backend_post.domain;

import com.azure.core.annotation.Get;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Entity
@Table(name = "post", //DB 테이블 이름을 명시적으로 지정
        indexes = { //indexes 속성은 해당 컬럼들에 인덱스를 생성해서 검색 성능 향상
                @Index(columnList = "user_id"),
                @Index(columnList = "created_datetime"),
                @Index(columnList = "updated_datetime")
        })
public class Post {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) //PK 값을 자동 생성
    @Getter
    @Setter
    private Long id;

    @Column(name = "title", nullable = false)
    @Getter
    private String title;

    @Column(name = "content", nullable = false)
    @Getter
    private String content;

    public void setPost(String title, String contents){
        this.title = title;
        this.content=contents;
        this.updatedDatetime = LocalDateTime.now();
    }

    @Column(name = "user_id", nullable = false)
    @Getter @Setter
    private String userId;

    @Column(name="created_datetime", nullable = false)
    @Getter
    private LocalDateTime createdDateTime = LocalDateTime.now();

    @Column(name = "updated_datetime")
    @Getter
    private LocalDateTime updatedDatetime;

    //1:n 관계 , mappedBy: 연관관계의 주인 필드 이름 (post)
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY) //LAZY: 실제 사용할 때까지 DB 조회 지연
    private List<PostComment> comments = new ArrayList<>();

    public List<PostComment> getComments() {
        return this.comments;
    }

    public void addComment(PostComment comment){
        comment.setPost(this);
        this.comments.add(comment);
    }

}
