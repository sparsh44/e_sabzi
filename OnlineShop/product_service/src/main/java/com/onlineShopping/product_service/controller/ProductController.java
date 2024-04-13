package com.onlineShopping.product_service.controller;

import com.onlineShopping.product_service.dto.ProductCreateRequest;
import com.onlineShopping.product_service.dto.ProductResponse;
import com.onlineShopping.product_service.dto.ProductUpdateRequest;
import com.onlineShopping.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody ProductCreateRequest productCreateRequest) {
        try {
            productService.createProduct(productCreateRequest);
            return new ResponseEntity<>("Product created successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create product", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable String productId) {
        try {
            ProductResponse productResponse = productService.getProductById(productId);
            if (productResponse != null) {
                return new ResponseEntity<>(productResponse, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to fetch product", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        try {
            List<ProductResponse> products = productService.getAllProducts();
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to fetch products", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable String productId) {
        try {
            productService.deleteProduct(productId);
            return new ResponseEntity<>("Product deleted successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Product not found for delete")) {
                return new ResponseEntity<>("Product not found for delete", HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>("Failed to delete product", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @PutMapping
    public ResponseEntity<String> updateProduct(@RequestBody ProductUpdateRequest productUpdateRequest) {
        try {
            productService.updateProduct(productUpdateRequest);
            return new ResponseEntity<>("Product updated successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Failed to update product", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/updateInventory")
    public ResponseEntity<String> updateInventory(@RequestBody List<ProductUpdateRequest> productUpdateRequests) {
        try {
            productService.updateInventory(productUpdateRequests);
            return new ResponseEntity<>("Inventory updated successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            if (e.getMessage() == "Please change the quantity!") {
                return new ResponseEntity<>("Please change the quantity!", HttpStatus.INTERNAL_SERVER_ERROR);
            } else if (e.getMessage() == "Failed to update inventory!") {
                return new ResponseEntity<>("Failed to update inventory!", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            else{
                return  new ResponseEntity<>("Product not found", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
}
