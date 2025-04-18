package gr.socialsphere.socialsphere.service;

import gr.socialsphere.socialsphere.model.*;
import gr.socialsphere.socialsphere.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private HashtagRepository hashtagRepository;

    public void addPost(Post post) {
        postRepository.save(post);
    }

    public void addPostWithHashtags(Post post, List<String> hashtags) {
        for (String hashtagName : hashtags) {
            Hashtag hashtag = hashtagRepository.findById(hashtagName).orElse(new Hashtag(hashtagName));
            hashtag.addPost(post);
            hashtagRepository.save(hashtag);
        }
        post.setHashtags(hashtags.stream().map(name -> hashtagRepository.findById(name).orElse(null)).toList());
        postRepository.save(post);
    }

    public void deletePost(Post post) {
        postRepository.delete(post);
    }

    public Post getPostById(Long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        return postOptional.orElse(null); // Returns null if no post is found
    }

    public void likePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found!"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        // Check if user already liked the post
        if (likeRepository.findByPostAndUser(post, user).isPresent()) {
            throw new RuntimeException("Post already liked by this user!");
        }

        // Save the like
        Like like = new Like(post, user);
        likeRepository.save(like);
    }

    public void commentOnPost(Long postId, Long userId, String content) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found!"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        // Save the comment
        Comment comment = new Comment(post, LocalDateTime.now(), user, content);
        commentRepository.save(comment);
    }
}