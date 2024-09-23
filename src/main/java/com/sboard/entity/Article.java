package com.sboard.entity;

import com.sboard.dto.ArticleDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="article")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;
    private String cate;
    private String title;
    private String contents;
    private int comment;
    private int file;
    private int hit;
    private String writer;
    private String regip;

    @CreationTimestamp
    private LocalDateTime rdate;



    public ArticleDTO toDTO() {
        return ArticleDTO.builder()
                .no(this.no)
                .cate(this.cate)
                .title(this.title)
                .contents(this.contents)
                .comment(this.comment)
                .file(this.file)
                .hit(this.hit)
                .writer(this.writer)
                .regip(this.regip)
                .rdate(rdate.toString())
                .build();
    }
}
