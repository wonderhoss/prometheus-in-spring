package horse.gargath.metricsexample;

import java.util.ArrayList;
import java.util.List;

public class ApiError {

    private List<String> errors;

    public ApiError() {
        this.errors = new ArrayList<String>();
    }

    public ApiError(String error) {
        this();
        this.addError(error);
    }

    public void addError(String error) {
        this.errors.add(error);
    }

    public List<String> getErrors() {
        return this.errors;
    }
    
}