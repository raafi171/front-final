package com.viva.config;

//import com.viva.authentication.JwtAuthenticationEntryPoint;
//import com.viva.authentication.JwtAuthenticationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {


//    @Autowired
//    private JwtAuthenticationEntryPoint unauthorizeHandler;
//
//    @Autowired
//    private JwtAuthenticationFilter jwtAuthenticationFilter;

//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception{
//        return super.authenticationManagerBean();
//    }

    @Override
    protected void configure(HttpSecurity http)throws Exception{
        http.cors().and().csrf().disable();
        http.authorizeRequests()
                .antMatchers("/css/**","/sign-up","/login","/save-user","/authenticate-user").permitAll()
                .anyRequest()
                .authenticated()
                .and().logout().disable()
                //.and()
//                .exceptionHandling().authenticationEntryPoint(unauthorizeHandler)
//                .and()
                //.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        ;

        //http.addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
