package com.example.oefentest;

import java.util.Objects;

public class BlogPost {

    private String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlogPost blogPost = (BlogPost) o;
        return id == blogPost.id && Objects.equals(title, blogPost.title) && Objects.equals(content, blogPost.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, content, id);
    }

    private String content;
    private int id;

    public BlogPost(int id, String titel, String content) {
        this.title = titel;
        this.content = content;
        this.id = id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
