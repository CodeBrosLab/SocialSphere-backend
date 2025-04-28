package gr.socialsphere.socialsphere.dto.auth;


public class RegisterRequest {
    private String email;
    private String password;
    private String profileName;
    private String displayName;

    public RegisterRequest(String email, String password, String profileName, String displayName) {
        this.email = email;
        this.password = password;
        this.profileName = profileName;
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getProfileName() {
        return profileName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
