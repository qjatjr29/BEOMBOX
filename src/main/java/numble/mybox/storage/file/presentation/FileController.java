package numble.mybox.storage.file.presentation;

import numble.mybox.common.presentation.CurrentUser;
import numble.mybox.storage.file.application.AwsS3Service;
import numble.mybox.storage.file.application.FileDetailResponse;
import numble.mybox.storage.file.application.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
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
  private final AwsS3Service awsS3Service;

  public FileController(FileService fileService,
      AwsS3Service awsS3Service) {
    this.fileService = fileService;
    this.awsS3Service = awsS3Service;
  }

  @PostMapping("/upload/{folderId}")
  public Mono<ResponseEntity<FileDetailResponse>> uploadFile(
      @CurrentUser String userId,
      @PathVariable String folderId,
      @RequestPart("file") FilePart file) {
    return fileService.uploadFile(userId, folderId, file).map(ResponseEntity::ok);
  }



}
