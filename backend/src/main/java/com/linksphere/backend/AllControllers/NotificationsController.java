package com.linksphere.backend.AllControllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linksphere.backend.AllModels.User;
import com.linksphere.backend.AllModels.Notification;
import com.linksphere.backend.AllServices.NotificationService;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationsController {
    private final NotificationService notificationService;

    public NotificationsController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public List<Notification> getUserNotifications(@RequestAttribute("authenticatedUser") User user) {
        return notificationService.getUserNotifications(user);
    }

    @PutMapping("/{notificationId}")
    public Notification markNotificationAsRead(@PathVariable Long notificationId) {
        return notificationService.markNotificationAsRead(notificationId);
    }
}
