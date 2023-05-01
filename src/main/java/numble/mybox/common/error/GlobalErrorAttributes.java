package numble.mybox.common.error;

import java.util.Map;
import numble.mybox.common.error.exception.BusinessException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

  @Override
  public Map<String, Object> getErrorAttributes(ServerRequest request,
      ErrorAttributeOptions options) {

    Map<String, Object> errorAttributes = super.getErrorAttributes(request, options);

    Throwable error = getError(request);

    if(error instanceof BusinessException) {

      BusinessException exception = (BusinessException) getError(request);
      errorAttributes.put("error", ErrorResponse.from(
          exception.getErrorCode().getStatusCode(),
          exception.getErrorCode().getResultCode(),
          exception.getErrorCode().getMessage(),
          exception.getErrors()
      ));
    }

    return errorAttributes;
  }
}
