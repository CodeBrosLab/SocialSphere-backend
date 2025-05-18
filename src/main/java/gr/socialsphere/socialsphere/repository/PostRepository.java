package gr.socialsphere.socialsphere.repository;

import gr.socialsphere.socialsphere.model.Post;
import gr.socialsphere.socialsphere.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p WHERE p.creator IN :following AND p.postId NOT IN " +
            "(SELECT pv.post.postId FROM PostView pv WHERE pv.user = :user) ORDER BY p.date DESC")
    List<Post> findPostsForUserExcludingSeen(User user, List<User> following);

    @Query("SELECT p FROM Post p WHERE p.creator IN (:followingUsers) " +
            "AND p.postId NOT IN (SELECT pv.post.postId FROM PostView pv WHERE pv.user.id = :userId) " +
            "ORDER BY p.date DESC")
    List<Post> findUnseenPostsForFeed(@Param("userId") Long userId, @Param("followingUsers") List<User> followingUsers);


}
