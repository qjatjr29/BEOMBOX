package numble.mybox.common.presentation;

import numble.mybox.user.user.infrastructure.JwtProvider;
import numble.mybox.user.user.domain.UserTokenData;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
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
    if (token != null) {

      jwtProvider.validateToken(token);
    }
    return chain.filter(exchange);
  }
}
