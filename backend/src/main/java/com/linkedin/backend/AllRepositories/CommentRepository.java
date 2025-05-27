package com.linkedin.backend.AllRepositories;

import com.linkedin.backend.AllModels.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}