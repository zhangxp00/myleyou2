package com.leyou.cartConfig;

import com.leyou.cartFilter.CartInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CartWebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private CartInterceptor cartInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(cartInterceptor).addPathPatterns("/**");
    }
}
