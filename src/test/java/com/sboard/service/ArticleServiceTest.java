package com.sboard.service;

import com.querydsl.core.Tuple;
import com.sboard.dto.PageRequestDTO;
import com.sboard.repository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class ArticleServiceTest {

    @Autowired
    private ArticleRepository articleRepository;


    @Test
    void insertArticle() {
    }

    @Test
    void updateArticle() {
    }

    @Test
    void updateArticleHit() {
    }

    @Test
    void getArticleById() {
    }

    @Test
    void getAllArticles() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .no(25)
                .pg(1)
                .size(10)
                .build();
        Pageable pageable = pageRequestDTO.getPageable("no");



        Page<Tuple> pageArticle =  articleRepository.selectARticleAllForList(pageRequestDTO, pageable);
        System.out.println(pageArticle.getContent());




    }

    @Test
    void deleteArticleById() {
    }
}