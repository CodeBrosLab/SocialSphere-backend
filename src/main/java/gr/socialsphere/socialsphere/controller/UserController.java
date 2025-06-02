package gr.socialsphere.socialsphere.controller;

import gr.socialsphere.socialsphere.dto.PostDTO;
import gr.socialsphere.socialsphere.dto.UpdatePrimaryDTO;
import gr.socialsphere.socialsphere.model.Post;
import gr.socialsphere.socialsphere.model.User;
import gr.socialsphere.socialsphere.model.UserLink;
import gr.socialsphere.socialsphere.repository.UserLinkRepository;
import gr.socialsphere.socialsphere.repository.UserRepository;
import gr.socialsphere.socialsphere.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/get-user")
    public User getUser(@RequestParam("email") String email) {
        return userService.getUserByEmail(email);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/{userId}/follow/{targetUserId}")
    public ResponseEntity<String> followUser(
            @PathVariable Long userId,
            @PathVariable Long targetUserId) {

        String targetUserEmail = userService.followUser(userId, targetUserId);
        return ResponseEntity.ok("Now following user: " + targetUserEmail);
    }

    @PostMapping("/{userId}/unfollow/{targetUserId}")
    public ResponseEntity<String> unfollowUser(
            @PathVariable Long userId,
            @PathVariable Long targetUserId) {

        String targetUserEmail = userService.unfollowUser(userId, targetUserId);
        return ResponseEntity.ok("You have unfollowed user: " + targetUserEmail);
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<User>> getFollowers(@PathVariable Long userId) {
        List<User> followers = userService.getFollowers(userId);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<List<User>> getFollowing(@PathVariable Long userId) {
        List<User> following = userService.getFollowing(userId);
        return ResponseEntity.ok(following);
    }

    @GetMapping("{userId}/posts")
    public ResponseEntity<List<PostDTO>> getUserPosts(@PathVariable Long userId) {
        List<PostDTO> postDTOs = userService.getUserPosts(userId);
        return ResponseEntity.ok(postDTOs);
    }

    @PutMapping("/update-primary")
    public ResponseEntity<String> update(@RequestBody UpdatePrimaryDTO updatePrimaryDTO) {
        userService.updateUserProfile(updatePrimaryDTO);
        return ResponseEntity.ok("User info updated successfully");
    }

    @PutMapping("/{userId}/update-bio")
    public ResponseEntity<String> updateUserBio(@PathVariable Long userId, @RequestBody String bio) {
        userService.updateUserBio(userId, bio);
        return ResponseEntity.ok("User bio updated successfully");
    }

    @PutMapping("/{userId}/update-secondary-info/{type}")
    public ResponseEntity<String> updateUserSkills(@PathVariable Long userId,
                                                   @RequestBody List<String> skills,
                                                   @PathVariable String type) {

        if (userService.updateUserSkills(userId, skills, type))
            return ResponseEntity.ok("User skills updated successfully");
        else
            return ResponseEntity.badRequest().body("Invalid type");
    }
}
