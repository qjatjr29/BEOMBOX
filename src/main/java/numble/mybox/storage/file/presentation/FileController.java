package numble.mybox.storage.file.presentation;

import numble.mybox.common.presentation.CurrentUser;
import numble.mybox.storage.file.application.FileDetailResponse;
import numble.mybox.storage.file.application.FileService;
import numble.mybox.storage.file.application.FileSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/files")
public class FileController {

  private final FileService fileService;

  public FileController(FileService fileService) {
    this.fileService = fileService;
  }

  @PostMapping("/upload/{folderId}")
  public Mono<ResponseEntity<FileDetailResponse>> uploadFile(
      @CurrentUser String userId,
      @PathVariable String folderId,
      @RequestPart("file") FilePart file) {
    return fileService.uploadFile(userId, folderId, file).map(ResponseEntity::ok);
  }

  @GetMapping("/folder/{folderId}")
  public Mono<ResponseEntity<Page<FileSummaryResponse>>> getAllFile(
      @CurrentUser String userId,
      @PathVariable String folderId,
      @PageableDefault(page = 0, size = 10) Pageable pageable) {

      return fileService.findAll(userId, folderId, pageable)
          .map(filesPage -> ResponseEntity.ok().body(filesPage));
  }

  @GetMapping("/{fileId}")
  public Mono<ResponseEntity<FileDetailResponse>> getFileDetail(@CurrentUser String userId, @PathVariable String fileId) {

    return fileService.getFile(userId, fileId).map(ResponseEntity::ok);
  }


}
