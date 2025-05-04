package gr.socialsphere.socialsphere.repository;

import gr.socialsphere.socialsphere.model.Hashtag;
import gr.socialsphere.socialsphere.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, String> {
    //Find an already existing hashtag
    Optional<Hashtag> findByName(String name);

    // Fetch all posts for a specific hashtag
    @Query("SELECT p FROM Hashtag h JOIN h.posts p WHERE h.name = :hashtagName")
    List<Post> findPostsByHashtag(String hashtagName);

    // Fetch all posts containing at least two or more hashtags from the given list
    @Query("SELECT DISTINCT p FROM Post p JOIN p.hashtags h WHERE h.name IN :hashtagNames GROUP BY p HAVING COUNT(h) >= :minHashtags")
    List<Post> findPostsByMultipleHashtags(List<String> hashtagNames, long minHashtags);

}
