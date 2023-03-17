package numble.mybox.user.user.application;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateRequest {

  private String nickname;
  private String imageUrl;

  public UpdateRequest(String nickname, String imageUrl) {
    this.nickname = nickname;
    this.imageUrl = imageUrl;
  }
}
