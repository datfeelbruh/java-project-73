package hexlet.code.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.impl.DefaultClock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.Map;

import static io.jsonwebtoken.impl.TextCodec.BASE64;

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

    public String createToken(final Map<String, Object> attributes) {
        JWT_UTIL_LOGGER.error("ATTRIBUTES {}", attributes);
        return Jwts.builder()
                .setClaims(getClaims(attributes, jwtExpired))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public Map<String, Object> verify(String token) {
        return Jwts.parser()
                .requireIssuer(issuer)
                .setClock(clock)
                .setAllowedClockSkewSeconds(clockSkewSec)
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            JWT_UTIL_LOGGER.error("Invalid JWT signature - {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            JWT_UTIL_LOGGER.error("Invalid JWT token - {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            JWT_UTIL_LOGGER.error("Expired JWT token - {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            JWT_UTIL_LOGGER.error("Unsupported JWT token - {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            JWT_UTIL_LOGGER.error("JWT claims string is empty - {}", ex.getMessage());
        }
        return false;
    }


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
