package numble.mybox.storage.file.application;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import lombok.extern.slf4j.Slf4j;
import numble.mybox.common.error.ErrorCode;
import numble.mybox.common.error.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@Component
public class AwsS3Service {

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  private final AmazonS3 amazonS3;

  public AwsS3Service(AmazonS3 amazonS3) {
    this.amazonS3 = amazonS3;
  }

  public String uploadFile(Long folderId, String name, MultipartFile file) {

    String fileName = folderId + "/" + name;
    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentLength(file.getSize());
    objectMetadata.setContentType(file.getContentType());
    URL url = null;

    try(InputStream inputStream = file.getInputStream()) {
      amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
          .withCannedAcl(CannedAccessControlList.PublicRead));

      url = amazonS3.getUrl(bucket, fileName);
    } catch (IOException e) {
      log.error("파일 업로드 실패!!!! -> {}", e.getMessage());
      throw new BusinessException(ErrorCode.AWS_S3_UPLOAD_FAIL);
    }
    return String.valueOf(url);
  }
}
