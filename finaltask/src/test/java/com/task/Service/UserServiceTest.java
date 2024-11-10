package com.task.Service;

import com.task.Model.User;
import com.task.Repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        user = new User();
        user.setUserName("Harish");
        user.setPassword("Harish@1");
    }

    @Test
    public void testAddUsers() {
        String expectedMessage = "User \"Harish\" Created Successfully!!";
        when(userRepository.addUserInfo(user)).thenReturn(expectedMessage);

        String result = userService.addUsers(user);

        assertEquals(expectedMessage, result);
        verify(userRepository, times(1)).addUserInfo(user); 
    }

    @Test
    public void testFetchDetails() {
        User user1 = new User();
        user1.setUserName("Harish");
        user1.setPassword("Harish@1");

        User user2 = new User();
        user2.setUserName("John");
        user2.setPassword("John@2");

        List<User> expectedUsers = List.of(user1, user2);

        when(userRepository.getUsers()).thenReturn(expectedUsers);

        List<User> result = userService.fetchDetails();

        assertEquals(expectedUsers, result);
        verify(userRepository, times(1)).getUsers();
    }

    @Test
    public void testVerifyLogin_Success() {
        when(userRepository.validateLogin(user)).thenReturn("Login successful");

        String result = userService.verifyLogin(user);

        assertEquals("Login successful", result);
        verify(userRepository, times(1)).validateLogin(user);
    }

    @Test
    public void testVerifyLogin_Failure() {
        when(userRepository.validateLogin(user)).thenReturn("Invalid credentials");

        String result = userService.verifyLogin(user);

        assertEquals("Invalid credentials", result);
        verify(userRepository, times(1)).validateLogin(user);
    }
}
