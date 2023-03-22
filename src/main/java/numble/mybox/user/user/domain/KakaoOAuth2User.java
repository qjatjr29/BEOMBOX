package numble.mybox.user.user.domain;

import java.util.Map;

public class KakaoOAuth2User extends OAuth2Attributes {

  public KakaoOAuth2User(Map<String, Object> attributes) {
    super(attributes);
  }

  @Override
  protected void setAttribute() {
    Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
    Map<String, Object> acounts = (Map<String, Object>) attributes.get("kakao_account");

    this.nameAttributeKey = String.valueOf(attributes.get("id"));
    this.name = String.valueOf(properties.get("nickname"));
    this.email = String.valueOf(acounts.get("email"));
    this.imageUrl = String.valueOf(properties.get("profile_image"));
  }

  @Override
  protected AuthProvider getAuthProviderEnum() {
    return AuthProvider.KAKAO;
  }


}
