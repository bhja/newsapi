package com.talkkia.data.services.collector.impl;


import com.talkkia.data.services.collector.CollectionScheduler;
import com.talkkia.data.services.collector.CollectorService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CollectionSchedulerImpl implements CollectionScheduler {

    List<CollectorService> services ;

    public CollectionSchedulerImpl(List<CollectorService> pCollectorServices){
        services  = pCollectorServices;
    }

    @Override
    @Scheduled(cron="${talkkia.data.collectors.schedule:0 0 */3 * * ?}")
    public void run() {

        //TODO -Introduce a delay before kicking off the next collector service.
        services.stream().forEach(s->s.collect());
    }
}
