package com.task.Repository;

import javax.servlet.ServletContext;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import com.task.Model.User;

import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.hibernate.criterion.Restrictions;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:RequestServlet-servlet.xml")
@WebAppConfiguration
public class UserRepositoryTest {

    @Autowired
    private WebApplicationContext wac;

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Criteria criteria;

    @InjectMocks
    private UserRepository userRepository;

    private User user1;
    private User user2;
    private User user3;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        user1 = new User();
        user1.setId(1);
        user1.setUserName("Harish");
        user1.setPassword("harish@1");
        user1.setVisitCount(4);
        String timestamp1 = "2024-11-05T14:52:22.242957";
        LocalDateTime lastLogin1 = LocalDateTime.parse(timestamp1);
        user1.setLastLogin(lastLogin1);

        user2 = new User();
        user1.setId(2);
        user2.setUserName("Ravi");
        user2.setPassword("Ravi@1");
        user2.setVisitCount(2);
        String timestamp2 = "2024-11-06T10:20:30.123456";
        LocalDateTime lastLogin2 = LocalDateTime.parse(timestamp2);
        user2.setLastLogin(lastLogin2);

        user3 = new User();
        user1.setId(3);
        user3.setUserName("Priya");
        user3.setPassword("Priya@1");
        user3.setVisitCount(0);
        String timestamp3 = "2024-11-07T08:30:40.654321";
        LocalDateTime lastLogin3 = LocalDateTime.parse(timestamp3);
        user3.setLastLogin(lastLogin3);

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.createCriteria(User.class)).thenReturn(criteria);
        when(criteria.add(any())).thenReturn(criteria);
        when(criteria.uniqueResult()).thenReturn(user1);
        when(criteria.list()).thenReturn(Arrays.asList(user1, user2, user3));
    }

    @Test
    public void configTest() {
        ServletContext servletContext = wac.getServletContext();
        Assert.assertNotNull(servletContext);
        Assert.assertTrue(servletContext instanceof MockServletContext);
        Assert.assertNotNull(wac.getBean("userRepository"));
    }

    @Test
    public void checkBeanDefinitions() {
        String[] beanNames = wac.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
        Assert.assertNotNull(wac.getBean("userRepository"));
    }

    @Test
    public void testAddUserInfo_UserAlreadyExists() {
        when(userRepository.checkExistingUser(user1)).thenReturn(user1);

        String result = userRepository.addUserInfo(user1);

        Assert.assertEquals("User Already Exists!", result);
    }

    @Test
    public void testAddUserInfo_UserCreatedSuccessfully() {
        when(userRepository.checkExistingUser(user1)).thenReturn(null);

        String result = userRepository.addUserInfo(user1);

        Assert.assertEquals("User \"" + user1.getUserName() + "\" Created Successfully!!", result);

        verify(session).save(user1);
    }

    @Test
    public void testAddUserInfo_ExceptionThrown() {

        doThrow(new HibernateException("Database error")).when(sessionFactory).getCurrentSession();
        String result = userRepository.addUserInfo(user1);

        System.out.println("The result of the error is: " + result);

        Assert.assertEquals("Corrupted", result);
    }

    @Test
    public void testAddUserInfo_GeneralException() {

        when(sessionFactory.getCurrentSession()).thenThrow(new RuntimeException("Database error"));

        String result = userRepository.addUserInfo(user1);

        System.out.println("The result of the error is: " + result);

        Assert.assertEquals("GeneralException", result);
    }

    @Test
    public void testSessionFactory() {
        Assert.assertNotNull(sessionFactory);
        Assert.assertNotNull(sessionFactory.getCurrentSession());
    }

    @Test
    public void testgetUsers_success() {

        List<User> expectedList = Arrays.asList(user1, user2);
        when(criteria.add(Restrictions.gt("visitCount", 0))).thenReturn(criteria);
        when(criteria.list()).thenReturn(expectedList);

        List<User> actualList = userRepository.getUsers();
        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void testgetUsers_nullReturn() {

        List<User> expectedList = null;
        when(criteria.add(Restrictions.gt("visitCount", 0))).thenReturn(criteria);
        when(criteria.list()).thenReturn(null);

        List<User> actualList = userRepository.getUsers();
        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void testgetUsers_exception() {
        when(session.createCriteria(User.class)).thenThrow(new HibernateException("Database error"));

        List<User> actualList = userRepository.getUsers();
        Assert.assertNull(actualList);
    }

    @Test
    public void validateLogin_invalidCredentials() {
        when(userRepository.checkExistingUser(user1)).thenThrow(new HibernateException("Database Error"));

        String expectedResult = "Invalid Credentials... Please check username or Password!!";
        String actualResult = userRepository.validateLogin(user1);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void validateLogin_loginSuccessful() {
        when(userRepository.checkExistingUser(user1)).thenReturn(user1);

        String expectedResult = "User \"" + user1.getUserName() + "\" has Logged in Successfully.";
        String actualResult = userRepository.validateLogin(user1);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void validateLogin_hibernateException() {
        doThrow(new HibernateException("DB error")).when(session).update(user1);

        String actualResult = userRepository.validateLogin(user1);
        Assert.assertEquals("Corrupted", actualResult);
    }

    @Test
    public void checkExistingUser_Exception() {
        when(session.createCriteria(User.class)).thenThrow(new RuntimeException("Runtime error"));

        User result = userRepository.checkExistingUser(user1);
        assertNull(result);

    }

}
