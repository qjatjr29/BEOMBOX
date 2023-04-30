package numble.mybox.user.user.domain;

import static java.util.Objects.isNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.mybox.common.domain.BaseDocument;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "user")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseDocument {

  @Id
  private String id;

  @Field(name = "name")
  private String name;

  @Field(name = "email")
  private String email;

  @Field(name = "profile_image")
  private String imageUrl;

  @Field(name = "storage_size")
  @Builder.Default
  private Long storageSize = 1024 * 1024 * 1024 * 30L;

  @Field(name = "provider")
  private AuthProvider provider;

  @Field(name = "user_role")
  @Builder.Default
  private Role role = Role.USER;

  @Field(name = "is_deleted")
  @Builder.Default
  private Boolean isDeleted = Boolean.FALSE;

  public User updateInfo(final String name, final String imageUrl) {
    setName(name);
    setImageUrl(imageUrl);
    return this;
  }

  public String getUserRole() {
    return this.role.getRole();
  }

  private void setName(final String name) {
    if(!isNull(name)) this.name = name;
  }

  private void setImageUrl(final String imageUrl) {
    if(!imageUrl.isBlank()) this.imageUrl = imageUrl;
  }

}
