package numble.mybox.user.user.application;

import static java.util.Objects.isNull;

import java.util.Map;
import numble.mybox.user.user.domain.UserTokenData;
import numble.mybox.user.user.infrastructure.JwtProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CustomOAuth2SuccessHandler implements ServerAuthenticationSuccessHandler {

  private final JwtProvider jwtProvider;

  public CustomOAuth2SuccessHandler(JwtProvider jwtProvider) {
    this.jwtProvider = jwtProvider;
  }

  @Override
  public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange,
      Authentication authentication) {

    OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
    OAuth2User oAuth2User = oauthToken.getPrincipal();

    Map<String, Object> attributes = oAuth2User.getAttributes();

    String registrationId = (String) attributes.get("provider");

    UserTokenData tokenData = OAuth2TokenDataFactory.getTokenData(registrationId, oAuth2User);
    ServerHttpResponse response = webFilterExchange.getExchange().getResponse();

    if(!isNull(tokenData)) {
      String accessToken = jwtProvider.generateAccessToken(tokenData);
      String refreshToken = jwtProvider.generateRefreshToken(tokenData);

      response.setStatusCode(HttpStatus.OK);
      response.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
      return response.writeWith(Mono.just(response.bufferFactory().wrap(refreshToken.getBytes())));
      // TODO: refreshToken을 Redis에 저장해 관리하기.
    }
    return response.writeWith(Mono.empty());
  }
}
