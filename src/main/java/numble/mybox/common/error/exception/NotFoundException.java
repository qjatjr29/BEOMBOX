package numble.mybox.common.error.exception;

import numble.mybox.common.error.ErrorCode;

public class NotFoundException extends BusinessException {

  public NotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }
}
