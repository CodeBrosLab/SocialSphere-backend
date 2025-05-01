package gr.socialsphere.socialsphere.dto;

import gr.socialsphere.socialsphere.model.UserLink;

import java.util.ArrayList;
import java.util.List;

public class UpdatePrimaryDTO {
    private String profileName;
    private String displayName;
    private String bio;
    private String location;
    private List<String> interests;
    private List<String> skills;
    private List<UserLinkDTO> userLinks;
    private Long userId;

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public List<UserLinkDTO> getUserLinks() {
        return userLinks;
    }

    public void setUserLinks(List<UserLinkDTO> userLinks) {
        this.userLinks = userLinks;
    }
}