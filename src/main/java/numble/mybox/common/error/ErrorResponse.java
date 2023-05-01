package numble.mybox.common.error;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

  private int statusCode;
  private String resultCode;
  private String message;
  private List<ErrorField> errors;

  private ErrorResponse(ErrorCode errorCode, List<ErrorField> errors) {
    this.statusCode = errorCode.getStatusCode();
    this.resultCode = errorCode.getResultCode();
    this.message = errorCode.getMessage();
    this.errors = errors;
  }

  private ErrorResponse(int statusCode, String resultCode, String message,
      List<ErrorField> errors) {
    this.statusCode = statusCode;
    this.resultCode = resultCode;
    this.message = message;
    this.errors = errors;
  }


  public static ErrorResponse from(int statusCode, String resultCode, String message, List<ErrorField> errors) {
    return new ErrorResponse(statusCode, resultCode, message, errors);
  }

  public static ErrorResponse from(final ErrorCode errorCode, final List<ErrorField> code) {
    return new ErrorResponse(errorCode, code);
  }

}
