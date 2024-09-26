package com.sboard.dto;


import com.sboard.entity.User;
import lombok.*;
import org.modelmapper.internal.bytebuddy.implementation.bind.annotation.IgnoreForBinding;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {

    private int no;
    private int parent;
    private String content;

    private String writer;
    private String regip;
    private String date;

    //추가필드
    private String nick;

    private UserDTO user;

    public String getSubStringRdate() {
        if(date ==null){
            return date;
        }else{
            date =  date.substring(0,10);
            return date;
        }
    }



}
