package gr.socialsphere.socialsphere.controller;

import gr.socialsphere.socialsphere.model.User;
import gr.socialsphere.socialsphere.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository; // We should have a service here but this is for testing only

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public List<User> sayHello() {
        List<User> allUsers = userRepository.findAll();

        for (User u : allUsers)
           logger.info("User: {}", u.getEmail());

        return allUsers;
    }

}
