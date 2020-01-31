package com.viva.controller;

import com.viva.dto.BaseResponseDTO;
import com.viva.dto.CompanyDTO;
import com.viva.dto.CompanyEmployeeListDTO;
import com.viva.dto.UserInfoDTO;
import com.viva.interceptor.CustomRequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static com.viva.entity.Constants.COMPANY_NAME;

@Controller
@Slf4j
public class SecurityController {


    @Autowired
    RestTemplate restTemplate;

    @Autowired
    @Qualifier("sessionRegistry")
    private SessionRegistry sessionRegistry;

    @Value("${backend.base.url}")
    String baseUrl;

    @GetMapping("/sign-up")
    public String signUp() {
        return "sign-up";
    }

    @PostMapping("/save-user")
    public String signUp(String userName, String password, Model model) {
        UserInfoDTO dto = UserInfoDTO.builder()
                .userName(userName)
                .password(password)
                .companyName(COMPANY_NAME)
                .build();

        BaseResponseDTO responseDto = restTemplate.postForObject(baseUrl + "/save-user", dto, BaseResponseDTO.class);
        model.addAttribute("message", responseDto.getMessage());
        return "login";

    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/authenticate-user")
    public String authUser(@RequestParam("userName") String name, @RequestParam("password") String pass, HttpSession session, RedirectAttributes redirect) {
        UserInfoDTO dto = UserInfoDTO.builder()
                .userName(name)
                .password(pass)
                .build();

        UserInfoDTO userInfoDTO = restTemplate.postForObject(baseUrl + "/authenticate-user", dto, UserInfoDTO.class);
        List<Object> principals = sessionRegistry.getAllPrincipals();
        List<String> userNamesList = new ArrayList<>();
        for(Object principal: principals){
            if(principal instanceof User){
                userNamesList.add(((User)principal).getUsername());
            }
        }
        if (userInfoDTO.getToken() == null) return "login";
        else {
            session.setAttribute("jwt", userInfoDTO.getToken());
            session.setAttribute("userName", name);
            session.setAttribute("userRole", userInfoDTO.getRole());
            session.setAttribute("companyName", userInfoDTO.getCompanyName());
            String userRole = String.format("ROLE_COMPANY_ADMIN");
            List<GrantedAuthority> role = new ArrayList<>();
            role.add(new SimpleGrantedAuthority(userInfoDTO.getRole()));
            Authentication authentication = new UsernamePasswordAuthenticationToken(name, null, role);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            if (userRole.equals(userInfoDTO.getRole()))
                return "redirect:/company-home";

            return "redirect:/home";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_COMPANY_ADMIN','ROLE_COMPANY_USER')")
    @GetMapping("/company-home")
    public String companyHome(Model model, HttpSession session) {
        String name = (String) session.getAttribute("userName");
        String companyName = (String) session.getAttribute("companyName");
        List<ClientHttpRequestInterceptor> interceptors = getHeader(session);
        restTemplate.setInterceptors(interceptors);
        List<CompanyEmployeeListDTO> dtoList = restTemplate.postForObject(baseUrl + "/employee-list", name, List.class);
        model.addAttribute("companyName", companyName);
        model.addAttribute("employeeList", dtoList);
        return "company-home";
    }

    public List<ClientHttpRequestInterceptor> getHeader(HttpSession session) {
        String token = (String) session.getAttribute("jwt");
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        CustomRequestInterceptor interceptor = new CustomRequestInterceptor("Authorization", String.format("Bearer %s", token));
        interceptors.add(interceptor);
        return interceptors;
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/home")
    public String view(HttpSession session, Model model) {
        List<ClientHttpRequestInterceptor> interceptors = getHeader(session);
        restTemplate.setInterceptors(interceptors);
        List<CompanyDTO> dtoList = restTemplate.getForObject(baseUrl + "/company-list", List.class);
//        List<CompanyDTO> dtoList= (List<CompanyDTO>) restTemplate.exchange(baseUrl + "/company-list", HttpMethod.GET, entity, ArrayList.class);
        model.addAttribute("companyList", dtoList);
        return "home";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null)
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        return "login";
    }

    @PostMapping("/save-company")
    public String saveCompany(HttpSession session, @RequestParam("companyName") String name, @RequestParam("contact") String contact, @RequestParam("admin") String admin) {
        List<ClientHttpRequestInterceptor> interceptors = getHeader(session);
        restTemplate.setInterceptors(interceptors);
        CompanyDTO companyDTO = CompanyDTO.builder()
                .company(name)
                .contact(contact)
                .admin(admin)
                .build();
        BaseResponseDTO dto = restTemplate.postForObject(baseUrl + "/add-company", companyDTO, BaseResponseDTO.class);
        return "redirect:/home";
    }

    @PreAuthorize("hasAnyRole('ROLE_COMPANY_ADMIN')")
    @GetMapping("/add-new-employee")
    public String addEmployee() {
        return "add_new_employee";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/add-new-company")
    public String addCompany() {
        return "add_new_company";
    }

    @PostMapping("/save-employee")
    public String addEmployee(HttpSession session, @RequestParam("employeeName") String employeeName, @RequestParam("contact") String contact) {
        String name = (String) session.getAttribute("userName");
        List<ClientHttpRequestInterceptor> interceptors = getHeader(session);
        restTemplate.setInterceptors(interceptors);
        CompanyEmployeeListDTO companyEmployeeListDTO = CompanyEmployeeListDTO.builder()
                .employeeName(employeeName)
                .employeeContactNo(contact)
                .admin(name)
                .build();
        BaseResponseDTO dto = restTemplate.postForObject(baseUrl + "/save-employee", companyEmployeeListDTO, BaseResponseDTO.class);
        return "redirect:/company-home";
    }
}
