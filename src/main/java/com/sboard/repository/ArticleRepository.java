package com.sboard.repository;

import com.sboard.dto.ArticleDTO;
import com.sboard.entity.Article;
import com.sboard.repository.custom.ArticleRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository  extends JpaRepository<Article, Integer> , ArticleRepositoryCustom {

    public boolean existsArticleByNo(int no);
    public boolean existsArticlesByNoAndWriter(int no, String writer);


}
