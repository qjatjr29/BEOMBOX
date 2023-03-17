package numble.mybox.user.user.domain;

import static java.util.Objects.isNull;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

@Document(collection = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class User implements OidcUser, Serializable {

  @Id
  protected String id;

  @Field(name = "name")
  protected String name;

  @Field(name = "email")
  protected String email;

  @Field(name = "nickname")
  protected String nickname;

  @Field(name = "profile_image")
  protected String imageUrl;

  @Field(name = "provider")
  protected String provider;

  protected Map<String, Object> attributes;

  @Field(name = "is_deleted")
  protected Boolean isDeleted;

  protected User(Map<String, Object> attributes) {
    this.attributes = attributes;
    this.provider = getAuthProviderEnum().getProviderType();
    this.isDeleted = Boolean.FALSE;
    setAttribute();
  }

  protected abstract void setAttribute();

  protected abstract AuthProvider getAuthProviderEnum();

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + getProvider()));
  }

  @Override
  public Map<String, Object> getClaims() {
    return this.getAttributes();
  }

  @Override
  public OidcUserInfo getUserInfo() {
    return null;
  }

  @Override
  public OidcIdToken getIdToken() {
    return null;
  }

  public void updateNickname(final String nickname) {
    if(!isNull(nickname)) setNickname(nickname);
  }

  public void updateImageUrl(final String imageUrl) {
    if(!isNull(imageUrl) && imageUrl.startsWith("http")) setImageUrl(imageUrl);
    else setImageUrl("");
  }

  private boolean isBlank(final String target) {
    if(target.isBlank()) return true;
    return false;
  }

  private void setNickname(final String nickname) {
    this.nickname = nickname;
  }

  private void setImageUrl(final String imageUrl) {
    this.imageUrl = imageUrl;
  }

}
