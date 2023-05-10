package numble.mybox.storage.file.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.mybox.common.domain.BaseDocument;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "file")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class File extends BaseDocument {

  @Id
  private String id;

  @Field(name = "file_name")
  private String fileName;

  @Field(name = "file_size")
  private Long fileSize;

  @Field(name = "file_url")
  private String fileUrl;

  @Field(name = "user_id")
  private String userId;

  @Field(name = "folder_id")
  private String folderId;

  @Field(name = "is_deleted")
  @Builder.Default
  private Boolean isDeleted = Boolean.FALSE;

  // TODO: 중요표시

  public void delete() {
    this.isDeleted = true;
  }

}
