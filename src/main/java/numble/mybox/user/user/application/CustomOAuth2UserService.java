package numble.mybox.user.user.application;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import numble.mybox.user.user.domain.OAuth2Attributes;
import numble.mybox.user.user.domain.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  private final UserService userService;

  public CustomOAuth2UserService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
    OAuth2User oAuth2User = delegate.loadUser(userRequest);

    String registrationId = userRequest.getClientRegistration().getRegistrationId();
    String userNameAttributeName = userRequest.getClientRegistration()
        .getProviderDetails()
        .getUserInfoEndpoint()
        .getUserNameAttributeName();

    OAuth2Attributes oAuth2Attributes = OAuth2AttributesFactory.getOAuth2User(registrationId, oAuth2User.getAttributes());
    User user = userService.saveOrUpdate(oAuth2Attributes);

    Map<String, Object> attributes = new HashMap<>(oAuth2Attributes.getAttributes());
    attributes.put("userId", user.getId());
    attributes.put("provider", registrationId);

    return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getUserRole())),
        attributes,
        userNameAttributeName);
  }

}
