package numble.mybox.storage.file.application;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.TreeMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import numble.mybox.config.properties.AwsProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;


@Slf4j
@Service
public class AwsS3Service {

  // AWS S3 AsyncClient
  private final AwsProperties s3ConfigProperties;
  private final WebClient webClient;

  private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyyMMdd");
  private static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("yyyyMMdd\'T\'HHmmss\'Z\'");
  private static final Charset CHARSET_NAME = StandardCharsets.UTF_8;
  private static final String HMAC_ALGORITHM = "HmacSHA256";
  private static final String HASH_ALGORITHM = "SHA-256";
  private static final String AWS_ALGORITHM = "AWS4-HMAC-SHA256";
  private static final String SERVICE_NAME = "s3";
  private static final String REQUEST_TYPE = "aws4_request";
  private static final String UNSIGNED_PAYLOAD = "UNSIGNED-PAYLOAD";

  public AwsS3Service(AwsProperties s3ConfigProperties,
      WebClient webClient) {
    this.s3ConfigProperties = s3ConfigProperties;
    this.webClient = webClient;
  }

  public Mono<String> upload(FilePart file, String userId, String filename, long size) {

    URI s3Url = getStorageUrl(userId, filename);

    return webClient.put()
        .uri(s3Url)
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .contentLength(size)
        .headers(headers -> setHttpHeader(HttpMethod.PUT, headers, s3Url))
        .body(BodyInserters.fromDataBuffers(file.content()))
        .retrieve()
        .toBodilessEntity()
        .map(entity -> s3Url.toString());
  }

  public Mono<Boolean> delete(String userId, String filename) {

    URI s3Url = getStorageUrl(userId, filename);

    return webClient.delete()
        .uri(s3Url)
        .headers(headers -> setHttpHeader(HttpMethod.DELETE, headers, s3Url))
        .retrieve()
        .bodyToMono(Void.class)
        .thenReturn(true);
  }

  private URI getStorageUrl(String userId, String filename) {

    return UriComponentsBuilder.fromHttpUrl(s3ConfigProperties.getEndpoint())
        .path("/{bucketName}")
        .path("/{userId}")
        .path("/{filename}")
        .build(s3ConfigProperties.getS3BucketName(), userId, filename);
  }

  private void setHttpHeader(HttpMethod method, HttpHeaders httpHeaders, URI storageUri) {
    Date now = new Date();
    DATE_FORMATTER.setTimeZone(TimeZone.getTimeZone("UTC"));
    TIME_FORMATTER.setTimeZone(TimeZone.getTimeZone("UTC"));
    String datestamp = DATE_FORMATTER.format(now);
    String timestamp = TIME_FORMATTER.format(now);

    httpHeaders.add("Host", storageUri.getHost());
    httpHeaders.add("X-Amz-Date", timestamp);
    httpHeaders.add("X-Amz-Content-Sha256", "UNSIGNED-PAYLOAD");

    String standardizedQueryParameters = "";
    TreeMap<String, String> sortedHeaders = getSortedHeaders(httpHeaders);
    String signedHeaders = getSignedHeaders(sortedHeaders);
    String standardizedHeaders = getStandardizedHeaders(sortedHeaders);
    String canonicalRequest = getCanonicalRequest(method, storageUri, standardizedQueryParameters, standardizedHeaders, signedHeaders);
    String scope = getScope(datestamp);

    String stringToSign = getStringToSign(timestamp, scope, canonicalRequest);
    String signature = getSignature(s3ConfigProperties.getSecretKey(), datestamp, stringToSign);
    String authorization = getAuthorization(s3ConfigProperties.getAccessKey(), scope, signedHeaders, signature);
    httpHeaders.add("Authorization", authorization);

  }

  private byte[] sign(String stringData, byte[] key) throws NoSuchAlgorithmException, InvalidKeyException {
    byte[] data = stringData.getBytes(CHARSET_NAME);
    Mac e = Mac.getInstance(HMAC_ALGORITHM);
    e.init(new SecretKeySpec(key, HMAC_ALGORITHM));
    return e.doFinal(data);
  }

  private String hash(String text) {
    try {
      MessageDigest e = MessageDigest.getInstance(HASH_ALGORITHM);
      e.update(text.getBytes(CHARSET_NAME));
      return Hex.encodeHexString(e.digest());
    } catch (Exception var3) {
      //
    }
    return "";
  }

  private TreeMap<String, String> getSortedHeaders(HttpHeaders httpHeaders) {
    TreeMap<String, String> sortedHeaders = new TreeMap<>();
    for (String key : httpHeaders.keySet()) {
      sortedHeaders.put(key, httpHeaders.getFirst(key));
    }

    return sortedHeaders;
  }

  private String getSignedHeaders(TreeMap<String, String> sortedHeaders) {
    StringBuilder signedHeadersBuilder = new StringBuilder();
    for (String headerName : sortedHeaders.keySet()) {
      if (signedHeadersBuilder.length() > 0)
        signedHeadersBuilder.append(';');
      signedHeadersBuilder.append(headerName.toLowerCase());
    }
    return signedHeadersBuilder.toString();
  }

  private String getStandardizedHeaders(TreeMap<String, String> sortedHeaders) {
    StringBuilder standardizedHeadersBuilder = new StringBuilder();
    for (String headerName : sortedHeaders.keySet()) {
      standardizedHeadersBuilder.append(headerName.toLowerCase()).append(":").append(sortedHeaders.get(headerName)).append("\n");
    }

    return standardizedHeadersBuilder.toString();
  }

  private String getCanonicalRequest(HttpMethod method, URI requestUri, String standardizedQueryParameters, String standardizedHeaders, String signedHeaders) {

    return method + "\n" +
        requestUri.getRawPath() + "\n" +
        standardizedQueryParameters + "\n" +
        standardizedHeaders + "\n" +
        signedHeaders + "\n" +
        UNSIGNED_PAYLOAD;
  }

  private String getScope(String datestamp) {
    return datestamp + "/" +
        s3ConfigProperties.getRegion() + "/" +
        SERVICE_NAME + "/" +
        REQUEST_TYPE;
  }

  private String getStringToSign(String timestamp, String scope, String canonicalRequest) {
    return AWS_ALGORITHM +
        "\n" +
        timestamp + "\n" +
        scope + "\n" +
        hash(canonicalRequest);
  }

  private String getSignature(String secretKey, String datestamp, String stringToSign) {
    try {
      byte[] kSecret = ("AWS4" + secretKey).getBytes(CHARSET_NAME);
      byte[] kDate = sign(datestamp, kSecret);
      byte[] kRegion = sign(s3ConfigProperties.getRegion(), kDate);
      byte[] kService = sign(SERVICE_NAME, kRegion);
      byte[] signingKey = sign(REQUEST_TYPE, kService);

      return Hex.encodeHexString(sign(stringToSign, signingKey));
    } catch (Exception e) {
      //
    }
    return "";
  }

  private String getAuthorization(String accessKey, String scope, String signedHeaders, String signature) {
    String signingCredentials = accessKey + "/" + scope;
    String credential = "Credential=" + signingCredentials;
    String signerHeaders = "SignedHeaders=" + signedHeaders;
    String signatureHeader = "Signature=" + signature;

    return AWS_ALGORITHM + " " +
        credential + ", " +
        signerHeaders + ", " +
        signatureHeader;
  }

}
