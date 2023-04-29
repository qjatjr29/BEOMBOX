package numble.mybox.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("aws")
@Getter
@Setter
public class AwsProperties {

  private String accessKey;
  private String secretKey;
  private String region;
  private String s3BucketName;
  private int multipartMinPartSize;
  private String endpoint;

}
