package horse.gargath.metricsexample;

import javax.ws.rs.core.Response;

import horse.gargath.metricsexample.repository.RepositoryError;

public class Util {

    static final ApiError conflictOrServerError(RepositoryError err) {
        return err.isA(RepositoryError.Type.CONFLICT)
            ? new ApiError(Response.Status.CONFLICT, err.getMessage())
            : new ApiError(Response.Status.INTERNAL_SERVER_ERROR, err.getMessage());
    }

    static final ApiError serverError(RepositoryError err) {
        return new ApiError(Response.Status.INTERNAL_SERVER_ERROR, err.getMessage());
    }
    
    static final ApiError notFoundOrServerError(RepositoryError err) {
        return err.isA(RepositoryError.Type.NOTFOUND)
            ? new ApiError(Response.Status.NOT_FOUND, err.getMessage())
            : new ApiError(Response.Status.INTERNAL_SERVER_ERROR, err.getMessage());
    } 
}