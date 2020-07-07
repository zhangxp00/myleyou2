package com.leyou.feign;

import com.leyou.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("user-service")
public interface UserFeign extends UserApi {
}
