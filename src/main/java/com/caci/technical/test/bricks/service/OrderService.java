package com.caci.technical.test.bricks.service;

import com.caci.technical.test.bricks.exceptions.FailedOrderException;
import com.caci.technical.test.bricks.exceptions.OrderNotFoundException;
import com.caci.technical.test.bricks.model.Order;
import com.caci.technical.test.bricks.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    public static final String ERROR_MSG_FOR_ORDER_FAILURE = "An error occurred while saving the order";

    private OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Long submitOrder(Order order) {
        Order savedOrder = getOrderRepository().save(order);
        return Optional.ofNullable(savedOrder).map(Order::getOrderRef).orElseThrow(() -> new FailedOrderException(ERROR_MSG_FOR_ORDER_FAILURE));

    }

    public Order findOrderById(long orderRef) throws OrderNotFoundException {
        return getOrderRepository().findById(orderRef).orElseThrow(() -> new OrderNotFoundException(String.format("Order %s does not exist", orderRef)));
    }

    public List<Order> fetchAllOrders() {
        List<Order> ordersList = new ArrayList<>();
        Iterable<Order> orders = getOrderRepository().findAll();
        Iterator<Order> orderIterator = orders.iterator();
        while (orderIterator.hasNext()) {
            ordersList.add(orderIterator.next());
        }

        return ordersList;

    }

    @Transactional
    public Order update(Order order) {
        return getOrderRepository().save(order);
    }

    public OrderRepository getOrderRepository() {
        return orderRepository;
    }
}
