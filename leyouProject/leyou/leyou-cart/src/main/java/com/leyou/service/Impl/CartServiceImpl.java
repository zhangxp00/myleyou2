package com.leyou.service.Impl;

import com.leyou.cartFilter.CartInterceptor;
import com.leyou.common.pojo.UserInfo;
import com.leyou.feign.GoodsFegin;
import com.leyou.item.pojo.Sku;
import com.leyou.pojo.Cart;
import com.leyou.service.CartService;
import com.leyou.utils.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private GoodsFegin goodsFegin;

    private final String KEY_PREFIX = "user:cart:";

    @Override
    public void addCart(Cart cart) {
        //获取用户
        UserInfo userInfo = CartInterceptor.getUserInfo();

        String key = KEY_PREFIX + userInfo.getId();

        Long skuId = cart.getSkuId();
        Integer num = cart.getNum();
        //查询购物车
        BoundHashOperations<String, Object, Object> HashOperations = redisTemplate.boundHashOps(key);
        //判断购物车里面是否含有该数据
        if (HashOperations.hasKey(skuId.toString())) {
            //含有，更新数量
            String skuJson = HashOperations.get(skuId.toString()).toString();
            cart = JsonUtils.parse(skuJson, Cart.class);
            cart.setNum(cart.getNum() + num);
        } else {
            //不含，添加
            Sku sku = goodsFegin.findSkuBySkuId(skuId);
            cart.setImage(StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(), ",")[0]);
            cart.setOwnSpec(sku.getOwnSpec());
            cart.setPrice(sku.getPrice());
            cart.setTitle(sku.getTitle());
            cart.setUserId(userInfo.getId());
        }
        HashOperations.put(skuId.toString(), JsonUtils.serialize(cart));
    }

    @Override
    public List<Cart> findCart() {
        UserInfo userInfo = CartInterceptor.getUserInfo();
        if (!redisTemplate.hasKey(KEY_PREFIX + userInfo.getId())) {
            return null;
        }
        BoundHashOperations<String, Object, Object> HashOperations = redisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());

        List<Object> JsonCarts = HashOperations.values();
        if (CollectionUtils.isEmpty(JsonCarts)) {
            return null;
        }
        return JsonCarts.stream().map(JsonCart -> {
            return JsonUtils.parse(JsonCart.toString(), Cart.class);
        }).collect(Collectors.toList());
    }

    @Override
    public Integer updateCart(Long skuId, Integer num) {
        UserInfo userInfo = CartInterceptor.getUserInfo();
        BoundHashOperations<String, Object, Object> HashOperations = redisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());
        String JsonSku = HashOperations.get(skuId.toString()).toString();
        Cart cart = JsonUtils.parse(JsonSku, Cart.class);
        cart.setNum(num);
        try {
            HashOperations.put(skuId.toString(), JsonUtils.serialize(cart));
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Integer deleteCart(Long skuId) {
        UserInfo userInfo = CartInterceptor.getUserInfo();
        BoundHashOperations<String, Object, Object> HashOperations = redisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());
        try {
            HashOperations.delete(skuId.toString());
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
