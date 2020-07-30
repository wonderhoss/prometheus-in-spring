package horse.gargath.metricsexample.api;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import horse.gargath.metricsexample.repository.Foo;
import horse.gargath.metricsexample.repository.RepositoryError;
import io.atlassian.fugue.Either;


public class UtilTest {

    @Test
    public void testConflict() {
        RepositoryError conflict = new RepositoryError(RepositoryError.Type.CONFLICT, "msgmsg");
        RepositoryError internal = new RepositoryError(RepositoryError.Type.STORAGEERROR, "msgmsg");

        Either<RepositoryError, Foo> fromConflict = Either.left(conflict);
        Either<RepositoryError, Foo> fromInternal = Either.left(internal);

        ApiError fromConflictError = fromConflict.leftMap(Util::conflictOrServerError).left().get();
        ApiError fromInternalError = fromInternal.leftMap(Util::conflictOrServerError).left().get();

        assertEquals(fromConflictError.getResponse().getStatus(), Response.Status.CONFLICT.getStatusCode());
        assertEquals(fromInternalError.getResponse().getStatus(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
      //  assertEquals(fromConflictError.getMessage(), "msgmsg");
    //    assertEquals(fromInternalError.getMessage(), "msgmsg");
    }

    @Test
    public void testNotFound() {
        RepositoryError notFound = new RepositoryError(RepositoryError.Type.NOTFOUND, "msgmsg");
        RepositoryError internal = new RepositoryError(RepositoryError.Type.STORAGEERROR, "msgmsg");

        Either<RepositoryError, Foo> fromNotFound = Either.left(notFound);
        Either<RepositoryError, Foo> fromInternal = Either.left(internal);

        ApiError fromNotFoundError = fromNotFound.leftMap(Util::notFoundOrServerError).left().get();
        ApiError fromInternalError = fromInternal.leftMap(Util::notFoundOrServerError).left().get();

        assertEquals(fromNotFoundError.getResponse().getStatus(), Response.Status.NOT_FOUND.getStatusCode());
        assertEquals(fromInternalError.getResponse().getStatus(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
       // assertEquals(fromNotFoundError.getMessage(), "msgmsg");
       // assertEquals(fromInternalError.getMessage(), "msgmsg");
    }
    
}