package com.caci.technical.test.bricks.service;

import com.caci.technical.test.bricks.exceptions.FailedOrderException;
import com.caci.technical.test.bricks.exceptions.InvalidOrderException;
import com.caci.technical.test.bricks.exceptions.OrderNotFoundException;
import com.caci.technical.test.bricks.model.Order;
import com.caci.technical.test.bricks.repository.OrderRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @Spy
    @InjectMocks
    OrderService orderService;

    @Before
    public void init() {
        Mockito.doReturn(orderRepository).when(orderService).getOrderRepository();
    }

    @Test
    public void givenOrderSuccessfullySavedThenReturnSavedOrderRef() throws InvalidOrderException {
        Order order = new Order(null, 10);
        Mockito.doReturn(new Order(1l, 10)).when(orderRepository).save(order);
        Long actualOrderRef = orderService.submitOrder(order);
        assertTrue(1l == actualOrderRef);
    }


    @Test(expected = FailedOrderException.class)
    public void givenOrderSavingFailureThenThrowException() throws InvalidOrderException {
        Order order = new Order(null, 10);
        Mockito.doReturn(null).when(orderRepository).save(order);
        orderService.submitOrder(order);
    }

    @Test(expected = InvalidOrderException.class)
    public void givenCreateOrderRequestWithZeroBricksThenThrowException() throws InvalidOrderException {
        Order order = new Order(null, 0);
        orderService.submitOrder(order);
    }

    @Test
    public void givenValidOrderRefAndOrderExistsThenReturnOrderDetails() throws OrderNotFoundException {
        Order expectedOrder = new Order(1l, 10);
        Mockito.doReturn(Optional.of(expectedOrder)).when(orderRepository).findById(Mockito.anyLong());
        Order actualOrder = orderService.findOrderById(1l);
        assertEquals(expectedOrder, actualOrder);

    }

    @Test(expected = OrderNotFoundException.class)
    public void givenInvalidOrderRefThenThrowException() throws OrderNotFoundException {
        Order expectedOrder = new Order(1l, 10);
        Mockito.doReturn(Optional.empty()).when(orderRepository).findById(Mockito.anyLong());
        Order actualOrder = orderService.findOrderById(1l);
        assertEquals(expectedOrder, actualOrder);

    }

    @Test
    public void givenRequestToFetchAllOrdersThenReturnAllOrders() {
        Mockito.doReturn(Arrays.asList(new Order(1l, 10), new Order(2l, 20))).when(orderRepository).findAll();
        List<Order> orders = orderService.fetchAllOrders();
        assertEquals(orders.size(), 2);
        assertTrue(orders.get(0).getOrderRef() == 1l);
        assertTrue(orders.get(0).getNumberOfBricks() == 10);
        assertTrue(orders.get(1).getOrderRef() == 2l);
        assertTrue(orders.get(1).getNumberOfBricks() == 20);

    }

    @Test
    public void givenUpdateRequestToExistingOrderRefThenModifyOrder() {
        Order order = new Order(1l, 5);
        Mockito.doReturn(1).when(orderRepository).updateNumberOfBricksOnOrder(order.getNumberOfBricks(), order.getOrderRef());
        Order updatedOrder = orderService.update(order);
        assertNotNull(updatedOrder);
    }

    @Test(expected = FailedOrderException.class)
    public void givenInvalidUpdateRequestToExistingOrderRefThenModifyOrder() {
        Order order = new Order(1l, 5);
        Mockito.doReturn(0).when(orderRepository).updateNumberOfBricksOnOrder(order.getNumberOfBricks(), order.getOrderRef());
        orderService.update(order);

    }
}

