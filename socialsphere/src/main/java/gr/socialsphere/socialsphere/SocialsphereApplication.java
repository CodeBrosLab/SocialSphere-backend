package gr.socialsphere.socialsphere;

import gr.socialsphere.socialsphere.comment.Comment;
import gr.socialsphere.socialsphere.comment.CommentRepository;
import gr.socialsphere.socialsphere.comment.CommentService;
import gr.socialsphere.socialsphere.post.Post;
import gr.socialsphere.socialsphere.post.PostRepository;
import gr.socialsphere.socialsphere.post.PostService;
import gr.socialsphere.socialsphere.user.User;
import gr.socialsphere.socialsphere.user.UserInAppActionsService;
import gr.socialsphere.socialsphere.user.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class SocialsphereApplication implements CommandLineRunner {
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private PostRepository postRepository;

	public static void main(String[] args) {
		SpringApplication.run(SocialsphereApplication.class, args);
	}

	@PostConstruct
	public void createSequencesIfNotExist() {
		createSequenceIfNotExists("user_id_seq");
		createSequenceIfNotExists("post_id_seq");
		createSequenceIfNotExists("comment_id_seq");
	}

	private void createSequenceIfNotExists(String sequenceName) {
		String sequenceCheckQuery = "SELECT 1 FROM pg_class WHERE relname = :sequenceName";
		Integer count = (Integer) entityManager.createNativeQuery(sequenceCheckQuery)
				.setParameter("sequenceName", sequenceName)
				.getSingleResult();

		if (count == 0) {
			String createSequenceQuery = "CREATE SEQUENCE IF NOT EXISTS " + sequenceName + " START WITH 1 INCREMENT BY 1";
			entityManager.createNativeQuery(createSequenceQuery).executeUpdate();
		}
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		User user = new User();
		Post post = new Post();
		Comment comment = new Comment();

		// Save the User first
		user.setUserId(1L);
		userRepository.save(user);
		userRepository.flush();

		// Set the User as the creator of the Post
		post.setCreator(user);
		postRepository.save(post);
		postRepository.flush();

		// Set the User and Post for the Comment
		comment.setUserCommented(user);
		comment.setPost(post);
		commentRepository.save(comment);
	}
}
