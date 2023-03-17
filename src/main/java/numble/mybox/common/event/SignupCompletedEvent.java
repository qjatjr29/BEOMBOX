package numble.mybox.common.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignupCompletedEvent extends Event {

  private String userId;
  private String name;

  public SignupCompletedEvent(final String userId, final String userNickname) {
    super();
    this.userId = userId;
    this.name = userNickname + "'s BOX";
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("SignupCompletedEvent{");
    sb.append("userId='").append(userId).append('\'');
    sb.append(", name='").append(name).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
