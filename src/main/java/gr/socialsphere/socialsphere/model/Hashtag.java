package gr.socialsphere.socialsphere.model;

import gr.socialsphere.socialsphere.model.Post;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hashtags")
public class Hashtag {
    @Id
    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "hashtags")
    private List<Post> posts; // Assign this to mappedBy attribute, above List<Hashtag> attribute of Hashtag class

    @Column(name = "count")
    private int count; //how many posts a hashtag appears

    public Hashtag() {
        this.name = ""; // We do not want null values
        this.posts = new ArrayList<>();
        this.count = 0;
    }

    public Hashtag(String name){
        this.name = name;
        this.posts = new ArrayList<>();
        this.count = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String aName) {
        this.name = aName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int aCount) {
        this.count = aCount;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public void addPost(Post post) {
        if(!posts.contains(post)){
            posts.add(post);
            count++;
        }
    }
}
