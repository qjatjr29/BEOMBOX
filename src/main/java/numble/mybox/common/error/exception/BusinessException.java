package numble.mybox.common.error.exception;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import numble.mybox.common.error.ErrorCode;
import numble.mybox.common.error.ErrorField;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
public class BusinessException extends ResponseStatusException {

  private final ErrorCode errorCode;
  private List<ErrorField> errors;

  public BusinessException(ErrorCode errorCode) {
    super(HttpStatus.valueOf(errorCode.getStatusCode()), errorCode.getMessage());
    this.errorCode = errorCode;
    this.errors = new ArrayList<>();
  }

  public BusinessException(ErrorCode errorCode, List<ErrorField> errors) {
    super(HttpStatus.valueOf(errorCode.getStatusCode()), errorCode.getMessage());
    this.errorCode = errorCode;
    this.errors = errors;
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }
}
