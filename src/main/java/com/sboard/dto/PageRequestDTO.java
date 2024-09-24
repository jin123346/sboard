package com.sboard.dto;


import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PageRequestDTO {
    @Builder.Default
    private int no =1;  //글번호

    @Builder.Default
    private String cate="free";
    @Builder.Default
    private int pg =1;  //페이지 번호

    @Builder.Default
    private int size=10; //한페이지에 검색할 갯수

    private String type;
    private String keyword;


    public Pageable getPageable(String sort) {
        return PageRequest.of(this.pg-1,this.size, Sort.by(sort).descending());
    }


}
