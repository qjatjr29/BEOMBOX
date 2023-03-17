package numble.mybox.storage.folder.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.mybox.storage.folder.domain.Folder;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FolderDetailResponse {

  private String title;
  private BigDecimal totalCapacity;
  private BigDecimal usedCapacity;

  private List<String> subFolders;

  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createdAt;

  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime updatedAt;

  private FolderDetailResponse (final Folder folder) {
    this.title = folder.getTitle();
    this.totalCapacity = folder.getTotalCapacity();
    this.usedCapacity = folder.getUsedCapacity();
    this.subFolders = folder.getSubFolderIds();
    this.createdAt = folder.getCreatedAt();
    this.updatedAt = folder.getUpdatedAt();
  }

  public static FolderDetailResponse of(final Folder folder) {
    return new FolderDetailResponse(folder);
  }

}
