package com.task.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import com.task.Service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:RequestServlet-servlet.xml")
@WebAppConfiguration
public class UserControllerTest {

    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testHomepage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("Login"));
    }

    @Test
    public void testLoginUser_Success() throws Exception {
        when(userService.authenticateUser(eq("test@example.com"), eq("password"), any(HttpSession.class)))
                .thenReturn(true);

        mockMvc.perform(post("/users")
                .param("emailId", "test@example.com")
                .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));

        verify(userService, times(1)).authenticateUser(eq("test@example.com"), eq("password"), any(HttpSession.class));
    }

    @Test
    public void testLoginUser_Failure() throws Exception {
        when(userService.authenticateUser(eq("test@example.com"), eq("password"), any(HttpSession.class)))
                .thenReturn(false);

        mockMvc.perform(post("/users")
                .param("emailId", "test@example.com")
                .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attributeExists("message"));

        verify(userService, times(1)).authenticateUser(eq("test@example.com"), eq("password"), any(HttpSession.class));
    }

    @Test
    public void testShowUserPage_NotAuthenticated() throws Exception {
        when(session.getAttribute("LoginUser")).thenReturn(null);

        mockMvc.perform(get("/users"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(userService, times(0)).prepareUserPage(eq(1), eq(10), any(HttpSession.class), any(Model.class));
    }

    @Test
    public void testLogout() throws Exception {
        mockMvc.perform(get("/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

    }

    @Test
    public void testChangePassword_Success() throws Exception {
        when(userService.updateUserPassword(any(HttpSession.class), eq("oldPassword"), eq("newPassword")))
                .thenReturn(true);

        mockMvc.perform(post("/users/changepassword")
                .param("oldPassword", "oldPassword")
                .param("newPassword", "newPassword"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("Message"))
                .andExpect(model().attribute("Message", "Password Updated Successfully"))
                .andExpect(view().name("Redirect"));

        verify(userService, times(1)).updateUserPassword(any(HttpSession.class), eq("oldPassword"), eq("newPassword"));
    }

    @Test
    public void testChangePassword_Failure() throws Exception {
        when(userService.updateUserPassword(any(HttpSession.class), eq("oldPassword"), eq("newPassword")))
                .thenReturn(false);

        mockMvc.perform(post("/users/changepassword")
                .param("oldPassword", "oldPassword")
                .param("newPassword", "newPassword"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("message"))
                .andExpect(view().name("ChangePassword"));

        verify(userService, times(1)).updateUserPassword(any(HttpSession.class), eq("oldPassword"), eq("newPassword"));
    }

    @Test
    public void testChangePasswordPage_Authenticated() throws Exception {
        mockMvc.perform(get("/users/changepassword")
                .sessionAttr("LoginUser", "admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("ChangePassword"));
    }

    @Test
    public void testChangePasswordPage_NotAuthenticated() throws Exception {
        when(session.getAttribute("LoginUser")).thenReturn(null);

        mockMvc.perform(get("/users/changepassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void testShowUserPage_Authenticated() throws Exception {
        mockMvc.perform(get("/users")
                .param("pageNumber", "1")
                .param("pageSize", "10")
                .sessionAttr("LoginUser", "admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("Details"));

        verify(userService, times(1))
                .prepareUserPage(eq(1), eq(10), any(HttpSession.class), any(Model.class));
    }

    @Test
    public void testViewInactiveUsers_Authenticated() throws Exception {
        mockMvc.perform(get("/users/inactiveUsers")
                .param("pageNumber", "1")
                .param("pageSize", "10")
                .sessionAttr("LoginUser", "admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("InactiveUsers"));

        verify(userService, times(1)).prepareInactiveUsersPage(eq(1), eq(10), any(HttpSession.class), any(Model.class));
    }

    @Test
    public void testViewInactiveUsers_NotAuthenticated() throws Exception {
        when(session.getAttribute("LoginUser")).thenReturn(null);

        mockMvc.perform(get("/users/inactiveUsers")
                .param("pageNumber", "1")
                .param("pageSize", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void testViewInfos_Authenticated() throws Exception {

        mockMvc.perform(get("/users/viewInfo")
                .param("userId", "1")
                .param("employeeId", "123")
                .sessionAttr("LoginUser", "admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("LoginInfo"));

        verify(userService, times(1)).prepareLoginInfoPage(eq("1"), eq(1), eq(10), any(Model.class));
    }

    @Test
    public void testViewInfos_NotAuthenticated() throws Exception {
        when(session.getAttribute("LoginUser")).thenReturn(null);

        mockMvc.perform(get("/users/viewInfo")
                .param("userId", "1")
                .param("employeeId", "123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

}
