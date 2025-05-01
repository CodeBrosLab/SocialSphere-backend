package gr.socialsphere.socialsphere.service;

import gr.socialsphere.socialsphere.dto.CommentDTO;
import gr.socialsphere.socialsphere.dto.PostDTO;
import gr.socialsphere.socialsphere.model.Comment;
import gr.socialsphere.socialsphere.model.Post;
import gr.socialsphere.socialsphere.model.User;
import gr.socialsphere.socialsphere.repository.PostRepository;
import gr.socialsphere.socialsphere.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<String> createPost(PostDTO postDTO) {
        Optional<User> creator = userRepository.findById(postDTO.getCreatorId());
        if (creator.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Creator not found");
        }

        Post post = new Post();
        post.setTitle(postDTO.getTitle());
        post.setDescription(postDTO.getDescription());
        post.setImageUrl(postDTO.getImageUrl());
        post.setDate(LocalDateTime.now());
        post.setCreator(creator.get());

        postRepository.save(post);
        creator.get().getPosts().add(post);
        userRepository.save(creator.get()); // Save the creator to persist the relationship
        return ResponseEntity.status(HttpStatus.CREATED).body("Post created successfully");
    }

    public ResponseEntity<String> editPost(Long postId, PostDTO postDTO) {
        Optional<Post> existingPost = postRepository.findById(postId);
        if (existingPost.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }

        Post post = existingPost.get();
        post.setTitle(postDTO.getTitle());
        post.setDescription(postDTO.getDescription());
        post.setImageUrl(postDTO.getImageUrl());

        postRepository.save(post);
        return ResponseEntity.ok("Post updated successfully");
    }

    public ResponseEntity<String> deletePost(Long postId, Long creatorId) {
        Optional<Post> existingPost = postRepository.findById(postId);
        if (existingPost.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }

        Post post = existingPost.get();
        if (!post.getCreator().getUserId().equals(creatorId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not allowed to delete this post");
        }

        postRepository.delete(post);
        return ResponseEntity.ok("Post deleted successfully");
    }

    public ResponseEntity<String> likePost(Long postId, Long userId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Post post = postOptional.get();
        User user = userOptional.get();
        if (!post.getUsersLiked().contains(user)) {
            post.getUsersLiked().add(user);
            postRepository.save(post);
        } else {
            post.getUsersLiked().remove(user);
            postRepository.save(post);
        }
        return ResponseEntity.ok("Post liked successfully");
    }

    public ResponseEntity<String> commentOnPost(CommentDTO commentDTO) {
        String content = commentDTO.getContent();
        Long postId = commentDTO.getPostId();
        Long userId = commentDTO.getUserId();
        if (content == null || content.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Comment content cannot be empty");
        }
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Create a comment (Assuming there is a Comment entity and repository)
        Post post = postOptional.get();
        User user = userOptional.get();
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setPost(post);
        comment.setUserCommented(user);

        post.getComments().add(comment); // Adding comment to post
        postRepository.save(post);

        return ResponseEntity.ok("Comment added successfully");
    }
}