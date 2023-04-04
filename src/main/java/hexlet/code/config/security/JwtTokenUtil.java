package hexlet.code.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.Map;

import static io.jsonwebtoken.impl.TextCodec.BASE64;

/**
 * A utility class that provides methods for working with JSON WEB TOKEN(JWT).
 * @author sobaxx
 * @see hexlet.code.filter.JWTAuthenticationFilter
 * @see hexlet.code.filter.JWTAuthorizationFilter
 */
@Component
public class JwtTokenUtil {
    private final String jwtSecret;
    private final String issuer;
    private final Long jwtExpired;
    private final Long clockSkewSec;
    private final Clock clock;
    private static final Logger JWT_UTIL_LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);

    public JwtTokenUtil(@Value("${security.jwt.issuer:project-73") final String issuer,
                        @Value("${security.jwt.expire-length:3600000}") final Long expirationSec,
                        @Value("${jwt.clock-skew-sec:300}") final Long clockSkewSec,
                        @Value("${security.jwt.secret:dev-secret}") final String secret) {
        this.jwtSecret = BASE64.encode(secret);
        this.issuer = issuer;
        this.jwtExpired = expirationSec;
        this.clockSkewSec = clockSkewSec;
        this.clock = DefaultClock.INSTANCE;
    }

    /**
     * Method to create a unique JWT token.
     *
     * @param attributes Map with user authentication data
     * @return unique string token for requests requiring authorization
     * @see hexlet.code.filter.JWTAuthenticationFilter
     */
    public String createToken(final Map<String, Object> attributes) {
        JWT_UTIL_LOGGER.error("ATTRIBUTES {}", attributes);
        return Jwts.builder()
                .setClaims(getClaims(attributes, jwtExpired))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    /**
     * Method to verify a unique JWT token from request.
     *
     * @param token Unique JWT token
     * @return Map with JWT token properties
     * @see hexlet.code.filter.JWTAuthorizationFilter
     */
    public Map<String, Object> verify(String token) {
        return Jwts.parser()
                .requireIssuer(issuer)
                .setClock(clock)
                .setAllowedClockSkewSeconds(clockSkewSec)
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Method to get {@link Claims} from attributes map.
     *
     * @param attributes Map with user authentication data
     * @param expiresInSec long value, how long after how long the token expires
     * @return {@link Claims}
     */
    private Claims getClaims(final Map<String, Object> attributes, final Long expiresInSec) {

        final Claims claims = Jwts.claims();
        claims.setIssuer(issuer);
        claims.setIssuedAt(clock.now());
        claims.putAll(attributes);

        if (expiresInSec > 0) {
            claims.setExpiration(new Date(System.currentTimeMillis() + jwtExpired));
        }
        return claims;
    }
}
