package com.sboard.dto;

import com.sboard.entity.Comment;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ArticleResponseDTO {

    private List<FileDTO> files;
    private List<CommentDTO> comments;

    private String cate = "free";
    private String title;
    private String contents;
    private int file;
    private int comment;
    private int hit;
    private String writer;
    private String regip;
    private String rdate;
    private String subStringRdate;

    private String fileOname;
    private int download;

    @Builder
    public ArticleResponseDTO(ArticleDTO articleDTO, List<FileDTO> files){
        this.files = files;
        this.cate = articleDTO.getCate();
        this.title = articleDTO.getTitle();
        this.contents = articleDTO.getContents();
        this.regip = articleDTO.getRegip();
        this.rdate = articleDTO.getRdate();
        this.subStringRdate = articleDTO.getSubStringRdate();
        this.hit = articleDTO.getHit();
        this.writer = articleDTO.getWriter();
        this.comment = articleDTO.getComment();
        this.file = articleDTO.getFile();

    }


}
