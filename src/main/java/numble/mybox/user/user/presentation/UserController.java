package numble.mybox.user.user.presentation;

import javax.validation.Valid;
import numble.mybox.common.presentation.CurrentUser;
import numble.mybox.user.user.application.UpdateRequest;
import numble.mybox.user.user.application.UserService;
import numble.mybox.user.user.domain.UserTokenData;
import numble.mybox.user.user.application.UserDetailResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/me")
  public Mono<ResponseEntity<UserDetailResponse>> getUserInfo(@CurrentUser UserTokenData user) {
     return userService.getUserDetailByEmailAndPrinciple(user)
         .map(ResponseEntity::ok);
  }

  @PutMapping("/me")
  public Mono<ResponseEntity<UserDetailResponse>> updateUserInfo(@Valid @RequestBody UpdateRequest request, @CurrentUser UserTokenData user) {
    return userService.update(request, user)
        .map(ResponseEntity::ok);
  }

}
