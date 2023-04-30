package numble.mybox.user.user.application;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import numble.mybox.user.user.domain.OAuth2Attributes;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomReactiveOAuth2UserService implements
    ReactiveOAuth2UserService<OAuth2UserRequest, OAuth2User> {

  private final UserService userService;

  public CustomReactiveOAuth2UserService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public Mono<OAuth2User> loadUser(OAuth2UserRequest userRequest)
      throws OAuth2AuthenticationException {
    ReactiveOAuth2UserService delegate = new DefaultReactiveOAuth2UserService();
    Mono<OAuth2User> oAuth2UserMono = delegate.loadUser(userRequest);
    return oAuth2UserMono
        .flatMap(oAuth2User -> {
              String registrationId = userRequest.getClientRegistration().getRegistrationId();
              String userNameAttributeName = userRequest.getClientRegistration()
                  .getProviderDetails()
                  .getUserInfoEndpoint()
                  .getUserNameAttributeName();

              OAuth2Attributes oAuth2Attributes = OAuth2AttributesFactory.getOAuth2User(registrationId,
                  oAuth2User.getAttributes());
              return userService.saveOrUpdate(oAuth2Attributes)
                  .map(user -> {
                    Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
                    attributes.put("userId", user.getId());
                    attributes.put("provider", registrationId);

                    return new DefaultOAuth2User(
                        Collections.singleton(new SimpleGrantedAuthority(user.getUserRole())),
                        attributes,
                        userNameAttributeName);
                  });

        });
  }
}
