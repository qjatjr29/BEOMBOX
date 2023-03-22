package numble.mybox.storage.folder.presentation;

import java.util.List;
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

@RestController
@RequestMapping("/folders")
public class FolderController {

  private final FolderService folderService;

  public FolderController(FolderService folderService) {
    this.folderService = folderService;
  }

  @PostMapping()
  public ResponseEntity<FolderDetailResponse> createSubFolder (@CurrentUser Long userId,
      @RequestBody CreateFolderRequest request) {

    FolderDetailResponse response = folderService.createSubFolder(userId, request);

    return ResponseEntity.ok(response);
  }

  @GetMapping("/{folderId}")
  public ResponseEntity<List<FolderSummaryResponse>> findAllSubFolder(@CurrentUser Long userId, @PathVariable Long folderId) {
    List<FolderSummaryResponse> response = folderService.findAllSubFolder(userId, folderId);

    return ResponseEntity.ok(response);
  }


}
