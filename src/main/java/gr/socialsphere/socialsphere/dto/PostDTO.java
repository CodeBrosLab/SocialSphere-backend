package gr.socialsphere.socialsphere.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class PostDTO {
    @NotEmpty(message = "Content is required")
    private String content;

    private String streamImageUrl;

    @NotNull(message = "Creator ID is required")
    private Long creatorId;

    private MultipartFile photo;

    // Getters and setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStreamImageUrl() {
        return streamImageUrl;
    }

    public void setStreamImageUrl(String imageUrl) {
        this.streamImageUrl = imageUrl;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public MultipartFile getPhoto() {
        return photo;
    }

    public void setPhoto(MultipartFile photo) {
        this.photo = photo;
    }
}