package numble.mybox.storage.folder.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import numble.mybox.common.event.SignupCompletedEvent;
import numble.mybox.storage.folder.application.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SignupCompletedEventListener {

  private final FolderService folderService;

  @Autowired
  private ObjectMapper objectMapper;

  public SignupCompletedEventListener(
      FolderService folderService) {
    this.folderService = folderService;
  }

  @KafkaListener(topics = "${spring.kafka.topic.signup-completed}")
  public void consume(String message) {

    try {
      SignupCompletedEvent event = objectMapper.readValue(message, SignupCompletedEvent.class);
      folderService.createRootFolder(event).subscribe();
    } catch (IOException e) {
      log.error("SignupCompletedEvent parsing error!");
      throw new RuntimeException();
    }
  }

}
