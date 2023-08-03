package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;

    private OrderRepository orderRepository = mock(OrderRepository.class);

    private UserRepository userRepository = mock(UserRepository.class);


    @Before
    public void setUp(){
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);

        Cart cart = new Cart();
        User user = new User();
        user.setUsername("Sarah");
        user.setPassword("1234");
        user.setCart(cart);

        Item item1 = new Item();
        item1.setName("item1");
        item1.setPrice(BigDecimal.valueOf(8777.77));
        item1.setDescription("good item");

        Item item2 = new Item();
        item2.setName("item2");
        item2.setPrice(BigDecimal.valueOf(8445.7));
        item2.setDescription("good item");

        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        cart.setItems(items);
        cart.setTotal(item1.getPrice().add(item2.getPrice()));

        when(userRepository.findByUsername("Sarah")).thenReturn(user);
    }

    @Test
    public void submitTest(){
        ResponseEntity<UserOrder> response = orderController.submit("Sarah");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        UserOrder userOrder = response.getBody();
        assertNotNull(userOrder);
        assertEquals(2, userOrder.getItems().size());
    }

    @Test
    public void getOrdersForUserTest(){
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("Sarah");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<UserOrder> userOrder = response.getBody();
        assertNotNull(userOrder);
    }

}
