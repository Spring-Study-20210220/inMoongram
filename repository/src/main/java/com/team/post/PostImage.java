package com.team.post;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    private String url;

    private String uploadFileName; // 유저가 업로드한 파일 이름

    private String storeFileName; // 서버 내부에서 관리하는 파일 이름

    private String path; // 파일 저장 경로

    @JoinColumn(name = "post_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    public PostImage(String uploadFileName, String storeFileName, String path) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
        this.path = path;
    }

    public void setPost(Post post){
        this.post = post;
    }
}
