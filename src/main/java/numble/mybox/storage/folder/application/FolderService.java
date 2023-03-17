package numble.mybox.storage.folder.application;

import static java.util.Objects.isNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import numble.mybox.common.error.ErrorCode;
import numble.mybox.common.error.exception.ConflictException;
import numble.mybox.common.event.SignupCompletedEvent;
import numble.mybox.storage.folder.domain.Folder;
import numble.mybox.storage.folder.domain.FolderRepository;
import numble.mybox.user.user.domain.UserTokenData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Transactional(readOnly = true)
public class FolderService {

  private static final BigDecimal DEFAULT_FOLDER_SIZE = new BigDecimal("32212254720");

  private final FolderRepository folderRepository;

  public FolderService(FolderRepository folderRepository) {
    this.folderRepository = folderRepository;
  }

  @Transactional
  public Mono<FolderDetailResponse> createFolder(UserTokenData user, CreateFolderRequest request) {

    return folderRepository.findByParentIdAndName(request.getParentFolderId(), request.getName())
        .flatMap(existedFolder -> {
          if (!isNull(existedFolder)) {
            throw new ConflictException(ErrorCode.DUPLICATE_FOLDER);
          } else {
            Folder folder = Folder.builder()
                .userId(user.getId())
                .parentId(request.getParentFolderId())
                .name(request.getName())
                .updatedAt(LocalDateTime.now())
                .build();
            Mono<Folder> save = folderRepository.save(folder);
            existedFolder.addSubFolder(save);
            return save.flatMap(f -> Mono.just(FolderDetailResponse.of(f)));
          }
        });
  }

  public Flux<FolderSummaryResponse> findAllSubFolder(String folderId) {

    Flux<Folder> folderList = folderRepository.findAllByParentId(folderId);

    return folderList.flatMap(FolderSummaryResponse::of);
  }

  @Transactional
  public Mono<Folder> createRootFolder(SignupCompletedEvent event) {

    Folder rootFolder = Folder.builder()
        .userId(event.getUserId())
        .name(event.getName())
        .isRoot(Boolean.TRUE)
        .totalSize(DEFAULT_FOLDER_SIZE)
        .updatedAt(LocalDateTime.now())
        .build();

    return folderRepository.save(rootFolder);
  }
}
