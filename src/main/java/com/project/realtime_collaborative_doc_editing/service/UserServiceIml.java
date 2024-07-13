package com.project.realtime_collaborative_doc_editing.service;

import com.project.realtime_collaborative_doc_editing.common.BaseResponse;
import com.project.realtime_collaborative_doc_editing.dto.LoginDto;
import com.project.realtime_collaborative_doc_editing.dto.UpdatePasswordReqDto;
import com.project.realtime_collaborative_doc_editing.dto.UserDto;
import com.project.realtime_collaborative_doc_editing.dto.UserResponseDto;
import com.project.realtime_collaborative_doc_editing.exceptions.InvalidCredentials;
import com.project.realtime_collaborative_doc_editing.exceptions.UserNotFoundException;
import com.project.realtime_collaborative_doc_editing.model.UserProfile;
import com.project.realtime_collaborative_doc_editing.repository.UserRepository;
import com.project.realtime_collaborative_doc_editing.service.impl.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceIml implements UserService
{
  private final UserRepository userRepository;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final RedisService redisService;

  public UserProfile saveUser(UserProfile user) {
    return userRepository.save(user);
  }

  public BaseResponse registerUser(UserDto userDto)
  {
    BaseResponse baseResponse = new BaseResponse();
    String email = userDto.getEmail();
    String username = userDto.getUsername();

    Optional<UserProfile> userOpt = userRepository.findByEmail(email);
    if(Objects.isNull(userOpt)){
      throw new UserNotFoundException("User with email "+userDto.getEmail()+" is already exists",HttpStatus.NOT_FOUND);
    }
    Optional<UserProfile> userOpt1 = userRepository.findByUsername(username);

    if(userOpt1.isPresent())
    {
      throw new UserNotFoundException("User already exists with username : "+userDto.getUsername(),HttpStatus.BAD_REQUEST);
    }

    UserProfile user = new UserProfile();
    user.setUsername(userDto.getUsername());
    user.setPassword(passwordEncoder.encode(userDto.getPassword()));
    user.setEmail(userDto.getEmail());
    user.setFirstName(userDto.getFirstName());
    user.setRole(userDto.getRole());
    userRepository.save(user);

    String jwtToken = jwtService.generateToken(user);
    //return AuthenticationResponse.builder().accessToken(jwtToken).build();

    UserResponseDto userResponseDto = userDtoToUserResponseDto(userDto);
    baseResponse.setMessage("Registration Successful");
    baseResponse.setSuccess(true);
    baseResponse.setStatusCode(HttpStatus.CREATED.toString());
    baseResponse.setPayload(userResponseDto);

    return baseResponse;
  }


  @Override
  public ResponseEntity<String> loginUser(LoginDto loginDto) {
    Optional<UserProfile> userOpt = userRepository.findByEmail(loginDto.getEmail());
    if (Objects.isNull(userOpt)) {
      throw new RuntimeException("User not found");
    }
    UserProfile user = userOpt.get();
    if (!user.getPassword().equals(loginDto.getPassword())) {
      throw new InvalidCredentials("Invalid Credentials",HttpStatus.FORBIDDEN);
    }
    String jwtToken = jwtService.generateToken(user);
    return new ResponseEntity<>("accessToken : "+jwtToken, HttpStatus.OK);
  }

  @Override
  public BaseResponse forgetPassword(String emailId) {
    return null;
  }

  @Override
  public BaseResponse updatePassword(UpdatePasswordReqDto updatePasswordReqDto) {
    BaseResponse baseResponse = new BaseResponse();
    // Check if entered passwords matches.
    String newPassword = updatePasswordReqDto.getNewPassword();
    String confirmPassword = updatePasswordReqDto.getConfirmPassword();
    if(!Objects.equals(newPassword,confirmPassword)){
      throw new InvalidCredentials("New password and confirmed password must be same.",HttpStatus.BAD_REQUEST);
    }
    String emailId = updatePasswordReqDto.getEmailID();
    Optional<UserProfile> userProfileOpt = userRepository.findByEmail(emailId);
    if(userProfileOpt.isEmpty()){
      throw new UserNotFoundException("User Doesn't Exist.",HttpStatus.NOT_FOUND);
    }
    UserProfile userProfile = userProfileOpt.get();
    UserResponseDto userResponseDto = userProfileToUserResponseSto(userProfile);
    userProfile.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(userProfile);
    baseResponse.setMessage("Your password updated successfully.");
    baseResponse.setSuccess(true);
    baseResponse.setStatusCode(HttpStatus.OK.toString());
    baseResponse.setPayload(userResponseDto);
    return baseResponse;

  }

  private UserResponseDto userDtoToUserResponseDto(UserDto userDto){
    UserResponseDto userResponseDto = new UserResponseDto();
    userResponseDto.setRole(userDto.getRole());
    userResponseDto.setEmail(userDto.getEmail());
    userResponseDto.setUsername(userDto.getUsername());
    userResponseDto.setFirstName(userDto.getFirstName());
    userResponseDto.setCreatedAt(new Date());
    return userResponseDto;
  }

  private UserResponseDto userProfileToUserResponseSto(UserProfile userProfile){
    UserResponseDto userResponseDto = new UserResponseDto();
    userResponseDto.setFirstName(userProfile.getFirstName());
    userResponseDto.setRole(userProfile.getRole());
    userResponseDto.setEmail(userProfile.getEmail());
    userResponseDto.setUsername(userProfile.getUsername());
    return userResponseDto;
  }

}
