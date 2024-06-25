package com.project.realtime_collaborative_doc_editing.service;

import com.project.realtime_collaborative_doc_editing.dto.AuthenticationResponse;
import com.project.realtime_collaborative_doc_editing.dto.LoginDto;
import com.project.realtime_collaborative_doc_editing.dto.UserDto;
import com.project.realtime_collaborative_doc_editing.model.User;
import com.project.realtime_collaborative_doc_editing.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

  public User saveUser(User user) {
    return userRepository.save(user);
  }

  public AuthenticationResponse registerUser(UserDto userDto)
  {
    String email = userDto.getEmail();
    String username = userDto.getUsername();
    Optional<User> userOpt = userRepository.findByEmail(email);
    if(Objects.isNull(userOpt)){
      throw new RuntimeException("User with email "+userDto.getEmail()+" is already exists");
    }
    if(userRepository.findByUsername(userDto.getFirstName()) != null)
    {
      throw new RuntimeException("User already exists");
    }

    User user = new User();
    user.setUsername(userDto.getUsername());
    user.setPassword(passwordEncoder.encode(userDto.getPassword()));
    //user.setPassword(new PassuserDto.getPassword());
    user.setEmail(userDto.getEmail());
    user.setFirstName(userDto.getFirstName());
    user.setRole(userDto.getRole());
    userRepository.save(user);
    String jwtToken = jwtService.generateToken(user);
    return AuthenticationResponse.builder().accessToken(jwtToken).build();

  }


  @Override
  public ResponseEntity<String> loginUser(LoginDto loginDto) {
    Optional<User> userOpt = userRepository.findByEmail(loginDto.getEmail());
    if (Objects.isNull(userOpt)) {
      throw new RuntimeException("User not found");
    }
    User user = userOpt.get();
    if (!user.getPassword().equals(loginDto.getPassword())) {
      throw new RuntimeException("Invalid password");
    }
    String jwtToken = jwtService.generateToken(user);
    return new ResponseEntity<>("accessToken : "+jwtToken, HttpStatus.OK);
  }

}
