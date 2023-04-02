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

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BricksCompanyApplicationTests {

    public static final String ORDER_CREATION_ENDPOINT = "/order/create";
    public static final String FIND_ORDER_BY_ID_ENDPOINT = "/order/1/";
    public static final String FIND_ALL_ORDERS = "/order/fetchAll";


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


    @Test
    public void givenApiCallToGetValidOrderRefThenReturnStatusOk() throws Exception {
        Order expectedOrder = new Order(1l, 10);
        Mockito.doReturn(Optional.of(expectedOrder)).when(orderRepository).findById(Mockito.anyLong());
        this.mockMvc.perform(get(FIND_ORDER_BY_ID_ENDPOINT)).andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void givenApiCallToGetInValidOrderRefThenReturnStatusNotFound() throws Exception {
        Mockito.doReturn(Optional.empty()).when(orderRepository).findById(Mockito.anyLong());
        this.mockMvc.perform(get(FIND_ORDER_BY_ID_ENDPOINT)).andDo(print()).andExpect(status().isNotFound()).andExpect(content().string(containsString("Order 1 does not exist")));
    }

    @Test
    public void givenApiCallToGetAllOrdersThenReturnAllOrdersInDB() throws Exception {
        Mockito.doReturn(Arrays.asList(new Order(1l,10),new Order(2l,20))).when(orderRepository).findAll();
        this.mockMvc.perform(get(FIND_ALL_ORDERS)).andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString("orderRef")));
    }


    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}




