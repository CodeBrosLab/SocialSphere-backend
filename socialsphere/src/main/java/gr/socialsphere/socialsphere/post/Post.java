package gr.socialsphere.socialsphere.post;

import gr.socialsphere.socialsphere.comment.Comment;
import gr.socialsphere.socialsphere.user.User;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_seq")
    @SequenceGenerator(name = "post_seq", sequenceName = "post_id_seq", allocationSize = 1)
    @Column(name = "post_id")
    private Long postId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "date")
    private String date;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User creator;

    @ManyToMany
    @JoinTable(
            name = "post_likes",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> usersLiked;

    @JoinColumn(name = "comment_id")
    @OneToMany
    private List<Comment> comments;

    public Post(Long postId, String title, String description, String imageUrl, String date, User creator, List<User> usersLiked, List<Comment> comments) {
        this.postId = postId;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.date = date;
        this.creator = creator;
        this.usersLiked = usersLiked;
        this.comments = comments;
    }

    public Post() {
        this.postId = 1L;
        this.title = "random";
        this.description = "random";
        this.imageUrl = "random";
        this.date = LocalDate.now().toString();
        this.creator = new User();
        this.usersLiked = new ArrayList<User>();
        this.comments = new ArrayList<Comment>();
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public List<User> getUsersLiked() {
        return usersLiked;
    }

    public void setUsersLiked(List<User> usersLiked) {
        this.usersLiked = usersLiked;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
