package gr.socialsphere.socialsphere.service;

import gr.socialsphere.socialsphere.dto.auth.AuthenticationRequest;
import gr.socialsphere.socialsphere.dto.auth.AuthenticationResponse;
import gr.socialsphere.socialsphere.dto.auth.RefreshRequest;
import gr.socialsphere.socialsphere.dto.auth.RegisterRequest;
import gr.socialsphere.socialsphere.model.Role;
import gr.socialsphere.socialsphere.model.User;
import gr.socialsphere.socialsphere.model.UserLink;
import gr.socialsphere.socialsphere.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class AuthService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        // Check if the email is already taken
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already taken");
        }
        User user = new User(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getProfileName(),
                request.getDisplayName(),
                Role.USER
        );

        UserLink linkedInLink = new UserLink();
        linkedInLink.setName("LinkedIn");

        UserLink githubLink = new UserLink();
        githubLink.setName("GitHub");
        List<UserLink> userLinks = new ArrayList<>();
        userLinks.add(linkedInLink);
        userLinks.add(githubLink);
        user.setUserLinks(userLinks);
        linkedInLink.setUser(user);
        githubLink.setUser(user);

        userRepository.save(user);
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        createUserFolderInServer(user.getProfileName());

        return new AuthenticationResponse(accessToken, refreshToken);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByEmail(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return new AuthenticationResponse(accessToken, refreshToken);
    }

    public AuthenticationResponse refresh(RefreshRequest request) {
        final String refreshToken = request.getRefreshToken();
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new RuntimeException("Refresh token is missing");
        }

        final String userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail == null) {
            throw new RuntimeException("Invalid refresh token");
        }

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new RuntimeException("Refresh token is not valid");
        }

        final String newAccessToken = jwtService.generateToken(user);
        return new AuthenticationResponse(newAccessToken, refreshToken);
    }


    // Helper method
    private void createUserFolderInServer(String profileName) {
        String rootFolderAsAString = "uploads";
        File rootFolder = new File(rootFolderAsAString);

        if (!rootFolder.exists()) {
            rootFolder.mkdir();
        }

        File userFolder = new File(rootFolder, profileName);

        if (!userFolder.exists()) {
            userFolder.mkdir();
        }
    }

}
