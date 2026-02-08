package com.itmo.nxzage.web4.init;

import java.util.List;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import com.itmo.nxzage.web4.entity.Role;
import com.itmo.nxzage.web4.repository.UserRespository;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;

@Singleton
@Startup
public class RoleInitializer {
    @Inject
    private UserRespository repository;

    @Inject
    @ConfigProperty(name = "app.roles.names")
    private List<String> roles;

    @Inject
    private Config config;

    @PostConstruct
    public void initRoles() {
        roles.forEach(role -> initRole(role));
    }

    private void initRole(String name) {
        Role role = new Role();
        role.setName(name);
        role.setDescription(readDescription(name));
        repository.createRole(role);
    }

    private String readDescription(String role) {
        return config.getOptionalValue("app.role.%s.desc".formatted(role), String.class).orElse("");
    }
}
