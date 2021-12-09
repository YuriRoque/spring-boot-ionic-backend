package com.yuri.cursomc.config;

import java.util.Arrays;

import com.yuri.cursomc.security.JWTAuthenticationFilter;
import com.yuri.cursomc.security.JWTAuthorizationFilter;
import com.yuri.cursomc.security.JWTUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  
  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private Environment env;

  @Autowired
  private JWTUtil jwtUtil;

  private static final String[] PUBLIC_MATCHERS = {
    "/h2-console/**"
  };

  private static final String[] PUBLIC_MATCHERS_GET = {
    "/produtos/**",
    "/categorias/**"
  };

  private static final String[] PUBLIC_MATCHERS_POST = {
    "/clientes/**",
    "/auth/forgot/**"
  };

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    // Condição para funcionar o Banco H2 quando estiver em ambiente de teste
    if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
      http.headers().frameOptions().disable();
    }

    /* Desabilita o tipo de proteção csrf pois é uma proteção para caso estivesse
    sendo usado armazenamento na sessão. */
    http.cors().and().csrf().disable();

    http.authorizeRequests()
        .antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
        .antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll()
        .antMatchers(PUBLIC_MATCHERS).permitAll().anyRequest().authenticated();

    http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil));

    http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService));

    // Garante que não vai criar uma sessão de usuário
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

  }

  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", new CorsConfiguration()
        .applyPermitDefaultValues());
    return source;
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
