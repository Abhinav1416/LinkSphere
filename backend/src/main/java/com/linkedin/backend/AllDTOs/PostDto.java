package com.linkedin.backend.AllDTOs;

public class PostDto {
    private String content;
    private String picture = null;

    public PostDto() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
