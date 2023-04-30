package numble.mybox.user.user.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserTokenData {

  private String id;
  private String email;
  private String provider;
  private String role;

  private UserTokenData(String id, String email, String provider, String role) {
    this.id = id;
    this.email = email;
    this.provider = provider;
    this.role = role;
  }

  public static UserTokenData of(final User user) {
    return new UserTokenData(user.getId(), user.getEmail(), user.getProvider().getProviderType(), user.getRole().getRole());
  }

  public static UserTokenData from(final String id, final String email, final String provider, final String role) {
    return new UserTokenData(id, email, provider, role);
  }

}
