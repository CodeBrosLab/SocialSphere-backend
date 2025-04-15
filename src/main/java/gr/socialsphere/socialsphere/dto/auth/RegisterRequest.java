package gr.socialsphere.socialsphere.dto.auth;


public class RegisterRequest {
    private String email;
    private String password;
    private String profileName;

    public RegisterRequest(String email, String password, String profileName) {
        this.email = email;
        this.password = password;
        this.profileName = profileName;
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
}
