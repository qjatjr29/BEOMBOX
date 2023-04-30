package numble.mybox.storage.file.application;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.mybox.storage.file.domain.File;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FileSummaryResponse {

  private String fileId;
  private String fileName;
  private Long fileSize;
  private String fileUrl;

  private FileSummaryResponse(final File file) {
    this.fileId = file.getId();
    this.fileName = file.getFileName();
    this.fileUrl = file.getFileUrl();
    this.fileSize = file.getFileSize();
  }

  public static FileSummaryResponse of(final File file) {
    return new FileSummaryResponse(file);
  }
}
