package com.leyou.search.service.Impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.item.pojo.*;
import com.leyou.search.feign.BrandFeign;
import com.leyou.search.feign.CategoryFeign;
import com.leyou.search.feign.GoodsFeign;
import com.leyou.search.feign.SpecParamFeign;
import com.leyou.search.pojo.Goods;
import com.leyou.search.service.GoodsService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private CategoryFeign categoryFeign;

    @Autowired
    private BrandFeign brandFeign;

    @Autowired
    private GoodsFeign goodsFeign;

    @Autowired
    private SpecParamFeign specParamFeign;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 根据spu来制作用于查询的Goods对象
     * @param spu
     * @return
     */
    @Override
    public Goods getGoodsBySpu(Spu spu) {
        //获取品牌
        Brand brand = brandFeign.findByBid(spu.getBrandId());
        //获取3级分类  名称
        List<String> name = categoryFeign.findNameByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        //获取对应的sku集合
        List<Sku> skus = goodsFeign.findSkuBySpuId(spu.getId());

        List<Map<String,Object>> skusMap=new ArrayList<>();
        //获取sku价格集合
        List<Long> prices = skus.stream().map(sku -> {
            Map<String, Object> skuMap = new HashMap<>();
            skuMap.put("id",sku.getId());
            skuMap.put("title",sku.getTitle());
            skuMap.put("price",sku.getPrice());
            skuMap.put("image",StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(),",")[0]);
            skusMap.add(skuMap);
            return sku.getPrice();
        }).collect(Collectors.toList());

        //获取对应分类下作为搜索条件的规格参数集合
        List<SpecParam> params = specParamFeign.findByGid(null, spu.getCid3(), null, true);
        //获取到对应的spuDetail
        SpuDetail spuDetail = goodsFeign.findSpuDetailBySpuId(spu.getId());


        Goods goods = new Goods();
        //将商品title以及分类、品牌拼接成一个供搜索用的字段
        goods.setAll(spu.getTitle() + " " + brand.getName() + " " + StringUtils.join(name, " "));
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setId(spu.getId());
        //需要一个spu中所有sku价格的集合，以供搜索
        goods.setPrice(prices);
        //需要对应的sku的集合

        Map<String,Object> SpecsMap=new HashMap<>();

        try {
            //添加json格式的对应的所有的sku集合
            goods.setSkus(OBJECT_MAPPER.writeValueAsString(skusMap));
            //将spu_detail中的通用规格参数由json转换为map对象，方便根据指定的key获取到对应的值
            Map<String, Object> GenericSpecMap = OBJECT_MAPPER.readValue(spuDetail.getGenericSpec(), new TypeReference<Map<String, Object>>() {
            });
            //将spu_detail中的特殊规格参数由json转换为map对象，方便根据指定的key获取到对应的值
            Map<String, Object> SpecialSpecMap = OBJECT_MAPPER.readValue(spuDetail.getSpecialSpec(), new TypeReference<Map<String, Object>>() {
            });

            params.forEach(param -> {
                //判断是否是通用的规格参数
                if (param.getGeneric()) {
                    String value = GenericSpecMap.get(param.getId().toString()).toString();
                    //判断该参数是否是数值类形的
                    if (param.getNumeric()) {
                        value = chooseSegment(value, param);
                    }
                    SpecsMap.put(param.getName(),value);
                }else {
                    Object value = SpecialSpecMap.get(param.getId().toString());
                    SpecsMap.put(param.getName(),value);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        //需要一个Map<String,Object>类形的规格参数集合
        goods.setSpecs(SpecsMap);
        goods.setSubTitle(spu.getSubTitle());
        return goods;
    }

    /**
     * 根据给的数字参数  以及数字范围  返回一个对应的范围字符串
     * @param value
     * @param p
     * @return
     */
    public String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        for (String Segment : p.getSegments().split(",")) {
            String[] Segs = Segment.split("-");
            double begin = NumberUtils.toDouble(Segs[0]);
            double end = Double.MAX_EXPONENT;
            if (Segs.length == 2) {
                end = NumberUtils.toDouble(Segs[1]);
            }
            if (val > begin && val < end) {
                if (Segs.length == 1) {
                    result = begin + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = end + p.getUnit() + "以下";
                } else {
                    result = Segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

}
