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

    //주문 등록
    public Long addOrder(OrderForm order) {
        orderRepository.save(order);
        return order.getOrderid();
    }

    //내가 한 주문
    public List<Order> findReq(Order order) {
        return orderRepository.findByClient(order);
    }

    //받은 주문
    public List<Order> findQ(Order order) {
        return orderRepository.findByOwner(order);
    }

}
