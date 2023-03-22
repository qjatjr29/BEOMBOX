package numble.mybox.common.presentation;

import java.io.IOException;
import java.util.Arrays;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import numble.mybox.user.user.domain.Role;
import numble.mybox.user.user.domain.UserTokenData;
import numble.mybox.user.user.infrastructure.JwtProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final String HEADER_AUTHORIZATION = "Authorization";

  private final JwtProvider jwtProvider;

  public JwtAuthenticationFilter(JwtProvider jwtProvider) {
    this.jwtProvider = jwtProvider;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {


    String token = request.getHeader(HEADER_AUTHORIZATION);

    if(token != null && token.length() != 0) {
      jwtProvider.validateToken(token);
      UserTokenData tokenData = jwtProvider.getTokenData(token);
      Authentication auth = getAuthentication(tokenData);
      SecurityContextHolder.getContext().setAuthentication(auth);
    }
    filterChain.doFilter(request, response);
  }

  public Authentication getAuthentication(UserTokenData tokenData) {
    return new UsernamePasswordAuthenticationToken(tokenData, "",
        Arrays.asList(new SimpleGrantedAuthority(Role.USER.getRole())));
  }
}
