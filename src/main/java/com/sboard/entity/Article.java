package com.sboard.entity;

import com.sboard.dto.ArticleDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
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

    //추가필드
    @Transient
    private String nick;

    @Transient
    private int pg;

    @Transient
    private String subStringRdate;

    @OneToMany(mappedBy = "ano", cascade = CascadeType.REMOVE)
    private List<FileEntity> fileList;

    @OneToMany(mappedBy = "parent",cascade =CascadeType.REMOVE )
    private List<Comment> commentList;






//
//    public ArticleDTO toDTO() {
//        return ArticleDTO.builder()
//                .no(this.no)
//                .cate(this.cate)
//                .title(this.title)
//                .contents(this.contents)
//                .comment(this.comment)
//                .file(this.file)
//                .hit(this.hit)
//                .writer(this.writer)
//                .regip(this.regip)
//                .rdate(rdate.toString())
//                .build();
//    }
}
