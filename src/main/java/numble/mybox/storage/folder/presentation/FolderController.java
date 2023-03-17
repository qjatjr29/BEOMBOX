package numble.mybox.storage.folder.presentation;

import numble.mybox.storage.folder.application.CreateFolderRequest;
import numble.mybox.storage.folder.application.FolderDetailResponse;
import numble.mybox.storage.folder.application.FolderService;
import numble.mybox.common.presentation.CurrentUser;
import numble.mybox.storage.folder.application.FolderSummaryResponse;
import numble.mybox.user.user.domain.UserTokenData;
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
  public Mono<ResponseEntity<FolderDetailResponse>> createFolder (@CurrentUser UserTokenData userTokenData,
      @RequestBody CreateFolderRequest request) {

    System.out.println("folder controller");
    Mono<FolderDetailResponse> response = folderService.createFolder(userTokenData, request);

    return response.map(ResponseEntity::ok);
  }

  @GetMapping("/{folderId}/all")
  public Flux<ResponseEntity<FolderSummaryResponse>> findChildrenFolder(@CurrentUser UserTokenData userTokenData, @PathVariable String folderId) {
    Flux<FolderSummaryResponse> response = folderService.findAllSubFolder(folderId);

    return response.map(ResponseEntity::ok);
  }


}
