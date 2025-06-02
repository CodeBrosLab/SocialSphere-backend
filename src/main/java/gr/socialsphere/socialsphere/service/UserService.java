package gr.socialsphere.socialsphere.service;

import gr.socialsphere.socialsphere.dto.PostDTO;
import gr.socialsphere.socialsphere.dto.UpdatePrimaryDTO;
import gr.socialsphere.socialsphere.model.Post;
import gr.socialsphere.socialsphere.model.User;
import gr.socialsphere.socialsphere.model.UserLink;
import gr.socialsphere.socialsphere.repository.UserLinkRepository;
import gr.socialsphere.socialsphere.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserLinkRepository userLinkRepository;

    public List<User> findAllByDisplayNameContainingIgnoreCase(String displayName) {
        Optional<List<User>> users = userRepository.findAllByDisplayNameContainingIgnoreCase(displayName);

        return users.get();
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByEmail(@RequestParam String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User getUserById(Long userId) {
       return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public String followUser(Long userId, Long targetUserId) {
        User follower = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        follower.follow(targetUser);
        userRepository.save(follower);

        // Save to persist the relationship
        userRepository.save(targetUser);

        return targetUser.getEmail();
    }

    public String unfollowUser(Long userId, Long targetUserId) {
        User follower = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        follower.unfollow(targetUser);
        userRepository.save(follower); // Save to persist the updates
        userRepository.save(targetUser);

        return targetUser.getEmail();
    }

    public List<User> getFollowers(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getFollowers();
    }

    public List<User> getFollowing(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getFollowing();
    }

    public List<PostDTO> getUserPosts(Long userId) {
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

        return postDTOs;
    }

    public boolean updateUserBio(Long userId, String bio) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setBio(bio);
        userRepository.save(existingUser);

        return true;
    }

    public boolean updateUserSkills(Long userId, List<String> skills, String type) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (type.equals("skills")) {
            String newSkills = String.join(", ", skills);
            existingUser.setSkills(newSkills);
        } else if (type.equals("interests")) {
            String newInterests = String.join(", ", skills);
            existingUser.setInterests(newInterests);
        } else {
            return false;
        }
        userRepository.save(existingUser);

        return true;
    }

    public boolean updateUserProfile(UpdatePrimaryDTO updatePrimaryDTO) {
        Long userId = updatePrimaryDTO.getUserId();
        String profileName = updatePrimaryDTO.getProfileName();
        String displayName = updatePrimaryDTO.getDisplayName();
        String bio = updatePrimaryDTO.getBio();
        String location = updatePrimaryDTO.getLocation();
        List<String> interests = updatePrimaryDTO.getInterests();
        List<String> skills = updatePrimaryDTO.getSkills();

        System.out.println(updatePrimaryDTO.getUserLinks().get(0).getUrl());
        System.out.println(updatePrimaryDTO.getUserLinks().get(1).getUrl());

        String linkedIn = updatePrimaryDTO.getUserLinks().get(0).getUrl();
        String github = updatePrimaryDTO.getUserLinks().get(1).getUrl();

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setProfileName(profileName);
        existingUser.setDisplayName(displayName);
        existingUser.setBio(bio);
        existingUser.setLocation(location);
        existingUser.setInterests(String.join(", ", interests));
        existingUser.setSkills(String.join(", ", skills));

        // Update user links
        UserLink linkedInLink = existingUser.getUserLinks().get(1);
        linkedInLink.setUrl(linkedIn);
        linkedInLink.setName("LinkedIn");
        linkedInLink.setUser(existingUser);

        UserLink githubLink = existingUser.getUserLinks().get(0);
        githubLink.setUrl(github);
        githubLink.setName("GitHub");
        githubLink.setUser(existingUser);

        userLinkRepository.deleteAll(existingUser.getUserLinks());
        List<UserLink> userLinks = new ArrayList<>();
        userLinks.add(linkedInLink);
        userLinks.add(githubLink);
        existingUser.setUserLinks(userLinks);

        userRepository.save(existingUser);

        return true;
    }
}
