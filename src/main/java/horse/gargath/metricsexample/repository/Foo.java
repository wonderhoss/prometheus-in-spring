package horse.gargath.metricsexample.repository;

public class Foo {
    private String id;
    private String name;

    public Foo() {}

    public Foo (String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        if (this.id != null) {
            return "{ \"id\": " + this.getId() + ", \"name\": \"" + this.name + "\" }";
        }
        return "{ \"name\": \"" + this.name + "\" }";
    }
}