package com.task.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.task.Model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:RequestServlet-servlet.xml")
@WebAppConfiguration
public class UserTest {

    private User user;
    private LocalDateTime lastLogin;
    @Before
    public void setUp() {
        user = new User();
        user.setId(1);
        user.setUserName("Harish");
        user.setPassword("harish@1");
        user.setVisitCount(4);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        lastLogin = LocalDateTime.parse("2024-11-05 14:52:22.242957", formatter);
        
        user.setLastLogin(lastLogin);
    }
    
    @Test
    public void testGetUserName() {
        assertEquals("Harish", user.getUserName());
    }

    @Test
    public void testGetPassword() {
        assertEquals("harish@1", user.getPassword());
    }

    @Test
    public void testGetVisitCount() {
        assertEquals(4, user.getVisitCount());
    }

    @Test
    public void testGetLastLogin() {
        assertEquals(lastLogin, user.getLastLogin());
    }

    @Test
    public void testGetId() {
        assertEquals(1, user.getId());
    }

    @Test
    public void testGetFormattedLastLogin() {
        String expectedFormattedDate = "05-11-2024 14:52:22";
        assertEquals(expectedFormattedDate, user.getFormattedLastLogin());
    }

    @Test
    public void testConstructor() {
        User newUser = new User("Harish", "harish@1");

        assertEquals("Harish", newUser.getUserName());
        assertEquals("harish@1", newUser.getPassword());
        assertEquals(0, newUser.getVisitCount());
        assertNull(newUser.getLastLogin());
    }

    @Test
    public void testToString() {
        String expectedToString = "User [id=1, userName=Harish, password=harish@1, visitCount=4, lastLogin=2024-11-05T14:52:22.242957]";
        assertEquals(expectedToString, user.toString());
    }
}
