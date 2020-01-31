//package com.viva.service;
//
//import com.viva.interceptor.CustomRequestInterceptor;
//import org.springframework.stereotype.Service;
//
//
//@Service
//public class UserService {
//    public CustomRequestInterceptor getHeader(String token){
//        CustomRequestInterceptor interceptor = new CustomRequestInterceptor("Authorization", String.format("Bearer %s",token));
//        return interceptor;
//    }
//}
