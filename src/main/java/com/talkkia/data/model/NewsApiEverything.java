package com.talkkia.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsApiEverything implements NewsApiEndpoint {

    String endPoint;
    String name;
    int pageSize;
    List<String> languages;
}
