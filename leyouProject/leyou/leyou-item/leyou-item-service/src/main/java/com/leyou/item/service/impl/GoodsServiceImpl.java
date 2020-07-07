package com.leyou.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.item.mapper.*;
import com.leyou.item.mydo.SpuDo;
import com.leyou.item.pojo.*;
import com.leyou.item.service.CategoryService;
import com.leyou.item.service.GoodsService;
import com.leyou.pojo.ResultPage;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private CategoryService categoryServiceImpl;

    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 根据条件分页查询商品
     *
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    @Override
    public ResultPage<SpuDo> findPage(String key, Boolean saleable, Integer page, Integer rows) {
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();

        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("title", "%" + key + "%");
        }
        if (saleable != null) {
            criteria.andEqualTo("saleable", saleable);
        }
        PageHelper.startPage(page, rows);

        List<Spu> spus = spuMapper.selectByExample(example);
        PageInfo<Spu> pageInfo = new PageInfo<>(spus);

        //将一个集合转换为另一个集合
        List<SpuDo> spuDos = spus.stream().map(spu -> {
            //循环集合  将spu的所有参数复制给spuDo
            SpuDo spuDo = new SpuDo();
            BeanUtils.copyProperties(spu, spuDo);
            //设置cname
            List<Category> categories = categoryServiceImpl.findByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));

            List<String> categoryNames = categories.stream().map(category -> {
                String name = category.getName();
                return name;
            }).collect(Collectors.toList());
            spuDo.setCname(StringUtils.join(categoryNames, "-"));

            //设置bname
            Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());
            spuDo.setBname(brand.getName());
            return spuDo;
        }).collect(Collectors.toList());

        return new ResultPage<SpuDo>(pageInfo.getTotal(), pageInfo.getPages(), spuDos);
    }

    /**
     * 添加商品
     *
     * @param spuDo
     */
    @Override
    @Transactional
    public void saveGoods(SpuDo spuDo) {
        //添加spu
        spuDo.setId(null);
        spuDo.setSaleable(true);
        spuDo.setValid(true);
        spuDo.setCreateTime(new Date());
        spuDo.setLastUpdateTime(spuDo.getCreateTime());
        spuMapper.insertSelective(spuDo);
        //添加spu_de
        spuDo.getSpuDetail().setSpuId(spuDo.getId());
        spuDetailMapper.insertSelective(spuDo.getSpuDetail());

        saveSkuAndStock(spuDo);
        //进行rabbit数据同步，发送消息到队列
        rabbitSend("item.insert", spuDo.getId());
    }

    /**
     * 抽取出来的rabbit发送方的方法
     *
     * @param myRoutingKey
     * @param id
     */
    private void rabbitSend(String myRoutingKey, Long id) {
        try {
            if (StringUtils.isBlank(myRoutingKey) || id == null) {
                return;
            }
            rabbitTemplate.convertAndSend(myRoutingKey, id);
        } catch (AmqpException e) {
            e.printStackTrace();
        }
    }

    /**
     * 抽取的 保存sku以及库存的方法
     *
     * @param spuDo
     */
    private void saveSkuAndStock(SpuDo spuDo) {
        List<Sku> skus = spuDo.getSkus();
        skus.forEach(sku -> {
            //添加sku
            sku.setId(null);
            sku.setSpuId(spuDo.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            skuMapper.insertSelective(sku);
            //添加stock
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stockMapper.insertSelective(stock);
        });
    }

    /**
     * 根据spuId获取对应的spuDetail
     *
     * @param id
     * @return
     */
    @Override
    public SpuDetail findSpuDetailBySpuId(Long id) {
        SpuDetail spuDetail = spuDetailMapper.selectByPrimaryKey(id);
        return spuDetail;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public List<Sku> findSkuBySpuId(Long id) {
        Sku sku = new Sku();
        sku.setSpuId(id);
        List<Sku> skus = skuMapper.select(sku);
        skus.forEach(sku1 -> {
            Stock stock = stockMapper.selectByPrimaryKey(sku1.getId());
            sku1.setStock(stock.getStock());
        });
        return skus;
    }

    @Override
    @Transactional
    public void updateGoods(SpuDo spuDo) {
        Sku sku = new Sku();
        sku.setSpuId(spuDo.getId());
        List<Sku> skus = skuMapper.select(sku);
        //删除stock里相关的所以信息
        skus.forEach(sku1 -> {
            stockMapper.deleteByPrimaryKey(sku1.getId());
        });
        //删除sku里所有相关的信息
        skuMapper.delete(sku);
        //添加sku以及stock
        saveSkuAndStock(spuDo);
        //添加spu以及spuDetail
        spuDo.setCreateTime(null);
        spuDo.setLastUpdateTime(new Date());
        spuDo.setValid(null);
        spuDo.setSaleable(null);
        spuMapper.updateByPrimaryKeySelective(spuDo);

        spuDetailMapper.updateByPrimaryKeySelective(spuDo.getSpuDetail());

        //rabbit实现数据的同步
        this.rabbitSend("item.update", spuDo.getId());
    }

    @Override
    public Spu findSpuById(Long id) {
        return spuMapper.selectByPrimaryKey(id);
    }

    @Override
    public Sku findSkuBySkuId(Long skuId) {
        return skuMapper.selectByPrimaryKey(skuId);
    }
}
