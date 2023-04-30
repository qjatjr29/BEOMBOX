package numble.mybox.user.user.application;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.mybox.user.user.domain.User;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserDetailResponse {

  private String id;
  private String name;
  private String email;
  private String imageUrl;
  private String provider;

  private UserDetailResponse(User user) {
    this.id = user.getId();
    this.name = user.getName();
    this.email = user.getEmail();
    this.imageUrl = user.getImageUrl();
    this.provider = user.getProvider().getProviderType();
  }

  public static UserDetailResponse of(User user) {
    return new UserDetailResponse(user);
  }
}
