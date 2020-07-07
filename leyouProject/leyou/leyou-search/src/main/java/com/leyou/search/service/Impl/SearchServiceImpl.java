package com.leyou.search.service.Impl;

import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.pojo.Spu;
import com.leyou.search.feign.BrandFeign;
import com.leyou.search.feign.CategoryFeign;
import com.leyou.search.feign.GoodsFeign;
import com.leyou.search.feign.SpecParamFeign;
import com.leyou.search.mapper.SearchMapper;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchParam;
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.service.GoodsService;
import com.leyou.search.service.SearchService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SearchMapper searchMapper;
    @Autowired
    private CategoryFeign categoryFeign;
    @Autowired
    private BrandFeign brandFeign;
    @Autowired
    private SpecParamFeign specParamFeign;

    @Autowired
    private GoodsFeign goodsFeign;

    @Autowired
    private GoodsService goodsServiceImpl;

    @Override
    public SearchResult searchPage(SearchParam searchParam) {
        String key = searchParam.getKey();
        if (key == null) {
            return null;
        }
        //自定义搜索条件
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //全文检索查询
        //QueryBuilder generalQuery = QueryBuilders.matchQuery("all", key).operator(Operator.AND);
        QueryBuilder generalQuery = searchQuery(searchParam);
        nativeSearchQueryBuilder.withQuery(generalQuery);
        //结果集过滤
        nativeSearchQueryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "skus", "subTitle"}, null));
        //分页
        nativeSearchQueryBuilder.withPageable(PageRequest.of(searchParam.getPageNum() - 1, searchParam.getSize()));
        //添加分类以及品牌的聚合
        String brandName = "brandName";
        String categoryName = "categoryName";
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms(brandName).field("brandId"));
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms(categoryName).field("cid3"));
        //执行查询
        AggregatedPage<Goods> goodsPage = (AggregatedPage<Goods>) searchMapper.search(nativeSearchQueryBuilder.build());

        //解析桶中的数据
        List<Brand> brands = getBrandAggregation(goodsPage.getAggregation(brandName));
        List<Map<String, Object>> categoryList = getCategoryAggregation(goodsPage.getAggregation(categoryName));
        List<Map<String, Object>> specs = null;
        //判断结果集查询是否只有一个分类,因为多个分类聚合的规格参数太多 不便于查询,无意义
        if (!CollectionUtils.isEmpty(categoryList) && categoryList.size() == 1) {
            specs = getSpecsAggregation((Long) (categoryList.get(0).get("id")), generalQuery);
        }

        //构建ResultPage
        SearchResult searchResult = new SearchResult(goodsPage.getTotalElements(), goodsPage.getTotalPages(), goodsPage.getContent(), categoryList, brands, specs);
        return searchResult;
    }

    /**
     * 添加search或者更新
     *
     * @param id
     */
    @Override
    public void saveOrUpdate(Long id) {
        Spu spu = goodsFeign.findSpuById(id);
        Goods goods = goodsServiceImpl.getGoodsBySpu(spu);
        searchMapper.save(goods);
    }

    /**
     * 组装基本的查询条件以及添加过滤条件
     *
     * @param searchParam
     * @return
     */
    private QueryBuilder searchQuery(SearchParam searchParam) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //添加基本查询
        boolQueryBuilder.must(QueryBuilders.matchQuery("all", searchParam.getKey()).operator(Operator.AND));
        //获取过滤的字段以及参数
        Map<String, String> searchfilter = searchParam.getSearchfilter();
        for (Map.Entry<String, String> entry : searchfilter.entrySet()) {
            String key = entry.getKey();
            if ("品牌".equals(key)) {
                key = "brandId";
            } else if ("分类".equals(key)) {
                key = "cid3";
            } else {
                key = "specs." + key + ".keyword";
            }
            boolQueryBuilder.filter(QueryBuilders.termQuery(key, entry.getValue()));
        }
        return boolQueryBuilder;
    }

    //当选择一个分类以后对规格参数进行聚合以及解析
    private List<Map<String, Object>> getSpecsAggregation(Long id, QueryBuilder generalQuery) {
        //构建自定义查询
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //根据用户输入的查询条件构建普通查询
        nativeSearchQueryBuilder.withQuery(generalQuery);
        //根据分类的id获取对应的规格参数
        List<SpecParam> specParams = specParamFeign.findByGid(null, id, null, true);
        //执行聚合
        specParams.forEach(specParam -> {
            nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms(specParam.getName()).field("specs." + specParam.getName() + ".keyword"));
        });
        //添加结果集过滤
        nativeSearchQueryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{}, null));
        //执行查询
        AggregatedPage<Goods> search = (AggregatedPage<Goods>) searchMapper.search(nativeSearchQueryBuilder.build());
        //解析聚合结果集,组装返回的参数格式
        //sting就是聚合名称,value就是聚合结果集
        Map<String, Aggregation> stringAggregationMap = search.getAggregations().asMap();
        List<Map<String, Object>> specsListMap = new ArrayList<>();

        for (Map.Entry<String, Aggregation> Entry : stringAggregationMap.entrySet()) {
            Map<String, Object> specsMap = new HashMap<>();

            specsMap.put("name", Entry.getKey());
            StringTerms terms = (StringTerms) Entry.getValue();
            List<String> valueList = terms.getBuckets().stream().map(bucket -> {
                return bucket.getKeyAsString();
            }).collect(Collectors.toList());
            specsMap.put("options", valueList);
            specsListMap.add(specsMap);
        }
        return specsListMap;
    }

    //解析分类桶数据
    private List<Map<String, Object>> getCategoryAggregation(Aggregation aggregation) {
        LongTerms aggregation1 = (LongTerms) aggregation;
        return aggregation1.getBuckets().stream().map(bucket -> {
            Map<String, Object> stringObjectMap = new HashMap<>();
            List<String> categoryNames = categoryFeign.findNameByIds(Arrays.asList(bucket.getKeyAsNumber().longValue()));
            stringObjectMap.put("id", bucket.getKeyAsNumber().longValue());
            stringObjectMap.put("name", categoryNames.get(0));
            return stringObjectMap;
        }).collect(Collectors.toList());
    }

    //解析品牌桶数据
    private List<Brand> getBrandAggregation(Aggregation aggregation) {
        LongTerms aggregation1 = (LongTerms) aggregation;
        //aggregation1.getBuckets().forEach(bucket -> System.out.println(bucket.getKeyAsNumber().longValue()));
        return aggregation1.getBuckets().stream().map(bucket -> {
            Brand brand = brandFeign.findByBid(bucket.getKeyAsNumber().longValue());
            return brand;
        }).collect(Collectors.toList());
    }
}
