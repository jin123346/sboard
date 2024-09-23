package com.sboard.dto;

import com.sboard.entity.Article;
import jakarta.mail.Multipart;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDTO {

    private int no;

    @Builder.Default
    private String cate = "free";
    private String title;
    private String contents;

    @Builder.Default
    private int comment = 0;

//    private MultipartFile file1;
    private List<MultipartFile> files;

    @Builder.Default
    private int file=0;
    @Builder.Default
    private int hit=0;

    private String writer;

    private String regip;

    private String rdate;

    private String subStringRdate;

    public String getSubStringRdate() {
        rdate =  rdate.substring(0,10);
        return rdate;
    }

    /*
     Entity 변환 메서드 대신 ModelMapper 사용
     */

//    public Article toEntity(){
//        return Article.builder()
//                .no(no)
//                .cate(cate)
//                .title(title)
//                .contents(contents)
//                .comment(comment)
//                .file(file)
//                .hit(hit)
//                .writer(writer)
//                .rdate(LocalDateTime.parse(regip))
//                .build();
//    }

}
