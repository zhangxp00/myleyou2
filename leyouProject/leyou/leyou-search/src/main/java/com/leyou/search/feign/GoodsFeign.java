package com.leyou.search.feign;

import com.leyou.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "item-service")
public interface GoodsFeign extends GoodsApi {
}