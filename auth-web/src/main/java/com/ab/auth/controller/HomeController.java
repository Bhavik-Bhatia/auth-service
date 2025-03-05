package com.ab.auth.controller;

import com.ab.auth.constants.AuthURI;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AuthURI.HOME_URI)
public class HomeController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> userMe(@RequestParam String token) {
        return ResponseEntity.status(HttpStatus.OK).body("Welcome");
    }

}
