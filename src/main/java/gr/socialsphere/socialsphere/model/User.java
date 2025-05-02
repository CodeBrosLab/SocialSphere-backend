package gr.socialsphere.socialsphere.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@JsonIdentityInfo( generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "userId")
@Entity
@Table(name="users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId; // We do not provide this to the constructors, because JPA create this from the db

    @Column(name = "email")
    private String email;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "bio")
    private String bio;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "location")
    private String location;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "password")
    private String password; // make sure later on this is encoded...do not store plain password in db

    @Column(name = "profile_name")
    private String profileName;

    @OneToMany(mappedBy = "creator")
    private List<Post> posts;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "skills")
    private String skills;

    @Column(name = "interests")
    private String interests;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserLink> userLinks;

    @ManyToMany
    //@JsonManagedReference
    @JoinTable(
            name = "user_followers",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id")
    )
    private List<User> followers;

    @ManyToMany(mappedBy = "followers")
    private List<User> following;

    public User() {
        this.profileName = "";
        this.email = "";
        this.password = "";
        this.role = Role.USER;
        this.displayName = "";
        this.posts = new ArrayList<>();
        this.followers = new ArrayList<>();
        this.following = new ArrayList<>();
    }

    public User(String email, String password, String profileName, String displayName, Role role) {
        this.email = email;
        this.password = password;
        this.profileName = profileName;
        this.displayName = displayName;
        this.role = role;
    }

    public User(String email, String password, String profileName, String displayName,List<Post> posts) {
        this.email = email;
        this.password = password;
        this.profileName = profileName;
        this.posts = posts;
        this.displayName = displayName;
        this.followers = new ArrayList<>();
        this.following = new ArrayList<>();
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /*When this user follows another user */
    public void follow(User aUser) {
        if (this.equals(aUser))
            throw new IllegalArgumentException("User cannot follow themselves");

        if (!this.following.contains(aUser)) {
            this.following.add(aUser);
            aUser.getFollowers().add(this);
        }
    }

    /* When this user no longer follows another user */
    public void unfollow(User aUser) {
        if (this.following.contains(aUser)) {
            this.following.remove(aUser);
            aUser.getFollowers().remove(this);
        }
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long id) {
        this.userId = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(Role.USER.toString()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    public List<User> getFollowing() {
        return following;
    }

    public void setFollowing(List<User> following) {
        this.following = following;
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

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public List<UserLink> getUserLinks() {
        return userLinks;
    }

    public void setUserLinks(List<UserLink> userLinks) {
        this.userLinks = userLinks;
    }
}
