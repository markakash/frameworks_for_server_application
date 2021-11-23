package com.example.oefentest;

import jdk.dynalink.linker.support.Guards;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class BlogControllerTestRestTemplate {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void listBlogPosts() {
        BlogPost[] posts = restTemplate.getForObject("/posts", BlogPost[].class);
        assertThat(posts)
                .isNotNull()
                .hasSizeGreaterThan(0);
    }

    @Test
    public void getBlogPost() {
        BlogPost post = restTemplate.getForObject("/posts/{id}", BlogPost.class, 1);
        assertThat(post)
                .isNotNull()
                .isEqualTo(BlogPostDaoRepository.helloWordlPost);
    }

    @Test
    public void createBlogPosts() {
        BlogPost post = new BlogPost(2, "title", "content");
        URI newPostLocation = restTemplate.postForLocation("/posts", post);

        BlogPost retrievedPost = restTemplate.getForObject(newPostLocation, BlogPost.class);
        assertThat(retrievedPost).isEqualTo(post);
    }

    @Test
    public void getUnExistingPost() {
        ResponseEntity<String> response = restTemplate.getForEntity("/posts/{id}", String.class, 999);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void updatePost() {
        BlogPost post = new BlogPost(2, "new title", "new content");
        restTemplate.put("/posts/{id}", post, 2);

        BlogPost retrievedPost = restTemplate.getForObject("/posts/{id}", BlogPost.class, 2);
        assertThat(retrievedPost).isEqualTo(post);
    }

    @Test
    public void deletePost() {
        restTemplate.delete("/posts/{id}", 2);

        ResponseEntity<String> response = restTemplate.getForEntity("/posts/{id}", String.class, 2);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
