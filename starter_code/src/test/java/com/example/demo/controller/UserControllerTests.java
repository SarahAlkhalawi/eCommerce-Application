package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.security.UserDetailsServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTests {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp(){
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void create_user_happy_path(){
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("Sarah");
        userRequest.setPassword("testPassword");
        userRequest.setConfirmPassword("testPassword");
        final ResponseEntity<?> response = this.userController.createUser(userRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User user = (User) response.getBody();
        assertNotNull(user);
        assertEquals("Sarah", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());
    }


    @Test
    public void findUserById(){
        User user =new User();
        user.setUsername("Rawan");
        user.setPassword("r111");
        when(userRepository.findById(0l)).thenReturn(Optional.of(user));
        final ResponseEntity<?> response = this.userController.findById(0l);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(200, response.getStatusCodeValue());
        User u = (User) response.getBody();
        assertEquals(0, u.getId());
    }

    @Test
    public void findUserByUsername(){
        User user =new User();
        user.setUsername("Budur");
        user.setPassword("b8888");
        when(userRepository.findByUsername("Budur")).thenReturn(user);
        final ResponseEntity<?> response = this.userController.findByUserName("Budur");
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(200, response.getStatusCodeValue());
        User u = (User) response.getBody();
        assertEquals("Budur", u.getUsername());
    }
}
