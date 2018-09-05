package com.snall.service;

import com.snall.common.ServerResponse;
import com.snall.pojo.Category;

import java.util.List;
import java.util.Set;

public interface ICategoryService {

    ServerResponse addCategory(String categoryName,Integer parentId);

    ServerResponse updateCategoryName(Integer categoryId,String categoryName);

    ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);

    ServerResponse selectCategoryAndChildrenById(Integer categoryId);





}
