package com.itmo.nxzage.web4.security;

import org.jboss.logging.Logger;
import com.itmo.nxzage.web4.auth.JwtService;
import com.itmo.nxzage.web4.exceptions.JwtException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import jakarta.security.enterprise.credential.CallerOnlyCredential;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ApplicationScoped
public class JwtAuthenticationMechanism implements HttpAuthenticationMechanism {
    private static final Logger LOG = Logger.getLogger(JwtAuthenticationMechanism.class);

    @Inject
    private JwtService jwtService;

    @Inject
    private IdentityStoreHandler identityStoreHandler;

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest request,
            HttpServletResponse response, HttpMessageContext context) {
        LOG.debugf("Processing authorization...");
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            LOG.debug("DO NOTHING");
            return context.doNothing();
        }
        String token = authHeader.substring("Bearer ".length());

        try {
            JwtPrincipal principal = jwtService.parseAndValidate(token);
            LOG.debugf("Auth success. User: %s.", principal.getName());
            return context.notifyContainerAboutLogin(
                    identityStoreHandler.validate(new CallerOnlyCredential(principal.getName())));
        } catch (JwtException e) {
            LOG.debugf("Auth failed: ", e.getMessage());
            return context.responseUnauthorized();
        }
    }
}
