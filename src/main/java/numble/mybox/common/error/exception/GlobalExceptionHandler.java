package numble.mybox.common.error.exception;

import java.util.List;
import numble.mybox.common.error.ErrorCode;
import numble.mybox.common.error.ErrorField;
import numble.mybox.common.error.ErrorResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  // custom exception 처리
  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponseDto> handleBusinessException(BusinessException e) {
    List<ErrorField> errors = e.getErrors();
    ErrorResponseDto response = ErrorResponseDto.of(e.getErrorCode(), errors);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
  }

  // @Valid 에러
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {

    return ResponseEntity
        .badRequest()
        .body(ErrorResponseDto.from(ErrorCode.INVALID_INPUT_VALUE, ex.getBindingResult()));
  }
}
