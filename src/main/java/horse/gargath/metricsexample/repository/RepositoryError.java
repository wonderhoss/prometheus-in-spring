package horse.gargath.metricsexample.repository;

public class RepositoryError {
    public enum Type {
        NOTFOUND,
        CONFLICT,
        STORAGEERROR
    }

    private Type errorType;

    private String message;

    public RepositoryError(Type t, String message) {
        this.errorType = t;
        this.message = message;
    }

    public final String getMessage() {
        return this.message;
    }

    public final Type getType() {
        return this.errorType;
    }

    public final boolean isA(Type t) {
        return this.errorType == t;
    }

}