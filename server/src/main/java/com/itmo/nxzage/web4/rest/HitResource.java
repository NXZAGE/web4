package com.itmo.nxzage.web4.rest;

import java.util.List;
import com.itmo.nxzage.web4.entity.Hit;
import com.itmo.nxzage.web4.services.HitService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/hits")
@RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.APPLICATION_JSON)
public class HitResource {
    @Inject
    private HitService service;

    @POST
    @Path("/hit")
    public Hit hit(@FormParam("x") double x, @FormParam("y") double y,
            @FormParam("r") double r) {
        // TODO make realisation
        // TODO make some validation
        Hit hit = service.registerHit(x, y, r);
        return hit;
    }

    @GET
    @Path("/get")
    public List<Hit> getUserHits() {
        // TODO make realisation  
        // TODO make some validation
        return service.getHitsOfOwner();
    }
}
