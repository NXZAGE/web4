package com.itmo.nxzage.web4.auth;

import java.util.List;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import com.itmo.nxzage.web4.entity.User;
import com.itmo.nxzage.web4.exceptions.RegistrationException;
import com.itmo.nxzage.web4.repository.UserRespository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.PasswordHash;

@Stateless // need transaction
public class RegistrationService {
    private static final Logger LOG = Logger.getLogger(RegistrationService.class);

    @Inject
    @ConfigProperty(name = "app.roles.default")
    private List<String> defaultRoles;

    @Inject
    private UserRespository userRespository;

    @Inject
    PasswordHash passwordHash;

    // TODO password validation? persistence level
    public void register(String username, char[] rawPassword) {
        if (userRespository.existsByUsername(username)) {
            throw new RegistrationException("User with the given name is already registred.");
        }

        LOG.debugf("User <%s> is not exist. Creating...");


        String hash = passwordHash.generate(rawPassword);
        LOG.debugf("Password coming from '%s' -> '%s' (hash)", hash);
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(hash);
        userRespository.save(user);
        defaultRoles.forEach(role -> userRespository.assignRoleToUser(username, role));
    }
}
