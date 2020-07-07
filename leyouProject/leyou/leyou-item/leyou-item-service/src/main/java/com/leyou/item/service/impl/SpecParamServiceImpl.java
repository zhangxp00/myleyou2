package com.leyou.item.service.impl;

import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class SpecParamServiceImpl implements SpecParamService {
    @Autowired
    private SpecParamMapper specParamMapper;

    @Override
    public List<SpecParam> findByParam(Long gid, Long cid, Boolean generic, Boolean searching) {
        /*SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        List<SpecParam> specParams = specParamMapper.select(specParam);*/

        Example example = new Example(SpecParam.class);
        Example.Criteria criteria = example.createCriteria();

        if(gid!=null){
            criteria.andEqualTo("groupId",gid);
        }
        if(cid!=null){
            criteria.andEqualTo("cid",cid);
        }
        if(generic!=null){
            criteria.andEqualTo("generic",generic);
        }
        if(searching!=null){
            criteria.andEqualTo("searching",searching);
        }

        List<SpecParam> specParams = specParamMapper.selectByExample(example);
        return specParams;
    }
}
