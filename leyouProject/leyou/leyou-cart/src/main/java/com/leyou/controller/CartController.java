package com.leyou.controller;

import com.leyou.pojo.Cart;
import com.leyou.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {

    @Autowired
    private CartService cartServiceImpl;

    /**
     * 添加购物车
     *
     * @param cart
     */
    @PostMapping
    public ResponseEntity<Void> addCart(@RequestBody Cart cart) {
        System.out.println(cart);
        cartServiceImpl.addCart(cart);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 查询购物车
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<List<Cart>> findCart() {
        List<Cart> carts = cartServiceImpl.findCart();
        if (CollectionUtils.isEmpty(carts)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(carts);
    }

    /**
     * 修改购物车
     *
     * @param skuId
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateCart(@RequestParam(value = "skuId") Long skuId, @RequestParam(value = "num") Integer num) {
        Integer res = cartServiceImpl.updateCart(skuId, num);
        if (res == 1) {
            return ResponseEntity.ok(null);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 删除购物车
     *
     * @param skuId
     * @return
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteCart(@RequestParam(value = "skuId") Long skuId) {
        Integer del = cartServiceImpl.deleteCart(skuId);
        if (del == 1) {
            return ResponseEntity.ok(null);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/storeCarts")
    public ResponseEntity<List<Integer>> storeCartsToRedis(@RequestBody List<Cart> storeCarts) {
        System.out.println("storeCarts = " + storeCarts);

        if (CollectionUtils.isEmpty(storeCarts)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        ArrayList<Integer> indexs = new ArrayList<>();
        for (int i = 0; i < storeCarts.size(); i++) {
            ResponseEntity<Void> voidResponseEntity = addCart(storeCarts.get(i));
            if (voidResponseEntity.getStatusCodeValue() >= 200 && voidResponseEntity.getStatusCodeValue() < 300) {
                indexs.add(i);
            }
        }
        return ResponseEntity.ok(indexs);
    }
}
