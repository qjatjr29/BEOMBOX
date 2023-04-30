package numble.mybox.config.properties;

import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties("spring.mongodb")
@Validated
@Getter
@Setter
public class MongoProperties {

  @NotEmpty
  private String uri;
  @NotEmpty
  private String database;
  @NotEmpty
  private String username;
  @NotEmpty
  private String password;

}
