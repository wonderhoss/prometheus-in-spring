package horse.gargath.metricsexample;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.springframework.stereotype.Component;


@Component
@Path("/hello")
public class Endpoint {

    @GET
    public String message() {
        return "👋 🐴 from Jersey!\n";
    }
}
