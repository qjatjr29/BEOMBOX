package numble.mybox.security.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class )
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@TestPropertySource(locations = "/application.yml")
@DisplayName("Oauth2 관련 테스트")
public class OAuthConfigTest {

  @Autowired
  private WebTestClient webTestClient;

  @Test
  @DisplayName("구글 로그인 시 - 리다이렉트 성공")
  void googleLogin_redirectLoginAuthenticationForm() throws Exception {
        webTestClient.get().uri("/oauth2/authorization/google")
        .exchange()
        .expectStatus().is3xxRedirection()
        .expectHeader().valueMatches("Location", "^https://accounts\\.google\\.com/o/oauth2/v2/auth.*$");
  }

  @Test
  @DisplayName("카카오 로그인 시 - 리다이렉트 성공")
  void kakaoLogin_redirectLoginAuthenticationForm() {
    webTestClient.get().uri("/oauth2/authorization/kakao")
        .exchange()
        .expectStatus().is3xxRedirection()
        .expectHeader().valueMatches("Location", "^https://kauth\\.kakao\\.com/oauth/authorize.*$");
    }

  @Test
  @DisplayName("네이터 로그인 시 - 리다이렉트 성공")
  void naverLogin_redirectLoginAuthenticationForm() {

    webTestClient.get().uri("/oauth2/authorization/naver")
        .exchange()
        .expectStatus().is3xxRedirection()
        .expectHeader().valueMatches("Location", "^https://nid\\.naver\\.com/oauth2\\.0/authorize.*$");
  }

}
