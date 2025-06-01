package com.linksphere.backend.AllRepositories;

import com.linksphere.backend.AllModels.Message;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MessageRepository extends JpaRepository<Message, Long> {
}