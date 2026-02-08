package com.itmo.nxzage.web4.rest;

import java.util.List;
import com.itmo.nxzage.web4.entity.User;
import com.itmo.nxzage.web4.repository.UserRespository;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/db/test")
public class DBTestResource {
    @Inject
    private UserRespository userRespository;

    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> showUsersTable() {
        return userRespository.getUsers();
    }


    @GET
    @Path("/users/count")
    @Produces(MediaType.TEXT_PLAIN)
    public String getUsersCount() {
        return "Registred %d users".formatted(userRespository.getUsers().size());
    }

    @GET
    @Path("/users/count/user-secret")
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed({"ROLE_USER"})
    public String getUsersCountSecret() {
        return "Registred %d users SECRET (FOR AUTHED ONLY)".formatted(userRespository.getUsers().size());
    }

    @GET
    @Path("/users/count/admin-secret") 
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed({"ROLE_ADMIN"})
    public String getUsersCountTopSecret() {
        return "Registred %d users TOP SECRET (ONLY FOR ADMINS)".formatted(userRespository.getUsers().size());
    }

}
