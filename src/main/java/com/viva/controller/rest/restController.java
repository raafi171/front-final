package com.viva.controller.rest;

import com.viva.interceptor.CustomRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
public class restController {

    @Autowired
    RestTemplate restTemplate;

    @Value("${backend.base.url}")
    String backUrl;

    public List<ClientHttpRequestInterceptor> getHeader(HttpSession session){
        String token = (String) session.getAttribute("jwt");
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        CustomRequestInterceptor interceptor = new CustomRequestInterceptor("Authorization", String.format("Bearer %s",token));
        interceptors.add(interceptor);
        return interceptors;
    }

    @PostMapping("/block-company")
    public String blockCompany(HttpSession session, @RequestParam ("companyId") int companyId, HttpServletRequest request, HttpServletResponse response, @RequestParam("companyAdmin") String admin){
        List<ClientHttpRequestInterceptor> interceptors = getHeader(session);
        restTemplate.setInterceptors(interceptors);
        String value = restTemplate.postForObject(backUrl + "/block-company" , companyId ,String.class);
        if(value.equals("Blocked"))
        {
//            Authentication authentication = new UsernamePasswordAuthenticationToken(admin,null,null);
//            if (authentication != null)
//                new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return value;
    }
}
