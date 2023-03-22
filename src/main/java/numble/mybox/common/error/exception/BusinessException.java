package numble.mybox.common.error.exception;

import java.util.List;
import lombok.Getter;
import numble.mybox.common.error.ErrorCode;
import numble.mybox.common.error.ErrorField;

@Getter
public class BusinessException extends RuntimeException {

  private final ErrorCode errorCode;
  private List<ErrorField> errors;

  public BusinessException(ErrorCode errorCode) {
    this.errorCode = errorCode;
  }

  public BusinessException(ErrorCode errorCode, List<ErrorField> errors) {
    this.errorCode = errorCode;
    this.errors = errors;
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }
}
