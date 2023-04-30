package numble.mybox.storage.folder.presentation;

import numble.mybox.storage.folder.application.CreateFolderRequest;
import numble.mybox.storage.folder.application.FolderDetailResponse;
import numble.mybox.storage.folder.application.FolderService;
import numble.mybox.common.presentation.CurrentUser;
import numble.mybox.storage.folder.application.FolderSummaryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/folders")
public class FolderController {

  private final FolderService folderService;

  public FolderController(FolderService folderService) {
    this.folderService = folderService;
  }

  @PostMapping()
  public Mono<ResponseEntity<FolderDetailResponse>> createSubFolder (@CurrentUser String userId,
      @RequestBody CreateFolderRequest request) {

    Mono<FolderDetailResponse> response = folderService.createSubFolder(userId, request);

    return response.map(ResponseEntity::ok);
  }

  @GetMapping("/subfolder/{folderId}")
  public Flux<ResponseEntity<FolderSummaryResponse>> findAllSubFolder(@CurrentUser String userId, @PathVariable String folderId) {
    Flux<FolderSummaryResponse> response = folderService.findAllSubFolder(userId, folderId);

    return response.map(ResponseEntity::ok);
  }

  @GetMapping("/{folderId}")
  public Mono<ResponseEntity<FolderDetailResponse>> getFolder(@CurrentUser String userId, @PathVariable String folderId) {
    Mono<FolderDetailResponse> response = folderService.getFolder(userId, folderId);

    return response.map(ResponseEntity::ok);
  }

  @GetMapping("/root")
  public Mono<ResponseEntity<FolderDetailResponse>> getRootFolder(@CurrentUser String userId) {
    Mono<FolderDetailResponse> response = folderService.getRootFolder(userId);
    return response.map(ResponseEntity::ok);
  }


}
