package numble.mybox.config;

import java.util.HashMap;
import java.util.Map;
import numble.mybox.common.event.SignupCompletedEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaProducerConfig {

  @Value("${spring.kafka.producer.bootstrap-servers}")
  private String bootstrapServers;

  @Value("${spring.kafka.topic.signup-completed}")
  private String SIGNUP_COMPLETE_TOPIC;

  @Bean
  public ProducerFactory<String, SignupCompletedEvent> signupCompletedEventProducerFactory() {
    Map<String, Object> configProps = new HashMap<>();
    configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    return new DefaultKafkaProducerFactory<>(configProps);
  }

  @Bean
  public KafkaTemplate<String, SignupCompletedEvent> signupKafkaTemplate() {
    return new KafkaTemplate<>(signupCompletedEventProducerFactory());
  }

  @Bean
  public NewTopic signupCompleteTopic() {
    return new NewTopic(SIGNUP_COMPLETE_TOPIC, 1, (short) 1);
  }



}
