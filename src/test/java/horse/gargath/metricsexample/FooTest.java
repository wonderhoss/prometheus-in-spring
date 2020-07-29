package horse.gargath.metricsexample;

import static io.restassured.RestAssured.given;

import static org.hamcrest.CoreMatchers.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.Test;
import org.junit.BeforeClass;
import io.restassured.RestAssured;


@SpringBootTest
public class FooTest {
    
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

        SpringApplication.run(App.class);
    }

    @Test
    public void shoudReturnCorrectFoo() {
        given().
        expect().
            body("id", equalTo(0)).
            statusCode(200).
        when().
            get("/foo/0");
    }

    @Test
    public void shoudReturnCorrectHeaders() {
        given().
        expect().
            header("Content-Type", equalTo("application/json")).
            statusCode(200).
        when().
            get("/foo/0");
    }

    @Test
    public void shouldReturnNotFound() {
        given().
        expect().
            statusCode(404).
        when().
            get("/foo/1");
    }

    @Test
    public void shouldAddFoo() {
        given().
            contentType("application/json").
            body("{ \"id\": 99, \"name\": \"TestFoo\" }").
            post("/foo").
        then().
            statusCode(201).
            header("Content-Type", equalTo("application/json")).
            body("id", equalTo(99));

        given().
        expect().
            statusCode(200).
            body("id", equalTo(99)).
        when().
            get("/foo/99");
    }
}