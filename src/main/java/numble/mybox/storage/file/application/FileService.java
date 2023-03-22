package numble.mybox.storage.file.application;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import numble.mybox.common.error.ErrorCode;
import numble.mybox.common.error.exception.ForbiddenException;
import numble.mybox.common.error.exception.NotFoundException;
import numble.mybox.storage.file.domain.File;
import numble.mybox.storage.file.domain.FileRepository;
import numble.mybox.storage.folder.domain.Folder;
import numble.mybox.storage.folder.domain.FolderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
@Transactional(readOnly = true)
public class FileService {

  private final FileRepository fileRepository;
  private final FolderRepository folderRepository;
  private final AwsS3Service s3Service;

  public FileService(FileRepository fileRepository,
      FolderRepository folderRepository, AwsS3Service s3Service) {
    this.fileRepository = fileRepository;
    this.folderRepository = folderRepository;
    this.s3Service = s3Service;
  }

  @Transactional
  public FileDetailResponse uploadFile(Long userId, Long folderId, MultipartFile fileData) {

    Folder folder = folderRepository.findById(folderId)
        .orElseThrow(() -> new NotFoundException(ErrorCode.FOLDER_NOT_FOUND));

    if(!Objects.equals(folder.getUserId(), userId)) throw new ForbiddenException(ErrorCode.INVALID_VERIFICATION_FILE_UPLOAD);

    String filename = setFileName(folderId, fileData.getOriginalFilename());

    String fileUrl = s3Service.uploadFile(folderId, filename, fileData);

    File file = File.builder()
        .fileName(filename)
        .folder(folder)
        .fileSize(BigDecimal.valueOf(fileData.getSize()))
        .fileUrl(fileUrl)
        .build();

    fileRepository.save(file);

    return FileDetailResponse.of(file);
  }

  private String setFileName(Long folderId, String originalFileName) {

    if (!fileRepository.existsByFolderIdAndFileName(folderId, originalFileName)) {
      return originalFileName;
    }

    AtomicInteger counter = new AtomicInteger(1);

    String[] name = originalFileName.split("\\.");
    String fileFormat = name[1];
    String filename = name[0];

    StringBuilder newFileNameBuilder = new StringBuilder();

    do {
      newFileNameBuilder.setLength(0);
      newFileNameBuilder.append(filename)
          .append("(")
          .append(counter.getAndIncrement())
          .append(")")
          .append(".")
          .append(fileFormat);
    } while (fileRepository.existsByFolderIdAndFileName(folderId, newFileNameBuilder.toString()));

    return newFileNameBuilder.toString();

  }

}


