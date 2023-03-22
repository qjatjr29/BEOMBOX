package numble.mybox.user.user.domain;

import lombok.Getter;

@Getter
public enum Role {

  USER("ROLE_USER"),
  ADMIN("ROLE_ADMIN");

  private final String role;

  Role(String role) {
    this.role = role;
  }
}
