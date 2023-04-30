package numble.mybox.storage.folder.domain;


import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FolderRepository extends ReactiveMongoRepository<Folder, String> {

  @Query("{'userId' : ?0, 'isRoot' : true}")
  Mono<Folder> findByUserIdAndIsRoot(String userId);

  Mono<Folder> findByIdAndUserId(String id, String userId);

  Flux<Folder> findAllByParentFolderId(String parentFolderId);

}
