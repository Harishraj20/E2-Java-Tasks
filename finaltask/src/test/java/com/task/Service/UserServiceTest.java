package com.task.Service;

import com.task.Model.User;
import com.task.Repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

}
