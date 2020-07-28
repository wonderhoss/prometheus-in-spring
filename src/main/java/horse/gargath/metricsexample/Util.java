package horse.gargath.metricsexample;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import horse.gargath.metricsexample.repository.RepositoryError;

public class Util {

    static final WebApplicationException conflictOrServerError(RepositoryError err) {
        return err.isA(RepositoryError.Type.CONFLICT)
            ? new WebApplicationException(err.getMessage(), Response.Status.CONFLICT)
            : new ServerErrorException(err.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
    }

    static final WebApplicationException serverError(RepositoryError err) {
        return new ServerErrorException(err.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);            
    }
    

    static final WebApplicationException notFoundOrServerError(RepositoryError err) {
        return err.isA(RepositoryError.Type.NOTFOUND)
            ? new NotFoundException(err.getMessage())
            : new ServerErrorException(err.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);            
    }
 
}