package com.project.realtime_collaborative_doc_editing.service;

import com.project.realtime_collaborative_doc_editing.dto.AuthenticationResponse;
import com.project.realtime_collaborative_doc_editing.dto.LoginDto;
import com.project.realtime_collaborative_doc_editing.dto.UserDto;


public interface UserService {
  AuthenticationResponse registerUser(UserDto userDto);
  AuthenticationResponse loginUser(LoginDto loginDto);
}
