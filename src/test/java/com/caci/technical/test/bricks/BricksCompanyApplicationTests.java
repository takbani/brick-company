package com.caci.technical.test.bricks;


import com.caci.technical.test.bricks.controller.OrderController;
import com.caci.technical.test.bricks.model.Order;
import com.caci.technical.test.bricks.repository.OrderRepository;
import com.caci.technical.test.bricks.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BricksCompanyApplicationTests {

    public static final String ORDER_CREATION_ENDPOINT = "/order/create";


    @Autowired
    MockMvc mockMvc;

    @Autowired
    @InjectMocks
    OrderController orderController;

    @MockBean
    OrderRepository orderRepository;

    @SpyBean
    OrderService orderService;

    @Test
    public void contextLoads() {
        assertThat(orderController).isNotNull();
    }

    @Test
    public void givenRequestToCreateOrderThenAssertStatusCreatedOnSuccess() throws Exception {
        Order order = new Order(null, 10);
        Mockito.doReturn(orderRepository).when(orderService).getOrderRepository();
        Mockito.doReturn(new Order(1l, 10)).when(orderRepository).save(Mockito.any());
        this.mockMvc.perform(post(ORDER_CREATION_ENDPOINT)
                .content(asJsonString(order))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void givenRequestToCreateOrderThenAssertInternalServerErrorOnFailure() throws Exception {
        Order order = new Order(null, 10);
        Mockito.doReturn(null).when(orderRepository).save(order);
        this.mockMvc.perform(post(ORDER_CREATION_ENDPOINT)
                .content(asJsonString(order))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }


    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}




