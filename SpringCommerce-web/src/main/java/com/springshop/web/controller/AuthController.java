package com.springshop.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springshop.service.entity.User;
import com.springshop.service.repository.UserRepository;
import com.springshop.service.security.UserDetailsServiceImpl;
import com.springshop.web.model.AuthenticationRequest;
import com.springshop.web.model.AuthenticationResponse;
import com.springshop.web.security.JwtUtils;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  @Autowired
  private JwtUtils jwtUtils;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @PostMapping("/register")
  public String registerUser(@RequestBody User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRole("USER");
    userRepository.save(user);
    return "User registered successfully";
  }

  @PostMapping("/login")
  public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
      throws Exception {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
            authenticationRequest.getPassword()));

    final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
    final String jwt = jwtUtils.generateToken(userDetails);

    return ResponseEntity.ok(new AuthenticationResponse(jwt));
  }
}