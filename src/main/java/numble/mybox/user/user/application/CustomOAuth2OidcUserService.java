package numble.mybox.user.user.application;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import numble.mybox.user.user.domain.OAuth2Attributes;
import numble.mybox.user.user.domain.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2OidcUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

  private final UserService userService;

  public CustomOAuth2OidcUserService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {

    OAuth2UserService<OidcUserRequest, OidcUser> delegate = new OidcUserService();
    OidcUser oidcUser = delegate.loadUser(userRequest);

    String registrationId = userRequest.getClientRegistration().getRegistrationId();
    String userNameAttributeName = userRequest.getClientRegistration()
        .getProviderDetails()
        .getUserInfoEndpoint()
        .getUserNameAttributeName();

    OAuth2Attributes oAuth2Attributes = OAuth2AttributesFactory.getOAuth2User(registrationId, oidcUser.getAttributes());

    User user = userService.saveOrUpdate(oAuth2Attributes);

    Map<String, Object> attributes = new HashMap<>(oidcUser.getAttributes());
    attributes.put("provider", registrationId);
    attributes.put("userId", user.getId());
    OidcUserInfo userInfo = new OidcUserInfo(attributes);

    return new DefaultOidcUser(Collections.singleton(new SimpleGrantedAuthority(user.getUserRole())),
        oidcUser.getIdToken(),
        userInfo,
        userNameAttributeName);
  }
}
