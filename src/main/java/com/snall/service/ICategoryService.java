package com.snall.service;

import com.snall.common.ServerResponse;

public interface ICategoryService {

    ServerResponse addCategory(String categoryName,Integer parentId);

    ServerResponse updateCategoryName(Integer categoryId,String categoryName);

}
