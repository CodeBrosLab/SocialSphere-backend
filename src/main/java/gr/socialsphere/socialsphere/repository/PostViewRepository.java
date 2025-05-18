package gr.socialsphere.socialsphere.repository;

import gr.socialsphere.socialsphere.model.Post;
import gr.socialsphere.socialsphere.model.PostView;
import gr.socialsphere.socialsphere.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostViewRepository extends JpaRepository<PostView, Long> {
    // Check if a post has already been viewed by a user
    boolean existsByUserAndPost(User user, Post post);

    // Get all post views for a specific user
    List<PostView> findAllByUser(User user);
}