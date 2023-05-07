package numble.mybox.storage.folder.application;

import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import numble.mybox.common.error.ErrorCode;
import numble.mybox.common.error.exception.NotFoundException;
import numble.mybox.common.event.SignupCompletedEvent;
import numble.mybox.storage.folder.domain.Folder;
import numble.mybox.storage.folder.domain.FolderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Slf4j
@Service
@Transactional(readOnly = true)
public class FolderService {

  private final FolderRepository folderRepository;

  public FolderService(FolderRepository folderRepository) {
    this.folderRepository = folderRepository;
  }

  @Transactional
  public Mono<FolderDetailResponse> createRootFolder(SignupCompletedEvent event) {

    return folderRepository.save(Folder.builder()
            .userId(event.getUserId())
            .name(event.getName())
            .isRoot(Boolean.TRUE)
            .build())
        .flatMap(savedFolder -> Mono.just(FolderDetailResponse.of(savedFolder)));
  }

  @Transactional
  public Mono<FolderDetailResponse> createSubFolder(String userId, CreateFolderRequest request) {

    return findFolderByIdAndUserId(request.getParentFolderId(), userId)
        .flatMap(parentFolder -> buildFolderName(parentFolder.getId(), request.getName())
              .flatMap(folderName -> {
                Folder subFolder = Folder.builder()
                    .name(folderName)
                    .userId(userId)
                    .parentFolderId(parentFolder.getId())
                    .build();
                return folderRepository.save(subFolder)
                    .flatMap(savedSubFolder -> {
                      parentFolder.updateSize(savedSubFolder.getUsedSize());
                      return folderRepository.save(parentFolder)
                          .then(Mono.just(savedSubFolder));
                    });
        })).map(FolderDetailResponse::of);
  }

  public Flux<FolderSummaryResponse> findAllSubFolder(String userId, String folderId) {
    return folderRepository.findByIdAndUserId(folderId, userId)
        .switchIfEmpty(Mono.error(new NotFoundException(ErrorCode.FOLDER_NOT_FOUND)))
        .flatMapMany(parentFolder -> folderRepository.findAllByParentFolderId(parentFolder.getId())
            .map(FolderSummaryResponse::ofFolder));
  }

  public Mono<FolderDetailResponse> getRootFolder(String userId) {
    Mono<Folder> folderMono = folderRepository.findByUserIdAndIsRoot(userId)
        .switchIfEmpty(Mono.error(new NotFoundException(ErrorCode.FOLDER_NOT_FOUND)));

    return folderMono.map(FolderDetailResponse::of);
  }

  public Mono<FolderDetailResponse> getFolder(String userId, String folderId) {
    return findFolderByIdAndUserId(folderId, userId).map(FolderDetailResponse::of);
  }

  private Mono<String> buildFolderName(String parentFolderId, String folderName) {
    return folderRepository.findById(parentFolderId)
        .switchIfEmpty(Mono.error(new NotFoundException(ErrorCode.FOLDER_NOT_FOUND)))
        .flatMap(parentFolder -> {

          return folderRepository.findAllByParentFolderId(parentFolderId)
              .collectList()
              .flatMap(subFolders -> {
                AtomicInteger counter = new AtomicInteger(1);
                StringBuilder newFolderNameBuilder = new StringBuilder();
                newFolderNameBuilder.append(folderName);

                while (subFolders.stream()
                    .anyMatch(folder -> folder.getName().equals(newFolderNameBuilder.toString()))) {

                  newFolderNameBuilder.setLength(0);
                  newFolderNameBuilder.append(folderName);
                  newFolderNameBuilder.append("(");
                  newFolderNameBuilder.append(counter.getAndIncrement());
                  newFolderNameBuilder.append(")");
                }

                return Mono.just(newFolderNameBuilder.toString());
              });
        });
  }

  private Mono<Folder> findFolderByIdAndUserId(String folderId, String userId) {
    return folderRepository.findByIdAndUserId(folderId, userId)
        .switchIfEmpty(Mono.error(new NotFoundException(ErrorCode.FOLDER_NOT_FOUND)));
  }
}
