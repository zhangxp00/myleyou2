package com.leyou.search.pojo;

import com.leyou.item.pojo.Brand;
import com.leyou.pojo.ResultPage;
import java.util.List;
import java.util.Map;

public class SearchResult extends ResultPage<Goods> {

    private List<Map<String,Object>> categoryList;

    private List<Brand> brands;

    private List<Map<String,Object>> specs;

    public SearchResult() {
    }

    public SearchResult(List<Map<String, Object>> categoryList, List<Brand> brands, List<Map<String, Object>> specs) {
        this.categoryList = categoryList;
        this.brands = brands;
        this.specs = specs;
    }

    public SearchResult(Long total, List<Goods> items, List<Map<String, Object>> categoryList, List<Brand> brands, List<Map<String, Object>> specs) {
        super(total, items);
        this.categoryList = categoryList;
        this.brands = brands;
        this.specs = specs;
    }

    public SearchResult(Long total, Integer totalPage, List<Goods> items, List<Map<String, Object>> categoryList, List<Brand> brands, List<Map<String, Object>> specs) {
        super(total, totalPage, items);
        this.categoryList = categoryList;
        this.brands = brands;
        this.specs = specs;
    }

    public List<Map<String, Object>> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Map<String, Object>> categoryList) {
        this.categoryList = categoryList;
    }

    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }

    public List<Map<String, Object>> getSpecs() {
        return specs;
    }

    public void setSpecs(List<Map<String, Object>> specs) {
        this.specs = specs;
    }
}
