package com.itmo.nxzage.web4.rest;

import com.itmo.nxzage.web4.auth.LoginService;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/login")
public class LoginResource {
    @Inject
    LoginService loginService;

    @POST
    @Path("/")
    public JsonObject login(@FormParam("username") String username,
            @FormParam("password") String password) {
        String token = loginService.login(username, password);
        return Json.createObjectBuilder().add("access", token).build();
    }

}
