package com.leyou.service;

import com.leyou.pojo.Cart;

import java.util.List;

public interface CartService {
    /**
     * 添加购物车
     * @param cart
     */
    void addCart(Cart cart);

    /**
     * 查询购物车
     * @return
     */
    List<Cart> findCart();

    /**
     * 修改购物车
     * @param skuId
     * @return
     */
    Integer updateCart(Long skuId,Integer num);

    /**
     * 删除购物车
     * @param skuId
     * @return
     */
    Integer deleteCart(Long skuId);
}
