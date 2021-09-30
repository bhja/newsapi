package com.talkkia.data.repository;

import com.talkkia.data.entity.Article;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ArticleRepository extends Neo4jRepository<Article, Long> {

}
