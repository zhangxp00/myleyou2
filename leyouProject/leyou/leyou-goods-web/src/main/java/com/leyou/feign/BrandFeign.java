package com.leyou.feign;

import com.leyou.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "item-service")
public interface BrandFeign extends BrandApi {
}
