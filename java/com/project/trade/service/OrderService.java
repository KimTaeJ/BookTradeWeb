package com.project.trade.service;

import com.project.trade.controller.OrderForm;
import com.project.trade.domain.Order;
import com.project.trade.repository.OrderRepository;

import java.util.List;

public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Long addOrder(OrderForm order) {
        orderRepository.save(order);
        return order.getOrderid();
    }

    public List<Order> findReq(Order order) {
        return orderRepository.findByClient(order);
    }

    public List<Order> findQ(Order order) {
        return orderRepository.findByOwner(order);
    }

}
