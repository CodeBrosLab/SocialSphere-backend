package gr.socialsphere.socialsphere.service;

import gr.socialsphere.socialsphere.dto.UpdatePrimaryDTO;
import gr.socialsphere.socialsphere.model.User;
import gr.socialsphere.socialsphere.model.UserLink;
import gr.socialsphere.socialsphere.repository.UserLinkRepository;
import gr.socialsphere.socialsphere.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserLinkRepository userLinkRepository;

    public User getUser(@RequestParam String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
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
