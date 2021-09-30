package com.talkkia.data.services.collector.impl;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.talkkia.data.entity.Article;
import com.talkkia.data.model.*;
import com.talkkia.data.repository.ArticleRepository;
import com.talkkia.data.services.ArticleService;
import com.talkkia.data.services.collector.CollectorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * This class collects information from the https://newsapi.org/
 */
public class NewsApiCollectorServiceImpl implements CollectorService, ArticleService<NewsApiArticle> {

    private Logger _logger = LoggerFactory.getLogger(NewsApiCollectorServiceImpl.class);
    private final String apiKey;
    private final Executor executor;
    private List<NewsApiEndpoint> apiEndPoints;
    private long lastRunTimestamp;
    private RestTemplate template;
    private ObjectMapper mapper;
    private HttpHeaders headers;
    private HttpEntity entity;


    @Autowired
    private ArticleRepository repository;


    public NewsApiCollectorServiceImpl(final String pApiKey, final Executor pExecutor, final List<NewsApiEndpoint> pApiEndPoints) {
        apiKey = pApiKey;
        executor = pExecutor;
        apiEndPoints = pApiEndPoints;
        initialize();
    }

    protected void initialize() {
        template = new RestTemplate();
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        headers = new HttpHeaders();
        headers.set("X-Api-Key", getApiKey());
        entity = new HttpEntity(headers);
    }

    protected String getApiKey() {
        return apiKey;
    }


    protected Executor getExecutor() {
        return executor;
    }

    @Override
    public void collect() {
        //Run the collection service here.
        long currentRun = System.currentTimeMillis();
        _logger.debug("Last executed run {}", lastRunTimestamp);
        _logger.debug("Kicking off the collection service for the news api at time {}", currentRun);
        runCollection();
    }

    protected void runCollection() {

        MultiValueMap<String, String> values = new LinkedMultiValueMap<>();

        //TODO - Check which country the targeted audience belong to and get rid of this.
        for (NewsApiEndpoint endpoint : apiEndPoints) {
            executeCall(endpoint);
        }
    }

    protected void executeCall(NewsApiEndpoint endpoint) {
        //Add check to determine if the calls for the current day is exhausted. Additional check to avoid rate limits.

        switch (endpoint.getName()) {
            case "topheadline":
                fetchTopHeadline((NewsApiTopHeadline) endpoint);
                break;
            case "everything":
                fetchEverything((NewsApiEverything) endpoint);
                break;
        }
    }

    protected void fetchTopHeadline(NewsApiTopHeadline topHeadline) {

        MultiValueMap<String, String> values = new LinkedMultiValueMap();
        for (String country : topHeadline.getCountry()) {
            values.set("country", country);
            values.set("pageSize", String.valueOf(topHeadline.getPageSize()));

            for (String category : topHeadline.getCategories()) {
                values.set("category", category);
                executeApiCall(values,topHeadline.getEndPoint(), Collections.singletonMap("category",category));

            }
        }
        lastRunTimestamp = System.currentTimeMillis();
        saveApiCallDetails();
    }

    protected void fetchEverything(NewsApiEverything everything){
            MultiValueMap<String, String> values = new LinkedMultiValueMap();
            values.set("pageSize", String.valueOf(everything.getPageSize()));
            executeApiCall(values,everything.getEndPoint(), Collections.singletonMap("name",everything.getName()));
    }

    protected void executeApiCall(MultiValueMap<String,String> values, String endpoint, Map<String,String> additionalParameters) {
        try {
            int page = 0;
            do {
                UriComponents components = UriComponentsBuilder.
                        fromUriString(endpoint).queryParams(values).build().
                        encode(StandardCharsets.UTF_8);
                ResponseEntity<String> responseEntity = template.exchange(components.toUri(), HttpMethod.GET, entity, String.class);

                if (responseEntity.getStatusCode() == HttpStatus.OK) {
                    _logger.debug("Response {}", responseEntity.getBody());
                    NewsApiResponse data = mapper.readValue(responseEntity.getBody(), NewsApiResponse.class);
                    data.getArticles().stream().forEach(article ->
                            CompletableFuture.runAsync(() ->
                                            saveArticle(article, additionalParameters)
                                    , getExecutor())
                    );

                    if (data.getPage() != 0) {
                        page = data.getPage();
                        values.set("page", String.valueOf(page));
                    }
                }
            } while (page != 0);

        } catch (Exception e) {
            _logger.error("Could not fetch the data due to error [ {} ]", e.getMessage());
        }
    }


    @Override
    @Transactional
    public void saveArticle(NewsApiArticle newsApiArticle,Map<String,String> additionalParameters) {
        try {
            String subCategory = mapSubCategory(newsApiArticle.getTitle(),newsApiArticle.getDescription());
            Article article = new Article();
            article.setCreatedTimestamp(System.currentTimeMillis());
            article.setArticleURL(newsApiArticle.getUrl());
            article.setDescription(newsApiArticle.getDescription());
            article.setTitle(newsApiArticle.getTitle());
            article.setUrlToImage(newsApiArticle.getUrlToImage());
            repository.save(article);
        } catch (Exception e) {
            _logger.error("Error persisting the data");
        }
    }

    protected String mapSubCategory(String title,String description){

        return "Other";
    }

    @Override
    public void saveApiCallDetails() {

    }
}
