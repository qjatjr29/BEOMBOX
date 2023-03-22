package numble.mybox.user.user.domain;

import static java.util.Objects.isNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.mybox.common.domain.BaseEntity;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;


@Entity
@Table(name = "user")
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE user SET is_deleted = true WHERE id = ?")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "email")
  private String email;

  @Column(name = "profile_image")
  private String imageUrl;

  @Column(name = "provider")
  @Enumerated(EnumType.STRING)
  private AuthProvider provider;

  @Column(name = "user_role")
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private Role role = Role.USER;

  @Column(name = "is_deleted")
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
