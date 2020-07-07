package com.leyou.search.feign;

import com.leyou.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "item-service")
public interface CategoryFeign extends CategoryApi {
}
