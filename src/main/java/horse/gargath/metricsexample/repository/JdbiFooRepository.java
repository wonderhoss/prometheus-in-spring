package horse.gargath.metricsexample.repository;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;
import org.springframework.stereotype.Component;

import io.atlassian.fugue.Either;

@Component
public class JdbiFooRepository implements FooRepository {
    
    private final Jdbi jdbi;

    public JdbiFooRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
        jdbi.useHandle(handle -> {
            handle.execute("CREATE TABLE foo (id INTEGER PRIMARY KEY, name VARCHAR)");
            handle.createUpdate("INSERT INTO foo(id, name) VALUES (?, ?)")
                .bind(0,0)
                .bind(1, "Construct")
                .execute();
        });
    }

    @Override
    public Either<RepositoryError, List<Foo>> listFoo() {
        List<Foo> foos = jdbi.withHandle(handle -> {
            return handle.createQuery("SELECT * FROM foo")
                .mapToBean(Foo.class)
                .list();
        });
        return Either.right(foos);
    }

    @Override
    public Either<RepositoryError, Foo> getFoo(int id) {
        try {
            Foo foo = jdbi.withHandle(handle -> {
                return handle.createQuery("SELECT * FROM foo WHERE id = ?")
                    .bind(0, id)
                    .mapToBean(Foo.class)
                    .one();
            });
            return Either.right(foo);
        }
        catch (IllegalStateException e) {
            RepositoryError re = new RepositoryError(RepositoryError.Type.NOTFOUND, "Foo with id " + id + " does not exist");
            return Either.left(re);
        }
        catch (Exception e) {
            RepositoryError re = new RepositoryError(RepositoryError.Type.STORAGEERROR, e.getMessage());
            return Either.left(re);
        }
    }

    @Override
    public Either<RepositoryError, Foo> addFoo(Foo foo) {
        try {
            jdbi.useHandle(handle -> {
                handle.createUpdate("INSERT INTO foo(id, name) VALUES (:id, :name)")
                    .bindBean(foo)
                    .execute();
            });    
        }
        catch (UnableToExecuteStatementException e) {
            return e.getCause() instanceof SQLIntegrityConstraintViolationException
                ?  Either.left(new RepositoryError(RepositoryError.Type.CONFLICT, "Foo with id " + foo.getId() + " already exists"))
                :  Either.left(new RepositoryError(RepositoryError.Type.STORAGEERROR, e.getCause().getMessage()));
        }
        return Either.right(foo);
    }

}