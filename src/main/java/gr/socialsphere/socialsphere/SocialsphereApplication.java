package gr.socialsphere.socialsphere;

import gr.socialsphere.socialsphere.model.*;
import gr.socialsphere.socialsphere.repository.CommentRepository;
import gr.socialsphere.socialsphere.repository.PostRepository;
import gr.socialsphere.socialsphere.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableTransactionManagement
public class SocialsphereApplication {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(SocialsphereApplication.class, args);
	}


}
