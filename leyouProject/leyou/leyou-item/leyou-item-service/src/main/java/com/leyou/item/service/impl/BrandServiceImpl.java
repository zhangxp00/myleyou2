package com.leyou.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import com.leyou.pojo.ResultPage;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandMapper brandMapper;

    @Override
    public ResultPage<Brand> findPage(String key, Integer page, Integer rows, String sortBy, Boolean desc) {
        //初始化Example
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();
        //配置查询条件
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("name", "%" + key + "%").orEqualTo("letter", key);
        }
        //配置分页信息
        PageHelper.startPage(page, rows);
        //配置排序信息
        if (StringUtils.isNotBlank(sortBy)) {
            example.setOrderByClause(sortBy + " " + (desc ? "desc" : "asc"));
        }
        List<Brand> brands = brandMapper.selectByExample(example);
        PageInfo<Brand> brandPageInfo = new PageInfo<>(brands);
        return new ResultPage<Brand>(brandPageInfo.getTotal(), brandPageInfo.getPages(), brandPageInfo.getList());
    }

    @Override
    @Transactional
    public void saveBrand(Brand brand, List<Long> cids) {
        brandMapper.insertSelective(brand);
        cids.forEach(cid -> {
            brandMapper.saveCategoryAndBrand(cid, brand.getId());
        });
    }

    @Override
    public List<Brand> findByCid(Long cid) {
        List<Brand> brands = brandMapper.findByCid(cid);
        return brands;
    }

    @Override
    public Brand findByBid(Long bid) {
        return brandMapper.selectByPrimaryKey(bid);
    }
}
