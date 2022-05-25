package com.example.godtudy.global.config;


import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@ConditionalOnDefaultWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SecurityConfig{

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers(
                "/v2/api-docs", "/configuration/**", "/swagger-resources/**",
                "/swagger-ui.html", "/webjars/**","/swagger/**", "/swagger-ui/**");
    }

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //Spring Security 5.3부터 extends WebSecurityConfigurerAdapter 를 권장히지 않고 Bean으로 등록받는걸 권장한다고함
        //https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter 참고1
        //https://honeywater97.tistory.com/264 참고2
        http
                .authorizeRequests()
                .mvcMatchers("/", "/api/member/**").permitAll()
                .anyRequest().authenticated();
        http
                .csrf().disable();

        return http.build();
    }


}
