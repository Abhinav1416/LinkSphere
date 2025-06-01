package com.linksphere.backend.AllRepositories;

import com.linksphere.backend.AllModels.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}