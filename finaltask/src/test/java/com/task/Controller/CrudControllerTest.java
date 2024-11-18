package com.task.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.task.Model.User;
import com.task.Service.UserService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:RequestServlet-servlet.xml")
@WebAppConfiguration
public class CrudControllerTest {

    private CrudController crudController;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        crudController = new CrudController(userService); // Ensure CrudController has this constructor
        mockMvc = MockMvcBuilders.standaloneSetup(crudController).build();
    }

    @Test
    public void testAddMethod_Success() throws Exception {
        User user = new User();
        user.setUserName("testUser");
        user.setPassword("password");

        when(userService.addUsers(any(User.class))).thenReturn(true);

        mockMvc.perform(post("/users/add")
                .param("userName", "testUser")
                .param("password", "password")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("msg"))
                .andExpect(view().name("message"));

        verify(userService, times(1)).addUsers(any(User.class));
    }

    @Test
    public void testAddMethod_Failure() throws Exception {
        when(userService.addUsers(any(User.class))).thenReturn(false);

        mockMvc.perform(post("/users/add")
                .param("userName", "testUser")
                .param("password", "password")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/addform"));

        verify(userService, times(1)).addUsers(any(User.class));
    }

    @Test
    public void testDeleteUser_Authenticated() throws Exception {
        mockMvc.perform(post("/users/delete")
                .param("userId", "1")
                .sessionAttr("LoginUser", "admin"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));

        verify(userService, times(1)).deleteUserById(1);
    }

    @Test
    public void testDeleteUser_NotAuthenticated() throws Exception {
        mockMvc.perform(post("/users/delete")
                .param("userId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(userService, never()).deleteUserById(anyInt());
    }

    @Test
    public void testUpdateForm() throws Exception {
        User user = new User();
        user.setUserName("testUser");

        when(userService.findUserById(1)).thenReturn(user);

        mockMvc.perform(get("/users/updateform")
                .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("UpdateForm"));

        verify(userService, times(1)).findUserById(1);
    }

    @Test
    public void testUpdateMethod_Success() throws Exception {
        when(userService.updateUsers(any(User.class), anyInt())).thenReturn(true);

        mockMvc.perform(post("/users/update")
                .param("userName", "testUser")
                .param("refUserID", "1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("Message"))
                .andExpect(view().name("message"));

        verify(userService, times(1)).updateUsers(any(User.class), eq(1));
    }

    @Test
    public void testUpdateMethod_Failure() throws Exception {
        when(userService.updateUsers(any(User.class), anyInt())).thenReturn(false);

        mockMvc.perform(post("/users/update")
                .param("userName", "testUser")
                .param("refUserID", "1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/form?userId=1"));

        verify(userService, times(1)).updateUsers(any(User.class), eq(1));
    }

    @Test
    public void testAddForm() throws Exception {
        mockMvc.perform(get("/users/addform"))
                .andExpect(status().isOk())
                .andExpect(view().name("Signup"));
    }
}