package gr.socialsphere.socialsphere.service;

import gr.socialsphere.socialsphere.dto.CommentDTO;
import gr.socialsphere.socialsphere.dto.PostDTO;
import gr.socialsphere.socialsphere.model.*;
import gr.socialsphere.socialsphere.repository.HashtagRepository;
import gr.socialsphere.socialsphere.repository.PostRepository;
import gr.socialsphere.socialsphere.repository.PostViewRepository;
import gr.socialsphere.socialsphere.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

// Add these imports at the top
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HashtagRepository hashtagRepository;

    @Autowired
    private PostViewRepository postViewRepository;

    @Transactional
    public boolean createPost(PostDTO postDTO) throws IOException {
        Optional<User> creator = userRepository.findById(postDTO.getCreatorId());
        if (creator.isEmpty()) return false;

        Post post = new Post();
        post.setContent(postDTO.getContent());
        post.setImageUrl("");
        post.setDate(LocalDateTime.now());
        post.setCreator(creator.get());

        Set<Hashtag> hashtags = extractAndSaveHashtags(postDTO.getContent(), post);
        post.setHashtags(hashtags);

        // Handle photo upload if exists
        if (postDTO.getPhoto() != null) {
            String profileName = post.getCreator().getProfileName();
            String photoFilename = postDTO.getPhoto().getOriginalFilename();
            String fileExtension = photoFilename.substring(photoFilename.lastIndexOf("."));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmmss");
            String postDate = LocalDate.now().toString() + LocalTime.now().format(formatter);
            photoFilename = profileName + "-" + postDate + fileExtension;

            // Create directory if it doesn't exist
            String filePath = "uploads/" + profileName;
            File userFolder = new File(filePath);
            if (!userFolder.exists()) {
                boolean created = userFolder.mkdirs();
                if (!created) {
                    throw new IOException("Failed to create directory: " + filePath);
                }
            }

            // Save the file
            byte[] bytes = postDTO.getPhoto().getBytes();
            Path path = Paths.get(filePath, photoFilename);
            Files.write(path, bytes);

            post.setImageUrl(filePath + "/" + photoFilename);
        }

        postRepository.save(post);
        creator.get().getPosts().add(post);
        userRepository.save(creator.get());

        return true;
    }

    // For now, we cannot change the photo of the post...just details of the post
    @Transactional
    public boolean editPost(Long postId, PostDTO postDTO) {

        Optional<Post> existingPost = postRepository.findById(postId);

        if (existingPost.isEmpty())
            return false;

        Post post = existingPost.get();
        post.setContent(postDTO.getContent());
        // post.setImageUrl(postDTO.getImageUrl());

        Set<Hashtag> existingHashtags = post.getHashtags();
        Set<Hashtag> newHashtags = extractAndSaveHashtags(postDTO.getContent(), post);
        updateHashtags(existingHashtags, newHashtags, post);

        postRepository.save(post);
        return true;
    }

    @Transactional
    public String deletePost(Long postId, Long creatorId) {

        Optional<Post> existingPost = postRepository.findById(postId);
        if (existingPost.isEmpty())
            return "NOT_FOUND";

        Post post = existingPost.get();
        if (!post.getCreator().getUserId().equals(creatorId))
            return "FORBIDDEN";

        // Delete the photo from the server if exists...
        String filepath = post.getImageUrl();
        System.out.println(filepath);

        // We have a photo included in the post so it has to be deleted
        if (!filepath.equals("")) {
            File photoToDelete = new File(filepath);

            if (photoToDelete.exists())
                photoToDelete.delete();
        }

        handlePostHashtagsOnDelete(post);
        postRepository.delete(post);
        return "SUCCESS";
    }

    public boolean toggleLikePost(Long postId, Long userId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        Optional<User> userOptional = userRepository.findById(userId);
        if (postOptional.isEmpty() || userOptional.isEmpty())
            return false;

        Post post = postOptional.get();
        User user = userOptional.get();

        if (!post.getUsersLiked().contains(user))
            post.getUsersLiked().add(user);
         else
            post.getUsersLiked().remove(user);


        postRepository.save(post);
        return true;
    }

    public String commentOnPost(CommentDTO commentDTO) {
        if (commentDTO.getContent() == null || commentDTO.getContent().isEmpty()) return "EMPTY";

        Optional<Post> postOptional = postRepository.findById(commentDTO.getPostId());
        Optional<User> userOptional = userRepository.findById(commentDTO.getUserId());
        if (postOptional.isEmpty())
            return "POST_NOT_FOUND";
        if (userOptional.isEmpty())
            return "USER_NOT_FOUND";

        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        comment.setPost(postOptional.get());
        comment.setUserCommented(userOptional.get());

        postOptional.get().getComments().add(comment);
        postRepository.save(postOptional.get());
        return "SUCCESS";
    }

    private Set<Hashtag> extractAndSaveHashtags(String description, Post post) {

        Set<Hashtag> hashtags = new HashSet<>();

        if (description != null && !description.isEmpty()) {
            Pattern pattern = Pattern.compile("#(\\w+)");
            Matcher matcher = pattern.matcher(description);

            while (matcher.find()) {
                String hashtagName = matcher.group(1).toLowerCase();
                Hashtag hashtag = hashtagRepository.findByName(hashtagName).orElseGet(() -> new Hashtag(hashtagName));

                hashtag.addPost(post);
                hashtags.add(hashtag);
            }
        }
        return new HashSet<>(hashtagRepository.saveAll(hashtags));
    }

    private void updateHashtags(Set<Hashtag> oldHashtags, Set<Hashtag> newHashtags, Post post) {
        for (Hashtag oldHashtag : oldHashtags) {
            if (!newHashtags.contains(oldHashtag)) {
                oldHashtag.getPosts().remove(post);
                oldHashtag.setCount(oldHashtag.getCount() - 1);

                if (oldHashtag.getCount() <= 0)
                    hashtagRepository.delete(oldHashtag);
                else
                    hashtagRepository.save(oldHashtag);
            }
        }

        for (Hashtag newHashtag : newHashtags) {
            if (!oldHashtags.contains(newHashtag)) {
                newHashtag.addPost(post);
                hashtagRepository.save(newHashtag);
            }
        }

        post.setHashtags(newHashtags);
    }

    private void handlePostHashtagsOnDelete(Post post) {
        Set<Hashtag> associatedHashtags = post.getHashtags();

        for (Hashtag hashtag : associatedHashtags) {
            hashtag.getPosts().remove(post);
            hashtag.setCount(hashtag.getCount() - 1);

            if (hashtag.getCount() <= 0)
                hashtagRepository.delete(hashtag);
            else
                hashtagRepository.save(hashtag);
        }
    }

    public Resource fetchPost(Long postId, Long userId) throws MalformedURLException {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        String filepath = post.getImageUrl();
        if (filepath == null || filepath.isEmpty()) {
            throw new RuntimeException("Image URL is missing for the post");
        }

        Path path = Paths.get(filepath).toAbsolutePath().normalize();
        if (!Files.exists(path)) {
            throw new RuntimeException("Image file does not exist at the specified path");
        }


        markPostAsViewed(userId, postId);

        return new UrlResource(path.toUri());
    }

    // Update the getFeedForUser method
    public Page<Post> getFeedForUser(Long userId, int page, int size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<User> followingUsers = user.getFollowing();

        if (followingUsers.isEmpty()) {
            return Page.empty();
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findUnseenPostsForFeed(userId, followingUsers, pageable);

        // Mark fetched posts as viewed
        posts.getContent().forEach(post -> markPostAsViewed(userId, post.getPostId()));

        return posts;
    }

    public void markPostAsViewed(Long userId, Long postId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        boolean alreadyViewed = postViewRepository.existsByUserAndPost(user, post);

        if (!alreadyViewed) {

            PostView postView = new PostView(user, post);
            postViewRepository.save(postView);
        }
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
}