package gr.socialsphere.socialsphere.controller;

import gr.socialsphere.socialsphere.model.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/private-message")
    public void sendPrivateMessage(ChatMessage message) {
        message.setTimestamp(java.time.LocalDateTime.now().toString());

        System.out.println("Message from " + message.getSender() +
                " ➡ to " + message.getReceiver() +
                ": " + message.getContent());

        messagingTemplate.convertAndSendToUser(
                message.getReceiver(),
                "/queue/messages",
                message
        );
    }

}
