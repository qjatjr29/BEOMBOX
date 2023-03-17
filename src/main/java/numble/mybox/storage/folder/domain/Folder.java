package numble.mybox.storage.folder.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;
import reactor.core.publisher.Mono;

@Document(collection = "folder")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Folder implements Serializable {

  @Id
  private String id;

  @Field(name = "userId")
  private String userId;

  @Field(name = "folder_title")
  private String title;

  @Field(name = "parent_folder_id")
  private String parentId;

  @Field(name = "sub_folder_ids")
  @Builder.Default
  private List<String> subFolderIds = new ArrayList<>();

  @Field(name = "total_capacity")
  @Builder.Default
  private BigDecimal totalCapacity = BigDecimal.ZERO;

  @Field(name = "used_capacity")
  @Builder.Default
  private BigDecimal usedCapacity = BigDecimal.ZERO;

  @Field(name = "created_at")
  @CreatedDate
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @Builder.Default
  private LocalDateTime createdAt = LocalDateTime.now();

  @Field(name = "updated_at")
  @LastModifiedDate
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime updatedAt;

  @Field(name = "is_root")
  @Builder.Default
  private Boolean isRoot = Boolean.FALSE;

  @Field(name = "is_deleted")
  @Builder.Default
  private Boolean isDeleted = Boolean.FALSE;

  public void addSubFolder(Mono<Folder> save) {
    save.subscribe(folder -> {
      subFolderIds.add(folder.getId());
      totalCapacity = totalCapacity.add(folder.getTotalCapacity());
    });
  }

  // Todo : 파일 추가시 폴더 용량 고려 및 증가

}
