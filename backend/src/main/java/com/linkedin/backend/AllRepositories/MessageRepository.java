package com.linkedin.backend.AllRepositories;

import com.linkedin.backend.AllModels.Message;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MessageRepository extends JpaRepository<Message, Long> {
}