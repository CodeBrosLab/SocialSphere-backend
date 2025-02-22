package gr.socialsphere.socialsphere.comment;

import gr.socialsphere.socialsphere.post.Post;
import gr.socialsphere.socialsphere.user.User;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_seq")
    @SequenceGenerator(name = "comment_seq", sequenceName = "comment_id_seq", allocationSize = 1)
    @Column(name = "comment_id")
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "date")
    private String date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userCommented;

    @Column(name = "content")
    private String content;

    public Comment(Long commentId, Post post, String date, User userCommented, String content) {
        this.commentId = commentId;
        this.post = post;
        this.date = date;
        this.userCommented = userCommented;
        this.content = content;
    }

    public Comment() {
        this.commentId = 1L;
        this.post = new Post();
        this.date = LocalDate.now().toString();
        this.userCommented = new User();
        this.content = "random comment";
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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
