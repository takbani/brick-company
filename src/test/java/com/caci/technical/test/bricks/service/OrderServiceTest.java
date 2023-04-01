package com.caci.technical.test.bricks.service;

import com.caci.technical.test.bricks.exceptions.FailedOrderException;
import com.caci.technical.test.bricks.model.Order;
import com.caci.technical.test.bricks.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @Spy
    @InjectMocks
    OrderService orderService;


    @Test
    public void givenOrderSuccessfullySavedThenReturnSavedOrderRef(){
        Order order = new Order(null,10);
        Mockito.doReturn(orderRepository).when(orderService).getOrderRepository();
        Mockito.doReturn(new Order(1l,10)).when(orderRepository).save(order);
        Long actualOrderRef = orderService.submitOrder(order);
        assertTrue(1l == actualOrderRef);
    }


    @Test(expected = FailedOrderException.class)
    public void givenOrderSavingFailureThenThrowException(){
        Order order = new Order(null,10);
        Mockito.doReturn(orderRepository).when(orderService).getOrderRepository();
        Mockito.doReturn(null).when(orderRepository).save(order);
        orderService.submitOrder(order);
    }
}
