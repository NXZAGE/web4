package com.itmo.nxzage.web4.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/hello")
public class HelloResource {
    @GET
    @Path("/world")
    @Produces(MediaType.TEXT_PLAIN)
    public Response sayHello() {
        return Response.ok("Hello from Jakarta EE 11.").build();
    }
}
