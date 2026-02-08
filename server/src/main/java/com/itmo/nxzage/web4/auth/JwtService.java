package com.itmo.nxzage.web4.auth;

import java.security.SecureRandom;
import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import com.itmo.nxzage.web4.exceptions.JwtException;
import com.itmo.nxzage.web4.security.JwtPrincipal;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class JwtService {
    private static final Logger LOG = Logger.getLogger(JwtService.class);
    private final String issuer = "web4";
    @Inject
    @ConfigProperty(name = "app.security.token.access.lifetime")
    private long lifetime;
    private byte[] secret = new byte[32];
    private JWSSigner signer;
    private JWSVerifier verifier;

    @PostConstruct
    private void init() {
        new SecureRandom().nextBytes(secret);
        try {
            signer = new MACSigner(secret);
            verifier = new MACVerifier(secret);
        } catch (JOSEException e) {
            throw new JwtException(e);
        }
    }


    public String issueToken(String subject, Set<String> roles) {
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder().subject(subject).issuer(issuer)
                .expirationTime(new Date(new Date().getTime() + lifetime)).claim("roles", roles)
                .build();
        SignedJWT token = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
        try {
            token.sign(signer);
        } catch (JOSEException e) {
            throw new JwtException(e);
        }
        return token.serialize();
    }

    public JwtPrincipal parseAndValidate(String token) {
        try {
            SignedJWT jwt = SignedJWT.parse(token);
            if (!jwt.verify(verifier)) {
                throw new JwtException("Invalid JWT signature");
            }

            JWTClaimsSet claims = jwt.getJWTClaimsSet();
            validateClaims(claims);

            LOG.debugf("JWT parsed claims: \n%s", claims.toString());

            String subject = claims.getSubject();
            Set<String> roles = new HashSet<>(claims.getStringListClaim("roles"));
            return new JwtPrincipal(subject, roles);
        } catch (ParseException | JOSEException e) {
            throw new JwtException(e);
        }
    }

    private void validateClaims(JWTClaimsSet claims) {
        if (claims.getExpirationTime() == null) {
            throw new JwtException("Invalid claims: exp required");
        }

        if (claims.getExpirationTime().before(new Date())) {
            throw new JwtException("Token expired");
        }

        if (!issuer.equals(claims.getIssuer())) {
            throw new JwtException("Invalid issuer");
        }
    }
}
