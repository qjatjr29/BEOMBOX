package numble.mybox.config;

import numble.mybox.common.presentation.JwtAuthenticationFilter;
import numble.mybox.user.user.application.OAuth2SuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

  private final OAuth2SuccessHandler oAuth2SuccessHandler;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  public SecurityConfig(
      OAuth2SuccessHandler oAuth2SuccessHandler,
      JwtAuthenticationFilter jwtAuthenticationFilter) {
    this.oAuth2SuccessHandler = oAuth2SuccessHandler;
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public SecurityWebFilterChain securityWebFilterChain(final ServerHttpSecurity serverHttpSecurity) {
    return serverHttpSecurity
        .csrf().disable()
        .httpBasic().disable()
        .authorizeExchange()
        .pathMatchers("/login/**").authenticated()
        .anyExchange().permitAll()
        .and()
        .oauth2Login().authenticationSuccessHandler(oAuth2SuccessHandler)
        .and()
        .addFilterBefore(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
        .logout()
        .and().exceptionHandling()
//        .accessDeniedHandler(((exchange, exception) -> Mono.error(new ApplicationContextException(ACCESS_DENIED))))
        .and().build();
  }


}
