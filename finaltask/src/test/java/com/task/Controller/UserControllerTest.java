package com.task.Controller;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.Test;
import com.task.Model.User;
import com.task.Service.UserService;

@ExtendWith(SpringExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User user;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUserName("Harish");
        user.setPassword("Harish@1");
    }


    @Test
    public void testHomePage(){




    }

}
