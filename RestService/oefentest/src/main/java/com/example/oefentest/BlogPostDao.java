package com.example.oefentest;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class BlogPostDao implements BlogPostDaoInterface {

    private HashMap<Integer, BlogPost> blogposten = new HashMap<>();
    public static BlogPost helloWordlPost = new BlogPost(1, "First blog", "This is a test of a blog!");

    public BlogPostDao() {
        blogposten.put(helloWordlPost.getId(), helloWordlPost);
    }

    @Override
    public Collection<BlogPost> haalBlogPosten() {
        return blogposten.values();
    }

    @Override
    public void blogpostToevoegen(BlogPost blog) {
        blogposten.put(blog.getId(), blog);
    }

    @Override
    public void blogpostVerwijderen(int id) {
        blogposten.remove(id);
    }

    @Override
    public BlogPost blogpostOpvragen(int id) {
        return blogposten.get(id);
    }

    @Override
    public void blogpostUpdaten(int id, String titel, String content) {
        BlogPost bla = blogposten.get(id);
        bla.setTitle(titel);
        bla.setContent(content);
        blogposten.put(bla.getId(), bla);
    }

    @Override
    public Map<Integer, BlogPost> getMap() {
        return blogposten;
    }
}
