package gr.socialsphere.socialsphere.repository;

import gr.socialsphere.socialsphere.model.Like;
import gr.socialsphere.socialsphere.model.Post;
import gr.socialsphere.socialsphere.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    // Check if a like already exists for a given post and user
    Optional<Like> findByPostAndUser(Post post, User user);

    // Count the number of likes for a specific post
    int countByPost(Post post);

    // Delete a like for a specific user and post
    void deleteByPostAndUser(Post post, User user);
}