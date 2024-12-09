package org.acme.resources;

import io.smallrye.jwt.build.Jwt;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.Collections;
import java.util.Set;

import org.acme.models.Credentials;
import org.acme.models.User;
import org.acme.service.UserService;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    UserService userService;

    @POST
    @Path("/login")
    public Response login(Credentials credentials) {
        User user = userService.authenticate(credentials.getUsername(), credentials.getPassword());
        if (user != null) {
            String token = Jwt.issuer("http://example.com")
                              .subject(user.getUsername())
                              .groups(Set.of(user.getRole()))
                              .expiresIn(3600)
                              .sign();
            return Response.ok(Collections.singletonMap("token", token)).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
        //return Response.ok().build();

    }
}
