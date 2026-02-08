package com.itmo.nxzage.web4.security;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.jboss.logging.Logger;
import com.itmo.nxzage.web4.entity.Role;
import com.itmo.nxzage.web4.entity.User;
import com.itmo.nxzage.web4.repository.UserRespository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.CallerOnlyCredential;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.Password;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;
import jakarta.security.enterprise.identitystore.PasswordHash;

@ApplicationScoped
public class DBIdentityStore implements IdentityStore {
    private static final Logger LOG = Logger.getLogger(DBIdentityStore.class);

    @Inject
    private UserRespository userRespository;

    @Inject
    private PasswordHash passwordHash;

    @Override
    public CredentialValidationResult validate(Credential credential) {
        LOG.debugf("ATTEMPTING OT VALIDATE. Credential: %s", credential.toString());
        if (credential instanceof UsernamePasswordCredential) {
            return validateUserPasswordCredential((UsernamePasswordCredential) credential);
        } else if (credential instanceof CallerOnlyCredential) {
            return validateCallerOnlyCredential((CallerOnlyCredential) credential);
        }

        return CredentialValidationResult.NOT_VALIDATED_RESULT;
    }

    private CredentialValidationResult validateUserPasswordCredential(
            UsernamePasswordCredential upc) {
        String username = upc.getCaller();
        Password password = upc.getPassword();
        Optional<User> userOpt = userRespository.findByNameWithRoles(username);

        if (userOpt.isEmpty()) {
            return CredentialValidationResult.NOT_VALIDATED_RESULT;
        }

        User user = userOpt.get();

        if (passwordHash.verify(password.getValue(), user.getPasswordHash())) {
            return assembleCredentialValidationResult(user);
        }

        return CredentialValidationResult.INVALID_RESULT;
    }

    private CredentialValidationResult validateCallerOnlyCredential(CallerOnlyCredential co) {
        String username = co.getCaller();
        Optional<User> userOpt = userRespository.findByNameWithRoles(username); // TODO DRY!
        if (userOpt.isEmpty()) {
            return CredentialValidationResult.NOT_VALIDATED_RESULT;
        }
        return assembleCredentialValidationResult(userOpt.get());
    }

    private CredentialValidationResult assembleCredentialValidationResult(User validatedUser) {
        Set<String> roles =
                validatedUser.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        return new CredentialValidationResult(validatedUser.getUsername(), roles);
    }
}
