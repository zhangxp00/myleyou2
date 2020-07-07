package com.leyou.feign;

import com.leyou.item.api.GroupApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "item-service")
public interface GroupFeign extends GroupApi {
}
