package numble.mybox.user.user.application;

import numble.mybox.common.error.ErrorCode;
import numble.mybox.common.error.exception.NotFoundException;
import numble.mybox.common.event.Events;
import numble.mybox.common.event.SignupCompletedEvent;
import numble.mybox.user.user.domain.OAuth2Attributes;
import numble.mybox.user.user.domain.User;
import numble.mybox.user.user.domain.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;


@Service
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Transactional
  public Mono<User> saveOrUpdate(OAuth2Attributes oAuth2Attributes) {
    return userRepository.findByEmailAndProvider(oAuth2Attributes.getEmail(),
            oAuth2Attributes.getProvider())
        .map(u -> u.updateInfo(oAuth2Attributes.getName(), oAuth2Attributes.getImageUrl()))
        .switchIfEmpty(Mono.defer(() -> {
          Mono<User> newUser = userRepository.save(oAuth2Attributes.toUser());
          return newUser.map(user -> {
            signupEventRaise(user.getId(), user.getName());
            return user;
          });
        }));
  }

  public Mono<UserDetailResponse> getUser(String userId) {
    Mono<User> user = userRepository.findById(userId)
        .switchIfEmpty(Mono.error(new NotFoundException(ErrorCode.USER_NOT_FOUND)));
    return user.map(UserDetailResponse::of);
  }

  private void signupEventRaise(final String userId, final String userName) {
    SignupCompletedEvent event = new SignupCompletedEvent(userId, userName);
    Events.raise(event);
  }

}
