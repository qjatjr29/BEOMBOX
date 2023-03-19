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

    Throwable throwable = getError(request);

    if(throwable instanceof BusinessException) {
      BusinessException ex = (BusinessException) getError(request);
      errorAttributes.remove("error");
      errorAttributes.put("exception", ex.getClass().getSimpleName());
      errorAttributes.put("message", ex.getErrorCode().getMessage());
      errorAttributes.put("status", ex.getErrorCode().getStatusCode());
      errorAttributes.put("errorCode", ex.getErrorCode().getResultCode());
    }

    return errorAttributes;
  }

}
