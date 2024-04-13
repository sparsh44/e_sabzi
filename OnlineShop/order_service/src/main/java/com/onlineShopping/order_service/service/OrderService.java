package com.onlineShopping.order_service.service;

import com.onlineShopping.order_service.dto.OrderResponse;
import com.onlineShopping.order_service.dto.ProductDto;
import com.onlineShopping.order_service.dto.OrderRequest;
import com.onlineShopping.order_service.modal.Order;
import com.onlineShopping.order_service.modal.Product;
import com.onlineShopping.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import org.springframework.http.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService implements OrderServiceInterface{
    private final OrderRepository orderRepository;

    String updateInventory(List<Product> productsList){
        //send the productList is request Body
        RestTemplate restTemplate = new RestTemplate();
        String productServiceUrl = "http://localhost:8083/api/product/updateInventory";

        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create request entity
        HttpEntity<List<Product>> requestEntity = new HttpEntity<>(productsList, headers);
        System.out.println(productsList);

        // Make POST request to update inventory
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                productServiceUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        // Get response body
        String response = responseEntity.getBody();
        System.out.println(response + responseEntity);
        return response;
    }

    @Transactional
    public Order placeOrder(OrderRequest orderRequest){
        try {
            Order order = new Order();
            BigDecimal totalPrice = BigDecimal.ZERO;

            List<Product> orderList = orderRequest.getOrderList();

            if(orderList.isEmpty()){
                throw new RuntimeException("Please add products to the cart!");
            }

            for (Product product : orderList) {
                BigDecimal productPrice = product.getPrice();
                BigDecimal productQuantity = product.getQuantity();
                BigDecimal productTotalCost = productPrice.multiply(productQuantity);
                totalPrice = totalPrice.add(productTotalCost);
            }

            order.setTotalAmount(totalPrice);
            order.setBuyerId(orderRequest.getBuyerId());
            order.setOrderList(orderList);

            System.out.println("Order"+order.getTotalAmount());
            //Update the Inventory in Product Service
            String response = updateInventory(orderList);

            if(!Objects.equals(response, "Inventory updated successfully")){
                throw new RuntimeException("Order Not Placed, Revisit your Cart!");
            }
            Order savedOrder = orderRepository.save(order);
            log.info("Order placed successfully: {}", savedOrder);
            return savedOrder;
        } catch (RuntimeException e) {
            if(Objects.equals(e.getMessage(), "Please add products to the cart!")) {
                throw new RuntimeException("Please add products to the cart!");
            }
            else{
                throw new RuntimeException("Failed To Place Order!");
            }
        }
    }


    @Override
    public String cancelOrder(Long orderId) {
        return null;
    }


    @Override
    public OrderResponse getOrderDetails(Long orderId) {
        try {
            Optional<Order> orderOptional = orderRepository.findById(orderId);
            if (orderOptional.isPresent()) {
                Order order = orderOptional.get();
                return mapToOrderResponse(order);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Error occurred while fetching order details with ID {}: {}", orderId, e.getMessage());
            throw new RuntimeException("Failed to fetch order details");
        }
    }

    private OrderResponse mapToOrderResponse(Order order) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderId(order.getOrderId());
        orderResponse.setOrderList(order.getOrderList());
        orderResponse.setBuyerId(order.getBuyerId());
        orderResponse.setTotalAmount(order.getTotalAmount());
        return orderResponse;
    }

}
