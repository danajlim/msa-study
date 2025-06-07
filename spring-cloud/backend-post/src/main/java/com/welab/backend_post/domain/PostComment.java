package com.welab.backend_post.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Entity
@Table(name = "post_comment",
        indexes = {
                @Index(columnList = "user_id"),
                @Index(columnList = "created_datetime"),
                @Index(columnList = "updated_datetime")
        })

public class PostComment {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;

    @Column(name = "comment", nullable = false)
    @Getter
    private String comment;

    public void setComment(String comment) {
        this.comment = comment;
        this.updatedDatetime = LocalDateTime.now();
    }

    @Column(name="user_id",nullable = false)
    @Getter @Setter
    private String userId;

    @Column(name = "created_datetime", nullable = false)
    @Getter
    private LocalDateTime createdDatetime = LocalDateTime.now();

    @Column(name = "updated_datetime")
    @Getter
    private LocalDateTime updatedDatetime;

    @ManyToOne(targetEntity = Post.class)
    @JoinColumn(name="post_id", referencedColumnName = "id", nullable = true)
    //실제 **DB 컬럼 명은 post_id**가 됨 → 이게 외래 키
    //referencedColumnName = "id": Post 엔티티의 id 컬럼을 참조
    @Setter
    private Post post;
}
