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
  
    @Autowired
    private UserRepository userRepository;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/get-user")
    public User getUser(@RequestParam("email") String email) {
        return userService.getUser(email);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(user);
    }

    @PostMapping("/{userId}/follow/{targetUserId}")
    public ResponseEntity<String> followUser(
            @PathVariable Long userId,
            @PathVariable Long targetUserId) {
        User follower = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        follower.follow(targetUser);
        userRepository.save(follower);

        // Save to persist the relationship
        userRepository.save(targetUser);
        return ResponseEntity.ok("Now following user: " + targetUser.getEmail());
    }

    @PostMapping("/{userId}/unfollow/{targetUserId}")
    public ResponseEntity<String> unfollowUser(
            @PathVariable Long userId,
            @PathVariable Long targetUserId) {
        User follower = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        follower.unfollow(targetUser);
        userRepository.save(follower); // Save to persist the updates
        userRepository.save(targetUser);
        return ResponseEntity.ok("You have unfollowed user: " + targetUser.getEmail());
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<User>> getFollowers(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(user.getFollowers());
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<List<User>> getFollowing(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(user.getFollowing());
    }

    @GetMapping("{userId}/posts")
    public ResponseEntity<List<PostDTO>> getUserPosts(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Post> userPosts = user.getPosts();
        List<PostDTO> postDTOs = userPosts.stream().map(post -> {
            PostDTO postDTO = new PostDTO();
            postDTO.setCreatorId(user.getUserId());
            postDTO.setContent(post.getContent());

            if (!post.getImageUrl().equals(""))
                postDTO.setStreamImageUrl("/post/fetch-photo/" + post.getPostId());
            else
                postDTO.setStreamImageUrl("/null");

            return postDTO;
        }).toList();

        return ResponseEntity.ok(postDTOs);
    }

    @PutMapping("/update-primary")
    public ResponseEntity<String> update(@RequestBody UpdatePrimaryDTO updatePrimaryDTO) {
        userService.updateUserProfile(updatePrimaryDTO);
        return ResponseEntity.ok("User info updated successfully");
    }

    @PutMapping("/{userId}/update-bio")
    public ResponseEntity<String> updateUserBio(@PathVariable Long userId, @RequestBody String bio) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setBio(bio);
        userRepository.save(existingUser);
        return ResponseEntity.ok("User bio updated successfully");
    }

    @PutMapping("/{userId}/update-secondary-info/{type}")
    public ResponseEntity<String> updateUserSkills(@PathVariable Long userId,
                                                   @RequestBody List<String> skills,
                                                   @PathVariable String type) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (type.equals("skills")) {
            String newSkills = String.join(", ", skills);
            existingUser.setSkills(newSkills);
        } else if (type.equals("interests")) {
            String newInterests = String.join(", ", skills);
            existingUser.setInterests(newInterests);
        } else {
            return ResponseEntity.badRequest().body("Invalid type");
        }
        userRepository.save(existingUser);
        return ResponseEntity.ok("User skills updated successfully");
    }


}
