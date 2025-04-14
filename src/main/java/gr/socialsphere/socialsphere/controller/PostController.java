package gr.socialsphere.socialsphere.controller;

import gr.socialsphere.socialsphere.model.Post;
import gr.socialsphere.socialsphere.model.User;
import gr.socialsphere.socialsphere.service.PostService;
import gr.socialsphere.socialsphere.dto.PostDTO;
import gr.socialsphere.socialsphere.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<String> createPost(@RequestBody PostDTO postDTO) {
        try {
            // Map DTO to Post entity
            Post post = new Post();
            post.setTitle(postDTO.getTitle());
            post.setDescription(postDTO.getDescription());
            post.setImageUrl(postDTO.getImageUrl());
            post.setDate(LocalDateTime.now());

            // Find User by ID and set as creator
            User creator = userRepository.findById(postDTO.getCreatorId())
                    .orElseThrow(() -> new RuntimeException("User not found!"));
            post.setCreator(creator);

            // Save post
            postService.addPost(post);

            return ResponseEntity.status(HttpStatus.CREATED).body("Post created successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create post: " + e.getMessage());
        }
    }

    @PutMapping("/edit/{postId}")
    public ResponseEntity<String> editPost(
            @PathVariable Long postId,
            @RequestBody PostDTO postDTO
    ) {
        try {
            // Find the post to be updated
            Post existingPost = postService.getPostById(postId);
            if (existingPost == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found!");
            }

            // Check if the user is the creator of the post
            if (!existingPost.getCreator().getUserId().equals(postDTO.getCreatorId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to edit this post!");
            }

            // Update the post details
            existingPost.setTitle(postDTO.getTitle());
            existingPost.setDescription(postDTO.getDescription());
            existingPost.setImageUrl(postDTO.getImageUrl());

            // Save updated post
            postService.addPost(existingPost);

            return ResponseEntity.ok("Post updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to edit post: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId, @RequestParam Long creatorId) {
        try {
            // Find the post to be deleted
            Post post = postService.getPostById(postId);
            if (post == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found!");
            }

            // Check if the user is the creator of the post
            if (!post.getCreator().getUserId().equals(creatorId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this post!");
            }

            // Delete the post
            postService.deletePost(post);

            return ResponseEntity.ok("Post deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete post: " + e.getMessage());
        }
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<String> likePost(@PathVariable Long postId, @RequestParam Long userId) {
        try {
            postService.likePost(postId, userId);
            return ResponseEntity.ok("Post liked successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to like post: " + e.getMessage());
        }
    }

    @PostMapping("/{postId}/comment")
    public ResponseEntity<String> commentOnPost(
            @PathVariable Long postId,
            @RequestParam Long userId,
            @RequestBody String content
    ) {
        try {
            postService.commentOnPost(postId, userId, content);
            return ResponseEntity.ok("Comment added successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add comment: " + e.getMessage());
        }
    }
}