package com.task.Controller;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.task.Model.User;
import com.task.Service.UserService;

@Controller
public class CrudController {
    private final UserService service;

    @Autowired
    public CrudController(UserService service) {
        this.service = service;
    }

    protected static final Logger logger = LogManager.getLogger();
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/users/add")
    public String addmethod(@ModelAttribute User user, Model model, RedirectAttributes redirectAttributes) {

        logger.info("received request to add new user.....");

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        boolean isAdded = service.addUsers(user);
        if (isAdded) {
            logger.info("User \"{}\" created successfully!", user.getUserName());
            model.addAttribute("msg", "User \"" + user.getUserName() + "\" Created Successfully!");

            return "message";
        } else {

            logger.warn("Error in adding user... Redirecting back to add user form page");
            redirectAttributes.addFlashAttribute("message", "User already exists with this MailId!");
            return "redirect:/users/addform";
        }

    }

    @PostMapping("/users/delete")

    public String deleteUser(@RequestParam String userId, Model model, HttpSession session) {

        logger.info("Request received to delete the user; {}", userId);

        if (session.getAttribute("LoginUser") == null) {
            return "redirect:/";
        }
        int user_id = Integer.parseInt(userId);
        logger.info("Attempting to delete user.....");
        service.deleteUserById(user_id);

        logger.info("Redirecting to user page after deleting the user...");

        return "redirect:/users";
    }

    @GetMapping("/users/addform")
    public String addForm(Model model, HttpSession session) {

        logger.info("User requesting Signup page.....");

        return "Signup";
    }

    @GetMapping("/users/updateform")
    public String updateForm(@RequestParam String userId, Model model, HttpSession session) {
        logger.info("Received request to display update form for user with ID: {}", userId);

        int userIdForAction = Integer.parseInt(userId);
        User userToUpdate = service.findUserById(userIdForAction);

        if (userToUpdate != null) {
            logger.info("User found: {}", userToUpdate.getUserName());
            model.addAttribute("user", userToUpdate);

        } else {
            logger.warn("User with ID: {} not found.", userId);
        }

        return "UpdateForm";
    }


    @PostMapping("/users/update")
    public String updatemethod(@ModelAttribute User updateUser, @RequestParam String refUserID, Model model,
            RedirectAttributes redirectAttributes, HttpSession session) {

        logger.info("Received request to update user with ID: {}", refUserID);

        boolean isUpdated = service.updateUsers(updateUser, Integer.parseInt(refUserID));

        if (isUpdated) {
            
            logger.info("User with ID: {} updated successfully", refUserID);

            session.setAttribute("LoginUser", service.findUserById(Integer.parseInt(refUserID)));
            model.addAttribute("Message", "User updated successfully!");

            return "message";
        } else {
            logger.warn("Failed to update user with ID: {}. Mail ID may be in use.", refUserID);
            redirectAttributes.addFlashAttribute("message", "Mail ID is already in use!");
            return "redirect:/users/form?userId=" + refUserID;
        }
    }

}
