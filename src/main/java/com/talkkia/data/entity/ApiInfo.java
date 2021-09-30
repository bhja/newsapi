package com.talkkia.data.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.Date;

@Node
@NoArgsConstructor
@AllArgsConstructor
public class ApiInfo {

    @Id
    Long id;
    String sourceName;
    String endPoint;
    int callCount;
    Date lastRunDate;

}
