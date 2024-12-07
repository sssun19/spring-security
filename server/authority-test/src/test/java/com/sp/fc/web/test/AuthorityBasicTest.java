package com.sp.fc.web.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthorityBasicTest extends WebIntegrationTest{


    TestRestTemplate client;

    @DisplayName("1. greeting 메세지를 불러온다.")
    @Test
    void test_1() {
        client = new TestRestTemplate("user1", "1111");
        ResponseEntity<String> response = client.getForEntity(uri("/greeting/suna"), String.class);

        assertEquals("hello suna", response.getBody());
        System.out.println(response.getBody());
    }

}
