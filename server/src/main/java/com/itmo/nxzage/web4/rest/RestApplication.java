package com.itmo.nxzage.web4.rest;

import jakarta.annotation.security.DeclareRoles;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@DeclareRoles({"ROLE_USER", "ROLE_ADMIN"})
@ApplicationPath("/api")
public class RestApplication extends Application {
    
}
