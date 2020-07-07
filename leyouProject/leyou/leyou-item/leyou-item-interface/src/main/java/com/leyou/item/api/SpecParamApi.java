package com.leyou.item.api;

import com.leyou.item.pojo.SpecParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("spec")
public interface SpecParamApi {

    /**
     * 根据不同参数查询对应的   规格参数的详细参数信息
     * @param gid
     * @param cid
     * @param generic
     * @param searching
     * @return
     */
    @GetMapping("params")
    public List<SpecParam> findByGid(
            @RequestParam(value = "gid", required = false) Long gid,
            @RequestParam(value = "cid", required = false) Long cid,
            @RequestParam(value = "generic", required = false) Boolean generic,
            @RequestParam(value = "searching", required = false) Boolean searching
    );
}
