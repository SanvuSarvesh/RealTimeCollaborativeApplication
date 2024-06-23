package com.project.realtime_collaborative_doc_editing.service;

import com.project.realtime_collaborative_doc_editing.dto.AuthenticationResponse;
import com.project.realtime_collaborative_doc_editing.dto.LoginDto;
import com.project.realtime_collaborative_doc_editing.dto.UserDto;
import com.project.realtime_collaborative_doc_editing.model.User;
import com.project.realtime_collaborative_doc_editing.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceIml implements UserService
{
  private final UserRepository userRepository;
  private final JwtService jwtService;


  public User saveUser(User user) {
    return userRepository.save(user);
  }

  public AuthenticationResponse registerUser(UserDto userDto)
  {
    System.out.println("----------- -- -- > inside the service");
    if(userRepository.findByUsername(userDto.getFirstName()) != null)
    {
      System.out.println("----------- -- -- > inside the repository-1");
      throw new RuntimeException("User already exists");
    }

    if(userRepository.findByEmail(userDto.getEmail()) == null)
    {
      System.out.println("----------- -- -- > inside the repository-2");
      throw new RuntimeException("Email already exists");
    }

    User user = new User();
    //user.setUsername(userDto.getUsername());
    user.setPassword(userDto.getPassword());
    user.setEmail(userDto.getEmail());
    user.setFirstName(userDto.getFirstName());
    user.setRole(userDto.getRole());
    System.out.println("----------- -- -- > inside the repository-3");
    String jwtToken = jwtService.generateToken(user);
    return AuthenticationResponse.builder().accessToken(jwtToken).build();

  }


  @Override
  public AuthenticationResponse loginUser(LoginDto loginDto)
  {
    User user = userRepository.findByUsername(loginDto.getEmail());

    if (user == null)
    {
      throw new RuntimeException("User not found");
    }

    if (!user.getPassword().equals(loginDto.getPassword()))
    {
      throw new RuntimeException("Invalid password");
    }
return null;
//    return jwtTokenProvider.generateToken(user.getUsername());
  }

}
