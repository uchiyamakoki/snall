package com.snall.service;

import com.snall.common.ServerResponse;
import com.snall.pojo.Product;

public interface IProductService {

    ServerResponse saveOrUpdateProduct(Product product);

    ServerResponse<String> setSaleStatus(Integer productId,Integer status);
}
