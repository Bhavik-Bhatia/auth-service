package com.ab.auth.security.handler;


import com.ab.auth.entity.User;
import com.ab.auth.helper.UserHelper;
import com.ab.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Will be used for OAuth Success scenarios when spring security has access token and user details we will persist them via
 * using success handler.
 */
@Component
public class OauthSuccessHandler implements AuthenticationSuccessHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(OauthSuccessHandler.class);
    private final UserHelper userHelper;
    private final JwtUtil jwtUtil;
    private final Environment environment;

    @Autowired
    public OauthSuccessHandler(UserHelper helper, JwtUtil jwt, Environment env) {
        userHelper = helper;
        jwtUtil = jwt;
        environment = env;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        if (!userHelper.isUserExists(oAuth2AuthenticationToken.getPrincipal().getAttribute("email"))) {
            User userEntity = new User();
//          2 provider is for oauth
            userEntity.setAuthProvider(2);
            userEntity.setUserName(oAuth2AuthenticationToken.getPrincipal().getAttribute("name"));
            userEntity.setEmail(oAuth2AuthenticationToken.getPrincipal().getAttribute("email"));
            userHelper.insertUserDetails(userEntity);
        } else {
            LOGGER.debug("Oauth User already exists");
        }
//      Generate JWT token and redirect
        String jwtToken = jwtUtil.generateJWTToken(oAuth2AuthenticationToken.getPrincipal().getAttribute("email"));
        response.sendRedirect(environment.getProperty("auth.home.redirect.url") + "?token=" + jwtToken);
    }
}
