package com.sboard.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ArticleController {




    @GetMapping("/article/list")
    public String list(){
        return "list";
    }

    @GetMapping("/article/modify")
    public String modify(){
        return "modify";
    }
    @GetMapping("/article/view")
    public String view(){
        return "view";
    }
    @GetMapping("/article/write")
    public String write(){
        return "write";
    }
}
