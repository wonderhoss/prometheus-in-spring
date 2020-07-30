package horse.gargath.metricsexample.repository;

import java.util.List;

import io.atlassian.fugue.Either;

public interface FooRepository {
    
    public Either<RepositoryError, List<Foo>> listFoo();
    
    public Either<RepositoryError, Foo> getFoo(String id);

    public Either<RepositoryError, Foo> addFoo(Foo foo);

    public Either<RepositoryError, Foo> updateFoo(Foo foo);

}