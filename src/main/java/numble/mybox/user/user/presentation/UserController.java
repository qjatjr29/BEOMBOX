package numble.mybox.user.user.presentation;

import javax.validation.Valid;
import numble.mybox.common.presentation.CurrentUser;
import numble.mybox.user.user.application.UpdateRequest;
import numble.mybox.user.user.application.UserService;
import numble.mybox.user.user.application.UserDetailResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/me")
  public ResponseEntity<UserDetailResponse> getUserInfo(@CurrentUser Long userId) {
    UserDetailResponse response =  userService.getUser(userId);
    return ResponseEntity.ok(response);
  }

//  @PutMapping("/me")
//  public ResponseEntity<UserDetailResponse> updateUserInfo(@Valid @RequestBody UpdateRequest request, @CurrentUser Long userId) {
//    return userService.update(request, userId)
//        .map(ResponseEntity::ok);
//  }

}
