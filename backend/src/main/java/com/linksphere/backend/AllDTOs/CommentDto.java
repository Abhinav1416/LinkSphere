package com.linksphere.backend.AllDTOs;

public class CommentDto {
    private String content;

    public CommentDto(String content) {
        this.content = content;
    }

    public CommentDto() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
