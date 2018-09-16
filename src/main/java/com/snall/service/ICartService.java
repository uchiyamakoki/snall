package com.snall.service;

import com.snall.common.ServerResponse;

public interface ICartService {

    ServerResponse add(Integer userId,Integer productId,Integer count);
}
