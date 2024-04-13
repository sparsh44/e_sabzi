package com.onlineShopping.product_service.service;

import com.onlineShopping.product_service.dto.ProductCreateRequest;
import com.onlineShopping.product_service.dto.ProductResponse;
import com.onlineShopping.product_service.dto.ProductUpdateRequest;
import com.onlineShopping.product_service.modal.Product;
import com.onlineShopping.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService implements ProductServiceInterface {

    private final ProductRepository productRepository;

    @Override
    public void createProduct(ProductCreateRequest productRequest) {
        try {
            Product product = Product.builder()
                    .name(productRequest.getName())
                    .description(productRequest.getDescription())
                    .price(productRequest.getPrice())
                    .farmerId(productRequest.getFarmerId())
                    .quantity(productRequest.getQuantity())
                    .build();

            productRepository.save(product);
            log.info("Product {} is saved!", product.getId());
        } catch (Exception e) {
            log.error("Error occurred while creating product: {}", e.getMessage());
            throw new RuntimeException("Failed to create product");
        }
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        try {
            List<Product> products = productRepository.findAll();
            return products.stream().map(this::mapToProductResponse).toList();
        } catch (Exception e) {
            log.error("Error occurred while fetching all products: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch products");
        }
    }

    @Override
    public void deleteProduct(String productId) {
        try {
            Optional<Product> productToDelete = productRepository.findById(productId);
            if (productToDelete.isPresent()) {
                productRepository.deleteById(productId);
            } else {
                throw new RuntimeException("Product not found for delete");
            }
        } catch (Exception e) {
            log.error("Error occurred while deleting product with ID {}: {}", productId, e.getMessage());
            throw new RuntimeException("Failed to delete product");
        }
    }

    @Override
    public void updateProduct(ProductUpdateRequest productUpdateRequest) {
        try {
            Optional<Product> productToUpdate = productRepository.findById(productUpdateRequest.getId());
            if (productToUpdate.isPresent()) {
                Product product = Product.builder()
                        .id(productUpdateRequest.getId())
                        .name(productUpdateRequest.getName())
                        .description(productUpdateRequest.getDescription())
                        .price(productUpdateRequest.getPrice())
                        .quantity(productUpdateRequest.getQuantity())
                        .farmerId(productUpdateRequest.getFarmerId())
                        .build();
                productRepository.save(product);
            }
            else{
                throw new RuntimeException("Product not found for update");
            }
        } catch (RuntimeException e) {
            log.error("Error occurred while updating product: {}", e.getMessage());
            throw new RuntimeException("Failed to update product : "+ e.getMessage());
        }
    }

    @Override
    public ProductResponse getProductById(String productId) {
        try {
            Optional<Product> productOptional = productRepository.findById(productId);
            if (productOptional.isPresent()) {
                Product product = productOptional.get();
                return mapToProductResponse(product);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Error occurred while fetching product with ID {}: {}", productId, e.getMessage());
            throw new RuntimeException("Failed to fetch product");
        }
    }

    @Override
    public void updateInventory(List<ProductUpdateRequest> productUpdateRequests) {
        //List of toBeDeleted by productId because remaining quantity is 0
        List<String>toBeDeleted = new ArrayList<>();

        //List of toBeUpdated by updateRequest because remaining quantity is > 0
        List<ProductUpdateRequest>toBeUpdated = new ArrayList<>();

        for(ProductUpdateRequest productUpdateRequest : productUpdateRequests){
            log.error("productUpdateRequest ", productUpdateRequest );
            System.out.println(productUpdateRequest);
            try{
                ProductResponse productResponse = getProductById(productUpdateRequest.getId());
                if (productResponse != null) {
                    if(productResponse.getQuantity().compareTo(productUpdateRequest.getQuantity()) > 0){
                        productUpdateRequest.setQuantity(productResponse.getQuantity().subtract(productUpdateRequest.getQuantity()));
                        toBeUpdated.add(productUpdateRequest);
                    }
                    else if(productResponse.getQuantity().compareTo(productUpdateRequest.getQuantity()) == 0){
                        toBeDeleted.add(productUpdateRequest.getId());
                    }
                    else{
                        log.error("Please change the quantity!");
                        throw new RuntimeException("Please change the quantity!");
                    }
                } else {
                    log.error("Product not found");
                    throw new RuntimeException("Product not found");
                }
            }
            catch (RuntimeException e){
                log.error("Failed to update inventory!");
                throw new RuntimeException("Failed to update inventory!");
            }
        }

        for(ProductUpdateRequest productUpdateRequest: toBeUpdated){
            updateProduct(productUpdateRequest);
            log.info("Inventory updated for {} by changing quantity", productUpdateRequest.getId());
        }
        for (String deleteProduct: toBeDeleted){
            deleteProduct(deleteProduct);
            log.info("Inventory updated for {} by deleting product", deleteProduct);
        }
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .quantity(product.getQuantity())
                .farmerId(product.getFarmerId())
                .build();
    }
}
