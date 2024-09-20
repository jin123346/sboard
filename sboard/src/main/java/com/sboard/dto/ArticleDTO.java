package com.sboard.dto;

import com.sboard.entity.Article;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDTO {
    private int no;
    private String cate;
    private String title;
    private String contents;
    private int comment;
    private int file;
    private int hit;
    private String writer;
    private String regip;
    private String rdate;

    public Article toEntity(){
        return Article.builder()
                .no(no)
                .cate(cate)
                .title(title)
                .contents(contents)
                .comment(comment)
                .file(file)
                .hit(hit)
                .writer(writer)
                .regip(regip)
                .rdate(LocalDateTime.parse(rdate))
                .build();
    }

}
