package numble.mybox.user.user.application;

import java.util.Map;
import numble.mybox.user.user.domain.AuthProvider;
import numble.mybox.user.user.domain.UserTokenData;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class OAuth2TokenDataFactory {

  public static UserTokenData getTokenData(String registrationId, OAuth2User principal) {

    if(registrationId.toUpperCase().equals(AuthProvider.GOOGLE.getProviderType())) {

      String id = principal.getAttribute("sub");
      String email = principal.getAttribute("email");

      return UserTokenData.from(id, email, AuthProvider.GOOGLE.getProviderType());

    }
    else if(registrationId.toUpperCase().equals(AuthProvider.KAKAO.getProviderType())) {

      Map<String, Object> acounts = (Map<String, Object>) principal.getAttributes().get("kakao_account");

      String id = String.valueOf(principal.getAttributes().get("id"));
      String email = String.valueOf(acounts.get("email"));

      return UserTokenData.from(id, email, AuthProvider.KAKAO.getProviderType());
    }

    else if(registrationId.toUpperCase().equals(AuthProvider.NAVER.getProviderType())) {
      Map<String, Object> response = (Map<String, Object>) principal.getAttribute("response");

      String id = String.valueOf(response.get("id"));
      String email = String.valueOf(response.get("email"));

      return UserTokenData.from(id, email, AuthProvider.NAVER.getProviderType());
    }

    return null;
  }

}
