package numble.mybox.config;

import numble.mybox.common.presentation.JwtAuthenticationFilter;
import numble.mybox.user.user.application.CustomOAuth2UserService;
import numble.mybox.user.user.application.OAuth2SuccessHandler;
import numble.mybox.user.user.domain.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final OAuth2SuccessHandler oAuth2SuccessHandler;
  private final CustomOAuth2UserService customOAuth2UserService;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  public SecurityConfig(
      OAuth2SuccessHandler oAuth2SuccessHandler,
      CustomOAuth2UserService customOAuth2UserService,
      JwtAuthenticationFilter jwtAuthenticationFilter) {
    this.oAuth2SuccessHandler = oAuth2SuccessHandler;
    this.customOAuth2UserService = customOAuth2UserService;
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .httpBasic().disable()
        .csrf().disable()
        .cors()
        .and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll()
        .antMatchers("/users/**").hasRole(Role.USER.name())
        .antMatchers("/folders/**").hasRole(Role.USER.name())
        .antMatchers("/files/**").hasRole(Role.USER.name())
        .anyRequest().permitAll()
        .and()
        .logout()
        .and()
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .oauth2Login()
        .successHandler(oAuth2SuccessHandler)
        .userInfoEndpoint()
        .userService(customOAuth2UserService);
  }

}
