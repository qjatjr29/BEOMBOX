package numble.mybox.common.presentation;

import java.util.Arrays;
import numble.mybox.user.user.domain.Role;
import numble.mybox.user.user.domain.UserTokenData;
import numble.mybox.user.user.infrastructure.JwtProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;


@Component
public class JwtAuthenticationFilter implements WebFilter {

  private final JwtProvider jwtProvider;

  public JwtAuthenticationFilter(JwtProvider jwtProvider) {
    this.jwtProvider = jwtProvider;
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

    String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

    if(StringUtils.hasText(token)) {
      jwtProvider.validateToken(token);
      UserTokenData tokenData = jwtProvider.getTokenData(token);
      Authentication authentication = getAuthentication(tokenData);
      ReactiveSecurityContextHolder.getContext().
          map(sc -> {
            sc.setAuthentication(authentication);
            return sc;
          });
    }
    return chain.filter(exchange);
  }

  private Authentication getAuthentication(UserTokenData tokenData) {
    return new UsernamePasswordAuthenticationToken(tokenData, "",
        Arrays.asList(new SimpleGrantedAuthority(Role.USER.getRole())));
  }
}
