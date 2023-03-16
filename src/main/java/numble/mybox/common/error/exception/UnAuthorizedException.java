package numble.mybox.common.error.exception;

import numble.mybox.common.error.ErrorCode;

public class UnAuthorizedException extends BusinessException {

  public UnAuthorizedException(ErrorCode errorCode) {
    super(errorCode);
  }
}
