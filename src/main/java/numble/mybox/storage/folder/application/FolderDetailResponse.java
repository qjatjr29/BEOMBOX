package numble.mybox.storage.folder.application;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.mybox.storage.folder.domain.Folder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FolderDetailResponse {

  private String id;
  private String name;
  private Long usedSize;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  private FolderDetailResponse (final Folder folder) {
    this.id = folder.getId();
    this.name = folder.getName();
    this.usedSize = folder.getUsedSize();
    this.createdAt = folder.getCreatedAt();
    this.updatedAt = folder.getUpdatedAt();
  }

  public static FolderDetailResponse of(final Folder folder) {
    return new FolderDetailResponse(folder);
  }

}
