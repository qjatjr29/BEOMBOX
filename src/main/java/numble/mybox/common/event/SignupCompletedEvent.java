package numble.mybox.common.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignupCompletedEvent extends Event {

  private Long userId;
  private String name;

  public SignupCompletedEvent(final Long userId, final String name) {
    super();
    this.userId = userId;
    this.name = name + "'s BOX";
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("SignupCompletedEvent{");
    sb.append("userId=").append(userId);
    sb.append(", name='").append(name).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
