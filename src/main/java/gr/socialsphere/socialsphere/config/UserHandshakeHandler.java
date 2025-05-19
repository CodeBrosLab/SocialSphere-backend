package gr.socialsphere.socialsphere.config;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

public class UserHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(
            ServerHttpRequest request,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes
    ) {
        final String name;

        Object rawUsername = attributes.get("username");
        if (rawUsername instanceof String && !((String) rawUsername).isEmpty()) {
            name = (String) rawUsername;
        } else {
            name = UUID.randomUUID().toString();
        }

        return new Principal() {
            @Override
            public String getName() {
                return name;
            }
        };
    }
}
