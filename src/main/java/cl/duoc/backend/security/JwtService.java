package cl.duoc.backend.security;

import cl.duoc.backend.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final long expirationMinutes;
    private final String issuer;

    public JwtService(
            JwtEncoder jwtEncoder,
            @Value("${app.jwt.expiration-minutes:120}") long expirationMinutes,
            @Value("${app.jwt.issuer:auth-duoc}") String issuer) {

        this.jwtEncoder = jwtEncoder;
        this.expirationMinutes = expirationMinutes;
        this.issuer = issuer;
    }

    public String generarToken(User user) {
        Instant ahora = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(ahora)
                .expiresAt(ahora.plus(Duration.ofMinutes(expirationMinutes)))
                .subject(user.getEmail())
                .claim("id", user.getId())
                .claim("nombre", user.getNombre())
                .claim("apellido", user.getApellido() != null ? user.getApellido() : "")
                .claim("rol", user.getRol().name())
                .claim("direccion", user.getDireccion() != null ? user.getDireccion() : "")
                .claim("region", user.getRegion() != null ? user.getRegion() : "")
                .claim("genero", user.getGenero() != null ? user.getGenero() : "")
                .claim("activo", user.getActivo() != null ? user.getActivo() : true)
                .build();

        JwsHeader header = JwsHeader
                .with(MacAlgorithm.HS256)
                .build();

        return jwtEncoder.encode(
                JwtEncoderParameters.from(header, claims)
        ).getTokenValue();
    }
}
