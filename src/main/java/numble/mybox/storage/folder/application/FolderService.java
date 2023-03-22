package numble.mybox.storage.folder.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import numble.mybox.common.error.ErrorCode;
import numble.mybox.common.error.exception.NotFoundException;
import numble.mybox.common.event.SignupCompletedEvent;
import numble.mybox.storage.folder.domain.Folder;
import numble.mybox.storage.folder.domain.FolderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
public class FolderService {

  private static final BigDecimal DEFAULT_FOLDER_SIZE = new BigDecimal("32212254720");

  private final FolderRepository folderRepository;

  public FolderService(FolderRepository folderRepository) {
    this.folderRepository = folderRepository;
  }

  @Transactional
  public FolderDetailResponse createRootFolder(SignupCompletedEvent event) {

    Folder rootFolder = Folder.builder()
        .userId(event.getUserId())
        .name(event.getName())
        .isRoot(Boolean.TRUE)
        .totalSize(DEFAULT_FOLDER_SIZE)
        .build();

    return FolderDetailResponse.of(folderRepository.save(rootFolder));
  }

  @Transactional
  public FolderDetailResponse createSubFolder(Long userId, CreateFolderRequest request) {

    Folder parentFolder = folderRepository.findByUserIdAndParentFolderId(userId,
            request.getParentFolderId())
        .orElseThrow(() -> new NotFoundException(ErrorCode.FOLDER_NOT_FOUND));

    Folder subFolder = Folder.builder()
        .name(request.getName())
        .userId(userId)
        .parentFolderId(request.getParentFolderId())
        .build();

    parentFolder.addSubFolder(subFolder);
    return FolderDetailResponse.of(subFolder);
  }

  public List<FolderSummaryResponse> findAllSubFolder(Long userId, Long folderId) {
    Folder folder = folderRepository.findByUserIdAndParentFolderId(userId, folderId)
        .orElseThrow(() -> new NotFoundException(ErrorCode.FOLDER_NOT_FOUND));

    return folder.getSubFolders()
        .stream()
        .map(FolderSummaryResponse::ofSubFolder)
        .collect(Collectors.toList());
  }

//  public Flux<FolderSummaryResponse> findAllSubFolder(String folderId) {
//
//    Flux<Folder> folderList = folderRepository.findAllByParentId(folderId);
//
//    return folderList.flatMap(FolderSummaryResponse::of);
//  }

}
