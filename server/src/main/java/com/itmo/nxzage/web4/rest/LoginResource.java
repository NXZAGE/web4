package com.itmo.nxzage.web4.rest;

import com.itmo.nxzage.web4.auth.LoginService;
import com.itmo.nxzage.web4.entity.User;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/login")
public class LoginResource {
    @Inject
    LoginService loginService;

    @Inject SecurityContext securityContext;

    @POST
    @Path("/")
    public JsonObject login(@FormParam("username") String username,
            @FormParam("password") String password) {
        String token = loginService.login(username, password);
        return Json.createObjectBuilder().add("token", token).add("username", username).build();
    }

    @GET
    @Path("/me")
    @RolesAllowed({"ROLE_USER", "ROLE_ADMIN"}) 
    public JsonObject isLoggedIn() {
        String currentUser = securityContext.getCallerPrincipal().getName(); 
        return Json.createObjectBuilder().add("username", currentUser).build();
    }
}
