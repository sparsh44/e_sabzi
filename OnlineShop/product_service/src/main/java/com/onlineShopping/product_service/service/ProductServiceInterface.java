package com.onlineShopping.product_service.service;

import com.onlineShopping.product_service.dto.ProductCreateRequest;
import com.onlineShopping.product_service.dto.ProductResponse;
import com.onlineShopping.product_service.dto.ProductUpdateRequest;
import com.onlineShopping.product_service.modal.Product;

import java.util.List;
import java.util.Optional;

public interface ProductServiceInterface {
    public void createProduct(ProductCreateRequest productRequest);

    public List<ProductResponse> getAllProducts();

    public void deleteProduct(String productId);

    public void updateProduct(ProductUpdateRequest productUpdateRequest) ;

    public ProductResponse getProductById(String productId);

    void updateInventory(List<ProductUpdateRequest> productUpdateRequests);
}
