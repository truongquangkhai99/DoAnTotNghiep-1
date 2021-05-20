package com.nhatquang99.api.mapper;

import com.nhatquang99.api.model.Product;
import com.nhatquang99.api.payload.response.ProductResponse;

import java.util.ArrayList;
import java.util.List;

public class ProductMapper {
    public ProductResponse toProductRes(Product product) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(product.getId());
        productResponse.setDescription(product.getContent());
        productResponse.setPrices(product.getPrice());
        productResponse.setCategoryCode(product.getCategory().getCode());
        productResponse.setNameProduct(product.getName());
        productResponse.setQuantity(product.getQuantity());
        productResponse.setTypeCode(product.getType());

        List<String> listImages = new ArrayList<>();
        product.getListImage().forEach(productImage -> listImages.add(productImage.getUrl()));
        productResponse.setImgUrl(listImages);

        return productResponse;
    }

    public List<ProductResponse> toListProductRes(List<Product> products) {
        List<ProductResponse> productResponses = new ArrayList<>();
        products.forEach(product -> productResponses.add(toProductRes(product)));
        return productResponses;
    }
}
