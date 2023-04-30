package numble.mybox.user.user.presentation;

import numble.mybox.common.presentation.CurrentUser;
import numble.mybox.user.user.application.UserService;
import numble.mybox.user.user.application.UserDetailResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
  public Mono<ResponseEntity<UserDetailResponse>> getUserInfo(@CurrentUser String userId) {
    Mono<UserDetailResponse> response =  userService.getUser(userId);
    return response.map(ResponseEntity::ok);
  }

}
