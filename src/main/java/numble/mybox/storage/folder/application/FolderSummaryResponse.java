package numble.mybox.storage.folder.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.mybox.storage.folder.domain.Folder;
import numble.mybox.storage.folder.domain.SubFolder;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FolderSummaryResponse {

  private Long id;
  private String name;
  private BigDecimal usedSize;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  private FolderSummaryResponse (SubFolder folder) {
    this.id = folder.getSubFolderId();
    this.name = folder.getSubFolderName();
    this.usedSize = folder.getSubFolderSize();
    this.createdAt = folder.getCreatedAt();
    this.updatedAt = folder.getUpdatedAt();
  }

  public static FolderSummaryResponse ofSubFolder(SubFolder subFolder) {
    return new FolderSummaryResponse(subFolder);
  }

}
