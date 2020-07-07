package com.leyou.item.mapper;

import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends Mapper<Brand> {

    @Insert("insert into tb_category_brand(category_id,brand_id) values(#{cid},#{bid})")
    public Integer saveCategoryAndBrand(@Param(value = "cid") Long cid,@Param(value = "bid") Long bid);

    @Select("SELECT * FROM tb_brand b INNER JOIN tb_category_brand cb ON b.`id`=cb.`brand_id` WHERE category_id=#{cid}")
    List<Brand> findByCid(Long cid);

}
