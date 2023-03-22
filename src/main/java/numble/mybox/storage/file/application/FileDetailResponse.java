package numble.mybox.storage.file.application;

import java.math.BigDecimal;
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

  private String fileName;
  private BigDecimal fileSize;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  private FileDetailResponse(File file) {
    this.fileName = file.getFileName();
    this.fileSize = file.getFileSize();
    this.createdAt = file.getCreatedAt();
    this.updatedAt = file.getUpdatedAt();
  }

  public static FileDetailResponse of(File file) {
    return new FileDetailResponse(file);
  }

}
