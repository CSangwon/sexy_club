package com.example.chattest.global.config;


import com.example.chattest.global.jwt.*;
import com.example.chattest.global.jwt.AccessDeniedHandler.CustomAccessDeniedHandler;
import com.example.chattest.global.jwt.AuthenticationEntryPoint.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtExceptionFilter jwtExceptionFilter;
    private final JwtTokenProvider jwtTokenProvider;
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/v2/api-docs", "/configuration/**", "/swagger-resources/**",
                "/swagger-ui.html", "/webjars/**", "/swagger/**", "/swagger-ui/**"
        );
        web.ignoring().antMatchers("/ws/**", "/", "/join", "/chatRoomList", "/js/**", "/css/**", "/error", "/h2-console/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .authorizeRequests()
                .mvcMatchers("/home", "/api/v1/member/**", "/chat").permitAll()
                .anyRequest().authenticated()
            .and()
                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())//401
            .and()
                .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())//403
            .and()
                .logout()
                .logoutSuccessHandler((new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK)));

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); //jwt필터
        http.addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


}


//Spring Security 5.3부터 extends WebSecurityConfigurerAdapter 를 권장히지 않고 Bean으로 등록받는걸 권장한다고함
//        //https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter 참고1
//        //https://honeywater97.tistory.com/264 참고2
