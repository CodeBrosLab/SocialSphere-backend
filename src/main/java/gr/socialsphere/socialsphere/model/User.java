package gr.socialsphere.socialsphere.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@JsonIdentityInfo( generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "userId")
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId; // We do not provide this to the constructors, because JPA create this from the db

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password; // make sure later on this is encoded...do not store plain password in db

    @Column(name = "profile_name")
    private String profileName;

    @OneToMany(mappedBy = "creator")
    private List<Post> posts;

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

    //@kati
   // private String displayName;

    public User() {
        this.profileName = "";
        this.email = "";
        this.password = "";
        this.posts = new ArrayList<>();
        this.followers = new ArrayList<>();
        this.following = new ArrayList<>();

    }

    public User(String email, String password, String profileName, List<Post> posts) {
        this.email = email;
        this.password = password;
        this.profileName = profileName;
        this.posts = posts;
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

    public String getPassword() {
        return password;
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
}
