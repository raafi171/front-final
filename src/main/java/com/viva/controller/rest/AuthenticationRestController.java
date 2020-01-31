//package com.viva.controller.rest;
//
//import com.fasterxml.jackson.annotation.JsonProperty;
//import com.viva.dto.UserInfoDTO;
//import com.viva.jwtauthentication.TokenProvider;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.*;
//
//import javax.naming.AuthenticationException;
//
//@CrossOrigin(origins = "*", maxAge = 3600)
//@RestController
//@RequestMapping("/token")
//public class AuthenticationRestController {
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private TokenProvider tokenProvider;
//
//    @RequestMapping(value = "/generate-token", method = RequestMethod.POST)
//    public ResponseEntity<?> register(@RequestBody UserInfoDTO userInfoDTO) throws AuthenticationException{
//        final Authentication authentication = authenticationManager.authenticate(
//             new UsernamePasswordAuthenticationToken(userInfoDTO.getUserName(),userInfoDTO.getPassword())
//        );
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        final String token = tokenProvider.generateToken(authentication);
//        return ResponseEntity.ok(new AuthToken(token));
//    }
//
//    public static class AuthToken{
//        private String token;
//        public AuthToken(String token){ this.token = token; }
//
//        @JsonProperty("token")
//        String getToken(){ return token; }
//
//        void setToken(String token) { this.token = token; }
//    }
//}
