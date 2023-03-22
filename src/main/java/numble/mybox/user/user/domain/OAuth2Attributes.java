package numble.mybox.user.user.domain;

import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public abstract class OAuth2Attributes {

  protected Map<String, Object> attributes;
  protected String nameAttributeKey;
  protected String name;
  protected String email;
  protected String imageUrl;
  protected AuthProvider provider;

  protected OAuth2Attributes(Map<String, Object> attributes) {
    this.attributes = attributes;
    this.provider = getAuthProviderEnum();
    setAttribute();
  }

  public User toUser() {
    return User.builder()
        .name(this.getName())
        .email(this.getEmail())
        .provider(this.getProvider())
        .imageUrl(this.getImageUrl())
        .build();
  }

  protected abstract void setAttribute();
  protected abstract AuthProvider getAuthProviderEnum();

}
