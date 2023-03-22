package numble.mybox.storage.folder.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderRepository extends JpaRepository<Folder, Long> {

  Optional<Folder> findByUserIdAndParentFolderId(Long userId, Long parentFolderId);
}
