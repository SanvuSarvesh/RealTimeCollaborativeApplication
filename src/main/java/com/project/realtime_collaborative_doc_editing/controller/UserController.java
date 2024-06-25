package com.project.realtime_collaborative_doc_editing.controller;


import com.project.realtime_collaborative_doc_editing.dto.AuthenticationResponse;
import com.project.realtime_collaborative_doc_editing.dto.LoginDto;
import com.project.realtime_collaborative_doc_editing.dto.UserDto;
import com.project.realtime_collaborative_doc_editing.model.User;
import com.project.realtime_collaborative_doc_editing.service.UserServiceIml;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/v1/auth")
public class UserController
{

  private final UserServiceIml userServiceIml;
  private final AuthenticationManager authenticationManager;
  @PostMapping("/save")
  public User saveUser(@RequestBody User user) {
    return userServiceIml.saveUser(user);
  }

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse>  register(@RequestBody UserDto userDto){
    AuthenticationResponse accessToken = userServiceIml.registerUser(userDto);
    return  ResponseEntity.ok(accessToken);
  }


  @PostMapping("/authenticate")
  public ResponseEntity<String> authenticate(@RequestBody LoginDto loginDto) {
    try {
      Authentication authentication = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
      );
      SecurityContextHolder.getContext().setAuthentication(authentication);
      return ResponseEntity.ok("Login successful");
    } catch (AuthenticationException e) {
      return ResponseEntity.status(401).body("Login failed: " + e.getMessage());
    }

  }

}
