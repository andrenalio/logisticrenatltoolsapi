package com.praxium.api.logisticrentaltools.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

public class JwksUtil {

	public static Map<String, PublicKey> fetchPublicKeys(String jwksUrl) throws Exception {
        Map<String, PublicKey> keys = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        try (InputStream is = new URL(jwksUrl).openStream()) {
            JsonNode root = mapper.readTree(is).get("keys");
            KeyFactory kf = KeyFactory.getInstance("RSA");

            for (JsonNode node : root) {
                String kid = node.get("kid").asText();
                String n = node.get("n").asText();
                String e = node.get("e").asText();

                byte[] modulusBytes = Base64.getUrlDecoder().decode(n);
                byte[] exponentBytes = Base64.getUrlDecoder().decode(e);
                java.math.BigInteger modulus = new java.math.BigInteger(1, modulusBytes);
                java.math.BigInteger exponent = new java.math.BigInteger(1, exponentBytes);

                PublicKey publicKey = kf.generatePublic(new java.security.spec.RSAPublicKeySpec(modulus, exponent));
                keys.put(kid, publicKey);
            }
        }
        return keys;
    }
}
