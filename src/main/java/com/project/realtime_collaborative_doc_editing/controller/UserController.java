package com.project.realtime_collaborative_doc_editing.controller;


import com.project.realtime_collaborative_doc_editing.dto.AuthenticationResponse;
import com.project.realtime_collaborative_doc_editing.dto.LoginDto;
import com.project.realtime_collaborative_doc_editing.dto.UserDto;
import com.project.realtime_collaborative_doc_editing.model.User;
import com.project.realtime_collaborative_doc_editing.service.UserServiceIml;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody LoginDto request) {
    return ResponseEntity.ok(userServiceIml.loginUser(request));
  }

}
