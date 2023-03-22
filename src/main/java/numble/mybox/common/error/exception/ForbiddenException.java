package numble.mybox.common.error.exception;

import numble.mybox.common.error.ErrorCode;

public class ForbiddenException extends BusinessException {

  public ForbiddenException(ErrorCode errorCode) {
    super(errorCode);
  }
}
