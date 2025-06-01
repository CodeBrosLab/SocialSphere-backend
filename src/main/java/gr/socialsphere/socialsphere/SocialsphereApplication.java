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
public class SocialsphereApplication implements CommandLineRunner {
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

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		/*// Create five new users
		User andreas = new User(
				"a@gmail.com",
				passwordEncoder.encode("123"),
				"Andreas",
				"andre",
				Role.USER
		);

		UserLink linkedInLink = new UserLink();
		linkedInLink.setUser(andreas);
		linkedInLink.setUrl("");
		linkedInLink.setName("LinkedIn");

		UserLink githubLink = new UserLink();
		githubLink.setUser(andreas);
		githubLink.setUrl("");
		githubLink.setName("Github");

		List<UserLink> andreasLinks = new ArrayList<>();
		andreasLinks.add(linkedInLink);
		andreasLinks.add(githubLink);

		andreas.setUserLinks(andreasLinks);
		userRepository.save(andreas);

		User thanos = new User(
				"t@gmail.com",
				passwordEncoder.encode("123"),
				"Thanos",
				"than",
				Role.USER
		);

		UserLink linkedInLink2 = new UserLink();
		linkedInLink2.setUser(thanos);
		linkedInLink2.setUrl("");
		linkedInLink2.setName("LinkedIn");

		UserLink githubLink2 = new UserLink();
		githubLink2.setUser(thanos);
		githubLink2.setUrl("");
		githubLink2.setName("Github");

		List<UserLink> thanosLinks = new ArrayList<>();
		thanosLinks.add(linkedInLink2);
		thanosLinks.add(githubLink2);

		thanos.setUserLinks(thanosLinks);
		userRepository.save(thanos);*/
	}
}
