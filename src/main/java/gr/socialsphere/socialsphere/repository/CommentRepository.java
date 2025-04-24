package gr.socialsphere.socialsphere.repository;

import gr.socialsphere.socialsphere.model.Comment;
import gr.socialsphere.socialsphere.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Retrieve all comments for a specific post
    List<Comment> findAllByPost(Post post);
}