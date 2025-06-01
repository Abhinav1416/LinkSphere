package com.linksphere.backend.AllRepositories;

import com.linksphere.backend.AllModels.User;
import com.linksphere.backend.AllModels.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipient(User recipient);

    List<Notification> findByRecipientOrderByCreationDateDesc(User user);
}
