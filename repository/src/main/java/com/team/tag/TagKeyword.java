package com.team.tag;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = @Index(name = "i_tagKeyword", columnList = "keyword"))
public class TagKeyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String keyword;

    @Builder
    public TagKeyword(Long id, String keyword) {
        this.id = id;
        this.keyword = keyword;
    }

    public TagKeyword(String keyword) {
        this.keyword = keyword;
    }
}
