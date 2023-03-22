package numble.mybox.common.presentation;

import lombok.extern.slf4j.Slf4j;
import numble.mybox.common.error.ErrorCode;
import numble.mybox.common.error.exception.BusinessException;
import numble.mybox.user.user.infrastructure.JwtProvider;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;



@Slf4j
@Component
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

  private static final String HEADER_AUTHORIZATION = "Authorization";

  private final JwtProvider jwtProvider;

  public CurrentUserArgumentResolver(JwtProvider jwtProvider) {
    this.jwtProvider = jwtProvider;
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(CurrentUser.class);
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

    String token = webRequest.getHeader(HEADER_AUTHORIZATION);

    if(token == null || token.length() == 0) throw new BusinessException(ErrorCode.TOKEN_NOT_EXISTS);

    return jwtProvider.getTokenData(token).getId();
  }
}
