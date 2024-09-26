package com.sboard.service;

import com.querydsl.core.Tuple;
import com.sboard.dto.*;
import com.sboard.entity.Article;
import com.sboard.entity.Comment;
import com.sboard.entity.FileEntity;
import com.sboard.repository.ArticleRepository;
import com.sboard.repository.FileRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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
    private final FileRepository fileRepository;

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

    public ArticleResponseDTO updateArticleHit(int no){
       List<Tuple> tuples = articleRepository.selectArticleById(no);



//        if(opt.isPresent()) {
//            Article article = opt.get();
//            article.setHit(article.getHit() + 1);
//            ArticleDTO viewArticle = modelMapper.map(articleRepository.save(article), ArticleDTO.class);
//
//            Optional<List<FileEntity>> fileopts = fileRepository.findByAno(no);
//            List<FileDTO>  filedtos=null;
//            if(fileopts.isPresent()) {
//                List<FileEntity> files = fileopts.get();
//                filedtos = files.stream().map(file -> modelMapper.map(file, FileDTO.class)).collect(Collectors.toList());
//
//            }
//
//
//            return ArticleResponseDTO.builder()
//                    .articleDTO(viewArticle)
//                    .files(filedtos)
//                    .build();
//
//        }
        return null;
    }

    public ArticleDTO selectArticleByNo(int no) {
        Optional<Article> opt = articleRepository.findById(no);
        if(opt.isPresent()) {
            Article article = opt.get();
            article.setHit(article.getHit() + 1);
            Article hitupdateArticle= articleRepository.save(article);
            return modelMapper.map(hitupdateArticle, ArticleDTO.class);
        }
//        Optional<FileEntity> fileentity = fileRepository.findByAno(no);
//        if(opt.isPresent()) {
//            List<FileEntity> fileEntities = fileentity.stream().toList();
//            modelMapper.map(fileEntities, FileDTO.class);
//        }

        return null;
    }
    public PageResponseDTO getAllArticles(PageRequestDTO pageRequestDTO) {

        Pageable pageable = pageRequestDTO.getPageable("no");
        Page<Tuple> pageArticle =null;
        if(pageRequestDTO.getKeyword()==null) {
            pageArticle = articleRepository.selectARticleAllForList(pageRequestDTO, pageable);
        }else{
            pageArticle = articleRepository.selectArticleForSearch(pageRequestDTO, pageable);

        }

        List<ArticleDTO> articleList = pageArticle.stream().map(tuple ->{
                    Article article = tuple.get(0,Article.class);
                    String nick=tuple.get(1,String.class);
                    article.setNick(nick);
                    return modelMapper.map(article, ArticleDTO.class);
                }
        ).toList();


        int total = (int)pageArticle.getTotalElements();

        log.info("total : "+total);
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
