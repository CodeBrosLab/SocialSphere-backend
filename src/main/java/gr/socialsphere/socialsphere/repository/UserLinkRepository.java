package gr.socialsphere.socialsphere.repository;

import gr.socialsphere.socialsphere.model.UserLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLinkRepository extends JpaRepository<UserLink, Long> {
}
