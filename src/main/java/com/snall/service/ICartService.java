package com.snall.service;

import com.snall.common.ServerResponse;
import com.snall.vo.CartVo;

public interface ICartService {

    ServerResponse add(Integer userId,Integer productId,Integer count);

    ServerResponse<CartVo> update(Integer userId,Integer productId,Integer count);

    ServerResponse<CartVo> deleteProduct(Integer userId,String productIds);
}
