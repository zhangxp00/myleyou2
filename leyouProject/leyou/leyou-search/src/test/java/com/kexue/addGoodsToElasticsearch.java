package com.kexue;

import com.leyou.LeYouSearchApplication;
import com.leyou.item.mydo.SpuDo;
import com.leyou.pojo.ResultPage;
import com.leyou.search.feign.GoodsFeign;
import com.leyou.search.mapper.SearchMapper;
import com.leyou.search.pojo.Goods;
import com.leyou.search.service.GoodsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(classes = LeYouSearchApplication.class)
@RunWith(SpringRunner.class)
public class addGoodsToElasticsearch {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private GoodsFeign goodsFeign;

    @Autowired
    private GoodsService goodsServiceImpl;

    @Autowired
    private SearchMapper goodsMapper;


    @Test
    public void saveGoods() {
        //elasticsearchTemplate.createIndex(Goods.class);
        //elasticsearchTemplate.putMapping(Goods.class);

        Integer page = 1;
        Integer rows = 100;

        do {
            ResultPage<SpuDo> resultPage = goodsFeign.findPage(null, null, page, rows);
            List<SpuDo> items = resultPage.getItems();

            List<Goods> goodsList = items.stream().map(item -> {
                return goodsServiceImpl.getGoodsBySpu(item);
            }).collect(Collectors.toList());
            goodsMapper.saveAll(goodsList);

            page++;
            rows = items.size();
        } while (rows == 100);


    }

    @Test
    public void deleteAll(){
        goodsMapper.deleteAll();
    }
}
