package horse.gargath.metricsexample;

import java.util.List;

public class ErrorList {
    private final List<String> errors;

    ErrorList(List<String> list) {
        this.errors = list;
    }

    public List<String> getErrors() {
        return this.errors;
    }

}
