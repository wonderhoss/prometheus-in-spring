package horse.gargath.metricsexample.repository;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.UUID;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.atlassian.fugue.Either;

@Component
public class JdbiFooRepository implements FooRepository {
    
    private final Logger logger = LoggerFactory.getLogger(JdbiFooRepository.class);


    private final Jdbi jdbi;

    public JdbiFooRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
        jdbi.useHandle(handle -> {
            handle.execute("CREATE TABLE foo (id VARCHAR PRIMARY KEY, name VARCHAR)");
            handle.createUpdate("INSERT INTO foo(id, name) VALUES (?, ?)")
                .bind(0,"0")
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
    public Either<RepositoryError, Foo> getFoo(String id) {
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
            logger.error("Failed to get Foo from DB", e);
            RepositoryError re = new RepositoryError(RepositoryError.Type.STORAGEERROR, e.getMessage());
            return Either.left(re);
        }
    }

    @Override
    public Either<RepositoryError, Foo> addFoo(Foo foo) {
        UUID uuid = UUID.randomUUID();
        foo.setId(uuid.toString());
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

    @Override
    public Either<RepositoryError, Foo> updateFoo(Foo newFoo) {
        try {
            jdbi.withHandle(handle -> {
                return handle.createQuery("SELECT * FROM foo WHERE id = ?")
                    .bind(0, newFoo.getId())
                    .mapToBean(Foo.class)
                    .one();
            });
        }
        catch (IllegalStateException e) {
            RepositoryError re = new RepositoryError(RepositoryError.Type.NOTFOUND, "Foo with id " + newFoo.getId() + " does not exist");
            return Either.left(re);
        }
        try {
            jdbi.useHandle(handle -> {
                handle.createUpdate("UPDATE foo SET name = :name WHERE id = :id")
                    .bindBean(newFoo)
                    .execute();
            });
        }
        catch(UnableToExecuteStatementException e) {
            logger.error("Failed to update Foo", e);
            return Either.left(new RepositoryError(RepositoryError.Type.STORAGEERROR, e.getCause().getMessage()));
        }
        return Either.right(newFoo);

    }

}