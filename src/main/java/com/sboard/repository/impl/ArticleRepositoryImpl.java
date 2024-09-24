package com.sboard.repository.impl;

import com.querydsl.core.QueryFactory;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sboard.dto.PageRequestDTO;
import com.sboard.entity.QArticle;
import com.sboard.entity.QUser;
import com.sboard.repository.custom.ArticleRepositoryCustom;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;


@Log4j2
@AllArgsConstructor
@Repository
public class ArticleRepositoryImpl implements ArticleRepositoryCustom {


    private final JPAQueryFactory queryFactory;
    private final QArticle qarticle = QArticle.article;
    private final QUser quser  = QUser.user;

    @Override
    public Page<Tuple> selectARticleAllForList(PageRequestDTO pagerequestDTO, Pageable pageable) {

        QueryResults<Tuple> results=  queryFactory
                                            .select(qarticle,quser.nick)
                                            .from(qarticle)
                                            .join(quser)
                                            .on(qarticle.writer.eq(quser.uid))
                                            .offset(pageable.getOffset())
                                            .limit(pageable.getPageSize())
                                            .orderBy(qarticle.no.desc())
                                            .fetchResults();

        log.info("result : "+results);

        List<Tuple> content = results.getResults();
        log.info("content : "+content);

        long total = results.getTotal();

        return new PageImpl<Tuple>(content,pageable,total);

    }
}
