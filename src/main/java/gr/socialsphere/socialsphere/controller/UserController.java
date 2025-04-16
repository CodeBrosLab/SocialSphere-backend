package gr.socialsphere.socialsphere.controller;

import gr.socialsphere.socialsphere.model.User;
import gr.socialsphere.socialsphere.repository.UserRepository;
import gr.socialsphere.socialsphere.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/get-user")
    public User getUser(@RequestParam("email") String email) {
        return userService.getUser(email);
    }

}
