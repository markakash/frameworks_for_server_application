package com.example.oefentest;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
@Profile("test")
public class BlogPostDaoRepository implements BlogPostDaoInterface{

    private final Map<Integer, BlogPost> blogPostMap = new HashMap<>();

    public static BlogPost helloWordlPost = new BlogPost(1, "First blog", "This is a test of a blog!");

    public BlogPostDaoRepository() {
        this.blogPostMap.put(helloWordlPost.getId(), helloWordlPost);
    }

    @Override
    public Collection<BlogPost> haalBlogPosten() {
        return blogPostMap.values();
    }

    @Override
    public void blogpostToevoegen(BlogPost blog) {
        blogPostMap.put(blog.getId(), blog);
    }

    @Override
    public void blogpostVerwijderen(int id) {
        blogPostMap.remove(id);
    }

    @Override
    public BlogPost blogpostOpvragen(int id) {
        return blogPostMap.get(id);
    }

    @Override
    public void blogpostUpdaten(int id, String titel, String content) {
        BlogPost post = blogPostMap.get(id);
        post.setTitle(titel);
        post.setContent(content);
        blogPostMap.put(post.getId(), post);
    }

    @Override
    public Map<Integer, BlogPost> getMap() {
        return blogPostMap;
    }
}
