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

  public UserTokenData(String id, String email, String provider) {
    this.id = id;
    this.email = email;
    this.provider = provider;
  }

  public static UserTokenData of(final User user) {
    return new UserTokenData(user.getId(), user.getEmail(), user.getProvider());
  }

  public static UserTokenData from(final String id, final String email, final String provider) {
    return new UserTokenData(id, email, provider);
  }

}
