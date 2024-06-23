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
    System.out.println("----------- -- -- > inside the controller");
    AuthenticationResponse user = userServiceIml.registerUser(userDto);
    //AuthenticationResponse authResponse = userServiceIml.registerUser(userDto);
    return  ResponseEntity.ok(user);
    //return "User Registered Successfully with username: " + user.getEmail();
  }


  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody LoginDto request
  ) {
    System.out.println("---------------- > inside the controller");
    return ResponseEntity.ok(userServiceIml.loginUser(request));
  }
}
