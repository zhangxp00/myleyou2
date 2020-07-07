package com.leyou.item.service.impl;

import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecGroupService;
import com.leyou.item.service.SpecParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecGroupServiceImpl implements SpecGroupService {
    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecParamService specParamServiceImpl;

    @Override
    public List<SpecGroup> findByCid(Long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        List<SpecGroup> specGroups = specGroupMapper.select(specGroup);
        return specGroups;
    }

    @Override
    public List<SpecGroup> findGroupAndParamById(Long cid) {
        List<SpecGroup> groups = this.findByCid(cid);
        groups.forEach(group ->{
            List<SpecParam> params= specParamServiceImpl.findByParam(group.getId(), null, null, null);
            group.setParams(params);
        });
        return groups;
    }
}
