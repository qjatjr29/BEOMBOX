package numble.mybox.user.user.application;

import java.util.Map;
import numble.mybox.user.user.domain.AuthProvider;
import numble.mybox.user.user.domain.User;
import numble.mybox.user.user.domain.UserRepository;
import numble.mybox.user.user.domain.UserTokenData;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class OAuth2TokenDataFactory {

  private final UserRepository userRepository;

  public OAuth2TokenDataFactory(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public static UserTokenData getTokenData(String registrationId, OAuth2User principal) {

    if(registrationId.toUpperCase().equals(AuthProvider.GOOGLE.getProviderType())) {

      String userId = principal.getAttribute("userId");
      String email = principal.getAttribute("email");

      return UserTokenData.from(userId, email, AuthProvider.GOOGLE.getProviderType());

    }
    else if(registrationId.toUpperCase().equals(AuthProvider.KAKAO.getProviderType())) {

      Map<String, Object> acounts = (Map<String, Object>) principal.getAttributes().get("kakao_account");

      String userId = principal.getAttribute("userId");
      String email = String.valueOf(acounts.get("email"));

      return UserTokenData.from(userId, email, AuthProvider.KAKAO.getProviderType());
    }

    else if(registrationId.toUpperCase().equals(AuthProvider.NAVER.getProviderType())) {
      Map<String, Object> response = (Map<String, Object>) principal.getAttribute("response");

      String userId = principal.getAttribute("userId");
      String email = String.valueOf(response.get("email"));

      return UserTokenData.from(userId, email, AuthProvider.NAVER.getProviderType());
    }

    return null;
  }

}
