package com.nhatquang99.api.mapper;

import com.nhatquang99.api.model.Category;
import com.nhatquang99.api.model.Product;
import com.nhatquang99.api.payload.request.CategoryRequest;
import com.nhatquang99.api.payload.response.CategoryResponse;
import com.nhatquang99.api.payload.response.ProductResponse;

import java.util.ArrayList;
import java.util.List;

public class CategoryMapper {
    public CategoryResponse toCategoryRes(Category category) {
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(category.getId());
        categoryResponse.setTypeCode(category.getCode());
        categoryResponse.setTypeName(category.getName());

        return categoryResponse;
    }

    public Category toCategory(CategoryRequest categoryReq) {
        Category category = new Category();
        category.setCode(categoryReq.getTypeCode());
        category.setName(categoryReq.getTypeName());
        category.setParentId(categoryReq.getCategoryParentID());

        return category;
    }
}
