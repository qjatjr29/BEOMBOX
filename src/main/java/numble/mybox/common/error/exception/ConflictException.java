package numble.mybox.common.error.exception;

import numble.mybox.common.error.ErrorCode;

public class ConflictException extends BusinessException {

  public ConflictException(ErrorCode errorCode) {
    super(errorCode);
  }
}
