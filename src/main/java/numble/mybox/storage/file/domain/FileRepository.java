package numble.mybox.storage.file.domain;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FileRepository extends ReactiveMongoRepository<File, String> {

  Mono<Boolean> existsByFolderIdAndFileName(String folderId, String fileName);

  Flux<File> findAllByUserIdAndFolderId(String userId, String folderId);

  Mono<File> findByIdAndUserId(String fileId, String userId);
}
