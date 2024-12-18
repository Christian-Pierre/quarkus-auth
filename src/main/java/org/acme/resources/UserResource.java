package org.acme.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.models.User;
import org.acme.services.UserService;

import java.util.List;
import java.util.Optional;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserService userService;

    // Get all users
    @GET
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Get user by ID
    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(Response::ok).orElse(Response.status(Response.Status.NOT_FOUND)).build();
    }

    // Create a new user
    @POST
    public Response createUser(User user) {
        User createdUser = userService.createUser(user);
        return Response.status(Response.Status.CREATED).entity(createdUser).build();
    }

    // Update an existing user
    @PUT
    @Path("/{id}")
    public Response updateUser(@PathParam("id") Long id, User user) {
        try {
            User updatedUser = userService.updateUser(id, user);
            return Response.ok(updatedUser).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    // Delete user by ID
    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    // Find user by username
    @GET
    @Path("/search")
    public Response findByUsername(@QueryParam("username") String username) {
        Optional<User> user = userService.findByUsername(username);
        return user.map(Response::ok).orElse(Response.status(Response.Status.NOT_FOUND)).build();
    }

    // Update token for a user
    @PATCH
    @Path("/{id}/token")
    public Response updateToken(@PathParam("id") Long id, @QueryParam("token") String token) {
        try {
            userService.updateToken(id, token);
            return Response.noContent().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }
}
