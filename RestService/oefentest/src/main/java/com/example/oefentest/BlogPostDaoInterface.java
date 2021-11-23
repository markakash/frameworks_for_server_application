package com.example.oefentest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public interface BlogPostDaoInterface {
    Collection<BlogPost> haalBlogPosten();

    void blogpostToevoegen(BlogPost blog);

    void blogpostVerwijderen(int id);

    BlogPost blogpostOpvragen(int id);

    void blogpostUpdaten(int id, String titel, String content);

    Map<Integer, BlogPost> getMap();
}
