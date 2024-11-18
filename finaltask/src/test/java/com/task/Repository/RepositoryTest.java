package com.task.Repository;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import com.task.Model.Login;
import com.task.Model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:RequestServlet-servlet.xml")
@WebAppConfiguration
public class RepositoryTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Criteria criteria;

    private Query<Login> query = mock(Query.class);
    Query<Long> mockQuery = mock(Query.class);

    @InjectMocks
    private UserRepository userRepository;
    private int offset = 0;
    private int pageSize = 5;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.createCriteria(User.class)).thenReturn(criteria);
        when(criteria.add(any())).thenReturn(criteria);
        when(criteria.setFirstResult(anyInt())).thenReturn(criteria);
        when(criteria.setMaxResults(anyInt())).thenReturn(criteria);
        when(criteria.setFirstResult(offset)).thenReturn(criteria);
        when(criteria.setMaxResults(pageSize)).thenReturn(criteria);
        when(criteria.setProjection(Projections.rowCount())).thenReturn(criteria);
        when(session.createQuery("SELECT COUNT(l) FROM Login l WHERE l.user.userId = :userId", Long.class))
                .thenReturn(mockQuery);
        when(session.createQuery("FROM Login l WHERE l.user.userId = :userId", Login.class))
                .thenReturn(query);

    }

    @Test
    public void testAddUserInfo_Success() {
        User user = new User();
        user.setEmailId("xyz@gmail.com");
        when(session.save(user)).thenReturn(1);
        boolean result = userRepository.addUserInfo(user);
        verify(session, times(1)).save(user);
        assertTrue(result);
    }

    @Test
    public void testAddUserInfo_runtimeexception() {
        User user = new User();
        user.setEmailId("harsha@gmail.com");

        doThrow(new RuntimeException("Exception")).when(session).save(user);
        boolean result = userRepository.addUserInfo(user);
        verify(session, times(1)).save(user);
        assertFalse(result);
    }

    @Test
    public void testAddUserInfo_HibernateException() {
        User user = new User();
        user.setEmailId("Harish@gmail.com");

        doThrow(new HibernateException("Exception")).when(session).save(user);
        boolean result = userRepository.addUserInfo(user);
        verify(session, times(1)).save(user);
        assertFalse(result);
    }

    @Test
    public void checkUserByEmailid_shouldReturnUser_whenUserExists() {
        String emailId = "Harish@gmail.com";
        User mockUser = new User();
        mockUser.setUserId(1);
        mockUser.setEmailId(emailId);

        when(criteria.uniqueResult()).thenReturn(mockUser);
        User result = userRepository.checkUserByEmailid(emailId);
        assertNotNull(result);
        assertEquals(emailId, result.getEmailId());
        verify(criteria, times(1)).add(any());
        verify(criteria, times(1)).uniqueResult();
    }

    @Test
    public void checkUserByEmailid_shouldReturnNull_whenUserDoesNotExist() {
        String emailId = "Harish@gmail.com";
        when(criteria.uniqueResult()).thenReturn(null);
        User result = userRepository.checkUserByEmailid(emailId);
        assertNull(result);

    }

    @Test
    public void checkUserByEmailid_shouldReturnNull_whenHibernateException() {
        String emailId = "Harish@gmail.com";

        when(criteria.add(any())).thenThrow(new HibernateException("Test Exception"));
        User result = userRepository.checkUserByEmailid(emailId);
        assertNull(result);
    }

    @Test
    public void checkUserByEmailid_shouldReturnNull_whenGenericException() {
        String emailId = "Harish@gmail.com";
        when(criteria.add(any())).thenThrow(new RuntimeException("Unexpected Error"));
        User result = userRepository.checkUserByEmailid(emailId);
        assertNull(result);
    }

    @Test
    public void testDeleteUser() {
        int userId = 1;
        User user = new User();
        user.setUserId(userId);

        when(session.get(User.class, userId)).thenReturn(user);
        doNothing().when(session).delete(user);

        userRepository.deleteUser(userId);
        verify(session, times(1)).get(User.class, userId);
        verify(session, times(1)).delete(user);
    }

    @Test
    public void testDeleteUser_UserNotFound() {
        int userId = 1;

        when(session.get(User.class, userId)).thenReturn(null);
        userRepository.deleteUser(userId);
        verify(session, times(1)).get(User.class, userId);
        verify(session, times(0)).delete(any());
    }

    @Test
    public void countInactiveUsers() {
        when(session.createCriteria(User.class)).thenReturn(criteria);

        when(criteria.add(Restrictions.eq("loginStatus", 0))).thenReturn(criteria);
        when(criteria.uniqueResult()).thenReturn(5L);
        int result = userRepository.countInactiveUsers();

        assertEquals(5, result);
    }

    @Test
    public void countInactiveUsers_whenNoInactiveUsers() {
        when(criteria.uniqueResult()).thenReturn(null);
        int result = userRepository.countInactiveUsers();
        assertEquals(0, result);
    }

    @Test
    public void countInactiveUsers_whenHibernateExceptionOccurs() {
        when(sessionFactory.getCurrentSession()).thenThrow(new HibernateException("Simulated Hibernate exception"));
        int result = userRepository.countInactiveUsers();
        assertEquals(0, result);
    }

    @Test
    public void countInactiveUsers_whenGenericExceptionOccurs() {
        when(session.createCriteria(User.class)).thenThrow(new RuntimeException("Test Generic Exception"));

        int result = userRepository.countInactiveUsers();
        assertEquals(0, result);
    }

    @Test
    public void findInactiveUsers_shouldReturnInactiveUsers_whenUsersExist() {

        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(new User("Harish", "Harish@1", "Harish@example.com", "17-01-2001", "React Developer", "Admin",
                0, "Male"));
        expectedUsers
                .add(new User("Ramesh", "Ramesh@1", "Ram@example.com", "20-10-2002", "Developer", "viewer", 0, "Male"));

        when(criteria.list()).thenReturn(expectedUsers);
        List<User> result = userRepository.findInactiveUsers(offset, pageSize);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedUsers, result);
    }

    @Test
    public void findInactiveUsers_shouldReturnEmptyList_whenHibernateExceptionOccurs() {
        when(sessionFactory.getCurrentSession()).thenThrow(new HibernateException("Simulated Hibernate exception"));
        List<User> result = userRepository.findInactiveUsers(offset, pageSize);
        assertNull(result);
    }

    @Test
    public void findInactiveUsers_shouldReturnNull_whenGenericExceptionOccurs() {

        when(criteria.list()).thenThrow(new RuntimeException("Test Generic Exception"));
        List<User> result = userRepository.findInactiveUsers(offset, pageSize);
        assertNull(result);
    }

    @Test
    public void getTotalLoginCount_shouldReturnLoginCount_whenExists() {
        int userId = 123;
        long expectedCount = 5L;

        Query<Long> mockQuery = mock(Query.class);
        when(session.createQuery("SELECT COUNT(l) FROM Login l WHERE l.user.userId = :userId", Long.class))
                .thenReturn(mockQuery);
        when(mockQuery.setParameter("userId", userId)).thenReturn(mockQuery);
        when(mockQuery.uniqueResult()).thenReturn(expectedCount);

        int result = userRepository.getTotalLoginCount(userId);
        assertEquals(5, result);
    }

    @Test
    public void getTotalLoginCount_shouldReturnZero_whenNoLogins() {
        int userId = 5;
        Long expectedCount = null;
        when(mockQuery.setParameter("userId", userId)).thenReturn(mockQuery);
        when(mockQuery.uniqueResult()).thenReturn(expectedCount);

        int result = userRepository.getTotalLoginCount(userId);

        assertEquals(0, result);
    }

    @Test
    public void getTotalLoginCount_shouldHandleHibernateException() {
        int userId = 5;
        when(mockQuery.setParameter("userId", userId)).thenReturn(mockQuery);

        when(mockQuery.uniqueResult()).thenThrow(new HibernateException("Test Exception"));
        int result = userRepository.getTotalLoginCount(userId);
        assertEquals(0, result);
    }

    @Test
    public void countTotalUsers_shouldReturnTotalCount_whenUsersExist() {
        long expectedCount = 3;
        when(criteria.uniqueResult()).thenReturn(expectedCount);

        int result = userRepository.countTotalUsers();

        assertEquals(3, result);
    }

    @Test
    public void countTotalUsers_shouldHandleHibernateException() {

        when(criteria.uniqueResult()).thenThrow(new HibernateException("Test Exception"));

        int result = userRepository.countTotalUsers();
        assertEquals(0, result);
    }

    @Test
    public void findUser_shouldReturnUser_whenUserExists() {
        int userId = 1;
        User mockUser = new User();
        mockUser.setUserId(userId);

        when(session.get(User.class, userId)).thenReturn(mockUser);
        User result = userRepository.findUser(userId);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
    }

    @Test
    public void findUser_shouldReturnNull_whenUserDoesNotExist() {
        int userId = 999;

        when(session.get(User.class, userId)).thenReturn(null);

        User result = userRepository.findUser(userId);
        assertNull(result);
    }

    @Test
    public void findUser_shouldHandleHibernateException() {
        int userId = 1;

        when(session.get(User.class, userId)).thenThrow(new HibernateException("Database error"));

        User result = userRepository.findUser(userId);

        assertNull(result);
    }

    @Test
    public void findUser_shouldHandleGenericException() {
        int userId = 1;

        when(session.get(User.class, userId)).thenThrow(new RuntimeException("Unexpected error"));

        User result = userRepository.findUser(userId);

        assertNull(result);

    }

    @Test
    public void getLoginInfo_shouldReturnList_whenValidUserId() {
        int userId = 1;
        int page = 1;
        int pageSize = 10;

        List<Login> mockLoginList = Arrays.asList(new Login(), new Login());
        when(query.setParameter("userId", userId)).thenReturn(query);
        when(query.setFirstResult((page - 1) * pageSize)).thenReturn(query);
        when(query.list()).thenReturn(mockLoginList);

        List<Login> result = userRepository.getLoginInfo(userId, page, pageSize);

        assertNotNull(result);
        assertEquals(mockLoginList.size(), result.size());
    }

    @Test
    public void getLoginInfo_shouldReturnEmptyList_whenNoLogins() {
        int userId = 1;
        int page = 1;
        int pageSize = 10;
        when(query.setParameter("userId", userId)).thenReturn(query);
        when(query.setMaxResults(pageSize)).thenReturn(query);
        when(query.list()).thenReturn(Collections.emptyList());

        List<Login> result = userRepository.getLoginInfo(userId, page, pageSize);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void getLoginInfo_shouldHandleHibernateException() {
        int userId = 1;
        int page = 1;
        int pageSize = 10;

        when(query.setParameter("userId", userId)).thenReturn(query);
        when(query.setFirstResult((page - 1) * pageSize)).thenReturn(query);
        when(query.setMaxResults(pageSize)).thenReturn(query);
        when(query.list()).thenThrow(new HibernateException("Database error"));

        List<Login> result = userRepository.getLoginInfo(userId, page, pageSize);

        assertNull(result);

    }

    @Test
    public void getLoginInfo_shouldReturnEmptyList_whenInvalidPageSize() {
        int userId = 1;
        int page = 1;
        int pageSize = 0;

        when(query.setParameter("userId", userId)).thenReturn(query);
        when(query.setFirstResult((page - 1) * pageSize)).thenReturn(query);
        when(query.setMaxResults(pageSize)).thenReturn(query);
        when(query.list()).thenReturn(Collections.emptyList());

        List<Login> result = userRepository.getLoginInfo(userId, page, pageSize);

        assertNotNull(result);
        assertTrue(result.isEmpty());

    }

    @Test
    public void getLoginInfo_shouldReturnResultsWithPagination() {
        int userId = 1;
        int page = 2;
        int pageSize = 10;

        List<Login> mockLoginList = Arrays.asList(new Login(), new Login());
        when(query.setFirstResult((page - 1) * pageSize)).thenReturn(query);
        when(query.setMaxResults(pageSize)).thenReturn(query);
        when(query.list()).thenReturn(mockLoginList);

        List<Login> result = userRepository.getLoginInfo(userId, page, pageSize);
        assertNotNull(result);
        assertEquals(mockLoginList.size(), result.size());
    }

    @Test
    public void findUserByEmailExcludingId_shouldReturnUser_whenUserExists() {
        String emailId = "Hari@gmail.com";
        int userId = 1;
        User user = new User();
        user.setEmailId(emailId);
        user.setUserId(2);

        when(criteria.add(Restrictions.eq("emailId", emailId))).thenReturn(criteria);
        when(criteria.add(Restrictions.ne("userId", userId))).thenReturn(criteria);
        when(criteria.uniqueResult()).thenReturn(user);

        User result = userRepository.findUserByEmailExcludingId(emailId, userId);
        assertNotNull(result);
        assertEquals(user, result);

    }

    @Test
    public void findUserByEmailExcludingId_shouldReturnNull_whenUserNotFound() {
        String emailId = "rohan@gmail.com";
        int userId = 1;

        when(criteria.uniqueResult()).thenReturn(null);
        User result = userRepository.findUserByEmailExcludingId(emailId, userId);
        assertNull(result);
    }

    @Test
    public void findUserByEmailExcludingId_shouldHandleHibernateException() {
        String emailId = "Ravi@.com";
        int userId = 5;
        when(criteria.uniqueResult()).thenThrow(new HibernateException("Database error"));
        User result = userRepository.findUserByEmailExcludingId(emailId, userId);
        assertNull(result);

    }

    @Test
    public void findUserByEmailExcludingId_shouldHandleGeneralException() {
        String emailId = "ravi@gamil.com";
        int userId = 12;
        when(criteria.uniqueResult()).thenThrow(new RuntimeException());
        User result = userRepository.findUserByEmailExcludingId(emailId, userId);
        assertNull(result);

    }

    @Test
    public void updateUser_shouldUpdateUserSuccessfully() {
        User user = new User();
        user.setUserId(1);

        userRepository.updateUser(user);
        verify(session).update(user);
    }

    @Test
    public void updateUser_shouldHandleHibernateException() {
        User user = new User();
        user.setUserId(1);

        doThrow(new HibernateException("Database error")).when(session).update(user);

        userRepository.updateUser(user);

        verify(session).update(user);
    }

    @Test
    public void updateUser_shouldHandleException() {
        User user = new User();
        user.setUserId(1);
        doThrow(new RuntimeException("Unexpected error")).when(session).update(user);

        userRepository.updateUser(user);
        verify(session).update(user);
    }

    @Test
    public void fetchUsersWithPagination_shouldReturnList_whenUsersExist() {
        int offset = 0;
        int pageSize = 10;
        List<User> mockUserList = Arrays.asList(new User(), new User());
        when(criteria.list()).thenReturn(mockUserList);
        List<User> result = userRepository.fetchUsersWithPagination(offset, pageSize);
        assertNotNull(result);
        assertEquals(mockUserList.size(), result.size());
    }

    @Test
    public void fetchUsersWithPagination_shouldReturnEmptyList_whenNoUsersFound() {
        List<User> mockUserList = Arrays.asList();

        List<User> result = userRepository.fetchUsersWithPagination(offset, pageSize);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void fetchUsersWithPagination_shouldHandleHibernateException() {

        when(criteria.list()).thenThrow(new HibernateException("Database error"));
        List<User> result = userRepository.fetchUsersWithPagination(offset, pageSize);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void countInactiveUsers_shouldReturnZero_whenNoInactiveUsers() {
        Long expectedCount = 0L;

        when(criteria.add(Restrictions.eq("loginStatus", 0))).thenReturn(criteria);
        when(criteria.uniqueResult()).thenReturn(expectedCount);
        int result = userRepository.countInactiveUsers();
        assertEquals(0, result);
    }

}