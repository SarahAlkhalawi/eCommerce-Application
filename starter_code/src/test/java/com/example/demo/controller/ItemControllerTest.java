package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);

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
        when(itemRepository.findAll()).thenReturn(items);
        when(itemRepository.findByName("item1")).thenReturn(Arrays.asList(items.get(0)));

    }

    @Test
    public void getItemsTest(){
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> items = (List<Item>) response.getBody();
        assertNotNull(items);
        assertEquals(2, items.size());
    }

    @Test
    public void getItemById(){
        Item item3 = new Item();
        item3.setName("item3");
        item3.setPrice(BigDecimal.valueOf(87444));
        item3.setDescription("good item");
        item3.setId(3l);
        when(itemRepository.findById(3l)).thenReturn(Optional.of(item3));
        ResponseEntity<?> response = this.itemController.getItemById(3l);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Item item = (Item) response.getBody();
        assertEquals("item3", item.getName());
    }

    @Test
    public void getItemByName(){
        ResponseEntity<List<Item>> response = this.itemController.getItemsByName("item1");
        assertNotNull(response);
        Item item = (Item) response.getBody().get(0);
        assertNotNull(item);
        assertEquals("item1", item.getName());

    }


}
