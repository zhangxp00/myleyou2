package com.leyou.item.controller;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.service.SpecGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("spec")
public class SpecGroupController {
    @Autowired
    private SpecGroupService specGroupServiceImpl;

    /**
     * 根据商品的分类id查询对应的  规格参数组信息
     *
     * @param cid
     * @return
     */
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> findByCid(@PathVariable(value = "cid") Long cid) {
        List<SpecGroup> specGroups = specGroupServiceImpl.findByCid(cid);
        if (CollectionUtils.isEmpty(specGroups)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(specGroups);
    }

    /**
     * 根据cid查询对应的组以及组内的信息
     * @param cid
     * @return
     */
    @GetMapping("{cid}")
    public ResponseEntity<List<SpecGroup>> findGroupAndParamById(@PathVariable(value = "cid") Long cid) {
        List<SpecGroup> specGroups = specGroupServiceImpl.findGroupAndParamById(cid);
        if (CollectionUtils.isEmpty(specGroups)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(specGroups);
    }
}
