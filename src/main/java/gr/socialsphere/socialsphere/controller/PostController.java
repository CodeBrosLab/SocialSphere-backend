package gr.socialsphere.socialsphere.controller;

import gr.socialsphere.socialsphere.dto.CommentDTO;
import gr.socialsphere.socialsphere.dto.PostDTO;
import gr.socialsphere.socialsphere.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/fetch-photo/{postId}")
    public ResponseEntity<Resource> streamPhoto(@PathVariable Long postId) throws MalformedURLException {
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(postService.fetchPost(postId));
    }

    @PostMapping(value = "/create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> createPost(@ModelAttribute PostDTO postDTO) throws IOException {
        if (postService.createPost(postDTO))
            return ResponseEntity.status(HttpStatus.CREATED).body("Post created successfully");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Creator not found");
    }

    @PutMapping("/edit/{postId}")
    public ResponseEntity<String> editPost(@PathVariable Long postId, @RequestBody PostDTO postDTO) {

        if (postService.editPost(postId, postDTO)) {
            return ResponseEntity.ok("Post updated successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId, @RequestParam Long creatorId) {

        return switch (postService.deletePost(postId, creatorId)) {
            case "NOT_FOUND" -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
            case "FORBIDDEN" -> ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not allowed to delete this post");
            default -> ResponseEntity.ok("Post deleted successfully");
        };
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<String> likePost(@PathVariable Long postId, @RequestParam Long userId) {

        if (postService.toggleLikePost(postId, userId)) {
            return ResponseEntity.ok("Post liked/unliked successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post or user not found");
    }

    @PostMapping("/comment")
    public ResponseEntity<String> commentOnPost(@RequestBody CommentDTO commentDTO) {

        return switch (postService.commentOnPost(commentDTO)) {
            case "EMPTY" -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Comment content cannot be empty");
            case "POST_NOT_FOUND" -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
            case "USER_NOT_FOUND" -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            default -> ResponseEntity.ok("Comment added successfully");
        };
    }
}
