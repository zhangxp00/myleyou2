package com.leyou.item.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("category")
public interface CategoryApi {

    /**
     *根据多个is查询多个分类信息的名字
     * @param ids
     * @return
     */
    @GetMapping
    public List<String> findNameByIds(@RequestParam(value = "ids") List<Long> ids);
}
