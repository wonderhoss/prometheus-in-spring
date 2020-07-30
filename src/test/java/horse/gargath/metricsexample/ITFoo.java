package horse.gargath.metricsexample;

import static io.restassured.RestAssured.given;

import static org.hamcrest.CoreMatchers.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import io.restassured.RestAssured;


@SpringBootTest
public class ITFoo {
    
    private static final int TEST_PORT = 8080;
    private static final String TEST_URL = "http://localhost";

    static {
        System.setProperty("jersey.config.test.container.port", String.valueOf(TEST_PORT)); // This isn't actually being honoured.
    }

    @BeforeClass
    public static void configureRestAssured() throws Exception
    {
        RestAssured.port = TEST_PORT;
        RestAssured.baseURI = TEST_URL;

//        SpringApplication.run(App.class);
    }

    @Test
    public void shoudReturnCorrectFoo() {
        given().
        expect().
            body("id", equalTo("0")).
            statusCode(200).
        when().
            get("/api/foo/0");
    }

    @Test
    public void shoudReturnCorrectHeaders() {
        given().
        expect().
            header("Content-Type", equalTo("application/json")).
            statusCode(201).
        when().
            get("/api/foo/0");
    }

    @Test
    public void shouldReturnNotFound() {
        given().
        expect().
            statusCode(404).
        when().
            get("/api/foo/1");
    }

    @Test
    public void shouldAddFoo() {
        String newId = given().
            contentType("application/json").
            body("{ \"name\": \"TestFoo\" }").
            post("/api/foo").
        then().
            statusCode(201).
            header("Content-Type", equalTo("application/json")).
            body("name", equalTo("TestFoo")).
        extract().
            path("id");
        
        given().
        expect().
            statusCode(200).
            body("id", equalTo(newId)).
        when().
            get("/api/foo/" + newId);
    }

    @Test
    public void shouldRejectIdInPost() {
        given().
            contentType("application/json").
            body("{ \"id\": 99, \"name\": \"TestFoo\" }").
            post("/api/foo").
        then().
            statusCode(400).
            header("Content-Type", equalTo("application/json")).
            body("errors", hasItem(containsString("must be empty")));
        }

    @Test
    public void shouldUpdateFoo() {
        String newId = given().
            contentType("application/json").
            body("{ \"name\": \"TestFoo\" }").
            post("/api/foo").
        then().
            statusCode(201).
            header("Content-Type", equalTo("application/json")).
            body("name", equalTo("TestFoo")).
        extract().
            path("id");
        
        given().
        expect().
            statusCode(200).
            body("id", equalTo(newId)).
        when().
            get("/api/foo/" + newId);

        given().
            contentType("application/json").
            body("{ \"name\": \"TestFooUpdated\" }").
            put("/api/foo/" + newId).
        then().
            statusCode(200).
            header("Content-Type", equalTo("application/json")).
            body("name", equalTo("TestFooUpdated"));

        given().
        expect().
            statusCode(200).
            header("Content-Type", equalTo("application/json")).
            body("name", equalTo("TestFooUpdated")).
        when().
            get("/api/foo/" + newId);
    }

    @Test
    public void shouldUpdateFooWithId() {
        String newId = given().
            contentType("application/json").
            body("{ \"name\": \"TestFoo\" }").
            post("/api/foo").
        then().
            statusCode(201).
            header("Content-Type", equalTo("application/json")).
            body("name", equalTo("TestFoo")).
        extract().
            path("id");
        
        given().
        expect().
            statusCode(200).
            body("id", equalTo(newId)).
        when().
            get("/api/foo/" + newId);

        given().
            contentType("application/json").
            body("{ \"id\": \""+ newId +"\", \"name\": \"TestFooUpdated\" }").
            put("/api/foo/" + newId).
        then().
            statusCode(200).
            header("Content-Type", equalTo("application/json")).
            body("name", equalTo("TestFooUpdated"));

        given().
        expect().
            statusCode(200).
            header("Content-Type", equalTo("application/json")).
            body("name", equalTo("TestFooUpdated")).
        when().
            get("/api/foo/" + newId);
    }

    @Test
    public void shouldRejectUpdateWithConflictingIds() {
        String newId = given().
            contentType("application/json").
            body("{ \"name\": \"TestFoo\" }").
            post("/api/foo").
        then().
            statusCode(201).
            header("Content-Type", equalTo("application/json")).
            body("name", equalTo("TestFoo")).
        extract().
            path("id");
        
        given().
        expect().
            statusCode(200).
            body("id", equalTo(newId)).
        when().
            get("/api/foo/" + newId);

        given().
            contentType("application/json").
            body("{ \"id\": \"bumble-fudge\", \"name\": \"TestFooUpdatedWithConflict\" }").
            put("/api/foo/" + newId).
        then().
            statusCode(400).
            header("Content-Type", equalTo("application/json")).
            body("errors", hasItem(containsString("must be empty or match")));

    }
}