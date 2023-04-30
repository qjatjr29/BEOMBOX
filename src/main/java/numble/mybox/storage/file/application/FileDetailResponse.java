package numble.mybox.storage.file.application;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.mybox.storage.file.domain.File;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FileDetailResponse {

  private String fileId;
  private String fileName;
  private Long fileSize;
  private String folderId;
  private String userId;
  private String fileUrl;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  private FileDetailResponse(File file) {
    this.fileId = file.getId();
    this.fileName = file.getFileName();
    this.fileSize = file.getFileSize();
    this.folderId = file.getFolderId();
    this.userId = file.getUserId();
    this.fileUrl = file.getFileUrl();
    this.createdAt = file.getCreatedAt();
    this.updatedAt = file.getUpdatedAt();
  }

  public static FileDetailResponse of(File file) {
    return new FileDetailResponse(file);
  }

}
