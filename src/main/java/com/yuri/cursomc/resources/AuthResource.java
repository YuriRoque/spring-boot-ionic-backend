package com.yuri.cursomc.resources;

import javax.servlet.http.HttpServletResponse;

import com.yuri.cursomc.security.JWTUtil;
import com.yuri.cursomc.security.UserSS;
import com.yuri.cursomc.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth")
public class AuthResource {
  
  @Autowired
  private JWTUtil jwtUtil;

  @RequestMapping(value = "/refresh_token", method = RequestMethod.POST)
  public ResponseEntity<Void> refreshToken(HttpServletResponse response) {

    UserSS user = UserService.authenticated();
    String token = jwtUtil.generateToken(user.getUsername());

    response.addHeader("Authorization", "Bearer " + token);

    return ResponseEntity.noContent().build();

  }

}
