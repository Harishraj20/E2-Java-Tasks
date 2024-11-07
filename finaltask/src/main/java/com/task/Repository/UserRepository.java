package com.task.Repository;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.task.Model.User;

@Repository
@Transactional
public class UserRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public String addUserInfo(User user) {
        try {
            User existingUser = checkExistingUser(user);
            if (existingUser != null) {
                return "User Already Exists!";
            } else {
                Session session = sessionFactory.getCurrentSession();
                session.save(user);
                return "User \"" + user.getUserName() + "\" Created Successfully!!";
            }
        } catch (HibernateException e) {
            System.out.println(e);
            return "Corrupted";
        } catch (Exception e) {
            System.out.println("General error: " + e);
            return "GeneralException";
        }

    }

    public List<User> getUsers() {
        Session session = sessionFactory.getCurrentSession();
        List<User> users = null;

        try {
            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.gt("visitCount", 0));
            users = criteria.list();
        } catch (HibernateException e) {
            System.out.println(e);
        } catch (Exception e) {
            System.out.println("General error: " + e);
            return null;
        }
        return users;
    }

    public String validateLogin(User user) {
        Session session = sessionFactory.getCurrentSession();
        String message = "Invalid Credentials... Please check username or Password!!";

        try {
            User existingUser = checkExistingUser(user);

            if (existingUser != null) {

                existingUser.setVisitCount(existingUser.getVisitCount() + 1);
                existingUser.setLastLogin(LocalDateTime.now());

                session.update(existingUser);

                message = "User \"" + existingUser.getUserName() + "\" has Logged in Successfully.";

            }
        } catch (HibernateException e) {
            System.out.println(e);
            return "Corrupted";
        } catch (Exception e) {
            System.out.println("General error: " + e);
            return "Exception";
        }

        return message;
    }

    public User checkExistingUser(User user) {
        Session session = sessionFactory.getCurrentSession();
        try {
            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.eq("userName", user.getUserName()));
            criteria.add(Restrictions.eq("password", user.getPassword()));
            User userInfo = (User) criteria.uniqueResult();

            return userInfo;
        } catch (HibernateException e) {
            System.out.println("Hibernate error: " + e);
            return null;
        } catch (Exception e) {
            System.out.println("General error: " + e);
            return null;
        }
    }
}
