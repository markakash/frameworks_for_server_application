package com.example.oefentest;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.FileNotFoundException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class Controller {
    private final Logger logger = LoggerFactory.getLogger(Controller.class);

    private BlogPostDao blogPostDao;

    @Autowired
    public Controller(BlogPostDao blogPostDao) {
        this.blogPostDao = blogPostDao;
    }

    @GetMapping("/posts")//om een endpoint te implementeren dat alle blogposten te halen
    @Secured({"ROLE_USER"})
    public Collection<BlogPost> getAllePosten() {
        return blogPostDao.haalBlogPosten();
    }

    @DeleteMapping("/posts/{id}")
    @Secured({"ROLE_ADMIN"})
    public void verwijderPost(@PathVariable("id") int id) {
        blogPostDao.blogpostVerwijderen(id);
    }

    @PutMapping("/posts/{id}")
    @Secured({"ROLE_ADMIN"})
    public void updatePost(@PathVariable("id") int id, @RequestBody String titel, String content) {
        blogPostDao.blogpostUpdaten(id, titel, content);
    }

    @PostMapping("/posts")
    @Secured({"ROLE_USER"})
    public ResponseEntity<Object> plaatsPost(@RequestBody BlogPost blog, UriComponentsBuilder builder) {
        URI location = builder.path("/posts/{id}").buildAndExpand(blog.getId()).toUri();
        blogPostDao.blogpostToevoegen(blog);
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/posts/{id}")
    @Secured({"ROLE_USER"})
    public BlogPost getSpecifiekPost(@PathVariable("id") int id) throws BlogPostException {
        if (!blogPostDao.getMap().containsKey(id)) {
            throw new BlogPostException();
        }
        return blogPostDao.blogpostOpvragen(id);
    }

    /**
     * Explicit exception handler to map PostNotFoundException to a 404 Not Found HTTP status code.
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(BlogPostException.class)
    public void handleNotFound(Exception ex) {
        logger.warn("Exception is: " + ex.getMessage());
        // return empty 404
    }
}
