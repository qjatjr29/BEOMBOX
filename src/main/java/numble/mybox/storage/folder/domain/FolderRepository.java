package numble.mybox.storage.folder.domain;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FolderRepository extends ReactiveMongoRepository<Folder, String> {

  Mono<Folder> findByParentIdAndName(String parentId, String name);
  Flux<Folder> findAllByParentId(String parentId);
}
