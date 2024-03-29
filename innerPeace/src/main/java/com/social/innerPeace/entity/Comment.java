package com.social.innerPeace.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"post","healer","reportList"})
public class Comment extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentNo;

    @Column(length = 200,nullable = false)
    private String commentContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postNo")
    private Post post;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "healerNickname")
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "comment",cascade = CascadeType.ALL)
    private List<Report> reportList = new ArrayList<>();

}
