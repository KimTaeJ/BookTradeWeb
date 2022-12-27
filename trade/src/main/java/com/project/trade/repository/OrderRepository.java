package com.project.trade.repository;

import com.project.trade.controller.OrderForm;
import com.project.trade.domain.Order;

import java.util.List;

public interface OrderRepository {

    OrderForm save(OrderForm order);

    List<Order> findByOwner(Order form);

    List<Order> findByClient(Order form);
}
