package com.leyou.feign;

import com.leyou.item.api.SpecParamApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "item-service")
public interface SpecParamFeign extends SpecParamApi {
}
