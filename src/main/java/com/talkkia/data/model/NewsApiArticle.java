package com.talkkia.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsApiArticle {

    private String title;
    private String author;
    private String description;
    private String url;
    private String urlToImage;
    private String content;
    private Date date;
}
