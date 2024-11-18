package com.task.Service;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;

import com.task.Model.Login;
import com.task.Model.User;
import com.task.Repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private User loginUser;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        testUser = new User("Arvind Kumar", "securePassword123", "arvind.kumar@gmail.com",
                "1992-12-10", "Software Engineer", "Admin", 1, "Male");
        loginUser = new User("John Doe", "password123", "john.doe@example.com", "1990-01-01", "Developer", "Admin", 1,
                "Male");
              
                
                when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
                when(userRepository.checkUserByEmailid(testUser.getEmailId())).thenReturn(testUser);
               
    }

    @Test
    public void testAddUser_Success() {
        User user = new User();
        user.setEmailId("test@example.com");

        when(userRepository.checkUserByEmailid(user.getEmailId())).thenReturn(null);
        when(userRepository.addUserInfo(user)).thenReturn(true);

        boolean result = userService.addUsers(user);

        assertTrue(result);
        verify(userRepository).addUserInfo(user);
    }

    @Test
    public void testAddUser_Failure_ExistingEmail() {
        User user = new User();
        user.setEmailId("test@example.com");

        when(userRepository.checkUserByEmailid(user.getEmailId())).thenReturn(new User());

        boolean result = userService.addUsers(user);

        assertFalse(result);
        verify(userRepository, never()).addUserInfo(user);
    }

    @Test
    public void testUpdateCredentials() {
        User user = new User();
        user.setLoginStatus(1);

        when(userRepository.findUser(user.getUserId())).thenReturn(user);
        doNothing().when(userRepository).updateUser(user);
        doNothing().when(userRepository).saveLoginInfo(any(Login.class));

        userService.updateCredentials(user);

        verify(userRepository).updateUser(user);
        verify(userRepository).saveLoginInfo(any(Login.class));
    }

    @Test
    public void testGetLoginById() {
        int userId = 1;
        int page = 1;
        int pageSize = 10;
        List<Login> logins = Arrays.asList(new Login(), new Login());

        when(userRepository.getLoginInfo(userId, page, pageSize)).thenReturn(logins);

        List<Login> result = userRepository.getLoginInfo(userId, page, pageSize);

        assertEquals(2, result.size());
        verify(userRepository).getLoginInfo(userId, page, pageSize);
    }

    @Test
    public void testGetTotalLoginCount() {
        int userId = 1;
        int loginCount = 5;

        when(userRepository.getTotalLoginCount(userId)).thenReturn(loginCount);

        int result = userRepository.getTotalLoginCount(userId);

        assertEquals(5, result);
        verify(userRepository).getTotalLoginCount(userId);
    }

    @Test
    public void testFetchInactiveUsers() {
        int pageNumber = 1;
        int pageSize = 10;
        List<User> inactiveUsers = Arrays.asList(new User(), new User());

        when(userRepository.findInactiveUsers(anyInt(), anyInt())).thenReturn(inactiveUsers);

        List<User> result = userService.fetchInactiveUsers(pageNumber, pageSize);

        assertEquals(2, result.size());
        verify(userRepository).findInactiveUsers(anyInt(), anyInt());
    }


    @Test
    public void testDeleteUserById_Success() {
        int userId = 1;
        User user = new User();
        user.setUserId(userId);

        when(userRepository.findUser(userId)).thenReturn(user);
        doNothing().when(userRepository).deleteUser(userId);

        boolean result = userService.deleteUserById(userId);

        assertTrue(result);
        verify(userRepository).deleteUser(userId);
    }

    @Test
    public void testDeleteUserById_Failure_UserNotFound() {
        int userId = 1;

        when(userRepository.findUser(userId)).thenReturn(null);

        boolean result = userService.deleteUserById(userId);

        assertFalse(result);
        verify(userRepository, never()).deleteUser(userId);
    }

    @Test
    public void testFindUserById() {
        int userId = 1;
        User user = new User();

        when(userRepository.findUser(userId)).thenReturn(user);

        User result = userService.findUserById(userId);

        assertNotNull(result);
        verify(userRepository).findUser(userId);
    }

    @Test
    public void testCheckUserByMailId_UserFound() {
        String emailId = "test@example.com";
        User user = new User();
        user.setEmailId(emailId);

        when(userRepository.checkUserByEmailid(emailId)).thenReturn(user);

        User result = userRepository.checkUserByEmailid(emailId);

        assertNotNull(result);
        assertEquals(emailId, result.getEmailId());
        verify(userRepository).checkUserByEmailid(emailId);
    }

    @Test
    public void testCheckUserByMailId_UserNotFound() {
        String emailId = "test@example.com";

        when(userRepository.checkUserByEmailid(emailId)).thenReturn(null);

        User result = userRepository.checkUserByEmailid(emailId);

        assertNull(result);
        verify(userRepository).checkUserByEmailid(emailId);
    }
    @Test
    public void testAuthenticateUser_Failure_InvalidPassword() {
        when(userRepository.checkUserByEmailid(testUser.getEmailId())).thenReturn(testUser);

        boolean result = userService.authenticateUser(testUser.getEmailId(), "wrongPassword", session);
        assertFalse(result);
        verify(session, times(0)).setAttribute("LoginUser", testUser);
    }

    @Test
    public void testUpdateUser_Success() {

        User updatedUser = new User("Arvind Kumar", "newPassword456", "arvind.kumar@southindian.com",
                "1992-12-10", "Senior Software Engineer", "Admin", 1, "Male");
        when(userRepository.findUser(updatedUser.getUserId())).thenReturn(testUser);
        when(userRepository.findUserByEmailExcludingId(updatedUser.getEmailId(), testUser.getUserId()))
                .thenReturn(null);
        boolean result = userService.updateUsers(updatedUser, testUser.getUserId());
        assertTrue(result);
        verify(userRepository, times(1)).updateUser(testUser);
    }

    @Test
    public void testUpdateUser_Failure_EmailAlreadyExists() {
        User updatedUser = new User("Arvind Kumar", "newPassword456", "existing.email@example.com",
                "1992-12-10", "Senior Software Engineer", "Admin", 1, "Male");
        when(userRepository.findUserByEmailExcludingId(updatedUser.getEmailId(), testUser.getUserId()))
                .thenReturn(testUser);

        boolean result = userService.updateUsers(updatedUser, testUser.getUserId());

        assertFalse(result);
    }

    @Test
    public void testFetchUsers() {
        when(userRepository.fetchUsersWithPagination(0, 10)).thenReturn(mock(List.class));

        List<User> users = userService.fetchInactiveUsers(1, 10);

        assertNotNull(users);
    }

    @Test
    public void testGetTotalUsers() {
        when(userRepository.countTotalUsers()).thenReturn(100);

        int totalUsers = userRepository.countTotalUsers();

        assertEquals(100, totalUsers);
        verify(userRepository, times(1)).countTotalUsers();
    }

    @Test
    public void testPrepareUserPage() {
        List<User> paginatedUsers = Arrays.asList(
                new User("John Doe", "password123", "john@example.com", "1990-01-01", "Manager", "Admin", 1, "Male"));
        when(userRepository.fetchUsersWithPagination(0, 10)).thenReturn(paginatedUsers);
        when(userRepository.countTotalUsers()).thenReturn(50);
        userService.prepareUserPage(1, 10, session, model);

        verify(model, times(1)).addAttribute("UserList", paginatedUsers);
        verify(model, times(1)).addAttribute("currentPage", 1);
        verify(model, times(1)).addAttribute("totalPages", 5);
    }

    @Test
    public void testPrepareLoginInfoPage() {
        String userId = "1";
        User user = new User("John Doe", "password123", "john.doe@example.com", "1990-01-01", "Developer", "Admin", 1, "Male");
        user.setUserId(1);
    
        int visitCount = 5;
        LocalDateTime loginTime = LocalDateTime.of(2024, 11, 1, 10, 0, 0, 0);
        Login login = new Login();
        login.setLogId(101);
        login.setUser(user);
        login.setLoginInfo(loginTime);
    
        assertEquals(101, login.getLogId());
        assertEquals(user, login.getUser());
        assertEquals(loginTime, login.getLoginInfo());
        assertEquals("10:00:00", login.getFormattedTime());
        assertEquals("01-11-2024", login.getFormattedDate());
    
        List<Login> logins = Arrays.asList(login);
    
        when(userRepository.getLoginInfo(1, 1, 10)).thenReturn(logins);
        when(userRepository.getTotalLoginCount(1)).thenReturn(15);

        userService.prepareLoginInfoPage(userId, 1, 10, model);
    
        verify(model, times(1)).addAttribute("userId", 1);
        verify(model, times(1)).addAttribute("Loggedinfo", logins);
        verify(model, times(1)).addAttribute("currentPage", 1);
        verify(model, times(1)).addAttribute("totalPages", 2);
        verify(model, times(1)).addAttribute("totalLogins", 15);
    }
    

    @Test
    public void testPrepareInactiveUsersPage() {
        int pageNumber = 1;
        int pageSize = 10;
        when(session.getAttribute("LoginUser")).thenReturn(loginUser);

        List<User> inactiveUsers = Arrays.asList(new User("Jane Doe", "password123", "jane.doe@example.com",
                "1992-01-01", "Tester", "User", 0, "Female"));
        when(userRepository.findInactiveUsers((pageNumber - 1) * pageSize, pageSize)).thenReturn(inactiveUsers);
        when(userRepository.countInactiveUsers()).thenReturn(25);

        userService.prepareInactiveUsersPage(pageNumber, pageSize, session, model);
        verify(model, times(1)).addAttribute("UserList", inactiveUsers);
        verify(model, times(1)).addAttribute("currentPage", pageNumber);
        verify(model, times(1)).addAttribute("totalPages", 3);
        verify(model, times(1)).addAttribute("inactiveUserCount", 25);
    }

    @Test
    public void testUpdateUserPassword_Failure() {
        when(session.getAttribute("LoginUser")).thenReturn(loginUser);
        when(userRepository.checkUserByEmailid("john.doe@example.com")).thenReturn(loginUser);

        boolean result = userService.updateUserPassword(session, "wrongOldPassword", "newPassword123");

        assertFalse(result);
    }

    @Test
    public void testUpdateUsers_WhenUserNotFound() {
        User updateUser = new User("John Doe", "password123", "john.doe@example.com", "1990-01-01", "Developer",
                "Admin", 1, "Male");
        int paramId = 1;

        when(userRepository.findUser(paramId)).thenReturn(null);

        boolean result = userService.updateUsers(updateUser, paramId);

        assertFalse(result);
        verify(userRepository, times(1)).findUser(paramId);
        verify(userRepository, never()).updateUser(any(User.class));
    }

    @Test
    public void testAuthenticateUser_WhenUserNotFoundOrPasswordDoesNotMatch() {
        String emailId = "john.doe@example.com";
        String password = "wrongpassword";

        when(userRepository.checkUserByEmailid(emailId)).thenReturn(null);

        boolean result = userService.authenticateUser(emailId, password, session);

        assertFalse(result);
        verify(session, never()).setAttribute(anyString(), any());
    }

}
