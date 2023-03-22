package numble.mybox.storage.folder.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.mybox.common.domain.BaseEntity;
import numble.mybox.storage.file.domain.File;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;


@Entity
@Table(name = "folder")
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE folder SET is_deleted = true WHERE id = ?")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Folder extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "folder_id")
  private Long id;

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "folder_name")
  private String name;

  @Column(name = "parent_folder_id")
  private Long parentFolderId;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "sub_folder", joinColumns = @JoinColumn(name = "folder_id"))
  private List<SubFolder> subFolders = new ArrayList<>();

  @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<File> files = new ArrayList<>();

  @Column(name = "total_size")
  @Builder.Default
  private BigDecimal totalSize = BigDecimal.ZERO;

  @Column(name = "used_size")
  @Builder.Default
  private BigDecimal usedSize = BigDecimal.ZERO;

  @Column(name = "is_root")
  @Builder.Default
  private Boolean isRoot = Boolean.FALSE;

  @Column(name = "is_deleted")
  @Builder.Default
  private Boolean isDeleted = Boolean.FALSE;

  public void addSubFolder(Folder subFolder) {
    subFolders.add(SubFolder.of(subFolder));
    usedSize = usedSize.add(subFolder.getUsedSize());
  }

  // Todo : 파일 추가시 폴더 용량 고려 및 증가

}
