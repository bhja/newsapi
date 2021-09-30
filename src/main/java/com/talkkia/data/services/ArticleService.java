package com.talkkia.data.services;

import java.util.Map;

public interface ArticleService<T> {

    void saveArticle(T data, Map<String,String> category);
    void saveApiCallDetails();
}