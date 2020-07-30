package horse.gargath.metricsexample;

import horse.gargath.metricsexample.api.Endpoint;
import horse.gargath.metricsexample.api.FooEndpoint;
import horse.gargath.metricsexample.api.ApiErrorMapper;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
@ApplicationPath("/api")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(Endpoint.class);
        register(FooEndpoint.class);
        register(ApiErrorMapper.class);
    }

}
