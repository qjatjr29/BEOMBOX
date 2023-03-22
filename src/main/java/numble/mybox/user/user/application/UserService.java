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


@Service
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Transactional
  public User saveOrUpdate(OAuth2Attributes oAuth2Attributes) {
    User savedUser = userRepository.findByEmailAndProvider(oAuth2Attributes.getEmail(),
            oAuth2Attributes.getProvider())
        .map(u -> u.updateInfo(oAuth2Attributes.getName(), oAuth2Attributes.getImageUrl()))
        .orElse(oAuth2Attributes.toUser());

    User user = userRepository.save(savedUser);
    signupEventRaise(user.getId(), user.getName());
    return user;
  }

  public UserDetailResponse getUser(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    return UserDetailResponse.of(user);
  }

  private void signupEventRaise(final Long userId, final String userName) {
    SignupCompletedEvent event = new SignupCompletedEvent(userId, userName);
    Events.raise(event);
  }

}
