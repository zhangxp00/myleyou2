package com.leyou.item.service.impl;

import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> findByPid(Long pid) {
        Category category = new Category();
        category.setParentId(pid);
        return categoryMapper.select(category);
    }

    @Override
    public List<Category> findByIds(List<Long> ids) {
        List<Category> categories = categoryMapper.selectByIdList(ids);
        return categories;
    }

    @Override
    public List<String> findNameByIds(List<Long> ids) {

        List<Category> categories = categoryMapper.selectByIdList(ids);
        List<String> categoryNames = categories.stream().map(category -> {
            return category.getName();
        }).collect(Collectors.toList());

        return categoryNames;
    }
}
