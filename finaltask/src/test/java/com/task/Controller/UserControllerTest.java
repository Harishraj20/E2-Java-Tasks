package com.task.Controller;

import com.task.Model.User;
import com.task.Service.UserService;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.util.List;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations = "classpath:RequestServlet-servlet.xml")
@WebAppConfiguration
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private User user;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        user = new User();
        user.setUserName("Harish");
        user.setPassword("Harish@1");
        // user.setVisitCount(3);
        // user.setLastLogin(LocalDateTime.of(2002, 10, 12, 12, 24, 0));
    }

    @Test
    public void testHomePage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("home"));
    }

    @Test
    public void testAddUser() throws Exception {

        when(userService.addUsers(any(User.class))).thenReturn("User added successfully");

        mockMvc.perform(MockMvcRequestBuilders.post("/addUser")
                .param("userName", "Harish")
                .param("password", "Harish@1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("message"))
                .andExpect(MockMvcResultMatchers.model().attribute("msg", "User added successfully"));
    }

    @Test
    public void testViewUsers() throws Exception {

        when(userService.fetchDetails()).thenReturn(List.of(user));

        mockMvc.perform(MockMvcRequestBuilders.get("/Views"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("Details"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("userList"))
                .andExpect(MockMvcResultMatchers.model().attribute("userList", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.model().attribute("userList", Matchers.hasItem(user)));
    }

    @Test
    public void testLoginUser() throws Exception {
        when(userService.verifyLogin(any(User.class))).thenReturn("Login successful");

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .param("userName", "Harish")
                .param("password", "Harish@1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("message"))
                .andExpect(MockMvcResultMatchers.model().attribute("msg", "Login successful"));
    }

    @Test
    public void testGoBack() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/back"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));
    }
}
