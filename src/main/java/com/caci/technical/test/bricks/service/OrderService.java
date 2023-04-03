package com.caci.technical.test.bricks.service;

import com.caci.technical.test.bricks.exceptions.FailedOrderException;
import com.caci.technical.test.bricks.exceptions.InvalidOrderException;
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
    public static final String INVALID_ORDER_PAYLOAD_MSG = "Number of bricks on order cannot be less than zero";

    private OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Long submitOrder(Order order) throws InvalidOrderException {
        if (order.getNumberOfBricks() <= 0) {
            throw new InvalidOrderException(INVALID_ORDER_PAYLOAD_MSG);
        }
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
        int updateSuccess = getOrderRepository().updateNumberOfBricksOnOrder(order.getNumberOfBricks(), order.getOrderRef());
        if (updateSuccess == 1) {
            return order;
        } else {
            throw new FailedOrderException(String.format("Failed to update order %s", order.getOrderRef()));
        }
    }

    public OrderRepository getOrderRepository() {
        return orderRepository;
    }
}
