package com.talkkia.data.repository;

import com.talkkia.data.entity.ApiInfo;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ApiRepository extends Neo4jRepository<ApiInfo,Long> {

}
