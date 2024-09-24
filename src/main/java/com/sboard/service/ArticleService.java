package com.sboard.service;

import com.querydsl.core.Tuple;
import com.sboard.dto.ArticleDTO;
import com.sboard.dto.PageRequestDTO;
import com.sboard.dto.PageResponseDTO;
import com.sboard.entity.Article;
import com.sboard.repository.ArticleRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ModelMapper modelMapper;

    public int insertArticle(ArticleDTO articleDTO, HttpServletRequest request) {

        log.info("article controller :"+articleDTO.toString());
        //첨부파일 객체 가져오기
        List<MultipartFile> files = articleDTO.getFiles();

        log.info("files size multipart : " + files.size());
        for(MultipartFile file : files) {
            log.info("file Name : " +file.getOriginalFilename());
        }



        articleDTO.setRegip(request.getRemoteAddr());
        log.info("regIp : "+request.getRemoteAddr());

        //ModelMapper
        Article article = modelMapper.map(articleDTO, Article.class);
        Article savedArticle =  articleRepository.save(article);

        return savedArticle.getNo();
    }
    public ArticleDTO updateArticle(ArticleDTO articleDTO) {
//        boolean isExists =  articleRepository.existsArticlesByNoAndWriter(articleDTO.getNo(),articleDTO.getWriter());
//
//        if(isExists) {
//            Article updateArticle =  articleRepository.updateArticle(articleDTO.toEntity());
//          return updateArticle.toDTO();
//        }
         return null;

    }

    public ArticleDTO updateArticleHit(int no){
        Optional<Article> opt = articleRepository.findById(no);
        if(opt.isPresent()) {
            Article article = opt.get();
            //LocalDateTime originalRdate = article.getRdate();
            //article.setRdate(originalRdate);
            article.setHit(article.getHit() + 1);


            return  modelMapper.map(articleRepository.save(article), ArticleDTO.class);

        }
        return null;
    }

    public ArticleDTO getArticleById(int id) {
        Article article = articleRepository.findById(id).orElse(null);
        if(article != null) {
            return modelMapper.map(article, ArticleDTO.class);
        }
        return null;
    }
    public PageResponseDTO getAllArticles(PageRequestDTO pageRequestDTO) {

        Pageable pageable = pageRequestDTO.getPageable("no");


       Page<Tuple> pageArticle =  articleRepository.selectARticleAllForList(pageRequestDTO, pageable);

//        List<Article> articles = articleRepository.findAll();
        List<ArticleDTO> articleList = pageArticle.stream().map(tuple ->{
                   Article article = tuple.get(0,Article.class);
                   String nick=tuple.get(1,String.class);
                   article.setNick(nick);
                   return modelMapper.map(article, ArticleDTO.class);
            }
                                                         ).toList();

        int total = (int)pageArticle.getTotalElements();

        return PageResponseDTO.builder()
                .dtoList(articleList)
                .total(total)
                .pageRequestDTO(pageRequestDTO)
                .build();



    }

    public int deleteArticleById(int id) {
        boolean isExists =  articleRepository.existsById(id);

        if(isExists) {
           articleRepository.deleteById(id);
           return 1;
        }
        return 0;
    }

}
