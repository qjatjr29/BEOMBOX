package numble.mybox.user.user.infrastructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import numble.mybox.common.error.ErrorCode;
import numble.mybox.common.error.exception.BusinessException;
import numble.mybox.common.error.exception.UnAuthorizedException;
import numble.mybox.user.user.domain.UserTokenData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtProvider {

  @Value("${jwt.secret-key}")
  private String secretKey;

  @Value("${jwt.accesstoken-expiration}")
  private Long accessTokenExpirationTime;

  @Value("${jwt.refreshtoken-expiration}")
  private Long refreshTokenExpirationTime;

  @PostConstruct
  protected void init() {
    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
  }

  public String generateAccessToken(UserTokenData tokenData) {
    return generateToken(tokenData.getId(),
        tokenData.getEmail(),
        tokenData.getProvider(),
        tokenData.getRole(),
        accessTokenExpirationTime);
  }

  public String generateRefreshToken(UserTokenData tokenData) {
    return generateToken(tokenData.getId(),
        tokenData.getEmail(),
        tokenData.getProvider(),
        tokenData.getRole(),
        refreshTokenExpirationTime);
  }

  public UserTokenData getTokenData(String token) {
    return UserTokenData.from(getUserId(token), getUserEmail(token), getUserProvider(token), getUserRole(token));
  }

  public void validateToken(String token) {
    if(!isExistToken(token)) throw new BusinessException(ErrorCode.TOKEN_NOT_EXISTS);

    try {
      if(isExpiredToken(token)) throw new UnAuthorizedException(ErrorCode.UNAUTHORIZED);
    } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
      log.info("Invalid JWT Token", e);
      throw new UnAuthorizedException(ErrorCode.INVALID_VERIFICATION_TOKEN);
    } catch (ExpiredJwtException e) {
      log.info("Expired JWT Token", e);
      throw new UnAuthorizedException(ErrorCode.EXPIRED_VERIFICATION_TOKEN);
    } catch (UnsupportedJwtException e) {
      log.info("Unsupported JWT Token", e);
      throw new UnAuthorizedException(ErrorCode.CERTIFICATION_TYPE_NOT_MATCH);
    } catch (IllegalArgumentException e) {
      log.info("JWT claims string is empty.", e);
      throw e;
    }
  }

  public Long getTokenExpiredIn(String token) {
    Claims claims = getClaims(token);
    return claims.getExpiration().getTime();
  }

  private String generateToken(String id, String email, String provider, String role, Long expireTime) {
    Claims claims = Jwts.claims();
    claims.put("userId", id);
    claims.put("email", email);
    claims.put("provider", provider);
    claims.put("role", role);
    Date now = new Date(System.currentTimeMillis());

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + expireTime))
        .signWith(getSigningKey(secretKey), SignatureAlgorithm.HS256)
        .compact();
  }

  private Key getSigningKey(String secretKey) {
    byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  private Claims getClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey(secretKey))
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  private String getUserEmail(String token) {
    if(!isExistToken(token)) throw new BusinessException(ErrorCode.TOKEN_NOT_EXISTS);

    Claims claims = getClaims(token);
    return claims.get("email", String.class);
  }

  private String getUserId(String token) {
    if(!isExistToken(token)) throw new BusinessException(ErrorCode.TOKEN_NOT_EXISTS);
    Claims claims = getClaims(token);
    return claims.get("userId", String.class);
  }

  private String getUserProvider(String token) {
    if(!isExistToken(token)) throw new BusinessException(ErrorCode.TOKEN_NOT_EXISTS);
    Claims claims = getClaims(token);
    return claims.get("provider", String.class);
  }

  private String getUserRole(String token) {
    if(!isExistToken(token)) throw new BusinessException(ErrorCode.TOKEN_NOT_EXISTS);
    Claims claims = getClaims(token);
    return claims.get("role", String.class);
  }

  private Boolean isExpiredToken(String token) {
    Date expiration = getClaims(token).getExpiration();
    return expiration.before(new Date());
  }

  private Boolean isExistToken(String token) {
    return token != null && token.length() != 0;
  }

}
