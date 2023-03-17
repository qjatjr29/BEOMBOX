package numble.mybox.common.presentation;

import lombok.extern.slf4j.Slf4j;
import numble.mybox.common.error.ErrorCode;
import numble.mybox.common.error.exception.BusinessException;
import numble.mybox.user.user.infrastructure.JwtProvider;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Slf4j
@Component
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

  private final JwtProvider jwtProvider;

  public CurrentUserArgumentResolver(JwtProvider jwtProvider) {
    this.jwtProvider = jwtProvider;
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(CurrentUser.class);
  }

  @Override
  public Mono<Object> resolveArgument(MethodParameter parameter, BindingContext bindingContext,
      ServerWebExchange exchange) {

    String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

    if(token == null || token.length() == 0) throw new BusinessException(
        ErrorCode.TOKEN_NOT_EXISTS);
    return Mono.just(jwtProvider.getTokenData(token));

  }
}
