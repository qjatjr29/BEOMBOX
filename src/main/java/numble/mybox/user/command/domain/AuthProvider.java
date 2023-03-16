package numble.mybox.user.command.domain;

import lombok.Getter;

public enum AuthProvider {

  GOOGLE("GOOGLE"),
  KAKAO("KAKAO"),
  NAVER("NAVER");

  String providerType;

  public String getProviderType() {
    return providerType;
  }

  AuthProvider(final String providerType) {
    this.providerType = providerType;
  }
}
