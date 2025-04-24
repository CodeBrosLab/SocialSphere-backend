package gr.socialsphere.socialsphere.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User userCommented;

    @Column(name = "content", nullable = false)
    private String content;

    public Comment() {
        this.date = LocalDateTime.now();
        this.content = "";
    }

    public Comment(Post post, LocalDateTime date, User userCommented, String content) {
        this.post = post;
        this.date = date;
        this.userCommented = userCommented;
        this.content = content;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public User getUserCommented() {
        return userCommented;
    }

    public void setUserCommented(User userCommented) {
        this.userCommented = userCommented;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}