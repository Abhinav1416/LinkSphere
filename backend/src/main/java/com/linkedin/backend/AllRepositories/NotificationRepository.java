package com.linkedin.backend.AllRepositories;

import com.linkedin.backend.AllModels.User;
import com.linkedin.backend.AllModels.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipient(User recipient);

    List<Notification> findByRecipientOrderByCreationDateDesc(User user);
}
