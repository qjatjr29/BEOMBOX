package numble.mybox.common.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

  INVALID_INPUT_VALUE(400, "COMMON-001", "유효성 검증에 실패한 경우"),
  DUPLICATE_INPUT_VALUE(409, "COMMON-002", "중복된 값이 들어온 경우"),
  BAD_REQUEST(400, "COMMON-003", "잘못된 요청이 들어온 경우"),
  ENTITY_NOT_FOUND(404, "COMMON-004", "엔티티를 찾을 수 없는 경우"),

  UNAUTHORIZED(401, "USER-001", "인증에 실패한 경우"),
  USER_NOT_FOUND(404, "USER-002", "계정을 찾을 수 없는 경우"),
  ROLE_NOT_EXISTS(403, "USER-003", "권한이 부족한 경우"),
  TOKEN_NOT_EXISTS(404, "USER-004", "인증 토큰이 존재하지 않는 경우"),
  DUPLICATE_LOGIN_ID(409, "USER-005", "계정명이 중복된 경우"),
  DUPLICATE_PHONE_NUMBER(409, "USER-006", "휴대폰 번호가 중복된 경우"),
  DUPLICATE_EMAIL(409, "USER-007", "이메일이 중복된 경우"),
  PROVIDER_NOT_SUPPORTED(400, "USER-008", "지원하지 않는 Provider로 로그인한 경우"),
  EXCEED_MAX_STORAGE_SIZE(403, "USER-009", "최대 저장 크기를 넘었을 경우"),

  DUPLICATE_FOLDER(409, "FOLDER-001", "해당 폴더에 같은 이름의 폴더가 이미 있는 경우"),
  FOLDER_NOT_FOUND(404, "FOLDER-002", "상위 폴더가 없는 경우"),

  FILE_ALREADY_EXISTS(409, "FILE-001", "이미 해당 파일이 있는 경우"),
  INVALID_VERIFICATION_FILE_ACCESS(403, "FILE-002", "파일을 올릴 권한이 없습니다."),
  FILE_NOT_FOUNT(404, "FILE-003", "찾는 파일이 없습니다"),

  EXPIRED_VERIFICATION_TOKEN(403, "AUTH-001", "인증 토큰이 만료된 경우"),
  INVALID_VERIFICATION_TOKEN(403, "AUTH-002", "토큰이 유효하지 않은 경우"),
  CERTIFICATION_TYPE_NOT_MATCH(403, "AUTH-003", "인증 타입이 일치하지 않은 경우"),

  AWS_S3_UPLOAD_FAIL(500, "S3-001", "S3 업로드에 실패했을 경우"),
  INVALID_FILE(400, "S3-002", "데이터가 없는 파일이 요청된 경우"),
  INVALID_TYPE_FILE(400, "S3-003", "지원하지 않은 형식의 파일이 들어온 경우"),
  S3_FILE_NOT_FOUND(404, "S3-004", "s3 버킷에 찾는 파일이 없는 경우"),
  AWS_S3_DOWNLOAD_FAIL(500, "S3-005", "파일 다운로드에 실패한 경우");

  int statusCode;
  String resultCode;
  String message;

  ErrorCode(int statusCode, String resultCode, String message) {
    this.statusCode = statusCode;
    this.resultCode = resultCode;
    this.message = message;
  }

}
