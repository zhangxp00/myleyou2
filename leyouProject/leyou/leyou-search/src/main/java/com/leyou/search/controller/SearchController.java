package com.leyou.search.controller;

import com.leyou.search.pojo.SearchParam;
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class SearchController {

    @Autowired
    private SearchService searchServiceImpl;

    @PostMapping("page")
    public ResponseEntity<SearchResult> searchPage(@RequestBody SearchParam searchParam){
        SearchResult searchResult = searchServiceImpl.searchPage(searchParam);
        if(CollectionUtils.isEmpty(searchResult.getItems())){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(searchResult);
    }
}
