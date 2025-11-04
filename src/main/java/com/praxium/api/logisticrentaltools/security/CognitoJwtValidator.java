package com.praxium.api.logisticrentaltools.security;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.security.PublicKey;
import java.util.Map;
import java.util.Base64;
@Component
public class CognitoJwtValidator {

	@Value("${aws.cognito.region}")
    private String region;

    @Value("${aws.cognito.userPoolId}")
    private String userPoolId;

    private Map<String, PublicKey> publicKeys;

    @PostConstruct
    public void loadKeys() {
        try {
            String jwksUrl = String.format("https://cognito-idp.%s.amazonaws.com/%s/.well-known/jwks.json", region, userPoolId);
            this.publicKeys = JwksUtil.fetchPublicKeys(jwksUrl);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar JWKS do Cognito", e);
        }
    }

    public Claims validate(String token) {
        try {
            // Extrai o header para pegar o kid
            String[] parts = token.split("\\.");
            if (parts.length < 2) {
                throw new SecurityException("Token JWT inválido");
            }

            String headerJson = new String(Base64.getUrlDecoder().decode(parts[0]));
            ObjectMapper mapper = new ObjectMapper();
            String kid = mapper.readTree(headerJson).get("kid").asText();

            // Busca a chave pública correspondente
            PublicKey key = publicKeys.get(kid);
            if (key == null) {
                throw new SecurityException("Chave pública não encontrada para KID: " + kid);
            }

            // Valida o token
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        } catch (JwtException e) {
            throw new SecurityException("Token inválido", e);
        } catch (Exception e) {
            throw new SecurityException("Erro ao validar JWT Cognito", e);
        }
    }
}
