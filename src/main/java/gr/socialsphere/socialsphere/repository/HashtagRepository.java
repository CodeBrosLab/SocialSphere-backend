package gr.socialsphere.socialsphere.repository;

import gr.socialsphere.socialsphere.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HashtagRepository extends JpaRepository<Post.Hashtag, String> {
}
