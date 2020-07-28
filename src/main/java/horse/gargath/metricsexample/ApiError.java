package horse.gargath.metricsexample;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class ApiError extends WebApplicationException {

    private static final long serialVersionUID = 4508350470635048055L;
    private List<String> errors;

    public ApiError(Response.Status status) {
        super(status);
        this.errors = new ArrayList<String>();
    }

    public ApiError(Response.Status status, String error) {
        this(status);
        this.addError(error);
    }

    public final void addError(String error) {
        this.errors.add(error);
    }

    public final ErrorList getErrors() {
        return new ErrorList(this.errors);
    }

    public final String toString() {
        return "ApiError (" + this.getResponse().getStatus() + ") - " + this.errors;
    }
}
