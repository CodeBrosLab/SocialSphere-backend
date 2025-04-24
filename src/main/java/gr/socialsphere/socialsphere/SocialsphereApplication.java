package gr.socialsphere.socialsphere;

import gr.socialsphere.socialsphere.model.Comment;
import gr.socialsphere.socialsphere.model.Hashtag;
import gr.socialsphere.socialsphere.repository.CommentRepository;
import gr.socialsphere.socialsphere.model.Post;
import gr.socialsphere.socialsphere.repository.PostRepository;
import gr.socialsphere.socialsphere.model.User;
import gr.socialsphere.socialsphere.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableTransactionManagement
public class SocialsphereApplication implements CommandLineRunner {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private PostRepository postRepository;

	public static void main(String[] args) {
		SpringApplication.run(SocialsphereApplication.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {

	}
}
