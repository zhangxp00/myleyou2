package com.leyou.item.controller;

import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("spec")
public class SpecParamController {
    @Autowired
    private SpecParamService specParamServiceImpl;

    /**
     * 根据不同参数查询对应的   规格参数的详细参数信息
     * @param gid
     * @param cid
     * @param generic
     * @param searching
     * @return
     */
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> findByGid(
            @RequestParam(value = "gid", required = false) Long gid,
            @RequestParam(value = "cid", required = false) Long cid,
            @RequestParam(value = "generic", required = false) Boolean generic,
            @RequestParam(value = "searching", required = false) Boolean searching
    ) {
        List<SpecParam> specParams = specParamServiceImpl.findByParam(gid, cid, generic, searching);
        if (CollectionUtils.isEmpty(specParams)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(specParams);
    }
}
