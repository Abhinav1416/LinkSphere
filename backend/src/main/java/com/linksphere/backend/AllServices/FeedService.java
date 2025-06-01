package com.linksphere.backend.AllServices;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.linksphere.backend.AllModels.User;
import com.linksphere.backend.AllRepositories.UserRepository;
import com.linksphere.backend.AllModels.Comment;
import com.linksphere.backend.AllModels.Post;
import com.linksphere.backend.AllRepositories.CommentRepository;
import com.linksphere.backend.AllRepositories.PostRepository;
import com.linksphere.backend.AllModels.Connection;
import com.linksphere.backend.AllModels.Status;
import com.linksphere.backend.AllRepositories.ConnectionRepository;

@Service
public class FeedService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final NotificationService notificationService;
    private final ConnectionRepository connectionRepository;
    private final StorageService storageService;

    public FeedService(PostRepository postRepository, UserRepository userRepository,
            CommentRepository commentRepository, NotificationService notificationService,
            ConnectionRepository connectionRepository, StorageService storageService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.notificationService = notificationService;
        this.connectionRepository = connectionRepository;
        this.storageService = storageService;
    }

    public Post createPost(MultipartFile picture, String content, Long id) throws Exception {
        User author = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Post post = new Post(content, author);
        post.setLikes(new HashSet<>());

        if (picture != null && !picture.isEmpty()) {
            String pictureUrl = storageService.saveImage(picture);
            post.setPicture(pictureUrl);
        }

        notificationService.sendNewPostNotificationToFeed(post);

        return postRepository.save(post);
    }


    public Post getPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Post not found"));
    }

    public Post editPost(Long postId, Long id, MultipartFile picture, String content) throws Exception {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!post.getAuthor().equals(user)) {
            throw new IllegalArgumentException("User is not the author of the post");
        }

        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Post content cannot be empty");
        }

        post.setContent(content);

        if (picture != null && !picture.isEmpty()) {
            String pictureUrl = storageService.saveImage(picture);
            post.setPicture(pictureUrl);
        }

        notificationService.sendEditNotificationToPost(postId, post);

        return postRepository.save(post);
    }



    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Post not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!post.getAuthor().equals(user)) {
            throw new IllegalArgumentException("User is not the author of the post");
        }
        notificationService.sendDeleteNotificationToPost(postId);
        postRepository.delete(post);
    }

    public Post likePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Post not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (post.getLikes().contains(user)) {
            post.getLikes().remove(user);
        } else {
            post.getLikes().add(user);
            notificationService.sendLikeNotification(user, post.getAuthor(), post.getId());
        }
        Post savedPost = postRepository.save(post);
        notificationService.sendLikeToPost(postId, savedPost.getLikes());
        return savedPost;
    }

    public Comment addComment(Long postId, Long userId, String content) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Post not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Comment comment = commentRepository.save(new Comment(post, user, content));
        notificationService.sendCommentNotification(user, post.getAuthor(), post.getId());
        notificationService.sendCommentToPost(postId, comment);
        return comment;
    }

    public Comment editComment(Long commentId, Long userId, String newContent) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!comment.getAuthor().equals(user)) {
            throw new IllegalArgumentException("User is not the author of the comment");
        }
        comment.setContent(newContent);
        Comment savedComment = commentRepository.save(comment);
        notificationService.sendCommentToPost(savedComment.getPost().getId(), savedComment);
        return savedComment;
    }

    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!comment.getAuthor().equals(user)) {
            throw new IllegalArgumentException("User is not the author of the comment");
        }
        commentRepository.delete(comment);
        notificationService.sendDeleteCommentToPost(comment.getPost().getId(), comment);
    }

    public List<Post> getPostsByUserId(Long userId) {
        return postRepository.findByAuthorId(userId);
    }

    public List<Post> getFeedPosts(Long authenticatedUserId) {
        List<Connection> connections = connectionRepository.findByAuthorIdAndStatusOrRecipientIdAndStatus(
                authenticatedUserId, Status.ACCEPTED, authenticatedUserId, Status.ACCEPTED);

        Set<Long> connectedUserIds = connections.stream()
                .map(connection -> connection.getAuthor().getId().equals(authenticatedUserId)
                        ? connection.getRecipient().getId()
                        : connection.getAuthor().getId())
                .collect(Collectors.toSet());

        return postRepository.findByAuthorIdInOrderByCreationDateDesc(connectedUserIds);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreationDateDesc();
    }

    public List<Comment> getPostComments(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Post not found"));
        return post.getComments();
    }

    public Set<User> getPostLikes(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Post not found"));
        return post.getLikes();
    }

}