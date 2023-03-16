package numble.mybox.common.error.exception;

import numble.mybox.common.error.ErrorCode;

public class BadRequestException extends BusinessException {

  public BadRequestException(ErrorCode errorCode) {
    super(errorCode);
  }
}
