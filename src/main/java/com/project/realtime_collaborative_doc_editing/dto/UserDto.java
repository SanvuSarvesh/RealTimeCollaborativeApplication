package com.project.realtime_collaborative_doc_editing.dto;


import com.project.realtime_collaborative_doc_editing.model.Role;
import com.project.realtime_collaborative_doc_editing.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto
{
  private String firstName;
  private String password;
  private String email;
  private Role role;
}
