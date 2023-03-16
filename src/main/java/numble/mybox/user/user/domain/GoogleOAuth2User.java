package numble.mybox.user.user.domain;

import java.util.Map;

public class GoogleOAuth2User extends User {

  public GoogleOAuth2User(Map<String, Object> attributes) {
    super(attributes);
  }

  @Override
  protected void setAttribute() {
    this.name = String.valueOf(attributes.get("sub"));
    this.nickname = String.valueOf(attributes.get("name"));
    this.email = String.valueOf(attributes.get("email"));
    this.imageUrl = String.valueOf(attributes.get("picture"));
  }

  @Override
  protected AuthProvider getAuthProviderEnum() {
    return AuthProvider.GOOGLE;
  }


}
