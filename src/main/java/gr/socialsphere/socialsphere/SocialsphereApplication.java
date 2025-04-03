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
		User user = new User();
		Post post = new Post();
		Comment comment = new Comment();
		Hashtag hashtag = new Hashtag();

		hashtag.setName("#Thanos");

		user.setEmail("random@example.com");
		// Save the User first
		userRepository.save(user);
		// userRepository.flush();

		// Set the User as the creator of the Post
		post.setCreator(user);
		post.setDate(LocalDateTime.now());
		postRepository.save(post);
		// postRepository.flush();

		// Set the User and Post for the Comment
		comment.setUserCommented(user);
		comment.setContent("Good job!");
		comment.setDate(LocalDateTime.now());
		comment.setPost(post);
		commentRepository.save(comment);

		/* Testing follow methods*/
		User andreas = new User();
		User thanos = new User();
		User dimitris = new User();
		andreas.setEmail("andreas@uom.edu.gr");
		thanos.setEmail("thanos@uom.edu.gr");
		dimitris.setEmail("dimitris@uom.edu.gr");

		andreas.follow(thanos);
		thanos.follow(dimitris);
		andreas.unfollow(thanos);

		userRepository.save(andreas);
		userRepository.save(thanos);
		userRepository.save(dimitris);
	}
}
