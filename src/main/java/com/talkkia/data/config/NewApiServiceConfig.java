package com.talkkia.data.config;

import com.talkkia.data.model.NewsApiEndpoint;
import com.talkkia.data.model.NewsApiEverything;
import com.talkkia.data.model.NewsApiTopHeadline;
import com.talkkia.data.services.collector.CollectorService;
import com.talkkia.data.services.collector.impl.NewsApiCollectorServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;

@Configuration
@ConditionalOnProperty(value = "talkkia.data.collectors.source.newsapi.enabled",havingValue = "true")
public class NewApiServiceConfig {

    @Bean(name="newsApiCollectorService")
    public CollectorService allocateNewApiCollector(
            @Value("${talkkia.data.collectors.source.newsapi.apikey}") String apiKey,
            List<NewsApiEndpoint> newsApiEndpoints,
            @Qualifier(value = "newsApiQueueExecutor") ThreadPoolTaskExecutor executor)
    {
        return
                new NewsApiCollectorServiceImpl(apiKey,executor,newsApiEndpoints);
    }

    @Bean
    @ConditionalOnProperty(prefix = "talkkia.data.collectors.source.newsapi.topheadline.enabled", name = "true")
    public NewsApiTopHeadline allocateNewsApiTopHeadline(
        @Value("${talkkia.data.collectors.source.newsapi.topheadline.endpoint}") String endpoint,
        @Value("${talkkia.data.collectors.source.newsapi.topheadline.pagesize}") int pagesize,
        @Value("${talkkia.data.collectors.source.newsapi.topheadline.country}") List<String> country,
        @Value("${talkkia.data.collectors.source.newsapi.topheadline.category}") List<String> category){
        return new NewsApiTopHeadline("topheadline",endpoint,country,category,pagesize);
    }

    @Bean
    @ConditionalOnProperty(prefix = "talkkia.data.collectors.source.newsapi.everything.enabled", name = "true")
    public NewsApiEverything allocateNewsApiEverything(
            @Value("${talkkia.data.collectors.source.newsapi.everything.endpoint}") String endpoint,
            @Value("${talkkia.data.collectors.source.newsapi.everything.pagesize}") int pagesize,
            @Value("${talkkia.data.collectors.source.newsapi.everything.languages}") List<String> languages
           ){
        return new NewsApiEverything("everything",endpoint,pagesize,languages);
    }

    @Bean(name="newsApiQueueExecutor")
    public ThreadPoolTaskExecutor allocateExecutor(
            final @Value("${talkkia.data.collectors.source.newsapi.collectionQueue.corePoolSize:1}") int corePoolSize,
            final @Value("${talkkia.data.collectors.source.newsapi.collectionQueue.maxPoolSize:1}") int maxPoolSize,
            final @Value("${talkkia.data.collectors.source.newsapi.collectionQueue.queueCapacity:5000000}") int queueCapacity,
            final @Value("${talkkia.data.collectors.source.newsapi.collectionQueue.threadPrefix:NewsApiQueue}") String threadPrefix){

        ThreadPoolTaskExecutor executor =
                new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(threadPrefix +"-");
        executor.initialize();
        return executor;
    }
}
