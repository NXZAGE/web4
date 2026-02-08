package com.itmo.nxzage.web4.rest;

import com.itmo.nxzage.web4.auth.RegistrationService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.APPLICATION_JSON)
@Path("/register") // TODO check if naming is correct
public class RegisterResource {
    @Inject
    private RegistrationService service;

    @POST
    @Path("")
    public Response register(@FormParam("username") String username,
            @FormParam("password") String password) {
        service.register(username, password.toCharArray());
        return Response.ok().build();
    }
}
