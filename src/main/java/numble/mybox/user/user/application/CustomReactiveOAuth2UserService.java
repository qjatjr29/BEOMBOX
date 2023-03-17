package numble.mybox.user.user.application;

import numble.mybox.user.user.domain.UserRepository;
import numble.mybox.user.user.domain.User;
import org.springframework.security.oauth2.client.userinfo.DefaultReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomReactiveOAuth2UserService implements ReactiveOAuth2UserService<OAuth2UserRequest, OAuth2User> {

  private final UserService userService;

  public CustomReactiveOAuth2UserService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public Mono<OAuth2User> loadUser(OAuth2UserRequest userRequest)
      throws OAuth2AuthenticationException {

    final DefaultReactiveOAuth2UserService defaultReactiveOAuth2UserService = new DefaultReactiveOAuth2UserService();
    final String registrationId = userRequest.getClientRegistration().getRegistrationId();

    Mono<OAuth2User> oAuth2User = defaultReactiveOAuth2UserService.loadUser(userRequest);

    return oAuth2User.flatMap(e -> {
      User user = OAuth2UserFactory.getOAuth2User(registrationId, e.getAttributes());
      return userService
          .getUserByEmailAndPrinciple(user.getEmail(), user.getProvider())
          .switchIfEmpty(Mono.defer(() -> userService.save(user)));
    });

  }
}
