package gr.socialsphere.socialsphere.service;

import gr.socialsphere.socialsphere.auth.AuthenticationRequest;
import gr.socialsphere.socialsphere.auth.AuthenticationResponse;
import gr.socialsphere.socialsphere.auth.RegisterRequest;
import gr.socialsphere.socialsphere.model.Role;
import gr.socialsphere.socialsphere.model.User;
import gr.socialsphere.socialsphere.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        User user = new User(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getProfileName(),
                Role.USER
        );

        userRepository.save(user);
        String jwt = jwtService.generateToken(user);
        return new AuthenticationResponse(jwt);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        String jwt = jwtService.generateToken(user);
        return new AuthenticationResponse(jwt);
    }

}
