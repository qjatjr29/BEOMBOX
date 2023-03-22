package numble.mybox.storage.folder.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubFolder {

  @Column(name = "sub_folder_id")
  private Long subFolderId;

  @Column(name = "sub_folder_name")
  private String subFolderName;

  @Column(name = "sub_folder_size")
  private BigDecimal subFolderSize;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  private SubFolder(final Folder subFolder) {
    this.subFolderId = subFolder.getId();
    this.subFolderName = subFolder.getName();
    this.subFolderSize = subFolder.getUsedSize();
    this.createdAt = subFolder.getCreatedAt();
    this.updatedAt = subFolder.getUpdatedAt();
  }

  public static SubFolder of(final Folder subFolder) {
    return new SubFolder(subFolder);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SubFolder subFolder = (SubFolder) o;
    return Objects.equals(subFolderId, subFolder.subFolderId) && Objects.equals(
        subFolderName, subFolder.subFolderName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(subFolderId, subFolderName);
  }
}
