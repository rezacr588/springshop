package com.springshop.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springshop.service.entity.User;
import com.springshop.service.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException("User not found");
    }
    return org.springframework.security.core.userdetails.User.builder()
        .username(user.getUsername())
        .password(user.getPassword())
        .roles(user.getRole())
        .build();
  }
}