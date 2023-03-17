package numble.mybox.user.user.application;

import numble.mybox.common.event.SignupCompletedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class SignupCompletedEventHandler {

  @Value("${spring.kafka.topic.signup-completed}")
  private String SIGNUP_COMPLETE_TOPIC;

  private final KafkaTemplate<String, SignupCompletedEvent> transferKafkaTemplate;

  public SignupCompletedEventHandler(
      KafkaTemplate<String, SignupCompletedEvent> transferKafkaTemplate) {
    this.transferKafkaTemplate = transferKafkaTemplate;
  }

  @EventListener(SignupCompletedEvent.class)
  public void handle(SignupCompletedEvent event) {
    transferKafkaTemplate.send(SIGNUP_COMPLETE_TOPIC, event);
  }

}
