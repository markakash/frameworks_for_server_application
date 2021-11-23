package com.example.oefentest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

import java.util.Collection;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class OefentestApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void contextLoads() {
	}

	@Test
	public void testgeefAllePosten() {
		this.webTestClient.get().uri("/posts")
				.header(ACCEPT, APPLICATION_JSON_VALUE)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(APPLICATION_JSON)
				.expectBodyList(BlogPost.class).hasSize(1);
	}

	@Test
	public void testverwijderPost() {
		this.webTestClient.delete()
				.uri("/posts/{id}", 1).exchange()
				.expectStatus().isOk();
		this.webTestClient.get()
				.uri("/posts/{id}", 1).exchange()
				.expectStatus().isNotFound();
	}

	@Test
	public void testCreatePost() {
		this.webTestClient.post()
				.uri("/posts").contentType(APPLICATION_JSON)
				.bodyValue(new BlogPost(2, "Title", "Content"))
				.exchange().expectStatus().isCreated()
				.expectHeader().exists("Location")
				.expectBody().isEmpty();
	}

	@Test
	public void testUpdatePost() {
		BlogPost updatedPost = new BlogPost(2, "new title", "new content");
		this.webTestClient.put()
				.uri("/posts/{id}", updatedPost)
				.contentType(APPLICATION_JSON)
				.bodyValue(updatedPost)
				.exchange().expectStatus().isBadRequest(); //let op hier. Opl has isNoContent()
	}
}
