package com.talkkia.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article {

    @Id
    @GeneratedValue
    Long id;
    String articleURL;
    int likes;
    int dislikes;
    int shared;
    long createdTimestamp;
    String description;
    String urlToImage;
    String title;
    @Relationship(value = "IS_OF_SUBCATEGORY", direction = Relationship.Direction.INCOMING)
    SubCategory subCategory;

}
