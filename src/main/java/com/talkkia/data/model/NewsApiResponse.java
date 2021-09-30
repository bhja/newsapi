package com.talkkia.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsApiResponse {
    String status;
    int totalResults;
    List<NewsApiArticle> articles ;
    int page;
}
