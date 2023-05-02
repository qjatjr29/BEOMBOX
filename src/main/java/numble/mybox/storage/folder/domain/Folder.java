package numble.mybox.storage.folder.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.mybox.common.domain.BaseDocument;
import numble.mybox.common.error.ErrorCode;
import numble.mybox.common.error.exception.BadRequestException;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "folder")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Folder extends BaseDocument {

  @Id
  private String id;

  @Field(name = "user_id")
  private String userId;

  @Field(name = "folder_name")
  private String name;

  @Field(name = "parent_folder_id")
  private String parentFolderId;

  @Field(name = "used_size")
  @Builder.Default
  private Long usedSize = 0L;

  @Field(name = "is_root")
  @Builder.Default
  private Boolean isRoot = Boolean.FALSE;

  @Field(name = "is_deleted")
  @Builder.Default
  private Boolean isDeleted = Boolean.FALSE;

  public boolean isRoot() {
    return this.isRoot;
  }

  public void updateSize(Long size) {
    if(size < 0L) subtractUsedSize(size);
    else addUsedSize(size);
  }


  private void addUsedSize(Long size) {
    this.usedSize += size;
  }

  private void subtractUsedSize(Long size) {
    if(this.usedSize + size >= 0) this.usedSize += size;
    else throw new BadRequestException(ErrorCode.INVALID_INPUT_VALUE);
  }

}
