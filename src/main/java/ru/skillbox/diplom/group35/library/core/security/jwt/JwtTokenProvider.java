package ru.skillbox.diplom.group35.library.core.security.jwt;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.ZonedDateTime;
import java.util.UUID;
@Component
public class JwtTokenProvider {



    public JwtEncoder jwtEncoder() {
        SecretKey key = new SecretKeySpec("SecretSpecialKeyOauth2.0Jwt256Bites".getBytes(), "HmacSHA256");
        JWKSource<SecurityContext> immutableSecret = new ImmutableSecret<>(key);
        return new NimbusJwtEncoder(immutableSecret);
    }

    public String createToken(UUID userId, String email) {
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .claim("id", userId.toString())
                .claim("email", email)
                .expiresAt(ZonedDateTime.now().plusHours(3).toInstant())
                .build();
        JwsAlgorithm jwsAlgorithm = JWSAlgorithm.HS256::getName;

        return jwtEncoder()
                .encode(JwtEncoderParameters.from(JwsHeader.with(jwsAlgorithm).build(), jwtClaimsSet))
                .getTokenValue();

    }

    public String systemToken(String systemKey){
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .claim("ROLE_", systemKey)
                .expiresAt(ZonedDateTime.now().plusHours(3).toInstant())
                .build();
        JwsAlgorithm jwsAlgorithm = JWSAlgorithm.HS256::getName;

        return jwtEncoder()
                .encode(JwtEncoderParameters.from(JwsHeader.with(jwsAlgorithm).build(), jwtClaimsSet))
                .getTokenValue();
    }
}
