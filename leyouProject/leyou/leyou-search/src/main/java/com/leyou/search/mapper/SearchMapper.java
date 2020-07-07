package com.leyou.search.mapper;

import com.leyou.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SearchMapper extends ElasticsearchRepository<Goods,Long> {
}
