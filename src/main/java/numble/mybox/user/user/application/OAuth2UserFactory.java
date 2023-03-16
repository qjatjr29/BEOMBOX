package numble.mybox.user.user.application;

import java.util.Map;
import numble.mybox.common.error.ErrorCode;
import numble.mybox.common.error.exception.BadRequestException;
import numble.mybox.user.user.domain.AuthProvider;
import numble.mybox.user.user.domain.GoogleOAuth2User;
import numble.mybox.user.user.domain.KakaoOAuth2User;
import numble.mybox.user.user.domain.NaverOAuth2User;
import numble.mybox.user.user.domain.User;

public class OAuth2UserFactory {

  public static User getOAuth2User(String registrationId, Map<String, Object> attributes) {
    if (registrationId.equalsIgnoreCase(AuthProvider.GOOGLE.getProviderType())) {
      return new GoogleOAuth2User(attributes);
    }
    else if (registrationId.equalsIgnoreCase(AuthProvider.KAKAO.getProviderType())) {
      return new KakaoOAuth2User(attributes);
    }
    else if (registrationId.equalsIgnoreCase(AuthProvider.NAVER.getProviderType())) {
      return new NaverOAuth2User(attributes);
    }
    else {
      throw new BadRequestException(ErrorCode.PROVIDER_NOT_SUPPORTED);
    }
  }

}
