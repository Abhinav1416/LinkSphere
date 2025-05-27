package com.linkedin.backend.AllControllers;

import com.linkedin.backend.AllDTOs.Response;
import com.linkedin.backend.AllModels.User;
import com.linkedin.backend.AllDTOs.MessageDto;
import com.linkedin.backend.AllModels.Conversation;
import com.linkedin.backend.AllModels.Message;
import com.linkedin.backend.AllServices.MessagingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/messaging")
public class MessagingController {
    private final MessagingService messagingService;

    public MessagingController(MessagingService messagingService) {
        this.messagingService = messagingService;
    }

    @GetMapping("/conversations")
    public List<Conversation> getConversations(@RequestAttribute("authenticatedUser") User user) {
        return messagingService.getConversationsOfUser(user);
    }

    @GetMapping("/conversations/{conversationId}")
    public Conversation getConversation(@RequestAttribute("authenticatedUser") User user, @PathVariable Long conversationId) {
        return messagingService.getConversation(user, conversationId);
    }

    @PostMapping("/conversations")
    public Conversation createConversationAndAddMessage(@RequestAttribute("authenticatedUser") User sender, @RequestBody MessageDto messageDto) {
        return messagingService.createConversationAndAddMessage(sender, messageDto.receiverId(), messageDto.content());
    }

    @PostMapping("/conversations/{conversationId}/messages")
    public Message addMessageToConversation(@RequestAttribute("authenticatedUser") User sender, @RequestBody MessageDto messageDto, @PathVariable Long conversationId) {
        return messagingService.addMessageToConversation(conversationId, sender, messageDto.receiverId(),
                messageDto.content());
    }

    @PutMapping("/conversations/messages/{messageId}")
    public Response markMessageAsRead(@RequestAttribute("authenticatedUser") User user, @PathVariable Long messageId) {
        messagingService.markMessageAsRead(user, messageId);
        return new Response("Message marked as read");
    }
}
