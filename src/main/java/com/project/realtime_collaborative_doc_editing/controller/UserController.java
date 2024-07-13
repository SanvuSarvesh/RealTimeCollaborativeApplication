package com.project.realtime_collaborative_doc_editing.controller;


import com.project.realtime_collaborative_doc_editing.common.BaseResponse;
import com.project.realtime_collaborative_doc_editing.dto.LoginDto;
import com.project.realtime_collaborative_doc_editing.dto.UpdatePasswordReqDto;
import com.project.realtime_collaborative_doc_editing.dto.UserDto;
import com.project.realtime_collaborative_doc_editing.model.UserProfile;
import com.project.realtime_collaborative_doc_editing.repository.UserRepository;
import com.project.realtime_collaborative_doc_editing.service.JwtService;
import com.project.realtime_collaborative_doc_editing.service.UserServiceIml;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/v1/auth")
public class UserController
{

  private final UserServiceIml userServiceIml;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final UserRepository userRepository;

  @PostMapping("/save")
  public UserProfile saveUser(@RequestBody UserProfile user) {
    return userServiceIml.saveUser(user);
  }

  @PostMapping("/register")
  public ResponseEntity<BaseResponse>  register(@RequestBody UserDto userDto){
    BaseResponse accessToken = userServiceIml.registerUser(userDto);
    return  ResponseEntity.ok(accessToken);
  }


  @PostMapping("/authenticate")
  public ResponseEntity<BaseResponse> authenticate(@RequestBody LoginDto loginDto) {
    BaseResponse baseResponse = new BaseResponse();
    try {
      Authentication authentication = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
      );
      SecurityContextHolder.getContext().setAuthentication(authentication);
      Optional<UserProfile> user = userRepository.findByEmail(loginDto.getEmail());
      String accessToken = jwtService.generateToken(user.get());

       baseResponse.setMessage("Login successful.");
       baseResponse.setSuccess(true);
       baseResponse.setStatusCode(HttpStatus.OK.toString());
       baseResponse.setPayload(accessToken);

       return new ResponseEntity<>(baseResponse,HttpStatus.OK);

    } catch (AuthenticationException e) {
      baseResponse.setMessage("Login failed.");
      baseResponse.setSuccess(false);
      baseResponse.setStatusCode(HttpStatus.FORBIDDEN.toString());
      baseResponse.setPayload("Access token Not generated, try again.");

      return new ResponseEntity<>(baseResponse,HttpStatus.UNAUTHORIZED);

    }

  }

  @PostMapping("/forget-password")
  public ResponseEntity<BaseResponse>  forgetPassword(@RequestParam String emailId){
    BaseResponse accessToken = new BaseResponse();
    return  ResponseEntity.ok(accessToken);
  }

  @PutMapping("/update-password")
  public ResponseEntity<BaseResponse>  updatePassword(@RequestBody UpdatePasswordReqDto updatePasswordReqDto){
    BaseResponse baseResponse = userServiceIml.updatePassword(updatePasswordReqDto);
    return new ResponseEntity<>(baseResponse,HttpStatus.OK);
  }

}
