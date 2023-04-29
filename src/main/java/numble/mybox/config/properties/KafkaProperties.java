package numble.mybox.config.properties;

import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties("spring.kafka")
@Validated
@Getter
@Setter
public class KafkaProperties {

  private Producer producer;
  private Consumer consumer;
  private Topic topic;

  @Getter
  @Setter
  @RequiredArgsConstructor
  public static class Producer {
    private String bootstrapServers;
    private String keySerializer;
    private String valueSerializer;
  }

  @Getter
  @Setter
  @RequiredArgsConstructor
  public static class Consumer {
    private String bootstrapServers;
    private String keySerializer;
    private String valueSerializer;
    private String groupId;
  }

  @Getter
  @Setter
  @RequiredArgsConstructor
  public static class Topic {
    @NotEmpty
    private String signupCompleted;
  }

}
