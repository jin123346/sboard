package com.sboard.controller;

import com.sboard.config.AppInfo;
import com.sboard.dto.ArticleDTO;
import com.sboard.dto.FileDTO;
import com.sboard.dto.PageRequestDTO;
import com.sboard.dto.PageResponseDTO;
import com.sboard.entity.Article;
import com.sboard.service.ArticleService;
import com.sboard.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Log4j2
@Controller
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final FileService fileService;



    @GetMapping(value={"/article/list","/"})
    public String list(Model model,PageRequestDTO pageRequestDTO) {
        log.info(pageRequestDTO);
        PageResponseDTO pageResponseDTO = articleService.getAllArticles(pageRequestDTO);

        log.info("start : "+pageResponseDTO.getStart());
        log.info("end : " +pageResponseDTO.getEnd());

        log.info(pageResponseDTO.getDtoList());
        model.addAttribute("pageResponseDTO", pageResponseDTO);


        return "/article/list";
    }

    @GetMapping("/article/modify")
    public String modify(){

        return "/article/modify";
    }
    @GetMapping("/article/view")
    public String view(@RequestParam("no") int no, Model model){
       ArticleDTO updateArticle =  articleService.updateArticleHit(no);
        model.addAttribute("article", updateArticle);
        return "/article/view";
    }
    @GetMapping("/article/write")
    public String write(){
        return "/article/write";
    }

    @PostMapping("/article/write")
    public String write(ArticleDTO articleDTO, @RequestParam("writer") String writer, HttpServletRequest request){
        log.info(articleDTO);
        articleDTO.setWriter(writer);


        //파일 업로드
        List<FileDTO> uploadFiles =  fileService.uploadFile(articleDTO);
        int file = uploadFiles.size();
        articleDTO.setFile(file);
        //글 저장
        int ano = articleService.insertArticle(articleDTO,request);
        //파일 저장
        if(!uploadFiles.isEmpty()){
            for(FileDTO fileDTO : uploadFiles){
                fileDTO.setAno(ano);
                int fno = fileService.insertFile(fileDTO);
            }


        }


        log.info(ano);
        if(ano != 0){
            return "redirect:/article/list?success=100";

        }

        return "redirect:/article/write?success=200";

    }



}
