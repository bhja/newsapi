package com.talkkia.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsApiTopHeadline implements NewsApiEndpoint{

    String endPoint;
    String name;
    List<String> country;
    List<String> categories;
    int pageSize;


}

