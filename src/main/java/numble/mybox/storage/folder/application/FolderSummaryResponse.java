package numble.mybox.storage.folder.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.mybox.storage.folder.domain.Folder;
import reactor.core.publisher.Mono;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FolderSummaryResponse {

  private String id;
  private String title;
  private BigDecimal usedCapacity;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  private FolderSummaryResponse (Folder folder) {
    this.id = folder.getId();
    this.title = folder.getTitle();
    this.usedCapacity = folder.getUsedCapacity();
    this.createdAt = folder.getCreatedAt();
    this.updatedAt = folder.getUpdatedAt();
  }

  public static Mono<FolderSummaryResponse> of(Folder folder) {
    return Mono.just(new FolderSummaryResponse(folder));
  }

}
