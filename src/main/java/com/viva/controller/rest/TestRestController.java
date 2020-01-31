//package com.viva.controller.rest;
//
////import com.viva.interceptor.CustomRequestInterceptor;
//import com.viva.jwtauthentication.TokenProvider;
//import com.viva.service.MyUserDetailsService;
//import com.viva.service.UserService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.*;
//import org.springframework.http.client.ClientHttpRequestInterceptor;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;
//
//import javax.servlet.http.HttpSession;
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.viva.entity.Constants.*;
//
//@RestController
//@Slf4j
//public class TestRestController {
//
//    @Autowired
//    TokenProvider tokenProvider;
//
//    @Autowired
//    MyUserDetailsService myUserDetailsService;
//    @Autowired
//    UserService userService;
//
//    @Autowired
//    RestTemplate restTemplate;
//
//    @PostMapping("/authenticateuser")
//    public Object authUser(@RequestParam("userName") String name, @RequestParam("password") String password, HttpSession session) {
//        String token = userService.getToken(name, password);
//        if (token != null) {
//            String url = "http://localhost:8080/view";
//            session.setAttribute("jwt", token);
//            session.setAttribute("userName", name);
//            log.info(token);
//
////            HttpHeaders headers = new HttpHeaders();
////            headers.setContentType(MediaType.APPLICATION_JSON);
////            headers.set(HEADER_STRING, TOKEN_PREFIX + token);
////            HttpEntity<?> entity = new HttpEntity<Object>(headers);
//
////			List<ClientHttpRequestInterceptor> interceptors = getInterceptors(session);
////			restTemplate.setInterceptors(getInterceptors(session));
////            ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
////            return result;
//			//restTemplate.getForObject(url, String.class);
//
//
//            //return null;
//            return "redirect:/view";
//        } else {
//            return "/login";
//        }
//    }
//
////    private List<ClientHttpRequestInterceptor> getInterceptors(HttpSession session) {
////        String jwt = (String) session.getAttribute("jwt");
////        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
////        interceptors.add(new CustomRequestInterceptor("ContentType",MediaType.APPLICATION_JSON_VALUE));
////        interceptors.add(new CustomRequestInterceptor(HEADER_STRING, String.format("Bearer %s", jwt)));
////        return interceptors;
////
////    }
//
//    @GetMapping("/all")
//    public String meth(HttpSession session) {
//        return "for all";
//    }
//
//    @PreAuthorize("hasAnyRole('ADMIN')")
//    @GetMapping("/admin")
//    public String method() {
//        return "i'm admin";
//    }
//}
