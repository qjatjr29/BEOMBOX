package numble.mybox.user.user.domain;

import java.util.Map;

public class NaverOAuth2User extends User {

  public NaverOAuth2User(Map<String, Object> attributes) {
    super(attributes);
  }

  @Override
  protected void setAttribute() {
    Map<String, Object> response = (Map<String, Object>) attributes.get("response");

    this.name = String.valueOf(response.get("id"));
    this.nickname = String.valueOf(response.get("name"));
    this.email = String.valueOf(response.get("email"));
    this.imageUrl = String.valueOf(response.get("profile_image"));
  }

  @Override
  protected AuthProvider getAuthProviderEnum() {
    return AuthProvider.NAVER;
  }


}
