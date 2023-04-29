package numble.mybox.common.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
public class BaseDocument implements Serializable {

  @CreatedDate
  @Field(name = "created_at")
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Field(name = "updated_at")
  private LocalDateTime updatedAt;


}
