package horse.gargath.metricsexample.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import horse.gargath.metricsexample.repository.Foo;
import horse.gargath.metricsexample.repository.FooRepository;
import io.atlassian.fugue.Either;
import io.atlassian.fugue.Eithers;


@Component
@Path("/foo")
public class FooEndpoint {

    private final Logger logger = LoggerFactory.getLogger(FooEndpoint.class);

    private FooRepository repo;

    public FooEndpoint(FooRepository repo) {
        this.repo = repo;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Foo> listFoo() {
        Either<ApiError, List<Foo>> response = 
            repo.listFoo().leftMap(Util::serverError);
        return Eithers.getOrThrow(response);
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Foo getFoo(@PathParam("id") String id) {
        Either<ApiError, Foo> response =
            repo.getFoo(id).leftMap(Util::notFoundOrServerError);
        return Eithers.getOrThrow(response);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addFoo(Foo f) {
        if (f.getId()!= null) {
            throw new ApiError(Response.Status.BAD_REQUEST, "'id' field must be empty");
        }
        Either<ApiError, Foo> response =
            repo.addFoo(f).leftMap(Util::conflictOrServerError);
        f = Eithers.getOrThrow(response);
        return Response.status(Response.Status.CREATED).entity(f).header("Location", "/foo/" + f.getId()).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateFoo(@PathParam("id") String id, Foo f) {
        logger.info("Request: " + id + " - " + f.toString());
        if (f.getId() != null && !f.getId().equals(id)) {
            logger.info("Rejecting because id not match or empty");
            throw new ApiError(Response.Status.BAD_REQUEST, "'id' field must be empty or match entity id "+ id);
        }
        f.setId(id);
        Either<ApiError, Foo> response = 
            repo.updateFoo(f).leftMap(Util::notFoundOrServerError);
        f = Eithers.getOrThrow(response);
        return Response.status(Response.Status.OK).entity(f).build();
    }
}
