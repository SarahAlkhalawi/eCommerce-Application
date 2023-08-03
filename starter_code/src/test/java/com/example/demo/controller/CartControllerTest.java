package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController controller;

    private UserRepository userRepository = mock(UserRepository.class);


    private CartRepository cartRepository = mock(CartRepository.class);


    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp(){
        controller = new CartController();
        TestUtils.injectObjects(controller, "userRepository", userRepository);
        TestUtils.injectObjects(controller, "cartRepository", cartRepository);
        TestUtils.injectObjects(controller, "itemRepository", itemRepository);


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
        when(itemRepository.findById(1l)).thenReturn(Optional.of(item1));
        when(itemRepository.findById(2l)).thenReturn(Optional.of(item2));
    }


    @Test
    public void addTocartTest(){
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("Sarah");
        request.setItemId(1l);
        request.setQuantity(1);
        ResponseEntity<Cart> response = controller.addTocart(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Cart cart = response.getBody();
        assertNotNull(cart);
    }
}
