package gr.socialsphere.socialsphere;

import gr.socialsphere.socialsphere.comment.Comment;
import gr.socialsphere.socialsphere.comment.CommentRepository;
import gr.socialsphere.socialsphere.post.Post;
import gr.socialsphere.socialsphere.post.PostRepository;
import gr.socialsphere.socialsphere.user.User;
import gr.socialsphere.socialsphere.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

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
		User user = new User();
		Post post = new Post();
		Comment comment = new Comment();

		user.setEmail("random@example.com");
		// Save the User first
		userRepository.save(user);
		// userRepository.flush();

		// Set the User as the creator of the Post
		post.setCreator(user);
		postRepository.save(post);
		// postRepository.flush();

		// Set the User and Post for the Comment
		comment.setUserCommented(user);
		comment.setPost(post);
		commentRepository.save(comment);
	}
}
