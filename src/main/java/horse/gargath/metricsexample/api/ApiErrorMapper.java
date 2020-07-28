package horse.gargath.metricsexample.api;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ApiErrorMapper implements ExceptionMapper<ApiError> {

    @Override
    public Response toResponse(ApiError e) {
        // System.out.println("🐴 In error mapper 🐴");
        // System.out.println("🐴 supplied status: 🐴");
        // System.out.println("🐴 " + e.getResponse().getStatus() + " 🐴");
        // System.out.println("🐴 supplied errors: 🐴");
        // System.out.println("🐴 " + e.getErrors() + " 🐴");

        return Response.fromResponse(e.getResponse()).entity(e.getErrors()).build();
    }
}
