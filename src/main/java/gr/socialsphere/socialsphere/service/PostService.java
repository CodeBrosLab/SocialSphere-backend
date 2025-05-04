package gr.socialsphere.socialsphere.service;

import gr.socialsphere.socialsphere.dto.CommentDTO;
import gr.socialsphere.socialsphere.dto.PostDTO;
import gr.socialsphere.socialsphere.model.Comment;
import gr.socialsphere.socialsphere.model.Hashtag;
import gr.socialsphere.socialsphere.model.Post;
import gr.socialsphere.socialsphere.model.User;
import gr.socialsphere.socialsphere.repository.HashtagRepository;
import gr.socialsphere.socialsphere.repository.PostRepository;
import gr.socialsphere.socialsphere.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
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

    @Transactional
    public boolean createPost(PostDTO postDTO) {

        Optional<User> creator = userRepository.findById(postDTO.getCreatorId());

        if (creator.isEmpty())
            return false;

        Post post = new Post();
        post.setTitle(postDTO.getTitle());
        post.setDescription(postDTO.getDescription());
        post.setImageUrl(postDTO.getImageUrl());
        post.setDate(LocalDateTime.now());
        post.setCreator(creator.get());

        Set<Hashtag> hashtags = extractAndSaveHashtags(postDTO.getDescription(), post);
        post.setHashtags(hashtags);

        postRepository.save(post);
        creator.get().getPosts().add(post);
        userRepository.save(creator.get());
        return true;
    }

    @Transactional
    public boolean editPost(Long postId, PostDTO postDTO) {

        Optional<Post> existingPost = postRepository.findById(postId);

        if (existingPost.isEmpty())
            return false;

        Post post = existingPost.get();
        post.setTitle(postDTO.getTitle());
        post.setDescription(postDTO.getDescription());
        post.setImageUrl(postDTO.getImageUrl());

        Set<Hashtag> existingHashtags = post.getHashtags();
        Set<Hashtag> newHashtags = extractAndSaveHashtags(postDTO.getDescription(), post);
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
}
