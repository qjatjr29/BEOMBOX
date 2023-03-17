package numble.mybox.user.user.application;

import numble.mybox.common.error.ErrorCode;
import numble.mybox.common.error.exception.NotFoundException;
import numble.mybox.common.event.Events;
import numble.mybox.common.event.SignupCompletedEvent;
import numble.mybox.user.user.domain.User;
import numble.mybox.user.user.domain.UserRepository;
import numble.mybox.user.user.domain.UserTokenData;
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
  public Mono<User> save(User user) {

    Mono<User> savedUser = userRepository.save(user);

    return savedUser.flatMap(u -> {
      SignupCompletedEvent event = new SignupCompletedEvent(u.getId(), u.getNickname());
      Events.raise(event);
      return Mono.just(u);
    });
  }

  public Mono<UserDetailResponse> getUserDetailByEmailAndPrinciple(UserTokenData user) {
    return userRepository.findByEmailAndProvider(user.getEmail(), user.getProvider())
        .switchIfEmpty(Mono.error(new NotFoundException(ErrorCode.USER_NOT_FOUND)))
        .flatMap(u -> Mono.just(UserDetailResponse.of(u)));
  }

  public Mono<User> getUserByEmailAndPrinciple(String email, String provider) {
    return userRepository.findByEmailAndProvider(email, provider)
        .switchIfEmpty(Mono.empty());
  }

  @Transactional
  public Mono<UserDetailResponse> update(UpdateRequest request, UserTokenData user) {
    return userRepository.findByEmailAndProvider(user.getEmail(), user.getProvider())
        .map((u) -> {
          u.updateNickname(request.getNickname());
          u.updateNickname(request.getImageUrl());
          return u;
        })
        .flatMap(userRepository::save)
        .map(UserDetailResponse::of);
  }

}
