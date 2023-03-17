package numble.mybox.user.user.application;

import numble.mybox.user.user.domain.User;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomReactiveOAuth2OidcUserService implements
    ReactiveOAuth2UserService<OidcUserRequest, OidcUser> {

  private final UserService userService;

  public CustomReactiveOAuth2OidcUserService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public Mono<OidcUser> loadUser(OidcUserRequest userRequest) {

    final OidcReactiveOAuth2UserService oidcReactiveOAuth2UserService = new OidcReactiveOAuth2UserService();
    final String registrationId = userRequest.getClientRegistration().getRegistrationId();

    Mono<OidcUser> oAuth2User = oidcReactiveOAuth2UserService.loadUser(userRequest);

    return oAuth2User.flatMap((oidcUser) -> {

      User user = OAuth2UserFactory.getOAuth2User(registrationId, oidcUser.getAttributes());

      return userService
          .getUserByEmailAndPrinciple(user.getEmail(), user.getProvider())
          .switchIfEmpty(Mono.defer(() -> userService.save(user)));
    });
  }

}
