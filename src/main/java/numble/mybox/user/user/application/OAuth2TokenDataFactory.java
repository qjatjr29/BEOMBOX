package numble.mybox.user.user.application;

import java.util.Map;
import numble.mybox.common.error.ErrorCode;
import numble.mybox.common.error.exception.BadRequestException;
import numble.mybox.user.user.domain.AuthProvider;
import numble.mybox.user.user.domain.Role;
import numble.mybox.user.user.domain.UserTokenData;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class OAuth2TokenDataFactory {

  public static UserTokenData getTokenData(String registrationId, OAuth2User principal) {

    if(registrationId.toUpperCase().equals(AuthProvider.GOOGLE.getProviderType())) {

      Long userId = principal.getAttribute("userId");
      String email = principal.getAttribute("email");

      return UserTokenData.from(userId, email, AuthProvider.GOOGLE.getProviderType(), Role.USER.getRole());

    }
    else if(registrationId.toUpperCase().equals(AuthProvider.KAKAO.getProviderType())) {

      Map<String, Object> acounts = (Map<String, Object>) principal.getAttributes().get("kakao_account");

      Long userId = principal.getAttribute("userId");
      String email = String.valueOf(acounts.get("email"));

      return UserTokenData.from(userId, email, AuthProvider.KAKAO.getProviderType(), Role.USER.getRole());
    }

    else if(registrationId.toUpperCase().equals(AuthProvider.NAVER.getProviderType())) {
      Map<String, Object> response = (Map<String, Object>) principal.getAttribute("response");

      Long userId = principal.getAttribute("userId");
      String email = String.valueOf(response.get("email"));

      return UserTokenData.from(userId, email, AuthProvider.NAVER.getProviderType(), Role.USER.getRole());
    }

    throw new BadRequestException(ErrorCode.PROVIDER_NOT_SUPPORTED);
  }

}
