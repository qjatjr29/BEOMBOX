package numble.mybox.storage.file.application;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import numble.mybox.common.error.ErrorCode;
import numble.mybox.common.error.exception.ForbiddenException;
import numble.mybox.common.error.exception.NotFoundException;
import numble.mybox.storage.file.domain.File;
import numble.mybox.storage.file.domain.FileRepository;
import numble.mybox.storage.folder.domain.Folder;
import numble.mybox.storage.folder.domain.FolderRepository;
import numble.mybox.user.user.domain.UserRepository;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Slf4j
@Service
@Transactional(readOnly = true)
public class FileService {

  private final FileRepository fileRepository;
  private final FolderRepository folderRepository;
  private final UserRepository userRepository;
  private final AwsS3Service awsS3Service;

  public FileService(FileRepository fileRepository,
      FolderRepository folderRepository,
      UserRepository userRepository, AwsS3Service s3Service) {
    this.fileRepository = fileRepository;
    this.folderRepository = folderRepository;
    this.userRepository = userRepository;
    this.awsS3Service = s3Service;
  }

  @Transactional
  public Mono<FileDetailResponse> uploadFile(String userId, String folderId, FilePart file) {

    return folderRepository.findById(folderId)
        .switchIfEmpty(Mono.error(new NotFoundException(ErrorCode.FOLDER_NOT_FOUND)))
        .filter(folder -> Objects.equals(folder.getUserId(), userId))
        .switchIfEmpty(Mono.error(new ForbiddenException(ErrorCode.INVALID_VERIFICATION_FILE_ACCESS)))
        // get file size
        .flatMap(folder -> file.content()
            .map(DataBuffer::readableByteCount).reduce(0L, Long::sum)
        // check storage size
        .flatMap(size -> folderRepository.findByUserIdAndIsRoot(userId)
              .flatMap(rootFolder ->
                userRepository.findById(userId)
                    .flatMap(user -> Mono.just(user.getStorageSize()))
                    .filter(storageMaxSize -> rootFolder.getUsedSize() + size <= storageMaxSize)
                    .switchIfEmpty(Mono.error(new ForbiddenException(ErrorCode.EXCEED_MAX_STORAGE_SIZE)))
                    .thenReturn(size)
              ))
            .flatMap(size -> buildFileName(folderId, file.filename())
                // upload file to s3
                .flatMap(filename -> awsS3Service.upload(file, userId, filename)
                    // save file
                    .flatMap(url -> saveFileToDatabase(userId, folderId, filename, size, url))
                )));
  }

  private Mono<FileDetailResponse> saveFileToDatabase(String userId, String folderId, String filename, Long size, String url) {

    File savedFile = File.builder()
        .userId(userId)
        .fileName(filename)
        .folderId(folderId)
        .fileSize(size)
        .fileUrl(url)
        .build();

    return fileRepository.save(savedFile)
        .flatMap(f -> addFile(folderId, f)
            .thenReturn(FileDetailResponse.of(f)));
  }

  public Mono<Page<FileSummaryResponse>> findAll(String userId, String folderId, Pageable pageable) {

    return fileRepository.findAllByUserIdAndFolderId(userId, folderId)
        .map(FileSummaryResponse::of)
        .collectList()
        .map(list -> {
          int total = list.size();
          int start = Math.toIntExact(pageable.getOffset());
          int end = Math.min((start + pageable.getPageSize()), total);
          return new PageImpl<>(list.subList(start, end), pageable, total);
        });
  }

  private Mono<Void> addFile(String folderId, File file) {
    return folderRepository.findById(folderId)
        .flatMap(folder -> addFileSize(folder, file.getFileSize()));
  }

  private Mono<Void> addFileSize(Folder folder, long size) {
      folder.addSize(size);
      return folderRepository.save(folder)
          .then(Mono.defer(() -> {
            if(folder.isRoot()) return Mono.empty();
            return folderRepository.findById(folder.getParentFolderId())
                .flatMap(parentFolder -> addFileSize(parentFolder, size));
          }));
  }

  private Mono<String> buildFileName(String folderId, String originalFileName) {

    return fileRepository.existsByFolderIdAndFileName(folderId, originalFileName)
        .flatMap(exists -> {
          if (!exists) return Mono.just(originalFileName);

          String[] name = originalFileName.split("\\.");
          String fileFormat = name[1];
          String filename = name[0];
          AtomicInteger counter = new AtomicInteger(1);
          StringBuilder newFileNameBuilder = new StringBuilder();

          return Mono.just(filename)
              .repeat()
              .flatMap(fn -> fileRepository.existsByFolderIdAndFileName(folderId, fn)
                  .map(exist -> Tuples.of(fn, exist)))
              .filter(tuple -> !tuple.getT2())
              .map(Tuple2::getT1)
              .map(fn -> {
                newFileNameBuilder.setLength(0);
                return newFileNameBuilder
                    .append(fn)
                    .append("(")
                    .append(counter.getAndIncrement())
                    .append(")")
                    .append(".")
                    .append(fileFormat)
                    .toString();
              })
              .next();
        });
  }

  public Mono<FileDetailResponse> getFile(String userId, String fileId) {

    return fileRepository.findByIdAndUserId(fileId, userId)
        .switchIfEmpty(Mono.defer(() -> Mono.error(new NotFoundException(ErrorCode.FILE_NOT_FOUNT))))
        .map(FileDetailResponse::of);
  }
}


