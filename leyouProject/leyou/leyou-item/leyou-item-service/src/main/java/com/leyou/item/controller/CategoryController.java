package com.leyou.item.controller;

import com.leyou.item.pojo.Category;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryServiceImpl;

    /**
     * 根据节点id查询同一级别的节点
     * @param pid
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<List<Category>> findByPid(@RequestParam(value = "pid", defaultValue = "0") Long pid) {
        System.out.println("过来了");
        if (pid == null || pid < 0) {
            //400 参数不合法
            //ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            return ResponseEntity.badRequest().build();
        }
        List<Category> categories = categoryServiceImpl.findByPid(pid);
        if (CollectionUtils.isEmpty(categories)) {
            //404 服务资源未找到
            return ResponseEntity.notFound().build();
        }
        //200 查询成功
        return ResponseEntity.ok(categories);
    }

    /**
     *根据多个is查询多个分类信息的名字
     * @param ids
     * @return
     */
    @GetMapping
    public ResponseEntity<List<String>> findNameByIds(@RequestParam(value = "ids") List<Long> ids){
        List<String> names = categoryServiceImpl.findNameByIds(ids);
        if(CollectionUtils.isEmpty(names)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(names);
    }
}
