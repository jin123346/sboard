package com.sboard.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PageResponseDTO {

    private List<ArticleDTO> dtoList;
    private String cate;
    private int pg;
    private int total;
    private int size;
    private int startNo;
    private int start, end;
    private boolean prev,next;

    @Builder
     public PageResponseDTO(PageRequestDTO pageRequestDTO,List<ArticleDTO> dtoList, int total){
         this.cate = pageRequestDTO.getCate();
         this.pg = pageRequestDTO.getPg();
         this.total = total;
         this.size = pageRequestDTO.getSize();
         this.dtoList = dtoList;
         this.startNo = total - ((pg-1)*size);  //개시물 시작번호
         this.end =(int) (Math.ceil(this.pg/10.0))*10;
         this.start = this.end -9;
         int last = (int)(Math.ceil(total/(double)size));
         this.end = end > last? last:end;
         this.prev = this.start>1;
         this.next = total > this.end *this.size;


     }

}
