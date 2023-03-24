package numble.mybox.user.user.application;

import static java.util.Objects.isNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import numble.mybox.user.user.domain.UserTokenData;
import numble.mybox.user.user.infrastructure.JwtProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

  @Value("${spring.security.oauth2.redirect-url}")
  private String redirectUrl;
  private final JwtProvider jwtProvider;
  private final ObjectMapper objectMapper;

  public OAuth2SuccessHandler(JwtProvider jwtProvider,
      ObjectMapper objectMapper) {
    this.jwtProvider = jwtProvider;
    this.objectMapper = objectMapper;
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
    Map<String, Object> attributes = oAuth2User.getAttributes();

    String registrationId = (String) attributes.get("provider");

    UserTokenData tokenData = OAuth2TokenDataFactory.getTokenData(registrationId, oAuth2User);

    if(!isNull(tokenData)) {
      String accessToken = jwtProvider.generateAccessToken(tokenData);
      String refreshToken = jwtProvider.generateRefreshToken(tokenData);

      // TODO: refreshToken을 Redis에 저장해 관리하기.
      response.sendRedirect(redirectUrl+"?accesstoken="+accessToken+"&refreshtoken="+refreshToken);
    }
  }
}
