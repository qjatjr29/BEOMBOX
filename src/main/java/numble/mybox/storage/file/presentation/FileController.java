package numble.mybox.storage.file.presentation;

import numble.mybox.common.presentation.CurrentUser;
import numble.mybox.storage.file.application.FileDetailResponse;
import numble.mybox.storage.file.application.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
public class FileController {

  private final FileService fileService;

  public FileController(FileService fileService) {
    this.fileService = fileService;
  }

  @PostMapping("/{folderId}")
  public ResponseEntity<FileDetailResponse> uploadFile(@CurrentUser Long userId,
      @PathVariable Long folderId,
      @RequestParam("file") MultipartFile file) {
    FileDetailResponse response = fileService.uploadFile(userId, folderId, file);

    return ResponseEntity.ok(response);
  }

}
