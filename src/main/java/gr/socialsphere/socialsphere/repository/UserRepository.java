package gr.socialsphere.socialsphere.repository;

import gr.socialsphere.socialsphere.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<List<User>> findAllByDisplayNameContainingIgnoreCase(String displayName);
}
