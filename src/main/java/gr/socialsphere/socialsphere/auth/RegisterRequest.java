package gr.socialsphere.socialsphere.auth;


public class RegisterRequest {
    private String email;
    private String password;
    private String profileName;

    public RegisterRequest() {}

    public RegisterRequest(String email, String password, String profileName) {
        this.email = email;
        this.password = password;
        this.profileName = profileName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }
}
