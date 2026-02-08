package com.itmo.nxzage.web4.auth;

import org.jboss.logging.Logger;
import com.itmo.nxzage.web4.exceptions.AuthException;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;

@Stateless // TODO do I need EJB here?
public class LoginService {
    private static final Logger LOG = Logger.getLogger(LoginService.class);

    @Inject
    private IdentityStoreHandler identityStoreHandler;

    @Inject
    private JwtService jwt;

    // return token. idk if it's right way
    public String login(String username, String password) {
        CredentialValidationResult result =
                identityStoreHandler.validate(new UsernamePasswordCredential(username, password));
        if (result.getStatus().equals(CredentialValidationResult.Status.VALID)) {
            LOG.debugf("Ready to issue token. Sun: %s, Groups: %s",
                    result.getCallerPrincipal().getName(), result.getCallerGroups().toString());
            return jwt.issueToken(result.getCallerPrincipal().getName(), result.getCallerGroups());
        }
        throw new AuthException("Login failed");
    }
}
