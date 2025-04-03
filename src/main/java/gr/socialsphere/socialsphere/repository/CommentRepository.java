package gr.socialsphere.socialsphere.repository;

import gr.socialsphere.socialsphere.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
