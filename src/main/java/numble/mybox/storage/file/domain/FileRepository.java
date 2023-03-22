package numble.mybox.storage.file.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {

  Boolean existsByFolderIdAndFileName(Long folderId, String originalFilename);
}
