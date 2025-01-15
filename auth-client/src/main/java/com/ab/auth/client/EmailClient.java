package com.ab.auth.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(value = "email", url = "http://bbhatia.asite.com:8080/notification")
public interface EmailClient {

    @PostMapping(value = "/sendemail", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Boolean> sendEmail(@RequestParam Map<String, String> mailParam);
}
