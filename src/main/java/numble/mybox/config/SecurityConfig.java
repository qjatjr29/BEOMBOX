package numble.mybox.config;

import numble.mybox.common.error.ErrorCode;
import numble.mybox.common.error.exception.BusinessException;
import numble.mybox.common.presentation.JwtAuthenticationFilter;
import numble.mybox.user.user.application.CustomOAuth2SuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

  private final CustomOAuth2SuccessHandler oAuth2SuccessHandler;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  public SecurityConfig(
      CustomOAuth2SuccessHandler oAuth2SuccessHandler,
      JwtAuthenticationFilter jwtAuthenticationFilter) {
    this.oAuth2SuccessHandler = oAuth2SuccessHandler;
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
  }

  @Bean
  public SecurityWebFilterChain securityWebFilterChain(final ServerHttpSecurity serverHttpSecurity) {

    return serverHttpSecurity
        .cors().and()
        .csrf().disable()
        .httpBasic().disable()
        .formLogin().disable()
        .authorizeExchange()
        .pathMatchers("/api/**").authenticated()
        .pathMatchers("/users").authenticated()
        .anyExchange().permitAll()
        .and()
        .oauth2Login()
        .authenticationSuccessHandler(oAuth2SuccessHandler)
        .and()
        .logout()
        .and().exceptionHandling()
        .accessDeniedHandler((exchange, exception) -> Mono.error(new BusinessException(ErrorCode.UNAUTHORIZED)))
        .and()
        .addFilterBefore(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
        .build();
  }

}
