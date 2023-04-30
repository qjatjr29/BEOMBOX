package numble.mybox.user.user.application;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import numble.mybox.user.user.domain.OAuth2Attributes;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
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
  public Mono<OidcUser> loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
    OidcReactiveOAuth2UserService delegate = new OidcReactiveOAuth2UserService();
    Mono<OidcUser> oidcUserMono = delegate.loadUser(userRequest);

    return oidcUserMono.flatMap(oidcUser -> {
      String registrationId = userRequest.getClientRegistration().getRegistrationId();
      String userNameAttributeName = userRequest.getClientRegistration()
          .getProviderDetails()
          .getUserInfoEndpoint()
          .getUserNameAttributeName();

      OAuth2Attributes oAuth2Attributes = OAuth2AttributesFactory.getOAuth2User(registrationId,
          oidcUser.getAttributes());

      return userService.saveOrUpdate(oAuth2Attributes)
          .map(user -> {
            Map<String, Object> attributes = new HashMap<>(oidcUser.getAttributes());
            attributes.put("userId", user.getId());
            attributes.put("provider", registrationId);

            return new DefaultOidcUser(
                Collections.singleton(new SimpleGrantedAuthority(user.getUserRole())),
                oidcUser.getIdToken(),
                oidcUser.getUserInfo(),
                userNameAttributeName);
          });

    });
  }
}
