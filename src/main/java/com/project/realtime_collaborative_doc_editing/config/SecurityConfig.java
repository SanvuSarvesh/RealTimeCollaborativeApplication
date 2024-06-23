package com.project.realtime_collaborative_doc_editing.config;


import static com.project.realtime_collaborative_doc_editing.model.Permission.ADMIN_CREATE;
import static com.project.realtime_collaborative_doc_editing.model.Permission.ADMIN_READ;
import static com.project.realtime_collaborative_doc_editing.model.Permission.MEMBER_CREATE;
import static com.project.realtime_collaborative_doc_editing.model.Permission.MEMBER_READ;
import static com.project.realtime_collaborative_doc_editing.model.Role.ADMIN;
import static com.project.realtime_collaborative_doc_editing.model.Role.MEMBER;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig
{


  private final AuthenticationProvider authenticationProvider;
  private final JwtAuthFilter jwtAuthFilter;


  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
  {
    return http
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(req ->
            req.requestMatchers("/*")
                .permitAll()
//                    .requestMatchers("/user/v1/auth/*")
//                        .permitAll()
                .requestMatchers("/user/v1/management/**").hasAnyRole(ADMIN.name(), MEMBER.name())
                .requestMatchers(GET, "/user/v1/management/**")
                .hasAnyAuthority(ADMIN_READ.name(), MEMBER_READ.name())
                .requestMatchers(POST, "/user/v1/auth/**")
                .hasAnyAuthority(ADMIN_CREATE.name(), MEMBER_CREATE.name())
                .anyRequest()
                .authenticated())
        .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }


//  @Configuration
//  public class AuthenticationProviderConfig
//  {
//
//    @Bean
//    public AuthenticationProvider authenticationProvider()
//    {
//      return new DaoAuthenticationProvider();
//    }
//  }
}
