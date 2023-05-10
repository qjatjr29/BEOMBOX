package numble.mybox.storage.file.presentation;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import numble.mybox.common.presentation.CurrentUser;
import numble.mybox.storage.file.application.FileDetailResponse;
import numble.mybox.storage.file.application.FileService;
import numble.mybox.storage.file.application.FileSummaryResponse;
import numble.mybox.storage.file.domain.File;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Slf4j
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

  @GetMapping("/download/{fileId}")
  public Mono<ResponseEntity<Resource>> downloadFile(@CurrentUser String userId,
      @PathVariable String fileId,
      ServerHttpResponse response) {
    Mono<Tuple2<File, Resource>> tuple2Mono = fileService.downloadFile(userId, fileId);

    return tuple2Mono.flatMap(tuple2 -> {
          File t1 = tuple2.getT1();
          Resource t2 = tuple2.getT2();
          HttpHeaders headers = response.getHeaders();
          headers.set(HttpHeaders.CONTENT_DISPOSITION,
              "attachment;filename=" + encodeFileName(t1.getFileName()));
          headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
          headers.set(HttpHeaders.CONTENT_LENGTH, String.valueOf(t1.getFileSize()));
          return Mono.just(t2);
        })
    .map(res -> ResponseEntity.ok().headers(response.getHeaders()).body(res));
  }

  @GetMapping("/folder/{folderId}")
  public Mono<ResponseEntity<Page<FileSummaryResponse>>> getAllFile(
      @CurrentUser String userId,
      @PathVariable String folderId,
      @PageableDefault(page = 0, size = 10) Pageable pageable) {

      return fileService.findAllByUserAndFolder(userId, folderId, pageable)
          .map(filesPage -> ResponseEntity.ok().body(filesPage));
  }

  @GetMapping("/{fileId}")
  public Mono<ResponseEntity<FileDetailResponse>> getFileDetail(@CurrentUser String userId, @PathVariable String fileId) {

    return fileService.getFile(userId, fileId).map(ResponseEntity::ok);
  }

  @DeleteMapping("/{fileId}")
  public Mono<ResponseEntity<Void>> deleteFile(@CurrentUser String userId, @PathVariable String fileId) {
    return fileService.deleteFile(userId, fileId)
        .then(Mono.just(ResponseEntity.noContent().build()));
  }


  private String encodeFileName(String fileName) {
    try {
      return URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");
    } catch (Exception e) {
      return fileName;
    }
  }

}
