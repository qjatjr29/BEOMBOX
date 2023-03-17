package numble.mybox.user.user.application;

import static java.util.Objects.isNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import numble.mybox.user.user.domain.UserTokenData;
import numble.mybox.user.user.infrastructure.JwtProvider;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Slf4j
@Component
public class OAuth2SuccessHandler implements ServerAuthenticationSuccessHandler {

  private final JwtProvider jwtProvider;
  private final ObjectMapper objectMapper;

  public OAuth2SuccessHandler(JwtProvider jwtProvider,
      ObjectMapper objectMapper) {
    this.jwtProvider = jwtProvider;
    this.objectMapper = objectMapper;
  }

  @Override
  public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange,
      Authentication authentication) {

    ServerHttpResponse response = webFilterExchange.getExchange().getResponse();

    // OAuth2AuthenticationToken을 가져옵니다.
    OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

    // OAuth2AuthenticationToken에서 Principal 가져오기
    OAuth2User principal = oauthToken.getPrincipal();
    String registrationId = oauthToken.getAuthorizedClientRegistrationId();

    UserTokenData tokenData = OAuth2TokenDataFactory.getTokenData(registrationId, principal);

    if(!isNull(tokenData)) {
      String accessToken = jwtProvider.generateAccessToken(tokenData);
      String refreshToken = jwtProvider.generateRefreshToken(tokenData);

      // TODO: refreshToken을 Redis에 저장해 관리하기.

      // 헤더에 토큰을 추가합니다.
      response.getHeaders().add("Authorization", "Bearer " + accessToken);
      response.getHeaders().add("Refresh-Token", refreshToken);
      AuthResponse authResponse = new AuthResponse(accessToken, refreshToken);

      // 응답 본문에 토큰 정보 추가
      try {
        byte[] bytes = objectMapper.writeValueAsBytes(authResponse);
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
      } catch (JsonProcessingException e) {
        log.error("tokendata parsing Error");
        throw new RuntimeException();
      }
    }

    return Mono.empty();
  }
}
