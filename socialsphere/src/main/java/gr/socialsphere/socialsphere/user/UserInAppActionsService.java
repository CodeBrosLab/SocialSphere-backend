package gr.socialsphere.socialsphere.user;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
I did not name this class as UserService, because we will have another class
called CustomUserDetailsService, that will be responsible for fetching users from the db and make
the authentication. Thus, it would be confusing.
 */

@Service
@Transactional
public class UserInAppActionsService {
    @Autowired
    private UserRepository userRepository;

    public void addUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}
